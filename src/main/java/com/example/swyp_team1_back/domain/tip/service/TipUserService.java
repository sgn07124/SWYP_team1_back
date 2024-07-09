package com.example.swyp_team1_back.domain.tip.service;

import com.example.swyp_team1_back.domain.tip.dto.request.CreateTipDTO;
import com.example.swyp_team1_back.domain.tip.dto.response.TipDetailDTO;
import com.example.swyp_team1_back.domain.tip.entity.Category;
import com.example.swyp_team1_back.domain.tip.entity.Tip;
import com.example.swyp_team1_back.domain.tip.repository.CategoryRepository;
import com.example.swyp_team1_back.domain.tip.repository.TipRepository;
import com.example.swyp_team1_back.global.common.response.CustomFieldException;
import com.example.swyp_team1_back.global.common.response.ErrorCode;
import com.example.swyp_team1_back.global.common.response.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TipUserService {

    private final TipRepository tipRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public Tip createUserTip(CreateTipDTO createTipDTO) {

        Category category = categoryRepository.findById(createTipDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
        Tip tip = Tip.createUserTip(createTipDTO);
        tip.setCategory(category);
        return tipRepository.save(tip);
    }

    @Transactional
    public void deleteTip(Long tipId) {
        Tip tip = tipRepository.findById(tipId)
                .orElseThrow(() -> new CustomFieldException("tipId", "팁을 찾을 수 없음", ErrorCode.FAIL_FIND_TIP));
        tipRepository.delete(tip);
    }

    @Transactional(readOnly = true)
    public TipDetailDTO getTipDetail(Long tipId) {
        Tip tip = tipRepository.findById(tipId)
                .orElseThrow(() -> new CustomFieldException("tipId", "팁을 찾을 수 없음", ErrorCode.FAIL_FIND_TIP));

        return convertToDetailDto(tip);
    }

    @Transactional
    public void updateTip(Long tipId, CreateTipDTO dto) {
        Tip tip = tipRepository.findById(tipId)
                .orElseThrow(() -> new ResourceNotFoundException("팁을 찾을 수 없음"));
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("카테고리 Id가 잘못됨"));

        tip.updateTip(dto, category);
        tipRepository.save(tip);

    }

    @Transactional
    public void updateActCntChecked(Long tipId, int actCntChecked) {
        Tip tip = tipRepository.findById(tipId)
                .orElseThrow(() -> new ResourceNotFoundException("팁을 찾을 수 없음"));

        tip.updateActCntChecked(actCntChecked);
        tipRepository.save(tip);
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")  // 매일 자정에 실행
    public void updateCheckCompleteStatus() {
        List<Tip> tips = tipRepository.findAll();
        for (Tip tip : tips) {
            tip.checkCompleteStatus();
            tipRepository.save(tip);
        }
    }

    private TipDetailDTO convertToDetailDto(Tip tip) {
        TipDetailDTO dto = new TipDetailDTO();
        dto.setId(tip.getId());
        dto.setTipLink(tip.getTipLink());
        dto.setTipTitle(tip.getTipTitle());
        dto.setCategoryId(tip.getCategory().getId());
        dto.setActCnt(tip.getActCnt());
        dto.setDeadLine_start(String.valueOf(tip.getDeadLine_start()));
        dto.setDeadLine_end(String.valueOf(tip.getDeadLine_end()));

        dto.setCategoryName(tip.getCategory().getCategoryName());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");  // yyyy-MM-dd 형식을 yyyy.MM.dd 형식으로 변환
        String start = tip.getDeadLine_start().format(formatter);
        String end = tip.getDeadLine_end().format(formatter);
        dto.setPeriodDate(start + " - " + end);
        dto.setD_day((int) ChronoUnit.DAYS.between(LocalDate.now(), tip.getDeadLine_end()));
        dto.setActCnt_checked(tip.getActCntChecked());
        return dto;
    }
}

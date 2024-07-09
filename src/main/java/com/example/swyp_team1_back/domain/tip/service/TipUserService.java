package com.example.swyp_team1_back.domain.tip.service;

import com.example.swyp_team1_back.domain.tip.dto.CreateTipDTO;
import com.example.swyp_team1_back.domain.tip.entity.Category;
import com.example.swyp_team1_back.domain.tip.entity.Tip;
import com.example.swyp_team1_back.domain.tip.repository.CategoryRepository;
import com.example.swyp_team1_back.domain.tip.repository.TipRepository;
import com.example.swyp_team1_back.global.common.response.CustomFieldException;
import com.example.swyp_team1_back.global.common.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}

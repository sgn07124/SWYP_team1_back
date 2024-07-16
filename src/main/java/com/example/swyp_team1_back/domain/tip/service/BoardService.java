package com.example.swyp_team1_back.domain.tip.service;

import com.example.swyp_team1_back.domain.tip.dto.response.BoardTipDTO;
import com.example.swyp_team1_back.domain.tip.dto.response.TipCompleteYnListDTO;
import com.example.swyp_team1_back.domain.tip.entity.Tip;
import com.example.swyp_team1_back.domain.tip.repository.TipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final TipRepository tipRepository;

    @Transactional
    public List<BoardTipDTO> getBoardSearch(Long cursor, int pageSize, String keyword) {
        Pageable pageable = PageRequest.of(0, pageSize);

        // 커서가 null인 경우 처음 조회
        if (cursor == null) {
            List<Tip> tips = tipRepository.searchByKeywordAndIsMineTrue(null, pageable, keyword);
            return tips.stream().map(BoardTipDTO::new).collect(Collectors.toList());
        }

        // 커서가 있는 경우 페이징 처리
        List<Tip> tips = tipRepository.searchByKeywordAndIsMineTrue(cursor, pageable, keyword);
        return tips.stream().map(BoardTipDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public List<BoardTipDTO> searchTipsByKeyword(Long cursor, int pageSize, String keyword) {
        Pageable pageable = PageRequest.of(0, pageSize);
        List<Tip> tips = tipRepository.searchByKeywordAndIsMineTrue(cursor, pageable, keyword);
        System.out.println("Tips found: " + tips.size());
        tips.forEach(tip -> System.out.println("Tip title: " + tip.getTipTitle() + ", regDate: " + tip.getRegDate()));
        return tips.stream().map(BoardTipDTO::new).collect(Collectors.toList());
    }
}

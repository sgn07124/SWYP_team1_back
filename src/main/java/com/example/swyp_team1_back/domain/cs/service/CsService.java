package com.example.swyp_team1_back.domain.cs.service;

import com.example.swyp_team1_back.domain.cs.entity.Cs;
import com.example.swyp_team1_back.domain.cs.repository.CsRepository;
import com.example.swyp_team1_back.domain.user.entity.User;
import com.example.swyp_team1_back.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CsService {

    private final CsRepository csRepository;
    private final UserRepository userRepository;


    public void saveCs(String email, String contents) {
        User user = userRepository.findWithCsListByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user email"));

        Cs cs = new Cs();
        cs.setContents(contents);
        cs.setUser(user);

        csRepository.save(cs);
    }
}
package com.example.swyp_team1_back.domain.user.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UpdateProfileImageDto {

    private MultipartFile image;

    public UpdateProfileImageDto(MultipartFile profileImage) {
        this.image = profileImage;
    }
}

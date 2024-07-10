package com.example.swyp_team1_back.domain.user.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @Column(name = "RT_KEY")
    private String key; //유저 id

    @Column(name = "RT_VALUE")
    private String value;// token값 들어감

    public RefreshToken updateValue(String token) {
        this.value = token;
        return this;
    }
}

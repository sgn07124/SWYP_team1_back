package com.example.swyp_team1_back.domain.cs.entity;


import com.example.swyp_team1_back.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "Cs")
@NoArgsConstructor
public class Cs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "csId")
    private Long id;

    @Column(name = "contents", nullable = false, length = 200)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "id")  // 주인
    private User user;

    public void setContents(String contents) {
        this.contents = contents;
    }

    public void setUser(User user) {
        this.user = user;
        if (!user.getCsList().contains(this)) {
            user.getCsList().add(this);
        }
    }

}

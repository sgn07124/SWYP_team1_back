package com.example.swyp_team1_back.domain.tip.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "Category")
@NoArgsConstructor
public class Category {

    @Id
    @Column(name="cateId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tip_Category", nullable = false)
    private String categoryName;

    @OneToMany(mappedBy = "category")
    private List<Tip> tips = new ArrayList<>();
}

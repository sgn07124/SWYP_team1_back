package com.example.swyp_team1_back.domain.tip.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "Category")
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    @Column(name="cateId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tip_Category", nullable = false)
    private String categoryName;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Tip> tips = new ArrayList<>();

    public Category(String name) {
        this.categoryName = name;
    }
}

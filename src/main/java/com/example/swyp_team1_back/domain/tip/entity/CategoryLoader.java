package com.example.swyp_team1_back.domain.tip.entity;

import com.example.swyp_team1_back.domain.tip.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class CategoryLoader implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    public CategoryLoader(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        // 카테고리 데이터 생성
        List<Category> categories = Arrays.asList(
                new Category("운동"),
                new Category("뷰티&패션"),
                new Category("요리"),
                new Category("공부"),
                new Category("독서"),
                new Category("금융"),
                new Category("인간관계"),
                new Category("회사생활"),
                new Category("기타")
        );

        // 카테고리 데이터가 존재하는지 확인 후 없으면 생성
        if (categoryRepository.count() == 0) {
            categoryRepository.saveAll(categories);
        }

    }
}

package com.example.swyp_team1_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SwypTeam1BackApplication {

	public static void main(String[] args) {
		SpringApplication.run(SwypTeam1BackApplication.class, args);
	}

}

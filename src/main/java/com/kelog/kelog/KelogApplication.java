package com.kelog.kelog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class KelogApplication {

	public static void main(String[] args) {
		SpringApplication.run(KelogApplication.class, args);
	}

}

package com.sourcery.gymapp.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
public class GymAppBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(GymAppBackendApplication.class, args);
	}

}

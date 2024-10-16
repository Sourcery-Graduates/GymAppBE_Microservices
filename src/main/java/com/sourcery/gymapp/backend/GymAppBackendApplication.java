package com.sourcery.gymapp.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// TODO: exclude in this annotation disables spring security
// TODO: delete it when needed
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class GymAppBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(GymAppBackendApplication.class, args);
	}

}

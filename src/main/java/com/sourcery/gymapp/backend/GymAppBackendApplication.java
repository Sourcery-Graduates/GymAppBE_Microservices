package com.sourcery.gymapp.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.modulith.Modulithic;

@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@Modulithic(sharedModules = { "com.sourcery.gymapp.backend.shared" }, useFullyQualifiedModuleNames = true)
@SpringBootApplication
public class GymAppBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(GymAppBackendApplication.class, args);
	}

}

package com.toolmanagementsystem.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication

public class ToolEntrySystemApplication {

	public static void main(String[] args) {

		SpringApplication.run(ToolEntrySystemApplication.class, args);
		System.out.println("working fine");
	}

}

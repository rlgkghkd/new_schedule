package com.example.newschedule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class NewScheduleApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewScheduleApplication.class, args);
	}

}

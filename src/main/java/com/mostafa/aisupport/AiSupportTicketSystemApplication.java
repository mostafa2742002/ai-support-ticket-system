package com.mostafa.aisupport;

import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAsync
public class AiSupportTicketSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiSupportTicketSystemApplication.class, args);
	}

}

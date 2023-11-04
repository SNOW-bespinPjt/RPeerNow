package com.example.peernow360;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PeerNow360Application {

	public static void main(String[] args) {
		SpringApplication.run(PeerNow360Application.class, args);
	}

}

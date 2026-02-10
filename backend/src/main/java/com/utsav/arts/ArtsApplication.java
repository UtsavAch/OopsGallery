package com.utsav.arts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ArtsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArtsApplication.class, args);
	}

}

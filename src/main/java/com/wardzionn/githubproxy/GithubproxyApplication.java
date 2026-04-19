package com.wardzionn.githubproxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GithubproxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(GithubproxyApplication.class, args);
	}

}

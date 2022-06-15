package com.codelikealexito.articles.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ArticlesApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArticlesApiApplication.class, args);
	}

}

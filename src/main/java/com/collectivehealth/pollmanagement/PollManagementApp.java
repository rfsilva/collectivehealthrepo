package com.collectivehealth.pollmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableCaching
@EnableSwagger2
public class PollManagementApp {

	public static void main(String[] args) {
		new PollManagementApp().run(args);
	}
	
	private void run(String... args) {
		SpringApplication.run(PollManagementApp.class, args);
	}

    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("GET", "POST", "PUT",
                "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT");
    }
}

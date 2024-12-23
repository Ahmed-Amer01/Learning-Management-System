package com.example.lms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.example.lms.Notifications.NotificationsManager")
@EntityScan(basePackages = "com.example.demo.Notifications.NotificationsManager")
//@SpringBootApplication
//@EnableJpaRepositories(basePackages = "com.example.demo.Notifications.NotificationsManager")
@SpringBootApplication
public class LmsApplication {

	public static void main(String[] args) {

		SpringApplication.run(LmsApplication.class, args);
	}

}

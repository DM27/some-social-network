package com.training.some_social_network;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.training.some_social_network.dao.mappers")
@SpringBootApplication
public class SomeSocialNetworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(SomeSocialNetworkApplication.class, args);
	}

}

package com.nood;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
@MapperScan(basePackages = {"com.nood.mapper"})
@ComponentScan
public class HrmAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(HrmAdminApplication.class, args);
	}

}

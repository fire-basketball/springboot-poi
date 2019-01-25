package com.ligh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = {"com.ligh.mapper"})
public class PoiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PoiApplication.class, args);
	}

}


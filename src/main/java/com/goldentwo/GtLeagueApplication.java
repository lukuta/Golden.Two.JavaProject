package com.goldentwo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableMBeanExport;

@SpringBootApplication
@EnableCaching
@EnableMBeanExport
public class GtLeagueApplication {

	public static void main(String[] args) {
		SpringApplication.run(GtLeagueApplication.class, args);
	}
}

package com.dstz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.CrossOrigin;

import cn.hutool.extra.spring.EnableSpringUtil;

/**
 * <pre>
 * Main application entry
 * </pre>
 */
@EnableSpringUtil
@SpringBootApplication
@EnableAsync
@CrossOrigin
public class AbSpringBootApp extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(AbSpringBootApp.class, args);
	}
}

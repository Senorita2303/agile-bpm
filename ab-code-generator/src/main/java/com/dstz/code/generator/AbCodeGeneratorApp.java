package com.dstz.code.generator;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.dstz.code.generator.core.AbCodeGenerator;
import com.dstz.code.generator.core.AbCodeGeneratorCommandLine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * AB Code Generator
 *
 */
@EnableConfigurationProperties(AbCodeGeneratorProperties.class)
@SpringBootApplication(exclude = MybatisPlusAutoConfiguration.class)
public class AbCodeGeneratorApp {

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(AbCodeGeneratorApp.class, args);
		AbCodeGeneratorProperties codeGeneratorProperties = applicationContext.getBean(AbCodeGeneratorProperties.class);
		AbCodeGenerator codeGenerator = applicationContext.getBean(AbCodeGenerator.class);
		if (Boolean.TRUE.equals(codeGeneratorProperties.getEnableGui())) {
			//TODO GUI
		} else {
			new AbCodeGeneratorCommandLine(codeGenerator).run();
		}
	}

}

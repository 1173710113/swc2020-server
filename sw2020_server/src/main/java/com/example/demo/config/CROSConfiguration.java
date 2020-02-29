package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SuppressWarnings("deprecation")
@Configuration
public class CROSConfiguration extends WebMvcConfigurerAdapter{
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry
			.addMapping("/**")
			.allowedMethods("*")
			.allowedOrigins("http://192.168.0.100:10012")
			.allowedHeaders("*")
			.allowCredentials(true);
	}

}

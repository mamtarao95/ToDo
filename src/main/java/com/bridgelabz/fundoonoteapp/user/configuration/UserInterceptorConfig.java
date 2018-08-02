package com.bridgelabz.fundoonoteapp.user.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.bridgelabz.fundoonoteapp.user.interceptors.LoggerInterceptor;
import com.bridgelabz.fundoonoteapp.user.interceptors.RequestProcessingTimeInterceptor;

@Configuration
public class UserInterceptorConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoggerInterceptor()).addPathPatterns("/**");
		registry.addInterceptor(new RequestProcessingTimeInterceptor()).addPathPatterns("/**");

	}
}

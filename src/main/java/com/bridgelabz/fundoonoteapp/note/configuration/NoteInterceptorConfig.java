package com.bridgelabz.fundoonoteapp.note.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.bridgelabz.fundoonoteapp.note.interceptors.ValidationInterceptor;

@Configuration
public class NoteInterceptorConfig implements WebMvcConfigurer {
	
	@Autowired
	ValidationInterceptor validationInterceptor;
	
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(validationInterceptor).addPathPatterns("/api/note/**");
	}
	
	

}

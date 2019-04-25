package com.nercel.exam;

import javax.servlet.MultipartConfigElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.google.gson.Gson;
import com.nercel.exam.filter.TimeFilter;

@Configuration
@EnableAsync
@EnableCaching
@EnableTransactionManagement
@EntityScan("com.nercel.exam.entity")
@ComponentScan(basePackages = { "com.nercel.exam.controller", "com.nercel.exam.service", "com.nercel.exam.dao",
		"com.nercel.exam.filter", "com.nercel.exam.interceptor", "com.nercel.exam.exception", "com.nercel.exam.util",
		"com.nercel.exam.listener" })
public class ApplicationConfig implements WebMvcConfigurer {

	@Autowired
	private TimeFilter timeFilter;

	/*
	 * @Bean public EmbeddedServletContainerCustomizer containerCustomizer() {
	 * return container -> container.addErrorPages(new
	 * ErrorPage(HttpStatus.NOT_FOUND, "/404"), new
	 * ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500")); }
	 */

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(timeFilter).addPathPatterns("/**");
	}

	@Bean
	public Gson gson() {
		return new Gson();
	}

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize("100MB");
		factory.setMaxRequestSize("200MB");
		return factory.createMultipartConfig();
	}

}

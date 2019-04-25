package com.nercl.music.cloud;

import javax.servlet.MultipartConfigElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.google.gson.Gson;
import com.nercl.music.cloud.interceptor.TimeInterceptor;

@Configuration
@EnableAsync
@EnableScheduling
@SpringBootApplication
@EnableEurekaClient
@EntityScan("com.nercl.music.cloud.entity")
@ComponentScan(basePackages = { "com.nercl.music.cloud.controller", "com.nercl.music.cloud.service",
		"com.nercl.music.cloud.dao", "com.nercl.music.util", "com.nercl.music.websocket" })
public class ApplicationConfig extends WebMvcConfigurerAdapter {

	@Autowired
	private TimeInterceptor timeInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(timeInterceptor).addPathPatterns("/**");
	}

	@Bean
	public Gson gson() {
		return new Gson();
	}

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize("1000MB");
		factory.setMaxRequestSize("1000MB");
		return factory.createMultipartConfig();
	}

	@Bean
	public RestTemplate restTemplate() {
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setBufferRequestBody(false);
		requestFactory.setConnectTimeout(120000);
		requestFactory.setReadTimeout(120000);
		RestTemplate restTemplate = new RestTemplate(requestFactory);
		return restTemplate;
	}

}
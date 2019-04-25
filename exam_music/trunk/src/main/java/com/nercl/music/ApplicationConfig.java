package com.nercl.music;

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
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.google.gson.Gson;
import com.nercl.music.filter.TimeFilter;
import com.nercl.music.interceptor.AuthenticateInterceptor;
import com.nercl.music.interceptor.PrivilegeInterceptor;
import com.nercl.music.interceptor.SessionInterceptor;

@Configuration
@EnableAsync
@EnableCaching
@EnableTransactionManagement
@EntityScan("com.nercl.music.entity")
@ComponentScan(basePackages = { "com.nercl.music.api", "com.nercl.music.controller", "com.nercl.music.service",
        "com.nercl.music.dao", "com.nercl.music.filter", "com.nercl.music.interceptor", "com.nercl.music.exception",
        "com.nercl.music.xml", "com.nercl.music.util", "com.nercl.music.listener" })
public class ApplicationConfig extends WebMvcConfigurerAdapter {

	@Autowired
	private TimeFilter timeFilter;

	@Autowired
	private SessionInterceptor sessionInterceptor;

	@Autowired
	private PrivilegeInterceptor privilegeInterceptor;

	@Autowired
	private AuthenticateInterceptor authenticateInterceptor;

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
		registry.addInterceptor(sessionInterceptor).addPathPatterns("/**").excludePathPatterns("/register",
		        "/examinee/login/exsit", "/examinee/email/exsit", "/examinee/phone/exsit", "/user/find_password",
		        "/send_email", "/check_email", "/repassword", "/examinee/photo/*", "/take_photo", "/error");
		registry.addInterceptor(privilegeInterceptor).addPathPatterns("/**");
		registry.addInterceptor(timeFilter).addPathPatterns("/**");
		registry.addInterceptor(authenticateInterceptor).addPathPatterns("/api/**").excludePathPatterns("/api/login",
		        "/api/exam_question", "/api/yueli/knowledge_map", "/api/question/explain", "/api/looksing/expert/score",
		        "/file");
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

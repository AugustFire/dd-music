package com.nercl.music.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class TimeFilter extends HandlerInterceptorAdapter {

	private String timeFilterStartParam = "com.nercl.music.filter.timefilter.start";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	        throws Exception {
		long startTime = System.currentTimeMillis();
		request.setAttribute(timeFilterStartParam, startTime);
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
	        throws Exception {
		long startTime = (long) request.getAttribute(timeFilterStartParam);
		System.out.println(
		        "url: " + request.getRequestURI() + "-------" + "times: " + (System.currentTimeMillis() - startTime));
		if (request.getRequestURI().contains("error")) {
			System.out.println("error");
		}
	}
}

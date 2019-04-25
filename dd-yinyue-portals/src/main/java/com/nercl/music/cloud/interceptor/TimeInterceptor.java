package com.nercl.music.cloud.interceptor;

import java.net.InetAddress;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.nercl.music.util.CommonUtils;

@Component
public class TimeInterceptor extends HandlerInterceptorAdapter {

	private String timeFilterStartParam = "com.nercl.music.filter.timefilter.start";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	        throws Exception {
		long startTime = System.currentTimeMillis();
		InetAddress addr = InetAddress.getLocalHost();
		request.setAttribute(timeFilterStartParam, startTime);
		// TODO session验证，后将用户id记录到日志中
		MDC.put("userId", "123456789");
		MDC.put("ip", CommonUtils.getIp(addr));
		MDC.put("host", CommonUtils.getHost(addr));
		return true;
		}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
	        throws Exception {
		super.afterCompletion(request, response, handler, ex);
		MDC.remove("userId");
		MDC.remove("ip");
		MDC.remove("host");
		long startTime = (long) request.getAttribute(timeFilterStartParam);
		System.out.println(
		        "url: " + request.getRequestURI() + "-------" + "times: " + (System.currentTimeMillis() - startTime));
	}
}

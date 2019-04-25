package com.nercl.music.interceptor;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.nercl.music.entity.user.Login;
import com.nercl.music.entity.user.RequiredPrivilege;
import com.nercl.music.entity.user.Role;

@Component
public class PrivilegeInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	        throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			RequiredPrivilege requires = handlerMethod.getMethodAnnotation(RequiredPrivilege.class);
			if (null != requires && null != requires.value()) {
				Role[] rs = requires.value();
				Object login = request.getSession().getAttribute("login");
				if (null != login) {
					if(login instanceof Login){
						Login lgin = (Login) login;
						Role role = lgin.getRole();
						boolean anyMatch = Arrays.stream(rs).anyMatch(r -> r.equals(role));
						if (anyMatch) {
							return true;
						} else {
							String url = request.getContextPath() + "/noprivilege";
							response.sendRedirect(url);
							return false;
						}
					}
				}else{
					String url = request.getContextPath() + "/session";
					response.sendRedirect(url);
					return false;
				}
			}
		}
		return true;
	}

}

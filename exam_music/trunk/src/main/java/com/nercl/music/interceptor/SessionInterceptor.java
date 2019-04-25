package com.nercl.music.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.nercl.music.api.ApiAnswerController;
import com.nercl.music.api.ApiExamPaperController;
import com.nercl.music.api.ApiExamineeController;
import com.nercl.music.api.ApiLoginController;
import com.nercl.music.api.ApiQuestionController;
import com.nercl.music.api.MFileController;
import com.nercl.music.api.mobile.ApiExerciserController;
import com.nercl.music.api.mobile.ApiMobileAnswerController;
import com.nercl.music.api.mobile.ApiMobileQuestionController;
import com.nercl.music.controller.RandomCodeController;
import com.nercl.music.controller.SessionController;
import com.nercl.music.entity.user.Login;

@Component
public class SessionInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	        throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			if (handlerMethod.getBean() instanceof SessionController
			        || handlerMethod.getBean() instanceof RandomCodeController
			        || handlerMethod.getBean() instanceof ApiAnswerController
			        || handlerMethod.getBean() instanceof ApiExamPaperController
			        || handlerMethod.getBean() instanceof ApiLoginController
			        || handlerMethod.getBean() instanceof ApiQuestionController
			        || handlerMethod.getBean() instanceof MFileController
			        || handlerMethod.getBean() instanceof ApiExamineeController
			        || handlerMethod.getBean() instanceof ApiExerciserController
			        || handlerMethod.getBean() instanceof ApiMobileAnswerController
			        || handlerMethod.getBean() instanceof ApiMobileQuestionController) {
				return true;
			} else {
				HttpSession session = request.getSession();
				Login login = (Login) session.getAttribute("login");
				if (null == login) {
					String url = request.getContextPath() + "/session";
					response.sendRedirect(url);
					return false;
				}
			}
		}
		return true;
	}

}

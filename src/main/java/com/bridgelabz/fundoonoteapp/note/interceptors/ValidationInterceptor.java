package com.bridgelabz.fundoonoteapp.note.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.bridgelabz.fundoonoteapp.note.utility.Utility;
import com.bridgelabz.fundoonoteapp.user.interceptors.LoggerInterceptor;

import io.jsonwebtoken.Claims;


@Component
public class ValidationInterceptor extends HandlerInterceptorAdapter {
	
	private static Logger logger = LoggerFactory.getLogger(LoggerInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		logger.info("Before handling the request with request url: " + request.getRequestURI());
		String token=request.getHeader("token");
		Claims claims=Utility.parseJWT(token);
		request.setAttribute("userId",claims.getId());
		return true;
	}
	
	

}

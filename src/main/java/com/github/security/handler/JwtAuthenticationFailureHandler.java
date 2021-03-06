package com.github.security.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.github.security.exception.JwtExpireException;
import com.github.security.exception.JwtVerificationFailedException;

public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler{
	
	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFailureHandler.class);

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		response.setContentType("Application/json");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		PrintWriter out = response.getWriter();
		
		if (exception instanceof BadCredentialsException) {
			out.print("用户名或密码错误");
		} else if (exception instanceof JwtVerificationFailedException) {
			out.print("token 验证失败");
		} else if (exception instanceof JwtExpireException) {
			out.print("登录过期，请重新登录");
		}
		
		out.flush();
		logger.error(exception.getMessage());
	}	
}

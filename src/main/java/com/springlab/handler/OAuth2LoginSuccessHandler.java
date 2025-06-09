package com.springlab.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.springlab.auth.PrincipalDetail;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		PrincipalDetail userDetails = (PrincipalDetail) authentication.getPrincipal();
		System.out.println("OAuth2 로그인 성공: " + userDetails.getUsername());

		request.getSession().setAttribute("username", userDetails.getUsername());

		response.sendRedirect("/boardList");
	}

}

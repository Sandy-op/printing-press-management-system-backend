package com.printlok.pdp.services.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.printlok.pdp.models.user.User;
import com.printlok.pdp.security.JwtUtils;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class LinkGeneratorService {

	@Autowired
	private JwtUtils jwtUtils;

	public String getActivationLink(User user, HttpServletRequest request) {
		System.out.println(user);
		String token = jwtUtils.generateJwtForEmail(user.getEmail());

		String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath());
		String activationLink = baseUrl + "/api/auth/activate?token=" + token;

		return activationLink;
	}
	
	public String getUpgradeConfirmationLink(User user, Long requestId, HttpServletRequest request) {
		String token = jwtUtils.generateRoleUpgradeToken(user.getEmail(), requestId);

		String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath());
		return baseUrl + "/api/role-upgrade/verify?token=" + token;
	}

}

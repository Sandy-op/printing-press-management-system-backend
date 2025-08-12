package com.printlok.pdp.controllers.auth;

import com.printlok.pdp.dto.ResponseStructure;
import com.printlok.pdp.dto.auth.LoginRequest;
import com.printlok.pdp.dto.user.ForgotPasswordRequest;
import com.printlok.pdp.dto.user.ResetPasswordRequest;
import com.printlok.pdp.dto.user.UserRequest;
import com.printlok.pdp.dto.user.UserResponse;
import com.printlok.pdp.services.auth.AuthService;
import com.printlok.pdp.services.user.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final UserService userService;
	private final AuthService authService;

	/**
	 * User registration endpoint
	 */
	@PostMapping("/signup")
	public ResponseEntity<ResponseStructure<UserResponse>> signup(@Valid @RequestBody UserRequest userRequest,
			HttpServletRequest request) {
		return userService.createUser(userRequest, request);
	}

	/**
	 * User login endpoint
	 */
	@PostMapping("/login")
	public ResponseEntity<ResponseStructure<Map<String, Object>>> login(@Valid @RequestBody LoginRequest loginRequest,
			HttpServletRequest request) {
		return authService.authenticateUser(loginRequest, request);
	}

	@GetMapping("/me")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ResponseStructure<UserResponse>> getCurrentUser() {
		return userService.getCurrentUser();
	}

	/**
	 * Refresh access token using refresh token
	 */
	@PostMapping("/refresh")
	public ResponseEntity<ResponseStructure<Map<String, Object>>> refreshToken(HttpServletRequest request) {
		return authService.refreshToken(request);
	}

	/**
	 * User logout endpoint
	 */
	@PostMapping("/logout")
	public ResponseEntity<ResponseStructure<String>> logout(HttpServletRequest request) {
		return authService.logout(request);
	}

	/**
	 * Activate user account using activation token
	 */
	@GetMapping("/activate")
	public ResponseEntity<ResponseStructure<UserResponse>> activateAccount(@RequestParam String token) {
		return userService.activate(token);
	}

	/**
	 * Health check endpoint for auth service
	 */
	@GetMapping("/health")
	public ResponseEntity<ResponseStructure<String>> health() {
		ResponseStructure<String> response = new ResponseStructure<>();
		response.setStatusCode(200);
		response.setMessage("Auth service is running");
		response.setData("OK");
		return ResponseEntity.ok(response);
	}

	// === PASSWORD OPERATIONS ===

	@PostMapping("/forgot-password")
	public ResponseEntity<ResponseStructure<String>> forgotPassword(
			@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest, HttpServletRequest request) {
		return userService.forgotPassword(forgotPasswordRequest, request);
	}

	@PostMapping("/reset-password")
	public ResponseEntity<ResponseStructure<String>> resetPassword(@RequestParam String token,
			@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
		return userService.resetPassword(token, resetPasswordRequest);
	}

}
package com.printlok.pdp.controllers;

import com.printlok.pdp.dto.ResponseStructure;
import com.printlok.pdp.dto.auth.LoginRequest;
import com.printlok.pdp.dto.user.UserRequest;
import com.printlok.pdp.dto.user.UserResponse;
import com.printlok.pdp.services.user.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private UserService userService;

	public AuthController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/signup")
	public ResponseEntity<ResponseStructure<UserResponse>> saveUser(@Valid @RequestBody UserRequest userRequest,
			HttpServletRequest request) {
		return userService.createUser(userRequest, request);
	}

	@PostMapping("/login")
	public ResponseEntity<ResponseStructure<Map<String, Object>>> authenticateUser(
			@Validated @RequestBody LoginRequest loginRequest) {

		return userService.authenticateUser(loginRequest);
	}

	@GetMapping("/activate")
	public ResponseEntity<ResponseStructure<UserResponse>> activateUser(@RequestParam String token) {
		return userService.activate(token);
	}

	@GetMapping("/me")
	public ResponseEntity<ResponseStructure<UserResponse>> getCurrentUser() {
		return userService.getCurrentUser();
	}

}

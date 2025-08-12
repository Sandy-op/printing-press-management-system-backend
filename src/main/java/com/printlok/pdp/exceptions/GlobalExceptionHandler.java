package com.printlok.pdp.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.printlok.pdp.dto.ResponseStructure;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	// === AUTHENTICATION & AUTHORIZATION EXCEPTIONS ===

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ResponseStructure<String>> handleBadCredentials(BadCredentialsException ex) {
		log.warn("Authentication Failed: Invalid email or password provided - {}", ex.getMessage());
		ResponseStructure<String> response = new ResponseStructure<>();
		response.setMessage("Authentication Failed");
		response.setData("Invalid email or password provided");
		response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}

	@ExceptionHandler(TokenCompromiseException.class)
	public ResponseEntity<ResponseStructure<String>> handleTokenCompromise(TokenCompromiseException ex) {
		// High-severity log for monitoring and alerting
		log.warn("Token Compromise Detected: {}", ex.getMessage());
		ResponseStructure<String> response = new ResponseStructure<>();
		response.setMessage("Session Invalid");
		response.setData("Your session has been terminated for security reasons. Please log in again");
		response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ResponseStructure<String>> handleUnauthorized(UnauthorizedException ex) {
		log.warn("Unauthorized access attempt: {}", ex.getMessage());
		ResponseStructure<String> response = new ResponseStructure<>();
		response.setMessage("Access denied");
		response.setData(ex.getMessage());
		response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ResponseStructure<String>> handleAccessDenied(AccessDeniedException ex) {
		log.warn("Access denied: {}", ex.getMessage());
		ResponseStructure<String> response = new ResponseStructure<>();
		response.setMessage("Access forbidden");
		response.setData("You don't have permission to access this resource");
		response.setStatusCode(HttpStatus.FORBIDDEN.value());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
	}

	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<ResponseStructure<String>> handleInvalidCredentials(InvalidCredentialsException ex) {
		log.warn("Invalid credentials: {}", ex.getMessage());
		ResponseStructure<String> response = new ResponseStructure<>();
		response.setMessage("Authentication failed");
		response.setData(ex.getMessage());
		response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}

	// === USER DATA EXCEPTIONS ===

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ResponseStructure<String>> handleUserNotFound(UserNotFoundException ex) {
		log.warn("User not found: {}", ex.getMessage());
		ResponseStructure<String> response = new ResponseStructure<>();
		response.setMessage("User not found");
		response.setData(ex.getMessage());
		response.setStatusCode(HttpStatus.NOT_FOUND.value());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

	@ExceptionHandler({ DuplicateEmailException.class, DuplicatePhoneException.class })
	public ResponseEntity<ResponseStructure<String>> handleDuplicateData(RuntimeException ex) {
		log.warn("Duplicate data conflict: {}", ex.getMessage());
		ResponseStructure<String> response = new ResponseStructure<>();
		response.setMessage("Data conflict");
		response.setData(ex.getMessage());
		response.setStatusCode(HttpStatus.CONFLICT.value());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}

	@ExceptionHandler(InvalidRoleException.class)
	public ResponseEntity<ResponseStructure<String>> handleInvalidRole(InvalidRoleException ex) {
		log.error("Invalid role assignment: {}", ex.getMessage());
		ResponseStructure<String> response = new ResponseStructure<>();
		response.setMessage("Role assignment failed");
		response.setData(ex.getMessage());
		response.setStatusCode(HttpStatus.BAD_REQUEST.value());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	// === VALIDATION EXCEPTIONS ===

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ResponseStructure<Map<String, String>>> handleValidationErrors(
			MethodArgumentNotValidException ex) {
		log.warn("Validation errors occurred: {}", ex.getMessage());

		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors()
				.forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

		ex.getBindingResult().getGlobalErrors()
				.forEach(error -> errors.put(error.getObjectName(), error.getDefaultMessage()));

		ResponseStructure<Map<String, String>> response = new ResponseStructure<>();
		response.setMessage("Validation failed");
		response.setData(errors);
		response.setStatusCode(HttpStatus.BAD_REQUEST.value());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ResponseStructure<String>> handleIllegalArgument(IllegalArgumentException ex) {
		log.warn("Invalid argument: {}", ex.getMessage());
		ResponseStructure<String> response = new ResponseStructure<>();
		response.setMessage("Invalid request");
		response.setData(ex.getMessage());
		response.setStatusCode(HttpStatus.BAD_REQUEST.value());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<ResponseStructure<String>> handleIllegalState(IllegalStateException ex) {
		log.debug("Illegal state encountered in auth flow: {}", ex.getMessage());
		ResponseStructure<String> response = new ResponseStructure<>();
		response.setMessage("Invalid Request");
		response.setData(ex.getMessage());
		response.setStatusCode(HttpStatus.BAD_REQUEST.value());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	// === JWT & TOKEN EXCEPTIONS ===

	@ExceptionHandler(io.jsonwebtoken.ExpiredJwtException.class)
	public ResponseEntity<ResponseStructure<String>> handleExpiredJwt(io.jsonwebtoken.ExpiredJwtException ex) {
		log.warn("JWT token expired: {}", ex.getMessage());
		ResponseStructure<String> response = new ResponseStructure<>();
		response.setMessage("Token expired");
		response.setData("Your session has expired. Please login again");
		response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}

	@ExceptionHandler(io.jsonwebtoken.MalformedJwtException.class)
	public ResponseEntity<ResponseStructure<String>> handleMalformedJwt(io.jsonwebtoken.MalformedJwtException ex) {
		log.warn("Malformed JWT token: {}", ex.getMessage());
		ResponseStructure<String> response = new ResponseStructure<>();
		response.setMessage("Invalid token");
		response.setData("Token format is invalid");
		response.setStatusCode(HttpStatus.BAD_REQUEST.value());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@ExceptionHandler(io.jsonwebtoken.security.SignatureException.class)
	public ResponseEntity<ResponseStructure<String>> handleJwtSignature(
			io.jsonwebtoken.security.SignatureException ex) {
		log.warn("JWT signature validation failed: {}", ex.getMessage());
		ResponseStructure<String> response = new ResponseStructure<>();
		response.setMessage("Token validation failed");
		response.setData("Invalid token signature");
		response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ResponseStructure<String>> handleAuthenticationException(AuthenticationException ex) {
		log.warn("Authentication failed: {}", ex.getMessage());
		ResponseStructure<String> response = new ResponseStructure<>();
		response.setMessage("Authentication failed");
		response.setData("Please check your credentials and try again");
		response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}

	// === EMAIL & COMMUNICATION EXCEPTIONS ===

	@ExceptionHandler(org.springframework.mail.MailException.class)
	public ResponseEntity<ResponseStructure<String>> handleMailException(org.springframework.mail.MailException ex) {
		log.error("Email sending failed: {}", ex.getMessage());
		ResponseStructure<String> response = new ResponseStructure<>();
		response.setMessage("Email service error");
		response.setData("Failed to send email. Please try again later");
		response.setStatusCode(HttpStatus.SERVICE_UNAVAILABLE.value());
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
	}

	// === DATABASE EXCEPTIONS ===

	@ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
	public ResponseEntity<ResponseStructure<String>> handleDataIntegrityViolation(
			org.springframework.dao.DataIntegrityViolationException ex) {
		log.error("Data integrity violation: {}", ex.getMessage());
		ResponseStructure<String> response = new ResponseStructure<>();
		response.setMessage("Data constraint violation");
		response.setData("The operation violates data integrity constraints");
		response.setStatusCode(HttpStatus.CONFLICT.value());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}

	@ExceptionHandler(org.springframework.dao.DataAccessException.class)
	public ResponseEntity<ResponseStructure<String>> handleDataAccessException(
			org.springframework.dao.DataAccessException ex) {
		log.error("Database access error: {}", ex.getMessage());
		ResponseStructure<String> response = new ResponseStructure<>();
		response.setMessage("Database error");
		response.setData("A database error occurred. Please try again later");
		response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

	// === HTTP & REQUEST EXCEPTIONS ===

	@ExceptionHandler(org.springframework.web.HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ResponseStructure<String>> handleMethodNotSupported(
			org.springframework.web.HttpRequestMethodNotSupportedException ex) {
		log.warn("Method not supported: {}", ex.getMessage());
		ResponseStructure<String> response = new ResponseStructure<>();
		response.setMessage("Method not allowed");
		response.setData("HTTP method " + ex.getMethod() + " is not supported for this endpoint");
		response.setStatusCode(HttpStatus.METHOD_NOT_ALLOWED.value());
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
	}

	@ExceptionHandler(org.springframework.web.bind.MissingServletRequestParameterException.class)
	public ResponseEntity<ResponseStructure<String>> handleMissingParameter(
			org.springframework.web.bind.MissingServletRequestParameterException ex) {
		log.warn("Missing request parameter: {}", ex.getMessage());
		ResponseStructure<String> response = new ResponseStructure<>();
		response.setMessage("Missing required parameter");
		response.setData("Required parameter '" + ex.getParameterName() + "' is missing");
		response.setStatusCode(HttpStatus.BAD_REQUEST.value());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@ExceptionHandler(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ResponseStructure<String>> handleTypeMismatch(
			org.springframework.web.method.annotation.MethodArgumentTypeMismatchException ex) {
		log.warn("Parameter type mismatch: {}", ex.getMessage());
		ResponseStructure<String> response = new ResponseStructure<>();
		response.setMessage("Invalid parameter type");
		response.setData("Parameter '" + ex.getName() + "' should be of type " + ex.getRequiredType().getSimpleName());
		response.setStatusCode(HttpStatus.BAD_REQUEST.value());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	// === GENERIC EXCEPTION HANDLER (FALLBACK) ===

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseStructure<String>> handleOtherExceptions(Exception ex, WebRequest request) {
		log.error("Unexpected error occurred at {}: {}", request.getDescription(false), ex.getMessage(), ex);

		ResponseStructure<String> response = new ResponseStructure<>();
		response.setMessage("Internal server error");
		response.setData("An unexpected error occurred. Please try again later");
		response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

	// === CUSTOM APPLICATION EXCEPTIONS ===

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ResponseStructure<String>> handleRuntimeException(RuntimeException ex) {
		log.error("Runtime exception: {}", ex.getMessage(), ex);
		ResponseStructure<String> response = new ResponseStructure<>();
		response.setMessage("Operation failed");
		response.setData(ex.getMessage());
		response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}
}
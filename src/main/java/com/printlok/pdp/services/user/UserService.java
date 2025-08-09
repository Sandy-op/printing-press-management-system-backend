package com.printlok.pdp.services.user;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.printlok.pdp.dao.UserDao;
import com.printlok.pdp.dto.ResponseStructure;
import com.printlok.pdp.dto.auth.LoginRequest;
import com.printlok.pdp.dto.email.EmailConfiguration;
import com.printlok.pdp.dto.user.UserRequest;
import com.printlok.pdp.dto.user.UserResponse;
import com.printlok.pdp.exceptions.DuplicateEmailException;
import com.printlok.pdp.exceptions.DuplicatePhoneException;
import com.printlok.pdp.exceptions.InvalidRoleException;
import com.printlok.pdp.exceptions.UnauthorizedException;
import com.printlok.pdp.exceptions.UserNotFoundException;
import com.printlok.pdp.models.role.Role;
import com.printlok.pdp.models.user.User;
import com.printlok.pdp.repositories.role.RoleRepository;
import com.printlok.pdp.repositories.user.UserRepository;
import com.printlok.pdp.security.JwtUtils;
import com.printlok.pdp.services.email.EmailService;
import com.printlok.pdp.services.email.LinkGeneratorService;
import com.printlok.pdp.utils.AccountStatus;
import com.printlok.pdp.utils.ERole;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserDao userDao;
	private final LinkGeneratorService linkGeneratorService;
	private final EmailConfiguration emailConfiguration;
	private final EmailService mailService;
	private final AuthenticationManager authenticationManager;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtils jwtUtils;
	private final UserRepository userRepository;

	// method to create new user (signup)
	public ResponseEntity<ResponseStructure<UserResponse>> createUser(@Valid @RequestBody UserRequest userRequest,
			HttpServletRequest request) {

		ResponseStructure<UserResponse> structure = new ResponseStructure<>();

		if (userDao.existsByEmail(userRequest.getEmail())) {
			throw new DuplicateEmailException("Email already in use: " + userRequest.getEmail());
		}

		if (userDao.existsByPhone(userRequest.getPhone())) {
			throw new DuplicatePhoneException("Phone number already in use: " + userRequest.getPhone());
		}

		User user = mapToUser(userRequest);
		user.setAccountStatus(AccountStatus.IN_ACTIVE);
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		Set<Role> roles = new HashSet<>();
		roleRepository.findByRole(ERole.USER).ifPresentOrElse(roles::add, () -> {
			throw new InvalidRoleException("Default USER role not found in database.");
		});
		user.setRoles(roles);

		user = userDao.saveUser(user);

		String activation_link = linkGeneratorService.getActivationLink(user, request);
		emailConfiguration.setSubject("Activate Your Account");
		emailConfiguration.setText(
				"Dear User, please activate your account by clicking on the following link: " + activation_link);
		emailConfiguration.setToAddress(user.getEmail());

		structure.setMessage(mailService.sendMail(emailConfiguration));
		structure.setData(mapToUserResponse(user));
		structure.setStatusCode(HttpStatus.CREATED.value());

		return ResponseEntity.status(HttpStatus.CREATED).body(structure);
	}

	// === Method To Verify User ===
	public ResponseEntity<ResponseStructure<Map<String, Object>>> authenticateUser(LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		// Set JWT in secure HTTP-only cookie
		ResponseCookie cookie = ResponseCookie.from("jwt", jwt).httpOnly(true).secure(true) // enable only for HTTPS
				.sameSite("None").path("/").maxAge(jwtUtils.getAuthTokenExpiryInSeconds()).build();

		Map<String, Object> data = Map.of("email", userDetails.getUsername(), "roles", userDetails.getAuthorities());

		ResponseStructure<Map<String, Object>> structure = new ResponseStructure<>();
		structure.setMessage("Login successful");
		structure.setData(data);
		structure.setStatusCode(HttpStatus.OK.value());

		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(structure);
	}

	// === Activation Logic To Activate Account Using Activation Link ===
	public ResponseEntity<ResponseStructure<UserResponse>> activate(String token) {
		ResponseStructure<UserResponse> structure = new ResponseStructure<>();

		if (!jwtUtils.validateJwtToken(token)) {
			throw new IllegalArgumentException("Invalid or expired token.");
		}

		String email = jwtUtils.extractUsername(token);
		String type = jwtUtils.extractClaim(token, claims -> claims.get("type", String.class));

		if (!"email-verification".equals(type)) {
			throw new IllegalArgumentException("Token type mismatch.");
		}

		Optional<User> optionalUser = userDao.findByEmail(email);
		if (optionalUser.isEmpty()) {
			throw new UserNotFoundException("User not found.");
		}

		User user = optionalUser.get();
		if (user.getAccountStatus().equals(AccountStatus.ACTIVE)) {
			structure.setMessage("Account is already activated.");
		} else {
			user.setAccountStatus(AccountStatus.ACTIVE);
			userDao.saveUser(user);
			structure.setMessage("Account activated successfully.");
		}

		structure.setData(mapToUserResponse(user));
		structure.setStatusCode(HttpStatus.OK.value());

		return ResponseEntity.ok(structure);
	}

	// === Retrive All Users Methods ===
	public ResponseEntity<ResponseStructure<List<User>>> getAllUsers() {
		List<User> users = userRepository.findAll();

		ResponseStructure<List<User>> structure = new ResponseStructure<>();
		structure.setStatusCode(HttpStatus.OK.value());

		if (users.isEmpty()) {
			structure.setMessage("No users found");
		} else {
			structure.setMessage("Users fetched successfully");
		}

		structure.setData(users);

		return ResponseEntity.ok(structure);
	}

	// === method to get current user details ===
	public ResponseEntity<ResponseStructure<UserResponse>> getCurrentUser() {
		// Get authentication from SecurityContext
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()
				|| authentication.getPrincipal().equals("anonymousUser")) {
			throw new UnauthorizedException("User is not authenticated");
		}

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		// Fetch full user from DB (so we have all fields)
		User user = userRepository.findByEmail(userDetails.getEmail())
				.orElseThrow(() -> new UserNotFoundException("User not found"));

		ResponseStructure<UserResponse> structure = new ResponseStructure<>();
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("User details fetched successfully");
		structure.setData(mapToUserResponse(user));

		return ResponseEntity.ok(structure);
	}

	// === Helper Methods ===

	private User mapToUser(UserRequest userRequest) {
		return User.builder().email(userRequest.getEmail()).name(userRequest.getName()).phone(userRequest.getPhone())
				.gender(userRequest.getGender()).age(userRequest.getAge()).password(userRequest.getPassword()).build();
	}

	private UserResponse mapToUserResponse(User user) {
		return UserResponse.builder().name(user.getName()).email(user.getEmail()).id(user.getId())
				.gender(user.getGender()).phone(user.getPhone()).age(user.getAge()).build();
	}

}

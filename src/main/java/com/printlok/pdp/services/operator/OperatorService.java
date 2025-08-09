package com.printlok.pdp.services.operator;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.printlok.pdp.dto.ResponseStructure;
import com.printlok.pdp.dto.user.UserResponse;
import com.printlok.pdp.models.user.User;
import com.printlok.pdp.repositories.user.UserRepository;
import com.printlok.pdp.utils.ERole;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OperatorService {

	private final UserRepository userRepository;

	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ResponseStructure<List<UserResponse>>> getAllOperators() {
		List<User> operators = userRepository.findByRoles_Role(ERole.OPERATOR);

		List<UserResponse> responseList = operators.stream().map(this::mapToUserResponse).collect(Collectors.toList());

		ResponseStructure<List<UserResponse>> response = new ResponseStructure<>();
		response.setMessage("List of all operators");
		response.setStatusCode(HttpStatus.OK.value());
		response.setData(responseList);

		return ResponseEntity.ok(response);
	}

	private UserResponse mapToUserResponse(User user) {
		return UserResponse.builder().id(user.getId()).name(user.getName()).email(user.getEmail())
				.phone(user.getPhone()).age(user.getAge()).gender(user.getGender()).build();
	}
}
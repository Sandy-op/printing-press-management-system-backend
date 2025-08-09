package com.printlok.pdp.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {

	@Email(message = "Invalid email format")
	private String email;

	@NotBlank(message = "Password is mandatory")
	@Size(min = 8, max = 40, message = "Password length must be between 8 to 15")
	private String password;
}

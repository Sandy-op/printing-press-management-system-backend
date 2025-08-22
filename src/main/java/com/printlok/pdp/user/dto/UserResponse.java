package com.printlok.pdp.user.dto;

import java.util.Set;

import com.printlok.pdp.common.enums.AccountStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
	private Long id;
	private String name;
	private String email;
	private String phone;
	private String gender;
	private Integer age;
	private Set<String> roles;
	private AccountStatus accountStatus;
}
package com.printlok.pdp.dto.auth;

import java.util.List;

import com.printlok.pdp.utils.ERole;
import com.printlok.pdp.utils.RequestStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
	private String token;
	private String type = "Bearer";
	private Long id;
	private String name;
	private String email;
	private Long phone;
	private int age;
	private String gender;
	private List<ERole> roles;
	private RequestStatus upgradeRequestStatus;
	
	public JwtResponse(String token, Long id, String name, String email, Long phone, int age, String gender, List<ERole> roles, RequestStatus upgradeRequestStatus) {
		this.token = token;
		this.id = id;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.age = age;
		this.gender = gender;
		this.roles = roles;
		this.upgradeRequestStatus = upgradeRequestStatus;
	}
	
}

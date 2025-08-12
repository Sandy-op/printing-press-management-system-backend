package com.printlok.pdp.dto.auth;

import java.util.List;

import com.printlok.pdp.utils.enums.ERole;
import com.printlok.pdp.utils.enums.RequestStatus;

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
	
}

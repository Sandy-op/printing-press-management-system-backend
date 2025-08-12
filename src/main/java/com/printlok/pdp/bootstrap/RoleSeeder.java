package com.printlok.pdp.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.printlok.pdp.models.role.Role;
import com.printlok.pdp.repositories.role.RoleRepository;
import com.printlok.pdp.utils.enums.ERole;

@Component
public class RoleSeeder implements CommandLineRunner {

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public void run(String... args) {
		for (ERole role : ERole.values()) {
			roleRepository.findByRole(role).orElseGet(() -> {
				Role newRole = Role.builder().role(role).build();
				roleRepository.save(newRole);
				System.out.println("Seeded role: " + role);
				return newRole;
			});
		}
	}
}

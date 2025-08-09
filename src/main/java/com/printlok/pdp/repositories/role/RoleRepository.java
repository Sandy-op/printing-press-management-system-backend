package com.printlok.pdp.repositories.role;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.printlok.pdp.models.role.Role;
import com.printlok.pdp.utils.ERole;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByRole(ERole role);
}

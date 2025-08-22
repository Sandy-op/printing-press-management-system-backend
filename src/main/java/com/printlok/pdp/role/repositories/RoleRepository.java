package com.printlok.pdp.role.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.printlok.pdp.common.enums.ERole;
import com.printlok.pdp.role.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByRole(ERole role);
}

package com.printlok.pdp.repositories.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.printlok.pdp.models.user.User;
import com.printlok.pdp.utils.ERole;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	boolean existsByEmail(String email);

	boolean existsByPhone(String phone);

	Optional<User> findByEmail(String email);

	List<User> findByRoles_Role(ERole role);
}

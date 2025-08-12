package com.printlok.pdp.repositories.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.printlok.pdp.models.auth.LoginAttempt;
import com.printlok.pdp.models.auth.LoginAttemptId;

public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, LoginAttemptId> {
	Optional<LoginAttempt> findByIdentifierAndRemoteAddr(String identifier, String remoteAddr);
}
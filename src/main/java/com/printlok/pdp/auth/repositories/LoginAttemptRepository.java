package com.printlok.pdp.auth.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.printlok.pdp.auth.models.LoginAttempt;
import com.printlok.pdp.auth.models.LoginAttemptId;

public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, LoginAttemptId> {
	Optional<LoginAttempt> findByIdentifierAndRemoteAddr(String identifier, String remoteAddr);
}
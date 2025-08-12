package com.printlok.pdp.repositories.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import com.printlok.pdp.models.auth.JwtBlacklist;

public interface JwtBlacklistRepository extends JpaRepository<JwtBlacklist, String> {
}
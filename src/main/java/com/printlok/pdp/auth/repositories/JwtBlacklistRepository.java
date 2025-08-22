package com.printlok.pdp.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.printlok.pdp.auth.models.JwtBlacklist;

public interface JwtBlacklistRepository extends JpaRepository<JwtBlacklist, String> {
}
package com.printlok.pdp.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.printlok.pdp.services.user.UserDetailsImpl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtils {

    @Value("${pdp.app.secretKey}")
    private String secretKey;

    @Value("${pdp.app.auth.token.expiration.ms}")
    private int authTokenExpirationMs;

    private SecretKey key;

    private final int EMAIL_VERIFICATION_EXPIRATION_MS = 15 * 60 * 1000;  // 15 minutes
    private final int PASSWORD_RESET_EXPIRATION_MS = 10 * 60 * 1000;      // 10 minutes
    private final int ROLE_UPGRADE_EXPIRATION_MS = 15 * 60 * 1000;        // 15 minutes

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // === General JWT Methods ===
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateJwtToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            Date expiration = claims.getExpiration();

            if (expiration != null && expiration.before(new Date())) {
                throw new ExpiredJwtException(null, claims, "Token expired");
            }

            return true;
        } catch (ExpiredJwtException e) {
            log.warn("JWT expired: {}", e.getMessage());
        } catch (SignatureException e) {
            log.warn("JWT signature invalid: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("JWT malformed: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("JWT unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("JWT claims empty: {}", e.getMessage());
        }

        return false;
    }

    // === Auth Token ===
    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .subject(userPrincipal.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + authTokenExpirationMs))
                .signWith(key)
                .compact();
    }

    // === Email Verification Token ===
    public String generateJwtForEmail(String email) {
        return Jwts.builder()
                .subject(email)
                .claim("type", "email-verification")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EMAIL_VERIFICATION_EXPIRATION_MS))
                .signWith(key)
                .compact();
    }

    // === Password Reset Token ===
    public String generateJwtForPasswordReset(String email) {
        return Jwts.builder()
                .subject(email)
                .claim("type", "password-reset")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + PASSWORD_RESET_EXPIRATION_MS))
                .signWith(key)
                .compact();
    }

    // === Role Upgrade Token ===
    public String generateRoleUpgradeToken(String email, Long requestId) {
        return Jwts.builder()
                .subject(email)
                .claim("type", "role-upgrade")
                .claim("requestId", requestId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ROLE_UPGRADE_EXPIRATION_MS))
                .signWith(key)
                .compact();
    }

    // === Expose Expiry Durations (for cookies) ===
    public long getAuthTokenExpiryInSeconds() {
        return authTokenExpirationMs / 1000L;
    }

    public long getEmailVerificationExpiryInSeconds() {
        return EMAIL_VERIFICATION_EXPIRATION_MS / 1000L;
    }

    public long getPasswordResetExpiryInSeconds() {
        return PASSWORD_RESET_EXPIRATION_MS / 1000L;
    }

    public long getRoleUpgradeExpiryInSeconds() {
        return ROLE_UPGRADE_EXPIRATION_MS / 1000L;
    }
}

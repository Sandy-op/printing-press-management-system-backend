package com.printlok.pdp.models.user;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.printlok.pdp.models.role.Role;
import com.printlok.pdp.utils.enums.AccountStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false, unique = true)
	private String phone;

	@Column(nullable = false)
	private int age;

	@Column(nullable = false)
	private String gender;

	@Column(nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@Builder.Default
	private AccountStatus accountStatus = AccountStatus.IN_ACTIVE;

	@Builder.Default
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	@Column(name = "is_email_verified", nullable = false)
	@Builder.Default
	private boolean isEmailVerified = false;

	@Column(name = "last_login_at")
	private Instant lastLoginAt;

	@Column(name = "created_at", updatable = false)
	@Builder.Default
	private Instant createdAt = Instant.now();

	@Column(name = "updated_at")
	private Instant updatedAt;

	@PrePersist
	protected void onCreate() {
		this.createdAt = Instant.now();
		this.updatedAt = Instant.now();
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = Instant.now();
	}
}

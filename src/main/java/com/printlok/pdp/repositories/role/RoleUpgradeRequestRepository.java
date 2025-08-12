package com.printlok.pdp.repositories.role;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.printlok.pdp.models.role.RoleUpgradeRequest;
import com.printlok.pdp.models.user.User;
import com.printlok.pdp.utils.enums.RequestStatus;

public interface RoleUpgradeRequestRepository extends JpaRepository<RoleUpgradeRequest, Long> {

	List<RoleUpgradeRequest> findByStatus(RequestStatus status);

	List<RoleUpgradeRequest> findByUser(User user);
}

package com.printlok.pdp.role.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.printlok.pdp.common.enums.RequestStatus;
import com.printlok.pdp.role.models.RoleUpgradeRequest;
import com.printlok.pdp.user.models.User;

public interface RoleUpgradeRequestRepository extends JpaRepository<RoleUpgradeRequest, Long> {

	List<RoleUpgradeRequest> findByStatus(RequestStatus status);

	List<RoleUpgradeRequest> findByUser(User user);
}

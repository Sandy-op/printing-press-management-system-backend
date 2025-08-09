package com.printlok.pdp.dto.role;


import java.time.LocalDateTime;
import com.printlok.pdp.utils.RequestStatus;
import lombok.Data;

@Data
public class RoleUpgradeResponse {
    private Long requestId;
    private String companyName;
    private String gstNumber;
    private String userEmail;
    private RequestStatus status;
    private LocalDateTime requestDate;
}


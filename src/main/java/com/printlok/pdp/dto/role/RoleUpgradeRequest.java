package com.printlok.pdp.dto.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleUpgradeRequest {
	@NotBlank(message = "Company name is mandatory")
	private String companyName;

	@NotBlank(message = "Gst Number is mandatory")
	@Size(min = 15, max = 15, message = "GST No. must be 15 digit")
	private String gstNumber;
}

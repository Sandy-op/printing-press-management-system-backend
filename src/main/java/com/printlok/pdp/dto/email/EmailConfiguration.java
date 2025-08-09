package com.printlok.pdp.dto.email;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class EmailConfiguration {
	private String toAddress;
	private String text;
	private String subject;
}

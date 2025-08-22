package com.printlok.pdp.config;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class EmailConfig {
	private String toAddress;
	private String text;
	private String subject;
}

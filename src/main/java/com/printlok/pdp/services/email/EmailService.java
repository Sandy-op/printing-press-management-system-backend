package com.printlok.pdp.services.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.printlok.pdp.dto.email.EmailConfiguration;

@Service
public class EmailService {
	@Autowired
	private JavaMailSender javaMailSender;

	public String sendMail(EmailConfiguration emailConfiguration) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(emailConfiguration.getToAddress());
		message.setText(emailConfiguration.getText());
		message.setSubject(emailConfiguration.getSubject());

		javaMailSender.send(message);
		return "Registration sucessfull and Verification link has been sent";
	}
}

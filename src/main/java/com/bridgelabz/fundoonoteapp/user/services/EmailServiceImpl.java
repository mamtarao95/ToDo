package com.bridgelabz.fundoonoteapp.user.services;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.bridgelabz.fundoonoteapp.user.models.EmailDTO;


@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender emailSender;

	public void sendActivationEmail(EmailDTO emailDTO) throws Exception {
		System.out.println("into mail sender");

		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setTo(emailDTO.getTo());
		helper.setSubject(emailDTO.getSubject());
		helper.setText(emailDTO.getMessage());
		emailSender.send(message);
		

	}

}
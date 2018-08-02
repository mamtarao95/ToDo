package com.bridgelabz.fundoonoteapp.user.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bridgelabz.fundoonoteapp.user.models.EmailDTO;
import com.bridgelabz.fundoonoteapp.user.services.EmailService;

@Component
public class ConsumerImpl {

	@Autowired
	EmailService emailService;
	
	@RabbitListener(queues = "${jsa.rabbitmq.queue}")
	public void recievedMessage(EmailDTO emailDTO) throws Exception {
		System.out.println("Recieved Message: ");
		emailService.sendActivationEmail(emailDTO);
		
	}
}

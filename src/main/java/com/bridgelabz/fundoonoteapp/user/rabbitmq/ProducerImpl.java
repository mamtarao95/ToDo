package com.bridgelabz.fundoonoteapp.user.rabbitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.bridgelabz.fundoonoteapp.user.models.EmailDTO;

@Component
public class ProducerImpl {
	
	@Autowired
	private AmqpTemplate rabbitTemplate;
	
	@Value("${jsa.rabbitmq.exchange}")
	private String exchange;
	
	@Value("${jsa.rabbitmq.routingkey}")
	private String routingKey;
	
	public void produceMessage(EmailDTO emailDTO){
		rabbitTemplate.convertAndSend(exchange, routingKey, emailDTO);
		System.out.println("Send msg");
	}
	
	
}

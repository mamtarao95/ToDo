package com.bridgelabz.fundoonoteapp.user.rabbitmq;

import com.bridgelabz.fundoonoteapp.user.models.EmailDTO;

public interface Producer {
	
	public void produceMessage(EmailDTO emailDTO);

}

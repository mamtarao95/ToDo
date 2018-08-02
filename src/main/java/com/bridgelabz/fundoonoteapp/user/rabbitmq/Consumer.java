package com.bridgelabz.fundoonoteapp.user.rabbitmq;

import com.bridgelabz.fundoonoteapp.user.models.EmailDTO;

public interface Consumer {
	
	public void recievedMessage(EmailDTO emailDTO) throws Exception ;
}

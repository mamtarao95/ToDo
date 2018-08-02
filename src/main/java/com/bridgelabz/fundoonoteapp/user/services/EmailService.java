package com.bridgelabz.fundoonoteapp.user.services;

import com.bridgelabz.fundoonoteapp.user.models.EmailDTO;

public interface EmailService {
	void sendActivationEmail(EmailDTO emailDTO) throws Exception ;
}

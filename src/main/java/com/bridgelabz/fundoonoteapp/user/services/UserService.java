package com.bridgelabz.fundoonoteapp.user.services;


import javax.security.auth.login.LoginException;

import com.bridgelabz.fundoonoteapp.user.exceptions.ForgotPasswordException;
import com.bridgelabz.fundoonoteapp.user.exceptions.RegisterationException;
import com.bridgelabz.fundoonoteapp.user.exceptions.UserActivationException;
import com.bridgelabz.fundoonoteapp.user.models.LoginDTO;
import com.bridgelabz.fundoonoteapp.user.models.RegistrationDTO;
import com.bridgelabz.fundoonoteapp.user.models.SetPasswordDTO;

public interface UserService {
	public void registerUser(RegistrationDTO registrationDTO)throws  RegisterationException, Exception;

	public String loginUser(LoginDTO loginDTO) throws LoginException, UserActivationException;

	public void activateAccount(String token) throws UserActivationException;

	public void forgotPassword(String email) throws Exception;

	void resetPassword(SetPasswordDTO setPasswordDTO, String token) throws ForgotPasswordException;

}

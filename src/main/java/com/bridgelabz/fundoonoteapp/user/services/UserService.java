package com.bridgelabz.fundoonoteapp.user.services;


import javax.security.auth.login.LoginException;

import com.bridgelabz.fundoonoteapp.user.exceptions.UserNotFoundException;
import com.bridgelabz.fundoonoteapp.user.exceptions.UserNotUniqueException;
import com.bridgelabz.fundoonoteapp.user.exceptions.EmailIdNotFoundException;
import com.bridgelabz.fundoonoteapp.user.exceptions.IncorrectPasswordException;
import com.bridgelabz.fundoonoteapp.user.exceptions.TokenExpiresException;
import com.bridgelabz.fundoonoteapp.user.exceptions.UserNotActivatedException;
import com.bridgelabz.fundoonoteapp.user.models.LoginDTO;
import com.bridgelabz.fundoonoteapp.user.models.RegistrationDTO;
import com.bridgelabz.fundoonoteapp.user.models.SetPasswordDTO;

public interface UserService {
	public void registerUser(RegistrationDTO registrationDTO) throws UserNotUniqueException, IncorrectPasswordException;

	public String loginUser(LoginDTO loginDTO) throws LoginException, UserNotActivatedException, UserNotFoundException, IncorrectPasswordException;

	public void activateAccount(String token) throws UserNotActivatedException, TokenExpiresException, UserNotFoundException;

	public void forgotPassword(String email) throws EmailIdNotFoundException ;

	void resetPassword(SetPasswordDTO setPasswordDTO, String token) throws UserNotFoundException;

}

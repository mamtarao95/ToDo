package com.bridgelabz.fundoonoteapp.user.services;

import java.util.Optional;

import javax.security.auth.login.LoginException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonoteapp.user.exceptions.EmailIdNotFoundException;
import com.bridgelabz.fundoonoteapp.user.exceptions.IncorrectPasswordException;
import com.bridgelabz.fundoonoteapp.user.exceptions.TokenExpiresException;
import com.bridgelabz.fundoonoteapp.user.exceptions.UserNotActivatedException;
import com.bridgelabz.fundoonoteapp.user.exceptions.UserNotFoundException;
import com.bridgelabz.fundoonoteapp.user.exceptions.UserNotUniqueException;
import com.bridgelabz.fundoonoteapp.user.models.EmailDTO;
import com.bridgelabz.fundoonoteapp.user.models.LoginDTO;
import com.bridgelabz.fundoonoteapp.user.models.RegistrationDTO;
import com.bridgelabz.fundoonoteapp.user.models.SetPasswordDTO;
import com.bridgelabz.fundoonoteapp.user.models.User;
import com.bridgelabz.fundoonoteapp.user.rabbitmq.ProducerImpl;
import com.bridgelabz.fundoonoteapp.user.repositories.RedisRepository;
import com.bridgelabz.fundoonoteapp.user.repositories.UserRepository;
import com.bridgelabz.fundoonoteapp.user.utility.Utility;
import org.springframework.core.env.Environment;

import io.jsonwebtoken.Claims;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	EmailService emailService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ProducerImpl producer;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	private Environment env;

	@Autowired
	private RedisRepository redisRepo;

	@Override
	public String loginUser(LoginDTO loginDTO) throws LoginException, UserNotFoundException, IncorrectPasswordException, UserNotActivatedException {

		Utility.loginValidation(loginDTO);

		Optional<User> optionalUser = userRepository.findByEmail(loginDTO.getEmail());
		if (!optionalUser.isPresent()) {
			throw new UserNotFoundException("User is not present");
		}
		if (!optionalUser.get().isActivated()) {
			throw new UserNotActivatedException("User is not activated");
		}
		if (!passwordEncoder.matches(loginDTO.getPassword(), optionalUser.get().getPassword())) {
			throw new IncorrectPasswordException("Password is incorrect");
		}
		return Utility.tokenGenerator(optionalUser.get().getId());
	}

	@Override
	public void registerUser(RegistrationDTO registrationDTO) throws UserNotUniqueException, IncorrectPasswordException{

		Utility.validateUserInformation(registrationDTO);

		Optional<User> optionalUser = userRepository.findByEmail(registrationDTO.getEmail());
		if (optionalUser.isPresent()) {
			System.out.println("user present");
			throw new UserNotUniqueException("User with same email-id already exists!!");
		}

		User user = modelMapper.map(registrationDTO, User.class);
		user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
		userRepository.save(user);

		Optional<User> optionalUser1 = userRepository.findByEmail(user.getEmail());
		if (optionalUser1.isPresent()) {
			String token = Utility.tokenGenerator(optionalUser1.get().getId());
			EmailDTO emailDTO = new EmailDTO();
			emailDTO.setMessage(env.getProperty("activation.subject") + token);
			emailDTO.setSubject(env.getProperty("activation.link"));
			emailDTO.setTo(user.getEmail());
			producer.produceMessage(emailDTO);
		}
	}

	@Override
	public void activateAccount(String token) throws TokenExpiresException, UserNotFoundException {
		if (Utility.isTokenExpired(token)) {
			throw new TokenExpiresException("Token is expired and is no longer valid");
		}
		Claims claims = Utility.parseJWT(token);
		Optional<User> optionalUser = userRepository.findById(claims.getId());
		if (!optionalUser.isPresent()) {
			throw new UserNotFoundException("Activation failed since user is not registered!!");
		}
		User user = optionalUser.get();
		user.setActivated(true);
		
		userRepository.save(user);
	}

	@Override
	public void forgotPassword(String email) throws EmailIdNotFoundException {
		Optional<User> optionalUser = userRepository.findByEmail(email);

		if (!optionalUser.isPresent()) {
			throw new EmailIdNotFoundException("Email id doesn't exists");
		}
		String uuid = Utility.generateUUID();

		redisRepo.save(uuid, optionalUser.get().getId());
		
		EmailDTO emailDTO = new EmailDTO();
		emailDTO.setTo(email);
		emailDTO.setSubject(env.getProperty("forgotpassword.subject"));
		emailDTO.setMessage(env.getProperty("forgotpassword.link") + uuid);
		producer.produceMessage(emailDTO);
	}

	@Override
	public void resetPassword(SetPasswordDTO setPasswordDTO, String uuid) throws UserNotFoundException  {
		
		Utility.resetPasswordValidation(setPasswordDTO);

		String userId = redisRepo.getValue(uuid);
		if(userId==null) {
			throw new UserNotFoundException("user is not present");
		}
		Optional<User> optionalUser = userRepository.findById(userId);
			
		User user = optionalUser.get();
		user.setPassword(passwordEncoder.encode(setPasswordDTO.getNewPassword()));
		
		userRepository.save(user);
		redisRepo.delete(uuid);

	}

}

package com.bridgelabz.fundoonoteapp.user.controllers;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoonoteapp.user.exceptions.IncorrectPasswordException;
import com.bridgelabz.fundoonoteapp.user.exceptions.TokenExpiresException;
import com.bridgelabz.fundoonoteapp.user.exceptions.UserNotActivatedException;
import com.bridgelabz.fundoonoteapp.user.exceptions.UserNotFoundException;
import com.bridgelabz.fundoonoteapp.user.models.LoginDTO;
import com.bridgelabz.fundoonoteapp.user.models.RegistrationDTO;
import com.bridgelabz.fundoonoteapp.user.models.Response;
import com.bridgelabz.fundoonoteapp.user.models.SetPasswordDTO;
import com.bridgelabz.fundoonoteapp.user.services.FacebookService;
import com.bridgelabz.fundoonoteapp.user.services.UserService;

@Controller
@RequestMapping("/user")
@ResponseBody
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private FacebookService facebookService;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<Response> loginUser(@RequestBody LoginDTO loginDTO, HttpServletResponse res)
			throws LoginException, UserNotActivatedException, UserNotFoundException, IncorrectPasswordException {
		String token = userService.loginUser(loginDTO);
		Response responseDTO = new Response();
		responseDTO.setMessage("Login Successfull!!");
		responseDTO.setStatus(2);
		res.setHeader("token", token);
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);

	}
	
	@GetMapping("/createFacebookAuthorization")
	    public String createFacebookAuthorization(){
	        return facebookService.createFacebookAuthorizationURL();
	    }



@GetMapping("/getName")
public String getNameResponse(){
    return facebookService.getName();
}



@GetMapping("/facebook")
public void createFacebookAccessToken(@RequestParam("code") String code){
    facebookService.createFacebookAccessToken(code);
}



	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<Response> registerUser(@RequestBody RegistrationDTO registrationDto) throws Exception {
		userService.registerUser(registrationDto);
		Response responseDTO = new Response();
		responseDTO.setMessage("Registeration Successfull!!");
		responseDTO.setStatus(1);
		return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);

	}

	@RequestMapping(value = "/activateaccount", method = RequestMethod.GET)
	public ResponseEntity<Response> activateAccount(@RequestParam("token") String token)
			throws UserNotActivatedException, TokenExpiresException, UserNotFoundException {
		System.out.println("token: " + token);
		userService.activateAccount(token);
		Response responseDTO = new Response();
		responseDTO.setMessage("account activated Successfull!!");
		responseDTO.setStatus(3);
		return new ResponseEntity<>(responseDTO, HttpStatus.ACCEPTED);
	}
	

	@RequestMapping(value = "/forgotpassword", method = RequestMethod.POST)
	public ResponseEntity<Response> forgotPassword(@RequestBody String email) throws Exception {
		System.out.println("into fp");
		userService.forgotPassword(email);
		Response responseDTO = new Response();
		responseDTO.setMessage("An email has been sent successfully to reset your password!!");
		responseDTO.setStatus(4);
		return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
		}

	@RequestMapping(value = "/setpassword", method = RequestMethod.POST)
	public ResponseEntity<Response> resetPassword(@RequestBody SetPasswordDTO setPasswordDTO,
			@RequestParam("token") String token) throws Exception {
		userService.resetPassword(setPasswordDTO, token);
		Response responseDTO = new Response();
		responseDTO.setMessage("New Password has been set Successfully!!");
		responseDTO.setStatus(5);
		return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);

	}
	

}

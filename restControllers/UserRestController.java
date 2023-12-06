package com.dalel.microsservice.restControllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dalel.microsservice.entities.Role;
import com.dalel.microsservice.entities.User;
import com.dalel.microsservice.service.UserService;
@RestController
@CrossOrigin(origins = "*")
public class UserRestController {
	@Autowired
	UserService userService;
	
	
	
	
	
	@RequestMapping(path = "all", method = RequestMethod.GET)
	public List<User> getAllUsers() {
		return userService.findAllUsers();
	}
	
	@PostMapping("/register")
    public User registerUser(@RequestBody User user) {
          return userService.registerUser(user);
    }
	
	
	
	@PostMapping("/checkcode")
    public String verifyUser(@RequestBody User request ) {
		String email = request.getEmail();
		int verificationCode= request.getVerificationCode();
        System.out.println("user activated: " + verificationCode);
        User user =userService.verifyCode(email, verificationCode);
        if (user!=null)
            return " account has been verified ";
        else
           return "account has not been verified";
    }}

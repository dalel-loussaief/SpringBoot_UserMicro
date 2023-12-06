package com.dalel.microsservice.service;

import java.util.List;

import com.dalel.microsservice.entities.Role;
import com.dalel.microsservice.entities.User;

public interface UserService {
	User saveUser(User user);
	User findUserByUsername (String username);
	Role addRole(Role role);
	User addRoleToUser(String username, String rolename);
	List<User> findAllUsers();
	User registerUser(User user);
	User findByEmail(String email);
	User verifyCode(String email,int verificationCode);
}
    


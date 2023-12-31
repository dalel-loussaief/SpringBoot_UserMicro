package com.dalel.microsservice.service;
import com.dalel.microsservice.Repos.RoleRepository;
import com.dalel.microsservice.Repos.UserRepository;
import com.dalel.microsservice.entities.Role;
import com.dalel.microsservice.entities.Token;
import com.dalel.microsservice.entities.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Transactional
@Service
public class UserServiceImpl implements UserService{
	@Autowired
	UserRepository userRep;
	@Autowired
	RoleRepository roleRep;
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private JavaMailSender mailSender;

	@Override
	public User saveUser(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		return userRep.save(user);
	}

	@Override
	public User findUserByUsername(String username) {
		return userRep.findByUsername(username);
	}

	@Override
	public Role addRole(Role role) {
		return roleRep.save(role);
	}

	@Override
	public User addRoleToUser(String username, String rolename) {
		User usr = userRep.findByUsername(username);
		Role r = roleRep.findByRole(rolename);
		System.out.println(r);
		if (usr.getRoles() == null) {
			usr.setRoles(new ArrayList<>());
		}
		if (!usr.getRoles().contains(r)) {
			usr.getRoles().add(r);
		}
		return usr;
	}

	@Override
	public List<User> findAllUsers() {
		return userRep.findAll();
	}

	@Override
	public User registerUser(User user) {
		
	/*	if (userRep.findByEmail(user.getEmail()) != null) {
			throw new RuntimeException("Email already registered");
		}
		System.out.println(user.getUsername());
		System.out.println(user.getPassword());

		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setEnabled(false);
		// set role
		List<Role> roles = new ArrayList<>();
		roles.add(roleRep.findRoleById(2L));
		user.setRoles(roles);
		// generate verification code
		int verificationCode = generateRandomVerificationCode();
		user.setVerificationCode(verificationCode);
		sendMail(user);
		return userRep.save(user);*/
		// Check if email is registered
				if (userRep.findByEmail(user.getEmail()) != null) {
					throw new RuntimeException("Email already registered");
				}
				user.setEnabled(false);
				user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
				// set role
				List<Role> roles = new ArrayList<>();
				roles.add(roleRep.findRoleById(2L));
				user.setRoles(roles);

				// create a token
				createVerificationToken(user, generateRandomVerificationCode());

				sendMail(user);

				return userRep.save(user);
	}

	private Token createVerificationToken(User user, int code) {
		Token token = new Token();
		token.setUser(user);
		token.setCode(code);
		token.setCreatedAt(LocalDateTime.now());
		token.setExpiredAt(LocalDateTime.now().plusHours(1));

		user.setToken(token);
		return token;
	}

	@Override
	public User findByEmail(String email) {
		return userRep.findByEmail(email);
	}

	private int generateRandomVerificationCode() {
		return new Random().nextInt((99999 - 10000) + 1) + 10000;
	}

	private void sendMail(User user) {
	    System.out.println("send to " + user.getEmail());
	    SimpleMailMessage msg = new SimpleMailMessage();
	    msg.setTo(user.getEmail());
	    System.out.println("send to 2 " + user.getEmail());
	    msg.setText("Hello " + user.getUsername() + "\nInsert your verification code : \n" + user.getToken().getCode()
	            + "\nto activate your account.");
	    msg.setSubject("Verification code");
	    mailSender.send(msg);
	    System.out.println(user.getEmail());
	}


//@Override
	//public User verifyCode(String email, int  int code) {
	/*	User user = findByEmail(email);

		if (user != null && user.getVerificationCode() == verificationCode) {
			user.setEnabled(true);
			saveUser(user);
			return user;

		} else {
			return null;
		}*/

@Override
public User verifyCode(String email, int verificationCode) {
	 User user = findByEmail(email);

	    if (user != null && user.getToken() != null) {
	        Token token = user.getToken();
	        if (token.getCode() == verificationCode && !token.isExpired()) {
	            user.setEnabled(true);
	        } else {
	            throw new RuntimeException("Incorrect or expired verification code");
	        }
	    } else {
	        throw new RuntimeException("Invalid verification code");
	    }

	    return userRep.save(user);
}}
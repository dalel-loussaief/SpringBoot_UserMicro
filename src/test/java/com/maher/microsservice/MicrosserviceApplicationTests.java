package com.maher.microsservice;

import com.dalel.microsservice.entities.Role;
import com.dalel.microsservice.entities.User;
import com.dalel.microsservice.service.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MicrosserviceApplicationTests {
    @Autowired
    UserService userService;
    @Test
	void contextLoads() {
	}
    
}

package com.vinn.vinnet.controllers.api;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vinn.vinnet.dto.UserDto;
import com.vinn.vinnet.service.UserService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {

	@Autowired
	private final UserService userService;

	@PostMapping("/user/create")
    public ResponseEntity<String> createUser(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("profileImageInput") MultipartFile profileImage) throws GeneralSecurityException, IOException {
		System.out.println("hi");
        UserDto userDto = new UserDto();
        userDto.setName(name);
        userDto.setEmail(email);
        userDto.setPassword(password);
        userDto.setProfileImageInput(profileImage);

        if (userService.createUser(userDto)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create user.");
        }
    }
}

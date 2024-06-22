package com.vinn.vinnet.controllers.api;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vinn.vinnet.dto.UserDto;
import com.vinn.vinnet.service.UserService;
import com.vinn.vinnet.util.GoogleDriveJSONConnector;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class SessionController {
    
    @Autowired
	private final UserService userService;

	@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
	@PostMapping("/session/create")
	public ResponseEntity<?> createUser(HttpSession session) {
        UserDto currentUser = (UserDto) session.getAttribute("currentUser");
		if (userService.getUserByEmail(currentUser.getEmail()) != null) {
			return ResponseEntity.status(HttpStatus.OK).body(currentUser);
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Session lost!!! Please Login again.");
		}
	}
	@PostMapping("/getFileUrl")
	public ResponseEntity<String> getFileUrl(@RequestParam("imageName") String imageName) {
		try {
			GoogleDriveJSONConnector driveConnector = new GoogleDriveJSONConnector();
			String fileId = driveConnector.getFileIdByName(imageName);
			//String imageUrl = driveConnector.getImageUrl(fileId);
			String thumbnailLink = driveConnector.getFileThumbnailLink(fileId);
        	return ResponseEntity.ok(thumbnailLink);
			//return ResponseEntity.status(HttpStatus.OK).body(downloadUrl);
		} catch (IOException | GeneralSecurityException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve image URL");
		}
	}
}

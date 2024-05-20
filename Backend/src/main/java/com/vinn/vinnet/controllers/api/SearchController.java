package com.vinn.vinnet.controllers.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vinn.vinnet.dto.UserDto;
import com.vinn.vinnet.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SearchController {
	
	@Autowired
	private final UserService userService;
	
	@PostMapping("/search")
	public ResponseEntity<?> search(@RequestParam ("value") String value) {
		List<UserDto> searchUserLists = userService.getUsersByName(value); 
		if (searchUserLists != null && !searchUserLists.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(searchUserLists);
		} else {
			return ResponseEntity.status(HttpStatus.OK).body("No result found.");
		}
	}
}

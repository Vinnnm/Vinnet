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
import com.vinn.vinnet.service.FriendshipService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class FriendshipController {
    
    @Autowired
	private final FriendshipService friendshipService;

	@PostMapping("/friendship/addFriend")
	public ResponseEntity<String> addFriend(@RequestParam ("userId") long userId, @RequestParam ("friendId") long friendId) {
		if (friendshipService.addFriend(userId, friendId)) {
			return ResponseEntity.status(HttpStatus.CREATED).body("Friend added successfully.");
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create user.");
		}
	}
	
	@PostMapping("/friendship/getAllPendingFriends")
    public ResponseEntity<?> getAllPendingFriends(@RequestParam ("friendId") long friendId) {
        List<UserDto> pendingFriends = friendshipService.getAllPendingFriends(friendId);
        if (pendingFriends != null && !pendingFriends.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(pendingFriends);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("No pending friends found.");
        }
    }
	
	@PostMapping("/friendship/getAllFriends")
    public ResponseEntity<?> getAllFriends(@RequestParam ("userId") long userId) {
        List<UserDto> mutualFriends = friendshipService.getAllFriends(userId);
        if (mutualFriends != null && !mutualFriends.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(mutualFriends);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("No friends found.");
        }
    }	

    @PostMapping("/friendship/mutualFriends")
    public ResponseEntity<?> getMutualFriends(@RequestParam ("userId1") long userId1, @RequestParam ("userId2") long userId2) {
        List<UserDto> mutualFriends = friendshipService.findMutualFriends(userId1, userId2);
        if (mutualFriends != null && !mutualFriends.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(mutualFriends);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("No mutual friends found.");
        }
    }

    @PostMapping("/friendship/acceptOrRemoveFriend")
	public ResponseEntity<String> acceptOrRemoveFriend(@RequestParam ("userId") long userId, @RequestParam ("friendId") long friendId, @RequestParam ("status") String status) {
		if (friendshipService.changeStatus(userId, friendId, status)) {
			return ResponseEntity.status(HttpStatus.CREATED).body("Friend " + status + " successfully.");
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to remove user.");
		}
	}
}

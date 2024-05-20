package com.vinn.vinnet.controllers.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vinn.vinnet.dto.PostDto;
import com.vinn.vinnet.service.PostService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/post")
@AllArgsConstructor
public class PostController {

	@Autowired
	private final PostService postService;

	@PostMapping("/create")
	public ResponseEntity<String> createPost(@RequestBody PostDto postDto) {
		if (postService.createPost(postDto)) {
			return ResponseEntity.status(HttpStatus.CREATED).body("Post created successfully.");
		} else {
			return ResponseEntity.status(HttpStatus.CREATED).body("Failed to create post.");
		}
	}

	@PostMapping("/view")
	public ResponseEntity<List<PostDto>> getAllPosts() {
		List<PostDto> postDtos = postService.getAllPosts();
		if (postDtos != null && !postDtos.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(postDtos);
		} else {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
	}
	
	@PostMapping("/react")
	public ResponseEntity<String> reactPost(@RequestParam ("userId") long userId, @RequestParam ("postId") long postId, @RequestParam ("react") boolean react) {
		if (postService.reactPost(userId, postId, react)) {
			return ResponseEntity.status(HttpStatus.CREATED).body("Post reacted successfully.");
		} else {
			return ResponseEntity.status(HttpStatus.CREATED).body("Failed to react post.");
		}
	}
}

package com.vinn.vinnet.service.implement;

import java.util.List;
import java.util.Optional;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vinn.vinnet.dto.PostDto;
import com.vinn.vinnet.model.Post;
import com.vinn.vinnet.model.PostLike;
import com.vinn.vinnet.model.User;
import com.vinn.vinnet.repository.PostLikeRepository;
import com.vinn.vinnet.repository.PostRepository;
import com.vinn.vinnet.repository.UserRepository;
import com.vinn.vinnet.service.PostService;
import com.vinn.vinnet.util.GoogleDriveJSONConnector;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImplement implements PostService {

	@Autowired
	private final PostRepository postRepo;

	@Autowired
	private final UserRepository userRepo;

	@Autowired
	private final PostLikeRepository postLikeRepo;

	@Override
	public boolean createPost(PostDto postDto) {
		ModelMapper modelMapper = new ModelMapper();
		Optional<User> userOptional = userRepo.findById(postDto.getUserId());
		if (userOptional.isPresent()) {
			postDto.setUser(userOptional.get());
			Post post = modelMapper.map(postDto, Post.class);
			try {
				postRepo.save(post);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}

	@Override
	public List<PostDto> getAllPosts() {
		ModelMapper modelMapper = new ModelMapper();
		List<Post> posts = postRepo.findAllByOrderByIdDesc();
		List<PostDto> postDtos = new ArrayList<>();
		for (Post post : posts) {
			try {
				GoogleDriveJSONConnector driveConnector = new GoogleDriveJSONConnector();
				String fileId = driveConnector.getFileIdByName(post.getUser().getProfileUrl());
				String thumbnailLink = driveConnector.getFileThumbnailLink(fileId);
				post.getUser().setProfileUrl(thumbnailLink);
			} catch (IOException | GeneralSecurityException e) {

			}
			PostDto postDto = modelMapper.map(post, PostDto.class);
			postDto.setLikeCount(postDto.getLikes().size());
			postDto.setCommentCount(postDto.getComments().size());
			postDtos.add(postDto);

		}
		return postDtos;
	}

	@Override
	public boolean reactPost(long userId, long postId, boolean react) {
		Optional<User> userOptional = userRepo.findById(userId);
		Optional<Post> postOptional = postRepo.findById(postId);

		if (userOptional.isPresent() && postOptional.isPresent()) {
			User user = userOptional.get();
			Post post = postOptional.get();

			Optional<PostLike> postLikeOptional = postLikeRepo.findByUserAndPost(user, post);

			if (react) {
				return handleReaction(postLikeOptional, user, post);
			} else {
				return handleUnreaction(postLikeOptional);
			}
		}
		return false;
	}

	private boolean handleReaction(Optional<PostLike> postLikeOptional, User user, Post post) {
		if (postLikeOptional.isPresent()) {
			return true; // Already liked
		}

		PostLike postLike = new PostLike();
		postLike.setUser(user);
		postLike.setPost(post);
		try {
			postLikeRepo.save(postLike);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean handleUnreaction(Optional<PostLike> postLikeOptional) {
		if (!postLikeOptional.isPresent()) {
			return false; // Nothing to unlike
		}

		try {
			postLikeRepo.delete(postLikeOptional.get());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}

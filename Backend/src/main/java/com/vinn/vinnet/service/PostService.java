package com.vinn.vinnet.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vinn.vinnet.dto.PostDto;

@Service
public interface PostService {
    boolean createPost(PostDto postDto);
    List<PostDto> getAllPosts();
	boolean reactPost(long userId, long postId, boolean react);
}

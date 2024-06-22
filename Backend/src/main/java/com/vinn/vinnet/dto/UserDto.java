package com.vinn.vinnet.dto;

import java.util.List;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.vinn.vinnet.model.Friendship;
import com.vinn.vinnet.model.Post;
import com.vinn.vinnet.model.Role;

import lombok.Data;

@Data
public class UserDto {
    private long id, allFriendsCount, mutualFriendsCount, createdAt;
    private String name, password, email, profileUrl, friendShipStatus;
    private boolean enabled, friend;
    private Set<Role> roles;
    private List<Post> posts;
    private List<Friendship> friendships;
    private MultipartFile profileImageInput;
}
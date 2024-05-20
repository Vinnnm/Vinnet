package com.vinn.vinnet.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vinn.vinnet.dto.UserDto;

@Service
public interface FriendshipService {
    boolean addFriend(long userId, long friendId);
    List<UserDto> getAllPendingFriends(long friendId);
    boolean changeStatus(long userId, long friendId, String status);
    List<UserDto> getAllFriends(long userId);
    List<UserDto> findByName(String name);
    List<UserDto> findMutualFriends(long userId1, long userId2);    
}

package com.vinn.vinnet.service.implement;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vinn.vinnet.dto.UserDto;
import com.vinn.vinnet.model.Friendship;
import com.vinn.vinnet.model.Status;
import com.vinn.vinnet.model.User;
import com.vinn.vinnet.repository.FriendshipRepository;
import com.vinn.vinnet.repository.UserRepository;
import com.vinn.vinnet.service.FriendshipService;
import com.vinn.vinnet.util.GoogleDriveJSONConnector;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FriendshipServiceImplement implements FriendshipService {

	@Autowired
	private final FriendshipRepository friendshipRepo;

	@Autowired
	private final UserRepository userRepo;

	@Override
	public boolean addFriend(long userId, long friendId) {
		Friendship friendship = new Friendship();
		Optional<User> userOptional = userRepo.findById(userId);
		Optional<User> friendOptional = userRepo.findById(friendId);
        if (userOptional.isPresent() && friendOptional.isPresent()) {
        	friendship.setUser(userOptional.get());
        	friendship.setFriend(friendOptional.get());
        }		
		friendship.setStatus(Status.PENDING.name());
		try {
			if (friendshipRepo.findByUserIdAndFriendId(userId, friendId).isEmpty()) {
				friendshipRepo.save(friendship);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public List<UserDto> getAllPendingFriends(long userId) {
		List<Friendship> pendingFriendships = friendshipRepo.findByFriendIdAndStatus(userId, Status.PENDING.name());
		List<UserDto> pendingFriends = new ArrayList<>();

		for (Friendship pendingFriend : pendingFriendships) {
			try {
				User pendingUser = pendingFriend.getUser();			
				UserDto userDto = new UserDto();
				userDto.setId(pendingUser.getId());
				userDto.setName(pendingUser.getName());
				//userDto.setProfileUrl(pendingUser.getProfileUrl());
				GoogleDriveJSONConnector driveConnector = new GoogleDriveJSONConnector();
				String fileId = driveConnector.getFileIdByName(pendingUser.getProfileUrl());
				String thumbnailLink = driveConnector.getFileThumbnailLink(fileId);
				userDto.setProfileUrl(thumbnailLink);
				userDto.setAllFriendsCount(getAllFriends(pendingUser.getId()).size());
				userDto.setMutualFriendsCount(findMutualFriends(userId, pendingUser.getId()).size());
				pendingFriends.add(userDto);
			} catch (IOException | GeneralSecurityException e) {
	
			}
		}

		return pendingFriends;
	}

	@Override
	public boolean changeStatus(long userId, long friendId, String status) {
		Optional<Friendship> friendshipOptional = friendshipRepo.findByUserIdAndFriendId(userId, friendId);
		if (friendshipOptional.isPresent()) {
			Friendship friendship = friendshipOptional.get();
			friendship.setStatus(status);
			try {
				if (status.equalsIgnoreCase("reject")) {
					friendshipRepo.delete(friendship);
				} else {
					friendshipRepo.save(friendship);
					updateInverseFriendshipStatus(friendId, userId);
				}
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}

	private void updateInverseFriendshipStatus(long userId, long friendId) {
		addFriend(userId, friendId);
		Optional<Friendship> inverseFriendshipOptional = friendshipRepo.findByUserIdAndFriendId(userId, friendId);
		if (inverseFriendshipOptional.isPresent()) {
			Friendship inverseFriendship = inverseFriendshipOptional.get();
			if (!inverseFriendship.getStatus().equalsIgnoreCase("accept")) {
				inverseFriendship.setStatus(Status.ACCEPT.name());
				friendshipRepo.save(inverseFriendship);
			}
		}
	}

	@Override
	public List<UserDto> getAllFriends(long userId) {
		List<Friendship> friendships = friendshipRepo.findByUserIdAndStatus(userId, Status.ACCEPT.name());
		List<UserDto> allFriends = new ArrayList<>();
		for (Friendship friend : friendships) {
			try {				
				User userFriend = friend.getFriend();
				UserDto userDto = new UserDto();
				userDto.setId(userFriend.getId());
				userDto.setName(userFriend.getName());
				GoogleDriveJSONConnector driveConnector = new GoogleDriveJSONConnector();
				String fileId = driveConnector.getFileIdByName(userFriend.getProfileUrl());
				String thumbnailLink = driveConnector.getFileThumbnailLink(fileId);
				userDto.setProfileUrl(thumbnailLink);
				allFriends.add(userDto);
			} catch (IOException | GeneralSecurityException e) {

			}			
		}
		return allFriends;
	}

	@Override
	public List<UserDto> findByName(String name) {
		List<User> searchFriends = friendshipRepo.findByUserId(userRepo.findIdByName(name));
		List<UserDto> searchFriendsDto = new ArrayList<>();
		for (User mutualFriend : searchFriends) {
			UserDto userDto = new UserDto();
			userDto.setId(mutualFriend.getId());
			userDto.setName(mutualFriend.getName());
			userDto.setProfileUrl(mutualFriend.getProfileUrl());
			searchFriendsDto.add(userDto);
		}
		return searchFriendsDto;
	}

	@Override
	public List<UserDto> findMutualFriends(long userId1, long userId2) {
		List<User> mutualFriends = friendshipRepo.findMutualFriends(userId1, userId2);
		List<UserDto> mutualFriendsNamesAndProfileUrl = new ArrayList<>();
		for (User mutualFriend : mutualFriends) {
			UserDto userDto = new UserDto();
			userDto.setId(mutualFriend.getId());
			userDto.setName(mutualFriend.getName());
			userDto.setProfileUrl(mutualFriend.getProfileUrl());
			mutualFriendsNamesAndProfileUrl.add(userDto);
		}
		return mutualFriendsNamesAndProfileUrl;
	}

}

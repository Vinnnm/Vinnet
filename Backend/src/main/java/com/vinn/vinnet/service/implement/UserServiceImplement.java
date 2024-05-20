package com.vinn.vinnet.service.implement;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.vinn.vinnet.dto.UserDto;
import com.vinn.vinnet.model.Friendship;
import com.vinn.vinnet.model.User;
import com.vinn.vinnet.repository.FriendshipRepository;
import com.vinn.vinnet.repository.UserRepository;
import com.vinn.vinnet.service.FriendshipService;
import com.vinn.vinnet.service.UserService;
import com.vinn.vinnet.util.GoogleDriveJSONConnector;
import com.vinn.vinnet.util.Helper;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImplement implements UserService {

	@Autowired
	private final UserRepository userRepo;
	
	@Autowired
	private final PasswordEncoder passwordEncoder;

	@Autowired
	private final Helper helper;
	
	@Autowired
	private final FriendshipService friendshipService;
	
	@Autowired
	private final FriendshipRepository friendshipRepo;

	@Override
	public boolean createUser(UserDto userDto) throws GeneralSecurityException, IOException {
		ModelMapper modelMapper = new ModelMapper();
		User user = modelMapper.map(userDto, User.class);
		String encodePassword = passwordEncoder.encode(userDto.getPassword());
		user.setPassword(encodePassword != null ? encodePassword : user.getPassword());
		user.setEnabled(true);
		File tempFile = File.createTempFile(user.getName() + "_" + Helper.getCurrentTimestamp(), null);
        userDto.getProfileImageInput().transferTo(tempFile);
        //String imageUrl = helper.uploadImageToDrive(tempFile);
		boolean isUploaded = helper.uploadImageToDrive(tempFile);
		if(isUploaded) {
			user.setProfileUrl(tempFile.getName());
		} else {
			return false;
		}		
		try {
			userRepo.save(user);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public UserDto getUserByName(String name) {
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userRepo.findByName(name), UserDto.class);
		try {
			GoogleDriveJSONConnector driveConnector = new GoogleDriveJSONConnector();
			String fileId = driveConnector.getFileIdByName(userDto.getProfileUrl());
			String thumbnailLink = driveConnector.getFileThumbnailLink(fileId);
			userDto.setProfileUrl(thumbnailLink);
		} catch (IOException | GeneralSecurityException e) {

		}
		return userDto;
	}
	
	@Override
	public UserDto getUserByEmail(String email) {
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userRepo.findByEmail(email), UserDto.class);
		try {
			GoogleDriveJSONConnector driveConnector = new GoogleDriveJSONConnector();
			String fileId = driveConnector.getFileIdByName(userDto.getProfileUrl());
			String thumbnailLink = driveConnector.getFileThumbnailLink(fileId);
			userDto.setProfileUrl(thumbnailLink);
		} catch (IOException | GeneralSecurityException e) {

		}
		return userDto;
	}
	
	@Override
	public List<UserDto> getUsersByName(String name) {
		ModelMapper modelMapper = new ModelMapper();
		List<User> users = userRepo.findAllByNameContainingOrderByNameAsc(name);
		List<UserDto> userDtos = new ArrayList<>();
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpSession session = attributes.getRequest().getSession(false);
		UserDto currentUser = (UserDto) session.getAttribute("currentUser");
		long loggedInUserId = currentUser.getId();
		for(User user : users) {
			UserDto userDto = modelMapper.map(user, UserDto.class);
			try {
				GoogleDriveJSONConnector driveConnector = new GoogleDriveJSONConnector();
				String fileId = driveConnector.getFileIdByName(userDto.getProfileUrl());
				String thumbnailLink = driveConnector.getFileThumbnailLink(fileId);
				userDto.setProfileUrl(thumbnailLink);
			} catch (IOException | GeneralSecurityException e) {

			}
			List<UserDto> allFriends = friendshipService.getAllFriends(loggedInUserId);
	        boolean isFriend = allFriends.stream().anyMatch(friend -> friend.getId() == userDto.getId());
	        userDto.setFriend(isFriend);
	        
	        String friendshipStatus = getFriendshipStatus(loggedInUserId, user.getId());
	        userDto.setFriendShipStatus(friendshipStatus);
	        
			userDtos.add(userDto);
		}
		return userDtos;
	}
	
	private String getFriendshipStatus(long userId, long friendId) {
	    Optional<Friendship> friendshipOptional = friendshipRepo.findByUserIdAndFriendId(userId, friendId);
	    if (friendshipOptional.isPresent()) {
	        Friendship friendship = friendshipOptional.get();
	        return friendship.getStatus();
	    }
	    return null;
	}

	@Override
	public UserDto getUserById(long id) {
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userRepo.findById(id), UserDto.class);
		try {
			GoogleDriveJSONConnector driveConnector = new GoogleDriveJSONConnector();
			String fileId = driveConnector.getFileIdByName(userDto.getProfileUrl());
			String thumbnailLink = driveConnector.getFileThumbnailLink(fileId);
			userDto.setProfileUrl(thumbnailLink);
		} catch (IOException | GeneralSecurityException e) {

		}
		return userDto;
	}	

}

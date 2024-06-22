package com.vinn.vinnet.dto;

import com.google.api.services.drive.model.User;

import lombok.Data;

@Data
public class FriendshipDto {
    private long id, userId, friendshipId, createdAt;
    private User user, friend;
    private String status;
}

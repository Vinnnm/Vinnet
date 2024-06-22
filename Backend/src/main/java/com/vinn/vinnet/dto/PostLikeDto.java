package com.vinn.vinnet.dto;

import com.vinn.vinnet.model.Post;
import com.vinn.vinnet.model.User;

import lombok.Data;

@Data
public class PostLikeDto {
    private long id, createdAt;
    private User user;
    private Post post;
}

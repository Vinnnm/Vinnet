package com.vinn.vinnet.dto;

import java.util.List;

import com.vinn.vinnet.model.PostComment;
import com.vinn.vinnet.model.PostLike;
import com.vinn.vinnet.model.User;

import lombok.Data;

@Data
public class PostDto {
    private long id, userId, likeCount, commentCount, createdAt;
    private String postText;
    private User user;
    private List<PostLike> likes;
    private List<PostComment> comments;
}

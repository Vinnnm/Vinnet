package com.vinn.vinnet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinn.vinnet.model.Post;
import com.vinn.vinnet.model.PostLike;
import com.vinn.vinnet.model.User;

public interface PostLikeRepository extends JpaRepository<PostLike, Long>{

	Optional<PostLike> findByUserAndPost(User user, Post post);

}

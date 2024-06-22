package com.vinn.vinnet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinn.vinnet.model.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByIdDesc();
}

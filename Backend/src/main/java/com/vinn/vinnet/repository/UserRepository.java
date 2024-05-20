package com.vinn.vinnet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinn.vinnet.model.User;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByName(String name);
    User findByEmail(String email);
    List<User> findAllByNameContainingOrderByNameAsc(String name);
    Long findIdByName(String name);
}
package com.shutter.photorize.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shutter.photorize.domain.user.model.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	Boolean existsByUsername(String username);

	User findByUsername(String username);
}

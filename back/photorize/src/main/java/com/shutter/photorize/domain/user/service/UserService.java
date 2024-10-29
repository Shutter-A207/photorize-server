package com.shutter.photorize.domain.user.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.shutter.photorize.domain.user.model.UserDto;
import com.shutter.photorize.domain.user.model.entity.User;
import com.shutter.photorize.domain.user.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	public void joinProcess(UserDto userDto) {
		String username = userDto.getUsername();
		String password = userDto.getPassword();

		Boolean isExist = userRepository.existsByUsername(username);

		if (isExist) {
			return;
		}

		User data = new User();

		data.setUsername(username);
		data.setPassword(bCryptPasswordEncoder.encode(password));
		data.setRole("ROLE_MEMBER");

		userRepository.save(data);
	}
}

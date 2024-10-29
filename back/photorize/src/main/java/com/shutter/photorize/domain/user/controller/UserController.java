package com.shutter.photorize.domain.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shutter.photorize.domain.user.model.UserDto;
import com.shutter.photorize.domain.user.service.UserService;

@Controller
@ResponseBody
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/join")
	public String join(UserDto userDto) {
		userService.joinProcess(userDto);
		return "ok";
	}
}

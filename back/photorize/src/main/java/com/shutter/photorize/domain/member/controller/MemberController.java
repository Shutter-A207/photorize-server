package com.shutter.photorize.domain.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shutter.photorize.domain.member.model.MemberDto;
import com.shutter.photorize.domain.member.service.MemberService;

@Controller
@ResponseBody
public class MemberController {

	private final MemberService memberService;

	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}

	@PostMapping("/join")
	public String join(MemberDto memberDto) {
		memberService.joinProcess(memberDto);
		return "ok";
	}
}

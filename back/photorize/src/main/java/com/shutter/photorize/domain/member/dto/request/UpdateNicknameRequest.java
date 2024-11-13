package com.shutter.photorize.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateNicknameRequest {

	@NotBlank
	@Size(min = 2, max = 8)
	@Pattern(
		regexp = "^[가-힣a-zA-Z0-9]*$",
		message = "닉네임은 한글과 영문자만 사용 가능합니다"
	)
	private String nickname;
}

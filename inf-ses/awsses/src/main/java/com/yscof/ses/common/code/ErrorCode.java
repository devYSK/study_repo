package com.yscof.ses.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode implements CInterface {
	NOT_VALID_EMAIL_REQUEST(-1, "잘못된 이메일 형식 입니다."),
	MAIL_RECEIVER_REQUIRED(-2, "전송에 필요한 이메일 주소가 없습니다."),
	MAIL_SEND_FAILED(-3, "메일 전송에 실패하였습니다.");

	private final Integer code;
	private final String message;
}

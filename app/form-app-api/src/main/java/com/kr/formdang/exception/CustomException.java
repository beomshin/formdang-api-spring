package com.kr.formdang.exception;

import com.kr.formdang.dto.GlobalCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CustomException extends Exception {

	private final GlobalCode code;

	public CustomException(GlobalCode code) {
		super(code.getMsg());
		this.code = code;
	}

}

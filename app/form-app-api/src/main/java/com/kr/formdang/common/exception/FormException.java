package com.kr.formdang.common.exception;

import com.kr.formdang.common.constant.ResultCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FormException extends Exception {

	private final ResultCode code;

	public FormException(ResultCode code) {
		super(code.getMsg());
		this.code = code;
	}

}

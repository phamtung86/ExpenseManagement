package com.vti.config.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
//@AllArgsConstructor
public class ErrorResponse {


	private String message;


	private String detailMessage;

	private Object error;

	private Integer code;

	private String moreInformation;

    public ErrorResponse(String message, String detailMessage, Object error, Integer code, String moreInformation) {
        this.message = message;
        this.detailMessage = detailMessage;
        this.error = error;
        this.code = code;
        this.moreInformation = moreInformation;
    }
}

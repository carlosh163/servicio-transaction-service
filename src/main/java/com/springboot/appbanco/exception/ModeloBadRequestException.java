package com.springboot.appbanco.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ResponseStatus(HttpStatus.NOT_FOUND)
public class ModeloBadRequestException extends RuntimeException {


	public ModeloBadRequestException(String mensaje) {
		super(mensaje);
	}

}

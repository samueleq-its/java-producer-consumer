package com.girque.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	public record ErrorResponse(
			int status,
			String message,
			LocalDateTime timestamp) {
	}

	@ExceptionHandler(ProdottoNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleProdottoNotFound(ProdottoNotFoundException ex) {
		ErrorResponse error = new ErrorResponse(
				ex.getStatusCode(),
				ex.getMessage(),
				ex.getTimeStamp());

		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	ResponseEntity<ErrorResponse> handleBadRequest(HttpMessageNotReadableException ex) {
		ErrorResponse error = new ErrorResponse(
				400,
				ex.getMessage(),
				LocalDateTime.now());

		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

}

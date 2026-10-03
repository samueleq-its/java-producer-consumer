package com.girque.exception;

import java.time.LocalDateTime;

public class ProdottoNotFoundException extends Exception {
	private final int STATUS_CODE = 404;
	private final LocalDateTime timeStamp;

	public ProdottoNotFoundException(String message, LocalDateTime timeStamp) {
		super(message);
		this.timeStamp = timeStamp;
	}

	public ProdottoNotFoundException() {
		this("Prodotto non trovato", LocalDateTime.now());
	}

	public int getStatusCode() {
		return STATUS_CODE;
	}

	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}
}

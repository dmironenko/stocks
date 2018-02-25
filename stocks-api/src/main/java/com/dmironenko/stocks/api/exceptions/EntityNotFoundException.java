package com.dmironenko.stocks.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {

	private static final String MESSAGE_PATTERN = "Object with id=%s doesn't exist";

	public EntityNotFoundException(final Long id) {
		super(String.format(MESSAGE_PATTERN, id));
	}
}

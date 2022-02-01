package com.heraizen.security.web;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponse {
	private int status;
	private String message;
	private LocalDateTime dateTime;
}

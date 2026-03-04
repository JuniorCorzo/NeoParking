package dev.angelcorzo.neoparking.api.commons.dto;

import java.time.OffsetDateTime;

public sealed interface BaseResponse permits Response, ResponseError {
	String status();
	String message();
	OffsetDateTime timestamp();
}

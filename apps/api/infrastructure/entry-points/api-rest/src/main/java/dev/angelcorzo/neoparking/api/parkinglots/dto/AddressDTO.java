package dev.angelcorzo.neoparking.api.parkinglots.dto;

public record AddressDTO(
		String street,
		String city,
		String state,
		String country,
		String zipCode
) {}

package dev.angelcorzo.neoparking.model.parkinglots;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class  Address {
    private String street;
    private String city;
    private String state;
    private String country;
    private String zipCode;
}

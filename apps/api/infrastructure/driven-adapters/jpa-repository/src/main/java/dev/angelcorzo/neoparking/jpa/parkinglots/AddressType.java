package dev.angelcorzo.neoparking.jpa.parkinglots;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Struct;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Struct(name = "address_t")
public class AddressType {
  private String street;
  private String city;
  private String state;
  private String country;
  private String zipCode;
}

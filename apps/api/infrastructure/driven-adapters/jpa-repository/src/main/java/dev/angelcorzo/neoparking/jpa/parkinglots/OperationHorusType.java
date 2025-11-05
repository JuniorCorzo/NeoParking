package dev.angelcorzo.neoparking.jpa.parkinglots;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Struct;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Struct(name = "operation_horus_t")
public class OperationHorusType {
	private String openTime;
	private String closeTime;
}

package dev.angelcorzo.neoparking.jpa.payments.mapper;

import dev.angelcorzo.neoparking.jpa.config.MapperStructConfig;
import dev.angelcorzo.neoparking.jpa.mappers.BaseMapper;
import dev.angelcorzo.neoparking.jpa.mappers.JacksonConverter;
import dev.angelcorzo.neoparking.jpa.payments.PaymentsData;
import dev.angelcorzo.neoparking.model.payments.Payments;
import org.mapstruct.Mapper;

@Mapper(
    config = MapperStructConfig.class,
    uses = {JacksonConverter.class})
public interface PaymentMapper extends BaseMapper<Payments, PaymentsData> {
  @Override
  Payments toEntity(PaymentsData data);

  @Override
  PaymentsData toData(Payments entity);
}

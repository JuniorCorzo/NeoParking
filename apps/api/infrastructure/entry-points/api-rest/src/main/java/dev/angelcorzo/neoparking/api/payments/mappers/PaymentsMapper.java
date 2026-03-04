package dev.angelcorzo.neoparking.api.payments.mappers;

import dev.angelcorzo.neoparking.api.commons.config.MapperStructConfig;
import dev.angelcorzo.neoparking.api.payments.dtos.request.check_out.check_out.CheckOutCommand;
import dev.angelcorzo.neoparking.api.payments.dtos.request.check_out.check_out.EmailCheckOutCommand;
import dev.angelcorzo.neoparking.api.payments.dtos.request.check_out.check_out.SMSCheckOutCommand;
import dev.angelcorzo.neoparking.api.payments.dtos.request.check_out.check_out.NoSendCheckOutCommand;
import dev.angelcorzo.neoparking.model.payments.valueobject.check_out.CheckOut;
import dev.angelcorzo.neoparking.model.payments.valueobject.check_out.EmailCheckOut;
import dev.angelcorzo.neoparking.model.payments.valueobject.check_out.SMSCheckOut;
import dev.angelcorzo.neoparking.model.payments.valueobject.check_out.NoSendCheckOut;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.SubclassMapping;
import org.mapstruct.SubclassMappings;

@Mapper(config = MapperStructConfig.class)
public interface PaymentsMapper {
  @SubclassMappings({
    @SubclassMapping(source = EmailCheckOutCommand.class, target = EmailCheckOut.class),
    @SubclassMapping(source = SMSCheckOutCommand.class, target = SMSCheckOut.class),
    @SubclassMapping(source = NoSendCheckOutCommand.class, target = NoSendCheckOut.class),
  })
  CheckOut toModel(CheckOutCommand command);

  @InheritConfiguration(name = "toModel")
  EmailCheckOut toEmailModel(EmailCheckOutCommand emailCheckOut);

  @InheritConfiguration(name = "toModel")
  SMSCheckOut toSMSModel(SMSCheckOutCommand emailCheckOut);

  @InheritConfiguration(name = "toModel")
  NoSendCheckOut toUrlModel(NoSendCheckOutCommand emailCheckOut);
}

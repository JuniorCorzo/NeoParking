package dev.angelcorzo.neoparking.api.slot.mappers;

import dev.angelcorzo.neoparking.api.commons.config.MapperStructConfig;
import dev.angelcorzo.neoparking.api.slot.dto.CreateSlotRequest;
import dev.angelcorzo.neoparking.api.slot.dto.SlotResponse;
import dev.angelcorzo.neoparking.api.slot.dto.UpdateSlotRequest;
import dev.angelcorzo.neoparking.model.slots.Slots;
import dev.angelcorzo.neoparking.usecase.createslot.CreateSlotUseCase;
import dev.angelcorzo.neoparking.usecase.editslot.EditSlotUseCase;
import org.mapstruct.Mapper;

@Mapper(config = MapperStructConfig.class)
public interface SlotsMapper {
  EditSlotUseCase.UpdateSlotCommand toModel(UpdateSlotRequest dto);

  CreateSlotUseCase.CreateSlotCommand toModel(CreateSlotRequest dto);

  SlotResponse toDto(Slots model);
}

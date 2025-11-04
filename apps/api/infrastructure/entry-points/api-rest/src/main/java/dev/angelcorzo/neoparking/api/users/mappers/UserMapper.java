package dev.angelcorzo.neoparking.api.users.mappers;

import dev.angelcorzo.neoparking.api.commons.config.MapperStructConfig;
import dev.angelcorzo.neoparking.api.users.dto.CreatedUserDTO;
import dev.angelcorzo.neoparking.api.users.dto.DeactivateUserDTO;
import dev.angelcorzo.neoparking.api.users.dto.ModifyRolDTO;
import dev.angelcorzo.neoparking.api.users.dto.UserDTO;
import dev.angelcorzo.neoparking.model.users.Users;
import dev.angelcorzo.neoparking.usecase.deactivateuser.DeactivateUserUseCase;
import dev.angelcorzo.neoparking.usecase.modifyuserrole.ModifyUserRoleUseCase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(config = MapperStructConfig.class)
public interface UserMapper {
  Users toModel(CreatedUserDTO createdUserDTO);

  ModifyUserRoleUseCase.ModifyUserRole toModel(ModifyRolDTO modifyRolDTO);

  @Mappings(@Mapping(target = "userIdToDeactivate", source = "userId"))
  DeactivateUserUseCase.DeactivateUserCommand toModel(DeactivateUserDTO dto);

  UserDTO toDTO(Users user);
}

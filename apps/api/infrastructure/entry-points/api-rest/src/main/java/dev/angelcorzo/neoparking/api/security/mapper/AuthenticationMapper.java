package dev.angelcorzo.neoparking.api.security.mapper;

import dev.angelcorzo.neoparking.api.commons.config.MapperStructConfig;
import dev.angelcorzo.neoparking.api.security.dto.AuthenticationResponseDTO;
import dev.angelcorzo.neoparking.api.security.dto.UserCredentialsDTO;
import dev.angelcorzo.neoparking.model.authentication.AuthResponse;
import dev.angelcorzo.neoparking.usecase.login.LoginUseCase;
import org.mapstruct.Mapper;

@Mapper(config = MapperStructConfig.class)
public interface AuthenticationMapper {
  LoginUseCase.UserCredentials toModel(UserCredentialsDTO dto);

  UserCredentialsDTO toDTO(LoginUseCase.UserCredentials model);

  AuthResponse toModel(AuthenticationResponseDTO dto);

  AuthenticationResponseDTO toDTO(AuthResponse model);
}

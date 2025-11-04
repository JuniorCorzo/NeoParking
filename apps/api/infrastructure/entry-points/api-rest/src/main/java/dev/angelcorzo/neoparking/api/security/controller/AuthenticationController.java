package dev.angelcorzo.neoparking.api.security.controller;

import dev.angelcorzo.neoparking.api.commons.dto.Response;
import dev.angelcorzo.neoparking.api.security.dto.AuthenticationResponseDTO;
import dev.angelcorzo.neoparking.api.security.dto.UserCredentialsDTO;
import dev.angelcorzo.neoparking.api.security.mapper.AuthenticationMapper;
import dev.angelcorzo.neoparking.model.authentication.AuthResponse;
import dev.angelcorzo.neoparking.usecase.login.LoginUseCase;
import dev.angelcorzo.neoparking.usecase.refreshsession.RefreshSessionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
  private final LoginUseCase loginUseCase;
  private final RefreshSessionUseCase refreshSessionUseCase;
  private final AuthenticationMapper authenticationMapper;

  @PostMapping("/login")
  Response<AuthenticationResponseDTO> login(@RequestBody UserCredentialsDTO userCredentials) {
    AuthResponse authResponse =
        this.loginUseCase.auth(this.authenticationMapper.toModel(userCredentials));
    return Response.ok(this.authenticationMapper.toDTO(authResponse), "Login successful");
  }

  @PostMapping("/refresh/{refreshToken}")
  Response<AuthenticationResponseDTO> refreshSession(
      @PathVariable("refreshToken") String refreshToken) {
    AuthResponse newTokens = this.refreshSessionUseCase.refreshAccessToken(refreshToken);

    return Response.ok(this.authenticationMapper.toDTO(newTokens), "Refresh successful");
  }
}

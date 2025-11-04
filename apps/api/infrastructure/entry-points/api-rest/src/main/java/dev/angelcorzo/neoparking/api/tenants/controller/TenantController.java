package dev.angelcorzo.neoparking.api.tenants.controller;

import dev.angelcorzo.neoparking.api.commons.dto.Response;
import dev.angelcorzo.neoparking.api.tenants.dto.RegisterTenantDTO;
import dev.angelcorzo.neoparking.api.tenants.enums.TenantsMessages;
import dev.angelcorzo.neoparking.api.users.dto.UserDTO;
import dev.angelcorzo.neoparking.api.users.mappers.UserMapper;
import dev.angelcorzo.neoparking.model.tenants.Tenants;
import dev.angelcorzo.neoparking.model.users.Users;
import dev.angelcorzo.neoparking.usecase.registertenant.RegisterTenantUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
    value = "/tenants",
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Tenant Controller", description = "Operations pertaining to tenants")
public class TenantController {
  private final RegisterTenantUseCase registerTenantUseCase;
  private final UserMapper userMapper;

  @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  Response<UserDTO> registerTenant(@RequestBody @Valid RegisterTenantDTO registerTenant) {
    final Users user = this.userMapper.toModel(registerTenant.user());
    final Tenants tenant = Tenants.builder().companyName(registerTenant.companyName()).build();

    final Users userCreated = this.registerTenantUseCase.register(user, tenant);

    return Response.created(
        this.userMapper.toDTO(userCreated), TenantsMessages.TENANT_CREATED_SUCCESSFULLY.toString());
  }
}

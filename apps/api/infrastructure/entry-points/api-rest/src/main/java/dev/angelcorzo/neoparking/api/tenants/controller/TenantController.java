package dev.angelcorzo.neoparking.api.tenants.controller;

import dev.angelcorzo.neoparking.api.tenants.dto.RegisterTenantDTO;
import dev.angelcorzo.neoparking.usecase.registertenant.RegisterTenantUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/tenants", produces = "application/json")
@RequiredArgsConstructor
public class TenantController {
  private final RegisterTenantUseCase registerTenantUseCase;

  @PostMapping("/register")
  void registerTenant(RegisterTenantDTO tenant) {}
}

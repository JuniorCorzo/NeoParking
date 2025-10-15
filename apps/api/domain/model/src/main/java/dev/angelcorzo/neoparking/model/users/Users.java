package dev.angelcorzo.neoparking.model.users;

import dev.angelcorzo.neoparking.model.tenants.Tenants;
import dev.angelcorzo.neoparking.model.users.enums.Roles;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class Users {
    private UUID id;
    private String fullName;
    private String email;
    private String password;
    private Roles role;
    private Tenants tenant;
    private String contactInfo;
    private UUID deletedBy;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private OffsetDateTime deletedAt;
}

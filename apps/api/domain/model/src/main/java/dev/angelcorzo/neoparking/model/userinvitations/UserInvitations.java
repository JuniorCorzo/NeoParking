package dev.angelcorzo.neoparking.model.userinvitations;
import dev.angelcorzo.neoparking.model.tenants.Tenants;
import dev.angelcorzo.neoparking.model.users.Users;
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
public class UserInvitations {
    private UUID id;
    private Tenants tenant;
    private String invitedEmail;
    private Roles role;
    private UUID token;
    private UserInvitationStatus status;
    private Users invitedBy;
    private OffsetDateTime createdAt;
    private OffsetDateTime acceptedAt;
    private OffsetDateTime expiredAt;
}

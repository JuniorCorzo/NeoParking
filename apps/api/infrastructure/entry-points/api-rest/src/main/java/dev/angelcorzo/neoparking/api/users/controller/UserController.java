package dev.angelcorzo.neoparking.api.controller;

import dev.angelcorzo.neoparking.model.userinvitations.UserInvitations;
import dev.angelcorzo.neoparking.usecase.acceptinvitation.AcceptInvitationUseCase;
import dev.angelcorzo.neoparking.usecase.deactivateuser.DeactivateUserUseCase;
import dev.angelcorzo.neoparking.usecase.inviteuserwithrol.InviteUserWithRolUseCase;
import dev.angelcorzo.neoparking.usecase.modifyuserrole.ModifyUserRoleUseCase;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final InviteUserWithRolUseCase inviteUserWithRolUseCase;
    private final AcceptInvitationUseCase acceptInvitationUseCase;
    private final ModifyUserRoleUseCase modifyUserRoleUseCase;
    private final DeactivateUserUseCase deactivateUserUseCase;
    private final MeterRegistry meterRegistry;

    @PostMapping("/invite-user")
    UserInvitations inviteUserWithRol(@RequestBody InviteUserWithRolUseCase.InviteUserWithRole inviteUserWithRole) {
        this.meterRegistry.counter("neoparking.users.invited").increment();
        return this.inviteUserWithRolUseCase.registerInvitation(inviteUserWithRole);
    }
}

package dev.angelcorzo.neoparking.api.users.controller;

import dev.angelcorzo.neoparking.api.commons.dto.Response;
import dev.angelcorzo.neoparking.api.userinvitations.dto.InviteUserDTO;
import dev.angelcorzo.neoparking.api.userinvitations.dto.UserInvitationsDTO;
import dev.angelcorzo.neoparking.api.userinvitations.mapper.UserInvitationsMapper;
import dev.angelcorzo.neoparking.api.users.dto.CreatedUserDTO;
import dev.angelcorzo.neoparking.api.users.dto.DeactivateUserDTO;
import dev.angelcorzo.neoparking.api.users.dto.ModifyRolDTO;
import dev.angelcorzo.neoparking.api.users.dto.UserDTO;
import dev.angelcorzo.neoparking.api.users.enums.UserMessages;
import dev.angelcorzo.neoparking.api.users.mappers.UserMapper;
import dev.angelcorzo.neoparking.model.userinvitations.UserInvitations;
import dev.angelcorzo.neoparking.model.users.Users;
import dev.angelcorzo.neoparking.usecase.acceptinvitation.AcceptInvitationUseCase;
import dev.angelcorzo.neoparking.usecase.deactivateuser.DeactivateUserUseCase;
import dev.angelcorzo.neoparking.usecase.inviteuserwithrol.InviteUserWithRolUseCase;
import dev.angelcorzo.neoparking.usecase.modifyuserrole.ModifyUserRoleUseCase;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
  private final UserInvitationsMapper userInvitationsMapper;
  private final UserMapper userMapper;

  private final InviteUserWithRolUseCase inviteUserWithRolUseCase;
  private final AcceptInvitationUseCase acceptInvitationUseCase;
  private final ModifyUserRoleUseCase modifyUserRoleUseCase;
  private final DeactivateUserUseCase deactivateUserUseCase;

  @PostMapping("/invite-user")
  @PreAuthorize("hasRole('MANAGER')")
  @ResponseStatus(HttpStatus.CREATED)
  Response<UserInvitationsDTO> inviteUserWithRol(@RequestBody InviteUserDTO inviteUser) {
    final UserInvitations invitationRegistered =
        this.inviteUserWithRolUseCase.registerInvitation(
            this.userInvitationsMapper.toModel(inviteUser));

    return Response.created(
        this.userInvitationsMapper.toDTO(invitationRegistered),
        UserMessages.USER_INVITATION_SEND.toString());
  }

  @PatchMapping("/accept-invitation/{token}")
  Response<UserInvitationsDTO> acceptInvitation(
      @PathVariable("token") UUID token, @RequestBody CreatedUserDTO user) {
    final UserInvitations invitationAccepted =
        this.acceptInvitationUseCase.accept(
            AcceptInvitationUseCase.Accept.builder()
                .user(this.userMapper.toModel(user))
                .token(token)
                .build());

    return Response.ok(
        this.userInvitationsMapper.toDTO(invitationAccepted),
        UserMessages.USER_INVITATION_ACCEPTED.toString());
  }

  @PatchMapping("/modify-rol")
  @PreAuthorize("hasRole('MANAGER')")
  Response<UserDTO> modifyRol(@RequestBody ModifyRolDTO modifyRol) {
    Users userWithRolUpdate =
        this.modifyUserRoleUseCase.modifyRole(this.userMapper.toModel(modifyRol));

    return Response.ok(
        this.userMapper.toDTO(userWithRolUpdate), UserMessages.USER_ROL_MODIFIED.toString());
  }

  @DeleteMapping("/deactivate-user")
  @PreAuthorize("hasRole('MANAGER')")
  Response<Void> deactivateUser(@RequestBody DeactivateUserDTO deactivateUser) {
    this.deactivateUserUseCase.deactivate(this.userMapper.toModel(deactivateUser));
    return Response.ok(null, UserMessages.USER_DEACTIVATED.toString());
  }
}

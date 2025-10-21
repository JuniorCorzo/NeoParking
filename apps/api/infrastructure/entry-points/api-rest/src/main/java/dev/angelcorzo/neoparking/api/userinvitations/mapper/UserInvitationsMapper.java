package dev.angelcorzo.neoparking.api.userinvitations.mapper;

import dev.angelcorzo.neoparking.api.commons.config.MapperStructConfig;
import dev.angelcorzo.neoparking.api.userinvitations.dto.InviteUserDTO;
import dev.angelcorzo.neoparking.api.userinvitations.dto.UserInvitationsDTO;
import dev.angelcorzo.neoparking.model.userinvitations.UserInvitations;
import dev.angelcorzo.neoparking.usecase.inviteuserwithrol.InviteUserWithRolUseCase;
import org.mapstruct.Mapper;

@Mapper(config =  MapperStructConfig.class)
public interface UserInvitationsMapper {
    InviteUserWithRolUseCase.InviteUserWithRole toModel(InviteUserDTO dto);
    UserInvitationsDTO toDTO(UserInvitations model);
}

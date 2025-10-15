package dev.angelcorzo.neoparking.usecase.deactivateuser;

import dev.angelcorzo.neoparking.model.users.Users;
import dev.angelcorzo.neoparking.model.users.enums.Roles;
import dev.angelcorzo.neoparking.model.users.exceptions.LastOwnerCannotBeDeactivatedException;
import dev.angelcorzo.neoparking.model.users.exceptions.UserAlreadyDeactivatedException;
import dev.angelcorzo.neoparking.model.users.exceptions.UserNotExistsException;
import dev.angelcorzo.neoparking.model.users.exceptions.UserNotExistsInTenantException;
import dev.angelcorzo.neoparking.model.users.gateways.UsersRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public class DeactivateUserUseCase {
    private final UsersRepository usersRepository;

    public DeactivationResult deactivate(final DeactivateUserCommand command) {
        this.validateDeactivatorExists(command.deactivatedBy());

        final Users userToDeactivate = this.findUserByIdAndTenantId(
                command.userIdToDeactivate(),
                command.tenantId()
        );

        this.validateUserIsActive(userToDeactivate);
        if (userToDeactivate.getRole() == Roles.OWNER) {
            this.validateNotLastOwner(command.tenantId());
        }

        final OffsetDateTime deactivatedAt = OffsetDateTime.now();
        userToDeactivate.setDeletedAt(deactivatedAt);
        userToDeactivate.setUpdatedAt(deactivatedAt);

        final Users deactivatedUser = this.usersRepository.save(userToDeactivate);

        return DeactivationResult.builder()
                .userId(deactivatedUser.getId())
                .deactivatedAt(deactivatedUser.getDeletedAt())
                .deactivatedBy(command.deactivatedBy())
                .build();
    }

    private void validateDeactivatorExists(UUID deactivatedBy) {
        if (!this.usersRepository.existsById(deactivatedBy)) {
            throw new UserNotExistsException(deactivatedBy);
        }
    }

    private Users findUserByIdAndTenantId(UUID userId, UUID tenantId) {
        return this.usersRepository.findByIdAndTenantId(userId, tenantId)
                .orElseThrow(() -> {
                    if (this.usersRepository.existsById(userId)) {
                        return new UserNotExistsInTenantException();
                    }
                    return new UserNotExistsException(userId);
                });
    }

    private void validateUserIsActive(Users user) {
        if (user.getDeletedAt() != null) {
            throw new UserAlreadyDeactivatedException(user.getId());
        }
    }

    private void validateNotLastOwner(UUID tenantId) {
        Long activeOwnersCount = this.usersRepository.countActiveOwnersByTenantId(tenantId);
        if (activeOwnersCount <= 1) {
            throw new LastOwnerCannotBeDeactivatedException();
        }
    }

    @Builder
    public record DeactivateUserCommand(
            UUID userIdToDeactivate,
            UUID deactivatedBy,
            UUID tenantId,
            String reason // Opcional, para auditoría futura
    ) {
    }

    @Builder
    public record DeactivationResult(
            UUID userId,
            UUID deactivatedBy,
            OffsetDateTime deactivatedAt
    ) {
    }
}

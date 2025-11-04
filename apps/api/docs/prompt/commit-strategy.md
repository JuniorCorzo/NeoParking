# Estrategia de Commits y Documentación JavaDocs
## Proyecto: Arquitectura Limpia - Bancolombia Scaffold

---

## 📋 Tabla de Contenidos

1. [Conventional Commits - Estructura](#conventional-commits---estructura)
2. [Tipos de Commits por Capa](#tipos-de-commits-por-capa)
3. [Scopes Recomendados](#scopes-recomendados)
4. [Estrategia de Commits](#estrategia-de-commits)
5. [Guía de Documentación JavaDocs](#guía-de-documentación-javadocs)
6. [Ejemplos Prácticos](#ejemplos-prácticos)

---

## 🎯 Conventional Commits - Estructura

### Formato Base

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Componentes del Mensaje

**Header (Obligatorio):**
- `type`: Tipo de cambio (feat, fix, refactor, etc.)
- `scope`: Módulo o capa afectada
- `subject`: Descripción corta en imperativo (máx. 50 caracteres)

**Body (Recomendado):**
Secciones estructuradas:
1. **CONTEXT:** Por qué se hizo el cambio
2. **CHANGES MADE:** Qué se modificó específicamente
3. **IMPACT:** Consecuencias del cambio
4. **TESTING:** Requerimientos de pruebas

**Footer (Opcional):**
- `BREAKING CHANGE:` Cambios que rompen compatibilidad
- Referencias a issues: `Closes #123`

---

## 🏗️ Tipos de Commits por Capa

### Domain Layer (Corazón del Negocio)

#### **feat(domain):** Nueva funcionalidad de negocio
```
feat(domain): add tenant existence check

Add a method to check for tenant existence by ID and include full documentation.

1. CONTEXT:
   - There was a need to verify if a Tenant exists without fetching the entire entity.

2. CHANGES MADE:
   - Added the 'existsById(UUID id)' method to the 'TenantsRepository' interface.
   - Added comprehensive JavaDoc for the entire interface and all its methods.

3. IMPACT:
   - Provides a more efficient way to check for tenant existence.
   - Improves the clarity and maintainability of the domain gateway.

4. TESTING:
   - The implementation of this method in the adapter layer will require a new unit test.
```

#### **feat(usecase):** Nuevo caso de uso
```
feat(usecase): add user authentication use case

Implement the core business logic for user authentication.

1. CONTEXT:
   - The application requires a secure authentication mechanism.
   - Users must be able to login with email and password.

2. CHANGES MADE:
   - Created 'AuthenticateUserUseCase' class in domain/usecase.
   - Added methods: validateCredentials(), generateTokens(), updateLastLogin().
   - Integrated with UsersRepository and TokenRepository gateways.

3. IMPACT:
   - Enables secure user authentication flow.
   - Separates authentication logic from infrastructure concerns.

4. TESTING:
   - Unit tests required for success and failure scenarios.
   - Mock repositories to test business logic in isolation.
```

#### **refactor(model):** Refactorización de entidades
```
refactor(model): improve User entity validation

Enhance validation logic and encapsulation in User entity.

1. CONTEXT:
   - Current validation was scattered across use cases.
   - Entity should enforce its own invariants.

2. CHANGES MADE:
   - Added private validation methods in User class.
   - Implemented builder pattern with validation.
   - Added @throws tags in JavaDoc for invalid states.

3. IMPACT:
   - Improved domain model integrity.
   - Reduced duplication in use cases.
   - Better separation of concerns.

4. TESTING:
   - Unit tests for entity validation rules required.
```

---

### Infrastructure Layer

#### **feat(adapter):** Nuevo driven adapter
```
feat(adapter): add PostgreSQL tenant repository adapter

Implement concrete repository for tenant persistence using PostgreSQL.

1. CONTEXT:
   - TenantsRepository gateway needs a database implementation.
   - PostgreSQL is the chosen database for tenant management.

2. CHANGES MADE:
   - Created 'PostgresTenantRepositoryAdapter' implementing TenantsRepository.
   - Added JPA entity mapper for Tenant domain entity.
   - Configured Spring Data JPA repository.
   - Implemented existsById() method using native query for performance.

3. IMPACT:
   - Completes the persistence layer for tenants.
   - Allows testing of tenant-related use cases end-to-end.

4. TESTING:
   - Integration tests with Testcontainers required.
   - Unit tests for mapper functionality.
```

#### **feat(entry-point):** Nuevo punto de entrada
```
feat(entry-point): add tenant management REST controller

Expose tenant operations through RESTful API endpoints.

1. CONTEXT:
   - Frontend needs HTTP endpoints to manage tenants.
   - API should follow RESTful conventions.

2. CHANGES MADE:
   - Created 'TenantController' in infrastructure/entry-points/api-rest.
   - Added endpoints: GET /tenants, POST /tenants, GET /tenants/{id}.
   - Implemented request/response DTOs.
   - Added OpenAPI annotations for documentation.

3. IMPACT:
   - Enables external interaction with tenant management.
   - Provides API documentation through Swagger.

4. TESTING:
   - Integration tests with MockMvc required.
   - Verify DTO mapping and HTTP status codes.
```

#### **fix(adapter):** Corrección en adaptador
```
fix(adapter): correct transaction handling in user repository

Fix rollback behavior in user update operations.

1. CONTEXT:
   - User updates were not rolling back properly on validation errors.
   - Data inconsistencies were occurring in error scenarios.

2. CHANGES MADE:
   - Added @Transactional annotation with proper propagation.
   - Implemented exception translation for repository operations.
   - Added rollback rules for domain exceptions.

3. IMPACT:
   - Ensures data consistency in error scenarios.
   - Improves reliability of user operations.

4. TESTING:
   - Integration tests verifying rollback behavior.
   - Tests for concurrent update scenarios.
```

---

### Application Layer

#### **chore(config):** Configuración de la aplicación
```
chore(config): add JWT configuration properties

Configure JWT token generation and validation settings.

1. CONTEXT:
   - Application needs configurable JWT settings.
   - Different environments require different token expiration times.

2. CHANGES MADE:
   - Added JWT properties in application.yaml.
   - Created @ConfigurationProperties class for type-safe configuration.
   - Added validation for required properties.

3. IMPACT:
   - Enables environment-specific JWT configuration.
   - Improves security configuration management.

4. TESTING:
   - Configuration validation tests required.
```

#### **build(deps):** Dependencias
```
build(deps): upgrade Spring Boot to 3.2.1

Update Spring Boot version for security patches and new features.

1. CONTEXT:
   - Current version has known security vulnerabilities.
   - New version includes performance improvements.

2. CHANGES MADE:
   - Updated spring-boot version in build.gradle.
   - Resolved deprecated API usage.
   - Updated related Spring dependencies.

3. IMPACT:
   - Improved security posture.
   - Access to new Spring features.
   - Potential breaking changes in test configurations.

4. TESTING:
   - Full regression test suite execution required.
   - Verify all adapters still function correctly.

BREAKING CHANGE: Requires Java 17 minimum version.
```

---

## 🎯 Scopes Recomendados

### Por Capa de Arquitectura

| Scope | Ubicación | Uso |
|-------|-----------|-----|
| `domain` | domain/model | Entidades, gateways (interfaces) |
| `usecase` | domain/usecase | Casos de uso, lógica de negocio |
| `adapter` | infrastructure/driven-adapters | Implementaciones de gateways |
| `entry-point` | infrastructure/entry-points | Controllers, APIs, listeners |
| `config` | applications/app-service | Configuración, beans, inyección |
| `helper` | infrastructure/helpers | Utilidades de infraestructura |

### Por Tipo de Funcionalidad

| Scope | Descripción | Ejemplo |
|-------|-------------|---------|
| `auth` | Autenticación y autorización | `feat(auth): add JWT validation` |
| `tenant` | Gestión de tenants | `feat(tenant): add multi-tenancy support` |
| `user` | Gestión de usuarios | `fix(user): correct email validation` |
| `api` | Endpoints REST | `feat(api): add pagination to users endpoint` |
| `db` | Base de datos | `fix(db): correct connection pool settings` |
| `security` | Seguridad | `feat(security): add rate limiting` |

---

## 📝 Estrategia de Commits

### 1. Commits Atómicos

**✅ Buena práctica:**
```
feat(domain): add User entity
feat(usecase): add CreateUserUseCase
feat(adapter): implement UserRepositoryAdapter
feat(entry-point): add UserController
```

**❌ Mala práctica:**
```
feat: add complete user management feature
```

### 2. Orden de Desarrollo y Commits

Para una nueva feature completa, seguir este orden:

```
1. feat(domain): add entity and gateway interface
2. feat(usecase): add use case with business logic
3. feat(adapter): implement gateway adapter
4. feat(entry-point): add API controller/listener
5. test(usecase): add unit tests for use case
6. test(adapter): add integration tests for adapter
7. docs(api): add OpenAPI documentation
8. chore(config): add required configuration
```

### 3. Commits de Documentación

```
docs(domain): add JavaDocs to TenantsRepository gateway

Complete documentation for all gateway methods.

1. CONTEXT:
   - Interface lacked proper documentation.
   - Developers needed clarity on method contracts.

2. CHANGES MADE:
   - Added class-level JavaDoc describing the gateway purpose.
   - Documented all method parameters, return values, and exceptions.
   - Added @since and @version tags.

3. IMPACT:
   - Improved code maintainability.
   - Clearer contracts for adapter implementations.

4. TESTING:
   - No testing required (documentation only).
```

### 4. Commits de Refactoring

```
refactor(usecase): extract validation logic from AuthUseCase

Improve code organization and reusability.

1. CONTEXT:
   - Authentication use case had grown too large.
   - Validation logic was duplicated in multiple use cases.

2. CHANGES MADE:
   - Created separate ValidationHelper in domain/usecase.
   - Extracted email, password, and token validation methods.
   - Updated AuthUseCase to use the helper.

3. IMPACT:
   - Reduced code duplication.
   - Improved testability of validation logic.
   - Easier to maintain validation rules.

4. TESTING:
   - Existing tests still pass.
   - Added unit tests for ValidationHelper.
```

---

## 📚 Guía de Documentación JavaDocs

### Nivel de Interfaces (Gateways)

```java
package com.bancolombia.model.tenant.gateways;

import com.bancolombia.model.tenant.Tenant;
import java.util.Optional;
import java.util.UUID;

/**
 * Gateway interface for managing tenant persistence operations.
 * <p>
 * This interface defines the contract for tenant repository implementations
 * in the infrastructure layer. It follows the Repository pattern and is part
 * of the domain layer, making the business logic independent of specific
 * persistence technologies.
 * </p>
 * <p>
 * Implementations of this gateway should handle all infrastructure-specific
 * concerns such as database connections, transaction management, and data
 * mapping between domain entities and persistence models.
 * </p>
 *
 * @author Tu Nombre
 * @version 1.0
 * @since 2025-10-29
 * @see Tenant
 */
public interface TenantsRepository {

    /**
     * Retrieves a tenant by its unique identifier.
     * <p>
     * This method performs a lookup in the persistence layer to find a tenant
     * matching the provided ID. If no tenant is found, an empty Optional is returned.
     * </p>
     *
     * @param id the unique identifier of the tenant to retrieve; must not be null
     * @return an {@link Optional} containing the tenant if found, or empty if not found
     * @throws IllegalArgumentException if id is null
     * @throws RepositoryException if there is an error accessing the persistence layer
     */
    Optional<Tenant> findById(UUID id);

    /**
     * Persists a new tenant or updates an existing one.
     * <p>
     * If the tenant does not have an ID or the ID does not exist in the persistence
     * layer, a new tenant will be created. Otherwise, the existing tenant will be updated.
     * </p>
     *
     * @param tenant the tenant entity to save; must not be null
     * @return the saved tenant with generated ID if it was a new entity
     * @throws IllegalArgumentException if tenant is null or violates business constraints
     * @throws RepositoryException if there is an error during the save operation
     * @see #findById(UUID)
     */
    Tenant save(Tenant tenant);

    /**
     * Checks if a tenant exists in the persistence layer by its unique identifier.
     * <p>
     * This method provides a more efficient way to verify tenant existence compared
     * to {@link #findById(UUID)}, as it does not require fetching and constructing
     * the entire entity object. This is particularly useful for validation scenarios
     * where only the existence needs to be confirmed.
     * </p>
     *
     * @param id the unique identifier of the tenant to check; must not be null
     * @return {@code true} if a tenant with the given ID exists, {@code false} otherwise
     * @throws IllegalArgumentException if id is null
     * @throws RepositoryException if there is an error accessing the persistence layer
     * @since 1.1
     */
    boolean existsById(UUID id);

    /**
     * Deletes a tenant from the persistence layer by its unique identifier.
     * <p>
     * If the tenant does not exist, this method completes successfully without
     * throwing an exception (idempotent operation).
     * </p>
     *
     * @param id the unique identifier of the tenant to delete; must not be null
     * @throws IllegalArgumentException if id is null
     * @throws RepositoryException if there is an error during the delete operation
     */
    void deleteById(UUID id);
}
```

### Nivel de Entidades (Domain Models)

```java
package com.bancolombia.model.tenant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a tenant in the multi-tenant application system.
 * <p>
 * A tenant is an organizational entity that has its own isolated data and
 * configuration within the application. This entity encapsulates all business
 * rules and invariants related to tenant management.
 * </p>
 * <p>
 * Tenants can be in different states (active, suspended, deleted) and must
 * maintain data isolation to ensure security and privacy between organizations.
 * </p>
 *
 * @author Tu Nombre
 * @version 1.0
 * @since 2025-10-29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tenant {

    /**
     * Unique identifier for the tenant.
     * <p>
     * This UUID is generated when the tenant is created and remains immutable
     * throughout the tenant's lifecycle. It is used as the primary key for
     * persistence and as a reference in all tenant-related operations.
     * </p>
     */
    private UUID id;

    /**
     * Unique name identifying the tenant.
     * <p>
     * The name must be unique across all tenants in the system and typically
     * represents the organization or company name. It must comply with naming
     * conventions: alphanumeric characters, hyphens, and underscores only,
     * with a maximum length of 100 characters.
     * </p>
     */
    private String name;

    /**
     * Unique subdomain or slug for the tenant.
     * <p>
     * Used for URL routing in multi-tenant scenarios (e.g., tenant1.app.com).
     * Must be unique, lowercase, and contain only alphanumeric characters and hyphens.
     * Maximum length: 50 characters.
     * </p>
     */
    private String slug;

    /**
     * Current status of the tenant.
     * <p>
     * Determines whether the tenant is active and can access the system.
     * Possible values: ACTIVE, SUSPENDED, DELETED.
     * </p>
     *
     * @see TenantStatus
     */
    private TenantStatus status;

    /**
     * Timestamp when the tenant was created.
     * <p>
     * This field is automatically set during tenant creation and should not
     * be modified afterwards. Uses UTC timezone.
     * </p>
     */
    private LocalDateTime createdAt;

    /**
     * Timestamp of the last modification to the tenant.
     * <p>
     * Updated automatically whenever any tenant property is modified.
     * Uses UTC timezone.
     * </p>
     */
    private LocalDateTime updatedAt;

    /**
     * Validates that the tenant meets all business invariants.
     * <p>
     * This method should be called before persisting a tenant to ensure
     * data integrity and compliance with business rules.
     * </p>
     *
     * @throws IllegalStateException if the tenant state is invalid
     */
    public void validate() {
        if (name == null || name.isBlank()) {
            throw new IllegalStateException("Tenant name cannot be null or empty");
        }
        if (slug == null || slug.isBlank()) {
            throw new IllegalStateException("Tenant slug cannot be null or empty");
        }
        if (status == null) {
            throw new IllegalStateException("Tenant status cannot be null");
        }
        if (!slug.matches("^[a-z0-9-]+$")) {
            throw new IllegalStateException("Tenant slug must contain only lowercase letters, numbers, and hyphens");
        }
    }

    /**
     * Checks if the tenant is currently active.
     *
     * @return {@code true} if the tenant status is ACTIVE, {@code false} otherwise
     */
    public boolean isActive() {
        return status == TenantStatus.ACTIVE;
    }
}
```

### Nivel de Casos de Uso (Use Cases)

```java
package com.bancolombia.usecase.tenant;

import com.bancolombia.model.tenant.Tenant;
import com.bancolombia.model.tenant.gateways.TenantsRepository;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

/**
 * Use case for validating tenant existence in the system.
 * <p>
 * This use case encapsulates the business logic for checking whether a tenant
 * exists before performing operations that require a valid tenant context.
 * It is commonly used in validation flows, authorization checks, and
 * multi-tenant request routing.
 * </p>
 * <p>
 * The use case follows the Single Responsibility Principle by focusing solely
 * on tenant existence validation without mixing other business concerns.
 * </p>
 *
 * @author Tu Nombre
 * @version 1.0
 * @since 2025-10-29
 */
@RequiredArgsConstructor
public class ValidateTenantExistenceUseCase {

    /**
     * Gateway for accessing tenant persistence operations.
     * <p>
     * Injected via constructor using dependency inversion principle,
     * allowing the use case to remain independent of infrastructure details.
     * </p>
     */
    private final TenantsRepository tenantsRepository;

    /**
     * Validates whether a tenant exists in the system.
     * <p>
     * This method uses the repository's {@code existsById} method for efficient
     * existence checking without the overhead of fetching the entire entity.
     * It is typically used in request interceptors, filters, or before executing
     * operations that require a valid tenant context.
     * </p>
     *
     * @param tenantId the unique identifier of the tenant to validate; must not be null
     * @return {@code true} if the tenant exists, {@code false} otherwise
     * @throws IllegalArgumentException if tenantId is null
     * @throws TenantValidationException if there is an error during validation
     *
     * @see TenantsRepository#existsById(UUID)
     */
    public boolean execute(UUID tenantId) {
        if (tenantId == null) {
            throw new IllegalArgumentException("Tenant ID cannot be null");
        }

        return tenantsRepository.existsById(tenantId);
    }

    /**
     * Validates tenant existence and throws an exception if not found.
     * <p>
     * Convenience method for scenarios where the absence of a tenant should
     * result in an exception rather than a boolean return value. This is useful
     * in workflows where a missing tenant is an exceptional condition.
     * </p>
     *
     * @param tenantId the unique identifier of the tenant to validate; must not be null
     * @throws TenantNotFoundException if the tenant does not exist
     * @throws IllegalArgumentException if tenantId is null
     */
    public void executeOrThrow(UUID tenantId) {
        if (!execute(tenantId)) {
            throw new TenantNotFoundException("Tenant with ID " + tenantId + " not found");
        }
    }
}
```

### Nivel de Adaptadores (Infrastructure)

```java
package com.bancolombia.adapter.jpa.tenant;

import com.bancolombia.adapter.jpa.tenant.entity.TenantEntity;
import com.bancolombia.model.tenant.Tenant;
import com.bancolombia.model.tenant.gateways.TenantsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * JPA-based implementation of the TenantsRepository gateway.
 * <p>
 * This adapter provides a concrete implementation of tenant persistence operations
 * using Spring Data JPA and PostgreSQL. It handles the translation between domain
 * entities ({@link Tenant}) and JPA entities ({@link TenantEntity}), as well as
 * exception handling and transaction management.
 * </p>
 * <p>
 * The adapter is part of the infrastructure layer and depends on the domain layer
 * through the {@link TenantsRepository} interface, following the Dependency
 * Inversion Principle of Clean Architecture.
 * </p>
 *
 * @author Tu Nombre
 * @version 1.0
 * @since 2025-10-29
 * @see TenantsRepository
 * @see TenantEntity
 * @see TenantMapper
 */
@Repository
@RequiredArgsConstructor
public class PostgresTenantRepositoryAdapter implements TenantsRepository {

    /**
     * Spring Data JPA repository for database operations.
     * <p>
     * Provides basic CRUD operations and custom query methods for tenant persistence.
     * </p>
     */
    private final JpaTenantRepository jpaRepository;

    /**
     * Mapper for converting between domain and JPA entities.
     * <p>
     * Handles bidirectional mapping with proper null handling and validation.
     * </p>
     */
    private final TenantMapper mapper;

    /**
     * {@inheritDoc}
     * <p>
     * This implementation uses JPA's {@code findById} method and maps the result
     * to a domain entity. Database connection errors are wrapped in
     * {@link RepositoryException}.
     * </p>
     *
     * @throws RepositoryException if database access fails
     */
    @Override
    public Optional<Tenant> findById(UUID id) {
        try {
            return jpaRepository.findById(id)
                    .map(mapper::toDomain);
        } catch (Exception e) {
            throw new RepositoryException("Error finding tenant by ID: " + id, e);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation validates the tenant before persisting, maps it to a
     * JPA entity, saves it using Spring Data JPA, and maps the result back to
     * a domain entity. The operation is transactional.
     * </p>
     *
     * @throws RepositoryException if database persistence fails
     */
    @Override
    public Tenant save(Tenant tenant) {
        try {
            tenant.validate();
            TenantEntity entity = mapper.toEntity(tenant);
            TenantEntity savedEntity = jpaRepository.save(entity);
            return mapper.toDomain(savedEntity);
        } catch (IllegalStateException e) {
            throw new IllegalArgumentException("Invalid tenant data", e);
        } catch (Exception e) {
            throw new RepositoryException("Error saving tenant", e);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation uses JPA's {@code existsById} method which executes
     * a COUNT query in the database, making it more efficient than fetching
     * the entire entity when only existence verification is needed.
     * </p>
     *
     * @throws RepositoryException if database access fails
     */
    @Override
    public boolean existsById(UUID id) {
        try {
            return jpaRepository.existsById(id);
        } catch (Exception e) {
            throw new RepositoryException("Error checking tenant existence for ID: " + id, e);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation uses JPA's {@code deleteById} method. The operation
     * is idempotent and does not throw an exception if the tenant does not exist.
     * </p>
     *
     * @throws RepositoryException if database deletion fails
     */
    @Override
    public void deleteById(UUID id) {
        try {
            jpaRepository.deleteById(id);
        } catch (Exception e) {
            throw new RepositoryException("Error deleting tenant with ID: " + id, e);
        }
    }
}
```

### Nivel de Entry Points (Controllers)

```java
package com.bancolombia.api.tenant;

import com.bancolombia.api.tenant.dto.CreateTenantRequest;
import com.bancolombia.api.tenant.dto.TenantResponse;
import com.bancolombia.usecase.tenant.CreateTenantUseCase;
import com.bancolombia.usecase.tenant.ValidateTenantExistenceUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

/**
 * REST controller for tenant management operations.
 * <p>
 * This controller exposes HTTP endpoints for creating, retrieving, and validating
 * tenants in the system. It acts as an entry point in the Clean Architecture,
 * receiving HTTP requests, delegating business logic to use cases, and returning
 * appropriate HTTP responses.
 * </p>
 * <p>
 * All endpoints follow RESTful conventions and return standardized response
 * formats with proper HTTP status codes. API documentation is available through
 * OpenAPI/Swagger annotations.
 * </p>
 *
 * @author Tu Nombre
 * @version 1.0
 * @since 2025-10-29
 */
@RestController
@RequestMapping("/api/v1/tenants")
@RequiredArgsConstructor
@Tag(name = "Tenants", description = "Tenant management operations")
public class TenantController {

    private final CreateTenantUseCase createTenantUseCase;
    private final ValidateTenantExistenceUseCase validateTenantExistenceUseCase;

    /**
     * Creates a new tenant in the system.
     * <p>
     * This endpoint receives tenant data in the request body, validates it,
     * delegates the creation logic to the use case, and returns the created
     * tenant with HTTP 201 status. Input validation is performed automatically
     * using Bean Validation annotations on the request DTO.
     * </p>
     *
     * @param request the tenant creation data; must not be null and must be valid
     * @return {@link ResponseEntity} containing the created tenant data with HTTP 201
     * @throws ValidationException if the request data is invalid
     * @throws DuplicateTenantException if a tenant with the same name or slug already exists
     */
    @PostMapping
    @Operation(
        summary = "Create a new tenant",
        description = "Creates a new tenant with the provided information. The tenant name and slug must be unique."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Tenant created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "409", description = "Tenant with the same name or slug already exists")
    })
    public ResponseEntity<TenantResponse> createTenant(
            @Valid @RequestBody CreateTenantRequest request) {
        
        Tenant tenant = createTenantUseCase.execute(request.toDomain());
        TenantResponse response = TenantResponse.fromDomain(tenant);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * Checks if a tenant exists by its unique identifier.
     * <p>
     * This lightweight endpoint is designed for validation purposes, such as
     * checking tenant existence before performing other operations. It returns
     * HTTP 200 if the tenant exists or HTTP 404 if not found.
     * </p>
     *
     * @param id the unique identifier of the tenant; must be a valid UUID
     * @return {@link ResponseEntity} with HTTP 200 if tenant exists, HTTP 404 otherwise
     * @throws IllegalArgumentException if the provided ID is not a valid UUID
     */
    @GetMapping("/{id}/exists")
    @Operation(
        summary = "Check tenant existence",
        description = "Verifies if a tenant with the given ID exists in the system."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tenant exists"),
        @ApiResponse(responseCode = "404", description = "Tenant not found"),
        @ApiResponse(responseCode = "400", description = "Invalid tenant ID format")
    })
    public ResponseEntity<Void> checkTenantExists(
            @Parameter(description = "Tenant unique identifier", required = true)
            @PathVariable UUID id) {
        
        boolean exists = validateTenantExistenceUseCase.execute(id);
        
        return exists 
            ? ResponseEntity.ok().build() 
            : ResponseEntity.notFound().build();
    }
}
```

---

## 🎨 Ejemplos Prácticos

### Ejemplo 1: Nueva Feature Completa (Gestión de Usuarios)

```bash
# Commit 1: Entidad de dominio
git commit -m "feat(domain): add User entity and UsersRepository gateway

Add core domain model for user management.

1. CONTEXT:
   - The application requires user management capabilities.
   - Users must have roles, authentication credentials, and profile data.

2. CHANGES MADE:
   - Created User entity with id, email, password, role, and timestamps.
   - Added validation methods for email format and password strength.
   - Created UsersRepository gateway interface with CRUD operations.
   - Added comprehensive JavaDocs for all classes and methods.

3. IMPACT:
   - Establishes the foundation for user management features.
   - Defines contracts for user persistence implementations.

4. TESTING:
   - Unit tests for User entity validation rules required."

# Commit 2: Caso de uso
git commit -m "feat(usecase): add CreateUserUseCase

Implement business logic for user registration.

1. CONTEXT:
   - Users need to register in the system.
   - Registration must enforce business rules for email uniqueness and password strength.

2. CHANGES MADE:
   - Created CreateUserUseCase class.
   - Implemented email uniqueness validation.
   - Added password hashing before persistence.
   - Integrated with UsersRepository gateway.

3. IMPACT:
   - Enables user registration functionality.
   - Ensures data integrity through business rule enforcement.

4. TESTING:
   - Unit tests for success and failure scenarios required.
   - Mock UsersRepository to test in isolation."

# Commit 3: Adaptador JPA
git commit -m "feat(adapter): implement JPA user repository adapter

Add PostgreSQL persistence for users using Spring Data JPA.

1. CONTEXT:
   - UsersRepository gateway needs a concrete implementation.
   - PostgreSQL is the chosen database for user data.

2. CHANGES MADE:
   - Created PostgresUserRepositoryAdapter implementing UsersRepository.
   - Added UserEntity JPA entity with proper mappings.
   - Implemented UserMapper for entity conversion.
   - Added custom query for email uniqueness check.

3. IMPACT:
   - Completes persistence layer for user management.
   - Enables end-to-end testing of user features.

4. TESTING:
   - Integration tests with Testcontainers required.
   - Test all CRUD operations and custom queries."

# Commit 4: Controller REST
git commit -m "feat(entry-point): add user registration REST endpoint

Expose user registration through RESTful API.

1. CONTEXT:
   - Frontend needs an HTTP endpoint for user registration.
   - API should follow REST conventions and provide validation feedback.

2. CHANGES MADE:
   - Created UserController with POST /api/v1/users endpoint.
   - Added RegisterUserRequest and UserResponse DTOs.
   - Implemented request validation using Bean Validation.
   - Added OpenAPI documentation annotations.

3. IMPACT:
   - Enables external systems to register users.
   - Provides API documentation through Swagger UI.

4. TESTING:
   - Integration tests with MockMvc required.
   - Verify request/response mapping and status codes."

# Commit 5: Tests
git commit -m "test(usecase): add unit tests for CreateUserUseCase

Ensure business logic correctness with comprehensive tests.

1. CONTEXT:
   - CreateUserUseCase requires test coverage for all scenarios.
   - Business rules must be verified through automated tests.

2. CHANGES MADE:
   - Added test class CreateUserUseCaseTest.
   - Implemented tests for: successful creation, duplicate email, weak password.
   - Used Mockito for mocking UsersRepository.
   - Achieved 100% code coverage for the use case.

3. IMPACT:
   - Ensures reliability of user registration logic.
   - Facilitates safe refactoring.

4. TESTING:
   - All tests passing."

# Commit 6: Configuración
git commit -m "chore(config): add user management configuration

Configure beans and properties for user management.

1. CONTEXT:
   - User management use cases need to be registered as Spring beans.
   - Password hashing requires configuration.

2. CHANGES MADE:
   - Added user use case beans in UseCasesConfig.
   - Configured BCryptPasswordEncoder bean.
   - Added user-related properties in application.yaml.

3. IMPACT:
   - Completes dependency injection setup for user management.
   - Enables password hashing with configurable strength.

4. TESTING:
   - Application context loads successfully."
```

### Ejemplo 2: Bugfix en Adaptador

```bash
git commit -m "fix(adapter): correct null handling in tenant mapper

Prevent NullPointerException when mapping null tenant fields.

1. CONTEXT:
   - TenantMapper was throwing NPE when optional fields were null.
   - This caused failures when retrieving tenants with incomplete data.

2. CHANGES MADE:
   - Added null checks in TenantMapper.toDomain() method.
   - Used Optional.ofNullable() for nullable fields.
   - Added defensive copying for mutable fields.

3. IMPACT:
   - Improves robustness of tenant data retrieval.
   - Prevents application crashes from malformed data.

4. TESTING:
   - Added unit test for null field scenarios.
   - Verified existing tests still pass."
```

### Ejemplo 3: Refactor de Código

```bash
git commit -m "refactor(usecase): extract common validation to base class

Reduce code duplication across use cases.

1. CONTEXT:
   - Multiple use cases duplicated validation logic.
   - Changes to validation rules required updates in many places.

2. CHANGES MADE:
   - Created BaseUseCase abstract class with common validations.
   - Extracted validateNotNull, validateEmail, validateUUID methods.
   - Updated all use cases to extend BaseUseCase.
   - Removed duplicated validation code.

3. IMPACT:
   - Reduced code duplication by ~30%.
   - Centralized validation logic for easier maintenance.
   - Improved consistency across use cases.

4. TESTING:
   - All existing tests pass without modification.
   - Added tests for BaseUseCase validation methods."
```

---

## 🏆 Mejores Prácticas

### DO ✅

1. **Commits atómicos**: Un commit = un cambio lógico
2. **Mensajes descriptivos**: Explicar el QUÉ y el POR QUÉ
3. **Seguir conventional commits**: Facilita changelog automático
4. **Documentar todas las clases públicas**: Especialmente interfaces
5. **Incluir ejemplos en JavaDocs**: Para métodos complejos
6. **Documentar excepciones**: Con @throws en JavaDoc
7. **Mantener consistencia**: Mismo estilo en todo el proyecto
8. **Commits antes de merge**: Squash commits WIP antes de merge a main

### DON'T ❌

1. **Commits gigantes**: "fix: varios bugs" con 50 archivos
2. **Mensajes vagos**: "fix", "update code", "changes"
3. **Mezclar refactor con features**: Separar en commits distintos
4. **Código sin documentar**: Especialmente interfaces públicas
5. **JavaDocs solo con nombres**: Documentar el propósito y comportamiento
6. **Ignorar excepciones en docs**: Siempre documentar @throws
7. **Commits de "WIP"**: En ramas compartidas
8. **Breaking changes sin avisar**: Usar BREAKING CHANGE en footer

---

## 📖 Referencias

- [Conventional Commits](https://www.conventionalcommits.org/)
- [Clean Architecture - Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Bancolombia Scaffold](https://github.com/bancolombia/scaffold-clean-architecture)
- [How to Write a Git Commit Message](https://chris.beams.io/posts/git-commit/)
- [JavaDoc Guidelines](https://www.oracle.com/technical-resources/articles/java/javadoc-tool.html)

---

**Versión:** 1.0  
**Fecha:** 2025-10-29  
**Autor:** Prompt Engineer  
**Proyecto:** Clean Architecture - Bancolombia Scaffold
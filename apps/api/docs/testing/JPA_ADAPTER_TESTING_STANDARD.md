
# Estándar de Testing para Adaptadores JPA

Este documento describe el estándar y las mejores prácticas para escribir tests de integración para los adaptadores de la capa de persistencia (`infrastructure/driven-adapters/jpa-repository`) en el proyecto NeoParking.

El objetivo es asegurar que los tests sean consistentes, legibles, robustos y mantenibles.

## 1. Checklist de Análisis Previo

Antes de escribir o modificar un test para un adaptador JPA, asegúrate de comprender el contexto:

- [ ] **Adaptador Bajo Prueba:** ¿Qué métodos públicos tiene y cuál es su responsabilidad? (Ej: `UserRepositoryAdapter`)
- [ ] **Puerto/Gateway de Dominio:** ¿Qué interfaz del dominio implementa el adaptador? (Ej: `UsersGateway`)
- [ ] **Entidades JPA:** ¿Qué clases con anotación `@Entity` están involucradas? (Ej: `UsersData`, `TenantsData`)
- [ ] **Modelos de Dominio:** ¿Cuáles son los objetos del modelo de negocio que mapea el adaptador? (Ej: `Users`)
- [ ] **Repositorios Spring Data:** ¿Qué interfaces `JpaRepository` utiliza el adaptador? (Ej: `UserRepositoryData`)
- [ ] **Relaciones:** ¿Existen relaciones entre las entidades (`@ManyToOne`, `@OneToMany`, etc.) que deban ser configuradas en los datos de prueba?

## 2. Plantilla de Test Estándar

Usa la siguiente estructura como base para tus clases de test.

```java
package dev.angelcorzo.neoparking.jpa.example;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("ExampleRepositoryAdapter Tests")
class ExampleRepositoryAdapterTest {

    @Autowired
    private ExampleRepositoryAdapter exampleRepositoryAdapter;

    @Autowired
    private ExampleRepositoryData exampleRepositoryData;
    
    // Inyectar otros repositorios necesarios para el setup

    // --- Ciclo de Vida y Setup ---

    @BeforeEach
    void setUp() {
        // 1. Limpiar todas las tablas relevantes para asegurar la independencia del test
        exampleRepositoryData.deleteAll();
        // otherRepository.deleteAll();

        // 2. Crear y guardar un conjunto de datos base (fixtures) que sea común a la mayoría de los tests
        // ExampleData fixture = ExampleData.builder()...
        // exampleRepositoryData.save(fixture);
    }

    // --- Agrupación de Tests por Operación ---

    @Nested
    @DisplayName("Save Operations")
    class SaveOperations {

        @Test
        @DisplayName("Should save a new entity successfully")
        void shouldSaveNewEntity() {
            // Arrange (Given): Prepara el objeto de dominio a guardar
            // ExampleModel model = ExampleModel.builder()...

            // Act (When): Llama al método del adaptador
            // ExampleModel savedModel = exampleRepositoryAdapter.save(model);

            // Assert (Then): Verifica el resultado
            // assertThat(savedModel).isNotNull();
            // assertThat(savedModel.getId()).isNotNull();
            
            // Verifica también el estado en la base de datos directamente
            // Optional<ExampleData> persistedData = exampleRepositoryData.findById(savedModel.getId());
            // assertThat(persistedData).isPresent();
            // assertThat(persistedData.get().getField()).isEqualTo(model.getField());
        }
    }

    @Nested
    @DisplayName("Find Operations")
    class FindOperations {

        @Test
        @DisplayName("Should find entity by ID when it exists")
        void shouldFindEntityByIdWhenExists() {
            // Arrange (Given): El dato ya existe gracias al setUp()

            // Act (When): Llama al método de búsqueda
            // Optional<ExampleModel> found = exampleRepositoryAdapter.findById(existingId);

            // Assert (Then): Verifica que se encontró el dato correcto
            // assertThat(found).isPresent();
            // assertThat(found.get().getId()).isEqualTo(existingId);
        }

        @Test
        @DisplayName("Should return empty when entity by ID does not exist")
        void shouldReturnEmptyWhenEntityDoesNotExist() {
            // Arrange (Given): Un ID que no existe
            // UUID nonExistentId = UUID.randomUUID();

            // Act (When): Llama al método de búsqueda
            // Optional<ExampleModel> found = exampleRepositoryAdapter.findById(nonExistentId);

            // Assert (Then): Verifica que el resultado es vacío
            // assertThat(found).isEmpty();
        }
    }

    // Agrega más clases @Nested para Update, Delete, Exists, Count, etc.
}
```

## 3. Guía de Convenciones

- **Anotaciones Principales:**
  - `@DataJpaTest`: Para configurar el entorno de testing de persistencia.
  - `@ActiveProfiles("test")`: Para usar la configuración de `application-test.yaml`.
  - `@DisplayName`: Para dar nombres legibles a clases y métodos en los reportes.
  - `@Nested`: Para agrupar tests por funcionalidad (Save, Find, Update, Delete, etc.).
  - `@BeforeEach`: Para la inicialización y limpieza de datos antes de cada test.

- **Nomenclatura de Métodos de Test:**
  - Usa el patrón `should<Accion><Objeto><Condicion>`.
  - **Ejemplos:**
    - `shouldSaveUserSuccessfully`
    - `shouldFindUserByIdWhenExists`
    - `shouldReturnEmptyWhenUserNotFound`
    - `shouldDeleteUserById`
    - `shouldCountActiveUsersByTenant`

- **Estructura del Test (Arrange-Act-Assert):**
  - **Arrange/Given:** Prepara los datos y mocks. Usa los datos del `setUp` siempre que sea posible. Si un test necesita datos adicionales, créalos dentro del propio método.
  - **Act/When:** Ejecuta el método del adaptador que estás probando.
  - **Assert/Then:** Usa aserciones de **AssertJ** (`assertThat`) para verificar los resultados. Siempre que sea posible, verifica tanto el objeto retornado como el estado final en la base de datos.

- **Manejo de Datos de Prueba:**
  - Limpia **TODAS** las tablas afectadas al inicio de cada test en `setUp`.
  - Crea un conjunto de datos base **mínimo y común** en `setUp`.
  - Para datos específicos de un test, créalos dentro del propio método para mejorar la claridad.
  - Utiliza métodos de ayuda (helpers) privados para construir entidades si la lógica de creación es compleja o repetitiva.

## 4. Catálogo de Escenarios Comunes a Cubrir

Asegúrate de que tus tests cubran, como mínimo, los siguientes escenarios para cada funcionalidad:

- **Happy Path:** El caso de uso exitoso y esperado.
- **Caso de "No Encontrado":** ¿Qué sucede si el ID o la entidad buscada no existe? El método debería devolver `Optional.empty()`, una lista vacía o `null` según corresponda.
- **Validación de Errores:** Aunque la lógica de negocio no reside aquí, prueba cómo reacciona el adaptador a datos de entrada inválidos si es relevante (ej. un ID nulo).
- **Casos Límite:**
  - Para búsquedas que devuelven listas (`findAll`), prueba el caso donde no hay ninguna entidad y debe devolver una lista vacía.
  - Para conteos (`count`), prueba el caso donde el resultado es cero.
- **Comportamiento con Datos Nulos:** Si una entidad tiene campos opcionales, prueba a guardarla y recuperarla con esos campos en nulo.

---


# Análisis de la Estructura del Proyecto: Scaffold de Arquitectura Limpia de Bancolombia

## 1. Resumen General

Este proyecto está estructurado siguiendo los principios de la **Arquitectura Limpia (Clean Architecture)**, utilizando específicamente el **Scaffold de Bancolombia**. Esta es una plantilla y un conjunto de herramientas de automatización (plugins de Gradle) que facilitan la creación y el mantenimiento de aplicaciones bajo este paradigma.

La estructura se basa en una separación estricta de responsabilidades, organizando el código en capas independientes que se comunican a través de interfaces. El objetivo es tener un dominio de negocio (lógica central) que sea independiente de la tecnología (frameworks, bases de datos, UI).

El proyecto es un build multi-módulo de Gradle, donde cada capa o subdominio es un módulo independiente.

## 2. Análisis de las Tareas de Gradle

El análisis de las tareas de Gradle (`./gradlew tasks`) revela un conjunto de comandos personalizados bajo el grupo "Clean Architecture tasks". Estos comandos son la clave para entender cómo se gestiona el proyecto:

- **`cleanArchitecture` (`ca`):** Inicia un nuevo proyecto con esta estructura.
- **`generateModel` (`gm`):** Crea una nueva entidad de negocio en la capa de dominio.
- **`generateUseCase` (`guc`):** Crea un nuevo caso de uso (lógica de aplicación) en la capa de dominio.
- **`generateEntryPoint` (`gep`):** Genera un punto de entrada en la capa de infraestructura (ej. un Controlador REST).
- **`generateDrivenAdapter` (`gda`):** Genera un adaptador conducido en la capa de infraestructura (ej. un repositorio para una base de datos específica).
- **`validateStructure` (`vs`):** Verifica que no se violen las reglas de dependencia entre las capas (ej. que el dominio no dependa de la infraestructura).

Estos comandos automatizan la creación de la estructura de archivos y clases necesaria, asegurando que se sigan las convenciones de la arquitectura.

## 3. Estructura de Directorios y Capas

La estructura de directorios refleja las capas de la Arquitectura Limpia:

### `domain`
Es el corazón de la aplicación. No tiene dependencias de ninguna otra capa.
- **`domain/model`:** Contiene las entidades del negocio (ej. `Users.java`) y las interfaces de los repositorios (`gateways`). Estas interfaces definen los contratos que la infraestructura debe implementar para la persistencia de datos.
- **`domain/usecase`:** Contiene la lógica de negocio específica de la aplicación (los casos de uso). Orquesta las entidades del modelo para realizar tareas concretas.

### `infrastructure`
Contiene las implementaciones concretas de la tecnología. Depende del dominio, pero no al revés.
- **`infrastructure/driven-adapters`:** Implementaciones de las interfaces definidas en el dominio (`gateways`). Por ejemplo, un adaptador para conectarse a una base de datos PostgreSQL o a un servicio externo.
- **`infrastructure/entry-points`:** Puntos de entrada a la aplicación desde el mundo exterior. Típicamente, aquí se encuentran los controladores REST que exponen la funcionalidad de los casos de uso.
- **`infrastructure/helpers`:** Clases de utilidad para la capa de infraestructura.

### `applications`
Es la capa más externa, responsable de configurar y ejecutar la aplicación.
- **`applications/app-service`:** Contiene la clase principal de la aplicación (ej. `MainApplication.java` en un proyecto Spring Boot), la configuración de inyección de dependencias (`UseCasesConfig.java`) y los archivos de configuración (`application.yaml`). Esta capa une todas las piezas, inyectando las implementaciones de la infraestructura en los casos de uso del dominio.

## 4. Flujo de una Petición (Ejemplo)

1.  Una petición HTTP llega a un **Controlador REST** en `infrastructure/entry-points`.
2.  El controlador llama a un **Caso de Uso** en `domain/usecase`, pasándole los datos necesarios (a menudo como un DTO).
3.  El caso de uso ejecuta la lógica de negocio, utilizando las **Entidades** de `domain/model`.
4.  Si necesita persistir o recuperar datos, el caso de uso invoca un método de una **interfaz de repositorio** (`gateway`) definida en `domain/model`.
5.  La inyección de dependencias (configurada en `applications/app-service`) asegura que la llamada a la interfaz sea resuelta por la implementación concreta en `infrastructure/driven-adapters`.
6.  El adaptador de infraestructura interactúa con la base de datos y devuelve los datos al caso de uso.
7.  El caso de uso finaliza su lógica y devuelve el resultado al controlador.
8.  El controlador transforma el resultado en una respuesta HTTP y la envía al cliente.

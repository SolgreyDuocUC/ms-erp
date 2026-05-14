# Antuan S.A. Gestión - Backend

Este repositorio contiene el código fuente del backend para el sistema ERP de Antuan S.A., construido sobre **Spring Boot 3.5.11** y **Java 25**.

## Arquitectura

El proyecto sigue una arquitectura basada en **Domain-Driven Design (DDD)** y **Arquitectura Hexagonal (Puertos y Adaptadores)**. Está estructurado como un **monolito modular**, donde cada módulo de negocio tiene su propio espacio aislado y se comunica a través de interfaces o eventos de dominio.

### Principios Fundamentales

* **Aislamiento del Dominio**: La lógica de negocio (_domain_) no tiene dependencias de frameworks externos (como Spring o JPA). Solo código puro de Java.
* **Inversión de Dependencias (DIP)**: Los casos de uso (_application_) interactúan con la infraestructura mediante interfaces (_ports_), que son implementadas en la capa externa (adaptadores).
* **Separación de Modelos**: Existen distintos modelos para el Dominio (Objetos puros enfocados en comportamiento) y para la Base de Datos (JPA Entities enfocados en persistencia). Un _Mapper_ traduce entre ambos.
* **Persistencia Abstraída**: Todo acceso a datos o APIs externas se canaliza exclusivamente por la capa _infrastructure_.

## Estructura de Módulos y Carpetas

La raíz del código fuente principal es `src/main/java/backend/com`. A continuación se describe la distribución modular principal:

```text
src/main/java/backend/com/
├── adquisiciones/  # Módulo de compras (Órdenes de Compra, Solicitudes a proveedores)
├── comercial/      # Módulo de ventas (Notas de Venta, Evaluaciones de Negocio, Cotizaciones)
├── config/         # Configuraciones globales de Spring (CORS, Jackson, Web, etc.)
├── produccion/     # Módulo de fabricación (Órdenes de Producción, Órdenes de Trabajo, Costeos)
├── shared/         # Kernel compartido (Entidades transversales base como Cliente/Proveedor, Value Objects genéricos)
├── trazabilidad/   # Módulo auxiliar transversal para auditar el histórico y ciclo de vida de entidades mayores
└── AntuanGestionApplication.java # Clase principal de arranque de Spring Boot
```

### Estructura Interna de un Módulo (Ej. `comercial`)

Dentro de cada módulo se encuentra una organización rigurosa por capas:

```text
comercial/
├── application/               # Lógica de coordinación (Casos de Uso)
│   └── usecase/               # Ej: CrearEVNUseCase, ConsultarCotizacionUseCase
├── domain/                    # Núcleo del negocio puro (Sin dependencias de framework)
│   ├── model/                 # Agregados y Entidades del dominio (Ej. EvaluacionNegocio)
│   └── ports/                 # Interfaces (Puertos de salida requeridos: Repositorios)
├── infrastructure/            # Implementaciones técnicas (Adaptadores)
│   └── persistence/           # Lógica de acceso a bases de datos
│       ├── adapter/           # Implementación de los puertos (Ej. EvaluacionNegocioRepositoryImpl)
│       ├── jpa/
│       │   ├── entity/        # Entidades manejadas por JPA (@Entity, @Table)
│       │   ├── mapper/        # Clases traductoras entre Domain y Entity
│       │   └── repository/    # Interfaces Spring Data JpaRepositories
└── controller/                # Controladores REST (Adaptadores de entrada HTTP / @RestController)
    └── dto/                   # Data Transfer Objects expuestos hacia la web
```

## Tecnologías Principales

* **Java 25**: Aprovechando las últimas mejoras de rendimiento y estabilidad (v3.5.11 stack).
* **Spring Boot 3.5.11**: Como ecosistema base (Web, Data JPA).
* **Base de Datos - H2 / PostgreSQL**: Motor de base de datos en memoria para fase de desarrollo ágil y motor productivo.
* **Lombok**: Para reducir el _boilerplate_ limitando el uso abusivo de anotaciones.
* **Maven**: Gestor del empaquetado y árbol de dependencias.

## Decisiones de Diseño Destacadas

1. **Cálculos "Al Vuelo" (On-the-fly)**: Las métricas derivadas de negocio como *Monto Total*, *Margen de Ganancia*, y *Rentabilidad Esperada* **no se persisten** en las tablas físicas de la base de datos para evitar redundancia e inconsistencia. En su lugar, se calculan dinámicamente en los métodos de las clases de la capa **Dominio** (e.g. `EvaluacionNegocio.getRentabilidadEsperada()`) y son mapeadas a los DTOs de respuesta directamente para el consumo del Front-End.
2. **Tipos de Datos Seguros**: Para costos, márgenes y valor nominal monetario se utiliza siempre `java.math.BigDecimal` (o nuestro Value Object propio `Money`) garantizando consistencia, eliminando la imprecisión en los tipos de flotantes. `LocalDate` gestiona fechas de transacciones sin estorbo del huso horario.
3. **Flujos DTO Desacoplados**: Los DTO de Input (como `CrearEVNCommand`) son los únicos insumos admitidos por los casos de uso, evitando el acoplamiento directo entre HTTP Requests y la lógica principal. Los repositorios de JPA exigen un "Mapper" que independiza el modelo de la BD de la lógica del negocio.
4. **Semilla de Base de Datos**: El script `src/main/resources/data.sql` contiene sentencias de inserción (DML) para popular automáticamente pre-condiciones iniciales en el entorno H2 (Clientes, Vendedores, Proveedores, etc). Esto levanta la aplicación lista para usarse.

## Cómo Iniciar en Entorno Local

1. Asegúrate de tener **JDK 25** en el path de tu sistema.
2. Ubícate en este directorio `backend` mediante tu terminal.
3. Ejecuta el entorno con el wrapper de Maven con el siguiente comando en Windows PowerShell / CMD:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

   _Si estás en UNIX/Linux/Mac, usa `./mvnw spring-boot:run`_
4. El backend se levantará en el puerto `8050` (según definido en las configuraciones de `application.properties`), en caso de conflictos asegurar su liberación.
5. Todo cambio en el esquema generará la recreación de las tablas gracias al modo ddl-auto de Hibernate.

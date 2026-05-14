---
description: Patrón de Diseño Backend y Workflow (Monolito Modular e Inspiración ERP)
---

# PATRÓN_DISEÑO_BACKEND: Antuan S.A.

## Descripción
Este documento define el **patrón oficial de diseño, construcción, nomenclatura y organización del backend** del ERP de **Antuan S.A.**

El backend está construido como un **monolito modular en Spring Boot (Java 25 / Spring Boot 3.5.12)**, diseñado como un sistema propio que busca **inspirarse en las mejores prácticas estructurales, organizativas y funcionales de Odoo**, aspirando a un nivel de madurez empresarial similar pero adaptado 100% a la realidad de Antuan.

> **IMPORTANTE**: Este sistema NO se integra operativamente con Odoo. Odoo es una referencia de excelencia estructural para guiar nuestro desarrollo propio.

---

# 1. Principios Base del Backend

## 1.1 Arquitectura de Monolito Modular
El backend se organiza por responsabilidades técnicas y funcionales dentro de módulos claramente separados para mantener orden, cohesión y bajo acoplamiento interno.

## 1.2 Objetivo del Diseño
- Representar fielmente el dominio del negocio ERP.
- Facilitar el mantenimiento y el crecimiento ordenado.
- Evitar ambigüedades mediante nombres descriptivos y únicos.
- Garantizar trazabilidad y consistencia operativa.

---

# 2. Estándares Técnicos Oficiales

- **Lenguaje:** Java 25 / Spring Boot 3.5.12
- **Persistencia:** Spring Data JPA / PostgreSQL (H2 en desarrollo).
- **Validaciones:** Bean Validation (JSR 380).
- **Documentación API:** Swagger / OpenAPI 3.1.
- **Boilerplate:** Lombok (`@Data`, `@Builder`, `@RequiredArgsConstructor`).

---

# 3. Estructura Oficial de Carpetas

Todo módulo debe seguir esta organización de paquetes en `com.antuan.erp`:

```text
├── model          (Entidades de dominio del sistema)
├── dto            (Objetos de transferencia: Request/Response)
├── jpa            (Acceso a datos: Repositorios)
├── adapter        (Transformación entre DTOs y Modelos)
├── service        (Contratos funcionales / Interfaces)
├── serviceImpl    (Implementación de lógica de negocio)
├── controller     (Controladores REST)
├── config         (Configuración técnica)
├── security       (Protección de recursos)
└── exception      (Manejo de errores personalizados)
```

---

# 4. Convenciones de Nomenclatura

## 4.1 Identificadores (IDs)
- **Base de Datos (explicit):** `id_area`, `id_usuario`, `id_producto`.
- **Backend (CamelCase):** `areaId`, `usuarioId`, `productoId`.
- **REGLA**: Nunca usar solo `id`. Debe ser único y descriptivo según el contexto.

## 4.2 Endpoints (REST)
Los endpoints representan recursos, no acciones.
- **Estructura**: `/api/v1/nombreRecurso` (Ej: `/api/v1/area`, `/api/v1/orden-compra`).
- **PROHIBIDO**: Usar prefijos como `/api/v1/get...`, `/api/v1/crear...` o incluir "Controller" en la ruta.

---

# 5. Workflow de Construcción (Orden Obligatorio)

Para garantizar consistencia, el desarrollo debe seguir este flujo:

1. **Model**: Definir la entidad del negocio y sus atributos descriptivos.
2. **Repository (JPA)**: Construir el acceso a datos.
3. **DTO**: Crear objetos de entrada/salida para proteger el modelo.
4. **Adapter**: Implementar mapeos entre capas para evitar lógica de transformación dispersa.
5. **Service**: Definir el contrato funcional (Interfaz).
6. **ServiceImpl**: Implementar la lógica del negocio y validaciones funcionales.
7. **Controller**: Exponer el CRUD base y operaciones específicas vía REST.
8. **Swagger + Seguridad**: Documentar profesionalmente y proteger el acceso.

---

# 6. Documentación y Swagger

- **Tono**: Profesional, breve y en tercera persona.
- **Contenido**: Cada endpoint debe indicar qué hace, qué recibe y qué devuelve.
- **Ejemplos**: `CRUD de Usuarios`, `Busca un producto por su identificador`, `Registra una nueva área`.

---

# 7. Reglas para el Desarrollo (Humanos e IA)

- **Claridad**: Priorizar nombres descriptivos sobre código "compacto". No usar nombres como `dato`, `valor`, `obj`.
- **Mantenibilidad**: No poner lógica de negocio en el controlador.
- **Eficiencia**: Optimizar consultas y evitar redundancias innecesarias.
- **Inspiración Odoo**: Buscar que la estructura de tablas y procesos refleje la robustez de un ERP clase mundial.

---
description: Plantilla Estándar para Creación de Módulos CRUD Backend
---

# PLANTILLA_CRUD_BACKEND: Antuan S.A.

Este documento define la **plantilla oficial** para construir módulos CRUD. El objetivo es asegurar que todo nuevo recurso siga el patrón inspirado en las mejores prácticas de ERP (Odoo style) pero adaptado a nuestra estructura real.

---

# 1. Estructura Técnica del Módulo

Cada recurso debe implementarse en este orden correlativo:

```text
model -> jpa -> dto -> adapter -> service -> serviceImpl -> controller
```

---

# 2. Definición por Capas

## 2.1 Model (Entidad)
Representa la estructura del negocio y la base de datos.
- **Nomenclatura ID**: `nombreEntidadId` (ej: `areaId`).
- **Atributos**: Descriptivos y en `CamelCase`.

```java
@Data
@Entity
@Table(name = "area")
public class Area {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_area")
    private Long areaId;

    @Column(name = "nombre_area", nullable = false)
    private String nombreArea;
}
```

## 2.2 JPA (Repository)
Interfaz básica de persistencia heredando de `JpaRepository`.

## 2.3 DTO (Data Transfer Object)
Separa la entrada/salida del modelo real.
- `RecursoRequestDto`: Datos que entran (con validaciones `@NotBlank`, etc).
- `RecursoResponseDto`: Datos que salen hacia el cliente.

## 2.4 Adapter (Mapper)
Clase `@Component` encargada de transformar entre Model y DTO. Evita ensuciar el `serviceImpl`.

## 2.5 Service & ServiceImpl
- **Service**: Interfaz con contratos claros (ej: `obtenerAreaPorId`).
- **ServiceImpl**: Implementación de la lógica, validación de reglas de negocio y transaccionalidad.

## 2.6 Controller
Capa delgada que expone los endpoints bajo la ruta `/api/v1/recurso`.
- **Obligatorio**: Documentación Swagger profesional (`@Operation`, `@Tag`).

---

# 3. Convenciones REST Obligatorias

Todo CRUD debe exponer como base:

- `GET /api/v1/recurso` -> Listar todos.
- `GET /api/v1/recurso/{id}` -> Buscar por identificador.
- `POST /api/v1/recurso` -> Crear nuevo.
- `PUT /api/v1/recurso/{id}` -> Actualizar existente.
- `DELETE /api/v1/recurso/{id}` -> Eliminar (si aplica).

---

# 4. Checklist de Calidad (Definition of Done)

- [ ] ¿Los nombres son únicos y descriptivos? (No usar `id` a secas).
- [ ] ¿El endpoint sigue la ruta `/api/v1/`?
- [ ] ¿Hay lógica de negocio en el controlador? (NUNCA debe haber).
- [ ] ¿Está documentado en Swagger con descripciones en tercera persona?
- [ ] ¿Se respetó el orden de construcción (Model primero)?
- [ ] ¿Se usó `CamelCase` en el código y nombres explícitos en BD?

---

# 5. Inspiración Estructural
> **Nota técnica**: Al diseñar la lógica de negocio y las relaciones entre entidades, observe la modularidad y trazabilidad que ofrece Odoo como referente de excelencia, buscando esa misma solidez en nuestra implementación propia.

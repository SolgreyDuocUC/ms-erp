---
description: Reglas Operativas para IA (Antigravity) en el Backend
---

# REGLAS_ANTIGRAVITY_BACKEND: Antuan S.A.

Este documento define las reglas de comportamiento que **Antigravity** debe seguir al trabajar en el backend. El foco es la consistencia con el proyecto real y la visión de excelencia estructural.

---

# 1. Regla de Oro: No Inventar Arquitectura
**No proponer arquitecturas teóricas o académicas (como Hexagonal puro) si el proyecto ya tiene un estándar operativo.**
- Estructura Real: `model, dto, jpa, adapter, service, serviceImpl, controller`.
- No migrar el proyecto a estructuras no solicitadas.

---

# 2. Claridad conceptual sobre Odoo
**Antuan S.A. NO se integra con Odoo.**
- Odoo se usa exclusivamente como **referente de mejores prácticas** estructurales y funcionales.
- Al redactar comentarios o documentación, nunca usar términos que sugieran convivencia técnica o conectores con Odoo.
- Usar términos como: "inspiración estructural", "modularidad ERP", "robustez operativa".

---

# 3. Nomenclatura y Estándares
Al generar código o documentación, Antigravity debe asegurar:
- **Naming Backend**: `CamelCase`. Nombres descriptivos y únicos (Ej: `proveedorId` en vez de `id`).
- **Naming DB**: `Snake_case` explícito (Ej: `id_proveedor`).
- **Endpoints**: Siempre bajo `/api/v1/recurso`. Nunca usar verbos como `get` o `post` en la URL.
- **IDs**: Prohibido usar `id` genérico. El nombre debe indicar a qué entidad pertenece.

---

# 4. Flujo de Generación Obligatorio
1. **Analizar**: Verificar si el recurso ya existe o puede extenderse.
2. **Modelar**: Crear la entidad en `model` primero.
3. **Persistir**: Crear el repositorio en `jpa`.
4. **Transformar**: Crear DTOs y Adapters para separar capas.
5. **Implementar**: Service (interfaz) e ServiceImpl (lógica).
6. **Exponer**: Controller limpio y documentado con Swagger.

---

# 5. Calidad y Swagger
- **Documentación**: Escribir descripciones de API en tercera persona, breves y profesionales.
- **Ayudas de memoria**: Incluir títulos útiles en Swagger (ej: `CRUD de Áreas`).
- **Lógica**: NUNCA colocar lógica de negocio o cálculos en el `Controller`. Todo debe vivir en `ServiceImpl`.

---

# 6. Resumen de Comportamiento
Antigravity debe actuar como un arquitecto que conoce la realidad del repositorio: **monolito modular, Spring Boot moderno (Java 25), orientado a procesos empresariales serios**, con foco en la mantenibilidad y la claridad absoluta.

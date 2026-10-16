# Pruebas de Papelería Campus

## Entorno
- Java: 25.0.2
- JavaFX: 25
- PostgreSQL: completar versión
- Base de datos local: papeleria_campus

## Validaciones

Cada prueba debe mostrar un mensaje concreto y dejar la base
sin cambios. Las entradas rechazadas se prueban al guardar
y al actualizar, cuando corresponda.

| Caso | Entrada o acción | Resultado esperado | Resultado obtenido | Estado | Evidencia |
|---|---|---|--------------------|---|---|
| V1 | Código vacío, espacios y 11 caracteres | Rechaza los tres casos | Observado          | Observado | |
| V2 | Nombre con espacios, AB y 81 caracteres | Rechaza los tres casos | Observado          | Observado | |
| V3 | Categoría sin seleccionar | Rechaza; no permite escribir categorías ajenas | Observado          | Observado | |
| V4 | Precio abc | Rechaza sin cerrar la aplicación | Observado          | Observado | |
| V5 | Precio 0 y -1 | Rechaza ambos | Observado          | Observado | |
| V6 | Precio 12.345 y 100000000.00 | Rechaza sin redondear | Observado          | Observado | |
| V7 | Stock abc y 2.5 | Rechaza ambos | Observado          | Observado | |
| V8 | Stock -1 y 2147483648; luego 0 | Rechaza los dos primeros y admite 0 | Observado          | Observado | |
| V9 | Código duplicado al crear y actualizar; conservar código propio | Rechaza duplicados y permite conservar el propio | Observado          | Observado | |
| V10 | Actualizar y eliminar sin selección | Bloquea y pide seleccionar | Observado          | Observado | |
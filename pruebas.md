# Pruebas de Papelería Campus

## 1. Objetivo

Comprobar el funcionamiento de la aplicación de escritorio, la conexión con PostgreSQL, las búsquedas, las operaciones CRUD, las validaciones y el manejo de errores.

Todas las pruebas se realizaron en una base de datos local. Al finalizar cada bloque se restauraron los 12 productos iniciales.

## 2. Entorno de pruebas

- IDE: IntelliJ IDEA.
- Java: Eclipse Temurin 25.0.2.
- JavaFX: 25.
- Acceso a datos: JDBC con el controlador de PostgreSQL.
- Base de datos: PostgreSQL, `papeleria_campus`.
- Administración de la base de datos: pgAdmin.
- Dependencias y construcción: Maven.
- Configuración de conexión: variables `DB_URL`, `DB_USER` y `DB_PASSWORD`.

## 3. Carga inicial y TableView

| Caso | Entrada o acción | Resultado esperado | Resultado obtenido | Estado |
|---|---|---|---|---|
| T1 | Abrir la aplicación con los datos iniciales | Mostrar seis columnas y 12 registros ordenados por código | Se mostraron ID, código, nombre, categoría, precio y stock; contador 12 y orden ascendente por código | Aprobado |
| T2 | Comparar el listado con un SELECT en pgAdmin | Coincidencia entre tabla y base de datos | Los valores mostrados coincidieron con PostgreSQL | Aprobado |
| T3 | Insertar un producto temporal desde pgAdmin y pulsar Recargar | Mostrar el nuevo producto y actualizar el contador | El producto apareció sin reiniciar la aplicación; después se retiró y se recuperaron los 12 registros | Aprobado |
| T4 | Seleccionar distintas filas | Cargar los cinco campos editables y conservar el ID | El formulario mostró los datos de cada producto y el ID permaneció sin posibilidad de edición | Aprobado |
| T5 | Pulsar Nuevo / Limpiar formulario | Limpiar campos y selección sin modificar filtros | Se vació el formulario, se eliminó la selección y se conservaron los filtros | Aprobado |

Evidencia: `evidencias/01-listado-inicial.png`.

## 4. Búsquedas y filtros

Las pruebas se realizaron sobre los 12 productos iniciales. Cada búsqueda consultó PostgreSQL mediante PreparedStatement.

| Caso | Entrada o acción | Resultado esperado | Resultado obtenido | Estado |
|---|---|---|---|---|
| B1 | Texto CUADERNO, categoría Todas y sin filtro de existencias | P001 y P002; contador 2 | Se mostraron P001 y P002; contador 2 | Aprobado |
| B2 | Texto T00, categoría Todas y sin filtro de existencias | T001, T002, T003 y T004; contador 4 | Se mostraron los cuatro productos indicados; contador 4 | Aprobado |
| B3 | Texto vacío y categoría Papelería | P001, P002, P003 y P004; contador 4 | Se mostraron los cuatro productos de Papelería; contador 4 | Aprobado |
| B4 | Texto vacío, categoría Todas y Solo con existencias | 9 productos; excluir P004, T004 y A003 | Se mostraron 9 productos y se excluyeron los tres productos sin existencias | Aprobado |
| B5 | Texto USB, categoría Tecnología y Solo con existencias | T001 y T003; contador 2 | Se mostraron exclusivamente T001 y T003; contador 2 | Aprobado |
| B6 | Texto ZZZ999, categoría Todas y sin filtro de existencias | Cero filas, contador 0 y aviso Sin coincidencias | Se vació el listado y se mostró Registros visibles: 0 — Sin coincidencias | Aprobado |
| B7 | Pulsar Limpiar filtros después de B5 | Texto vacío, Todas, casilla desmarcada y 12 filas | Se restablecieron los controles y se mostraron los 12 registros | Aprobado |

Evidencia: `evidencias/02-filtro-combinado.png`.

## 5. Operaciones CRUD y persistencia

Las pruebas del ciclo CRUD se realizaron con los filtros limpios.

| Caso | Entrada o acción | Resultado esperado | Resultado obtenido | Estado |
|---|---|---|---|---|
| CRUD1 | Crear X001, Producto de prueba, Accesorios, precio 10.00 y stock 1 | Insertar una fila y mostrar 13 registros | X001 se guardó en PostgreSQL, apareció en el TableView y el contador cambió a 13 | Aprobado |
| CRUD2 | Consultar X001 en pgAdmin y reiniciar la aplicación | Mantener el producto creado | El SELECT devolvió los datos ingresados y X001 permaneció después del reinicio | Aprobado |
| CRUD3 | Actualizar X001 a precio 15.50 y stock 3 | Modificar solo X001 y mantener 13 filas | Cambiaron únicamente el precio y stock de X001; contador 13 | Aprobado |
| CRUD4 | Consultar X001 en pgAdmin y reiniciar la aplicación | Conservar la actualización | El SELECT y la aplicación mostraron precio 15.50 y stock 3 después del reinicio | Aprobado |
| CRUD5 | Solicitar la eliminación de X001 y cancelar | Conservar el producto y las 13 filas | X001 permaneció en la base y en el listado; contador 13 | Aprobado |
| CRUD6 | Solicitar nuevamente la eliminación y confirmar | Eliminar solo X001 y mostrar 12 filas | X001 se eliminó y el contador volvió a 12 | Aprobado |
| CRUD7 | Consultar X001 en pgAdmin y reiniciar | No encontrar X001 | El SELECT devolvió cero filas y X001 no apareció después del reinicio | Aprobado |
| CRUD8 | Con Solo con existencias activo, actualizar un producto temporal de stock 1 a 0 | Guardar el cambio y retirar el producto del listado filtrado | El stock se actualizó en PostgreSQL y el producto desapareció del listado al recargar | Aprobado |
| CRUD9 | Guardar o eliminar un producto temporal con filtros activos | Mantener los filtros y actualizar el listado según corresponda | Se conservaron los filtros y la tabla reflejó los resultados de la nueva consulta | Aprobado |

Se comprobó que UPDATE y DELETE utilizan `id_producto` para identificar la fila.

Evidencias:

- `evidencias/04-crud-insertar.png`
- `evidencias/05-crud-actualizar.png`
- `evidencias/06-crud-cancelar.png`
- `evidencias/07-crud-eliminar.png`
- `evidencias/08-verificacion-sql.png`

## 6. Validaciones

Las entradas inválidas se probaron al crear y actualizar, cuando correspondía. Después de cada rechazo se comprobó que PostgreSQL conservara los datos anteriores.

| Caso | Entrada o acción | Resultado esperado | Resultado obtenido | Estado |
|---|---|---|---|---|
| V1 | Código vacío, solo espacios y de 11 caracteres, al crear y actualizar | Rechazar todos los casos | Se exigió un código de 1 a 10 caracteres después de quitar espacios laterales; no se guardaron cambios | Aprobado |
| V2 | Nombre solo con espacios, AB y de 81 caracteres, al crear y actualizar | Rechazar todos los casos | Se exigió un nombre de 3 a 80 caracteres después de quitar espacios laterales; no se guardaron cambios | Aprobado |
| V3 | Categoría sin seleccionar, al crear y actualizar; intentar introducir una categoría ajena | Rechazar la ausencia e impedir valores ajenos al catálogo | Se solicitó una categoría válida y el ComboBox impidió escribir categorías adicionales | Aprobado |
| V4 | Precio abc, al crear y actualizar | Rechazar sin cerrar la aplicación | Se mostró un mensaje de formato inválido y la aplicación continuó funcionando | Aprobado |
| V5 | Precio 0 y -1, al crear y actualizar | Rechazar ambos valores | Se informó el rango permitido y no se modificó la base | Aprobado |
| V6 | Precio 12.345 y 100000000.00, al crear y actualizar | Rechazar exceso de decimales y de rango sin redondear | Ambos valores fueron rechazados antes de enviarse a PostgreSQL | Aprobado |
| V7 | Stock abc y 2.5, al crear y actualizar | Rechazar texto y cantidades no enteras | Se solicitó un número entero y no se guardaron cambios | Aprobado |
| V8 | Stock -1 y 2147483648; después stock 0, al crear y actualizar | Rechazar los dos primeros y aceptar 0 | Se rechazaron los valores inválidos y se guardó correctamente stock 0 | Aprobado |
| V9 | Crear con código " p001 "; actualizar otro producto a P001; actualizar P001 conservando su código | Rechazar duplicados después de normalizar y permitir conservar el código propio | El código se normalizó a P001, los duplicados fueron rechazados y la edición del propio P001 fue aceptada | Aprobado |
| V10 | Pulsar Actualizar y Eliminar sin seleccionar una fila | Bloquear ambas acciones y solicitar selección | Ambas acciones mostraron el aviso correspondiente y no modificaron PostgreSQL | Aprobado |

También se comprobó la eliminación de espacios laterales en código y nombre y la conversión del código a mayúsculas.

Evidencia: `evidencias/03-validacion.png`.

## 7. Excepciones y operaciones fallidas

| Caso | Entrada o acción | Resultado esperado | Resultado obtenido | Estado |
|---|---|---|---|---|
| E1 | Ejecutar con un puerto incorrecto en una configuración de prueba | Mostrar un error comprensible, mantener la aplicación abierta y evitar presentar el fallo como una consulta exitosa sin resultados | Se mostró el aviso de conexión, la ventana permaneció abierta y el estado indicó que el listado no se había actualizado | Aprobado |
| E2 | Invocar directamente el DAO con un código duplicado y con datos que incumplen un CHECK, sin pasar por las validaciones del formulario | Gestionar SQLException y conservar la integridad | PostgreSQL rechazó las escrituras, el DAO registró la operación y el SQLState y los datos existentes permanecieron sin cambios | Aprobado |
| E3-A | Seleccionar un producto temporal, eliminarlo en pgAdmin e intentar actualizarlo desde la aplicación | Detectar cero filas afectadas y no anunciar éxito | Se mostró que el producto ya no existía y se permitió recargar el listado | Aprobado |
| E3-B | Seleccionar un producto temporal, eliminarlo en pgAdmin e intentar eliminarlo desde la aplicación | Detectar cero filas afectadas y no anunciar éxito | Se mostró que el producto ya no existía y se permitió recargar el listado | Aprobado |
| E4 | Revisar los mensajes y la consola durante operaciones fallidas | Registrar operación y SQLState; no mostrar contraseñas ni trazas al usuario; evitar avisos falsos de escritura exitosa | La consola registró la operación y el SQLState; la interfaz mostró mensajes comprensibles y no anunció éxito en las escrituras fallidas | Aprobado |

Al finalizar E1 se restauró la configuración correcta. Las pruebas directas al DAO se realizaron fuera de la interfaz final, sin desactivar restricciones ni agregar botones de prueba.

Evidencias:

- `evidencias/09-error-conexion.png`
- `evidencias/10-restriccion-dao.png`
- `evidencias/11-cero-filas-afectadas.png`
- `evidencias/12-registro-sqlstate.png`

## 8. Verificación de restricciones en PostgreSQL

Las restricciones se comprobaron también mediante sentencias SQL ejecutadas directamente en la base local.

| Restricción | Comprobación | Resultado obtenido | Estado |
|---|---|---|---|
| PRIMARY KEY e IDENTITY | Insertar sin proporcionar ID | PostgreSQL generó un identificador único | Aprobado |
| NOT NULL | Intentar insertar NULL en cada campo obligatorio | PostgreSQL rechazó cada inserción | Aprobado |
| UNIQUE | Intentar insertar un código existente | PostgreSQL rechazó el duplicado | Aprobado |
| CHECK de código | Intentar guardar código vacío, con espacios laterales o en minúsculas | PostgreSQL rechazó los valores | Aprobado |
| CHECK de nombre | Intentar guardar un nombre cuya longitud tras trim fuera menor que 3 | PostgreSQL rechazó el valor | Aprobado |
| CHECK de categoría | Intentar guardar una categoría ajena al catálogo | PostgreSQL rechazó el valor | Aprobado |
| CHECK de precio | Intentar guardar precio 0 y negativo | PostgreSQL rechazó ambos valores | Aprobado |
| CHECK de stock | Intentar guardar stock negativo | PostgreSQL rechazó el valor | Aprobado |

## 9. Revisión del acceso a datos

Se comprobó lo siguiente:

- La aplicación establece una conexión real con PostgreSQL.
- Los valores variables se envían mediante parámetros de PreparedStatement.
- Las consultas utilizan executeQuery y las escrituras executeUpdate.
- Connection, PreparedStatement y ResultSet se cierran mediante try-with-resources.
- Las sentencias SQL están en ProductoDAO.
- La configuración de conexión se obtiene de variables de entorno.
- El precio se representa con BigDecimal.
- Después de las escrituras se vuelve a consultar PostgreSQL conservando los filtros.

## 10. Estado final

Finalizadas las pruebas, se eliminaron los productos temporales y se restauraron los 12 registros originales.

Se verificó el estado final mediante:

```sql
SELECT *
FROM producto
ORDER BY codigo ASC;

SELECT COUNT(*) AS total
FROM producto;
```

El resultado fue de 12 productos, sin registros adicionales. La aplicación mostró los mismos datos después de reiniciarse.
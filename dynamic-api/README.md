# API REST Dinámica para Consulta de Tablas MySQL

Esta aplicación Spring Boot permite consultar datos de cualquier tabla en una base de datos MySQL alojada en Aiven de forma dinámica, entregando los resultados a través de una API REST.

## Características

- Conexión con base de datos MySQL en Aiven
- Consulta dinámica de cualquier tabla existente en la base de datos
- Paginación de resultados
- Ordenamiento por cualquier columna
- Filtrado por valor en una columna específica
- Documentación de API con Swagger

## Requisitos

- Java 17 o superior
- Maven 3.6 o superior
- Base de datos MySQL (configurada en Aiven)

## Configuración

La aplicación viene preconfigurada para conectarse a una base de datos MySQL alojada en Aiven con los siguientes parámetros:

```properties
spring.datasource.url=jdbc:mysql://ferrefull-ferrefull-ro.g.aivencloud.com:25526/defaultdb?sslMode=REQUIRED
spring.datasource.username=avnadmin
spring.datasource.password=AVNS_3CYhqEaVQF1Zk86Vjdo
```

Estos parámetros están configurados en el archivo `application.properties`.

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── ferrefull/
│   │           └── dynamicapi/
│   │               ├── controller/
│   │               │   └── DynamicTableController.java
│   │               ├── dto/
│   │               │   ├── ApiResponse.java
│   │               │   └── TableRequestDTO.java
│   │               ├── exception/
│   │               │   └── GlobalExceptionHandler.java
│   │               ├── service/
│   │               │   └── DynamicQueryService.java
│   │               └── DynamicApiApplication.java
│   └── resources/
│       └── application.properties
```

## Compilación y Ejecución

Para compilar y ejecutar el proyecto, sigue estos pasos:

1. Clona el repositorio o descarga los archivos
2. Navega al directorio raíz del proyecto
3. Ejecuta los siguientes comandos:

```bash
mvn clean package
java -jar target/dynamic-api-0.0.1-SNAPSHOT.jar
```

O simplemente ejecuta:

```bash
mvn spring-boot:run
```

La aplicación se iniciará y estará disponible en `http://localhost:8080/api`.

## Documentación de la API

La documentación de la API está disponible a través de Swagger UI en:

```
http://localhost:8080/api/swagger-ui.html
```

## Endpoints de la API

### Listar todas las tablas

```
GET /api/tables
```

Devuelve la lista de todas las tablas disponibles en la base de datos.

### Obtener datos de una tabla

```
GET /api/tables/{tableName}
```

Parámetros de consulta opcionales:
- `page`: Número de página (0-based)
- `size`: Tamaño de página
- `orderBy`: Columna por la que ordenar
- `orderDirection`: Dirección de ordenamiento (ASC o DESC)
- `filterColumn`: Columna por la que filtrar
- `filterValue`: Valor para filtrar

### Obtener estructura de una tabla

```
GET /api/tables/{tableName}/columns
```

Devuelve información sobre las columnas de la tabla especificada.

### Consultar tabla con parámetros complejos

```
POST /api/tables/query
```

Cuerpo de la solicitud (JSON):
```json
{
  "tableName": "nombre_tabla",
  "page": 0,
  "size": 10,
  "orderBy": "columna_orden",
  "orderDirection": "ASC",
  "filterColumn": "columna_filtro",
  "filterValue": "valor_filtro"
}
```

## Ejemplos de Uso

### Listar todas las tablas

```
GET http://localhost:8080/api/tables
```

Respuesta:
```json
{
  "success": true,
  "message": "Tablas obtenidas correctamente",
  "data": [
    "usuarios",
    "productos",
    "categorias"
  ]
}
```

### Obtener datos de la tabla 'usuarios'

```
GET http://localhost:8080/api/tables/usuarios?page=0&size=10&orderBy=nombre&orderDirection=ASC
```

Respuesta:
```json
{
  "success": true,
  "message": "Datos obtenidos correctamente de la tabla usuarios",
  "data": {
    "rows": [
      {
        "id": 1,
        "nombre": "Juan",
        "email": "juan@example.com"
      },
      {
        "id": 2,
        "nombre": "María",
        "email": "maria@example.com"
      }
    ],
    "columns": [
      {
        "name": "id",
        "type": "INT",
        "size": 10,
        "nullable": false
      },
      {
        "name": "nombre",
        "type": "VARCHAR",
        "size": 100,
        "nullable": false
      },
      {
        "name": "email",
        "type": "VARCHAR",
        "size": 100,
        "nullable": true
      }
    ],
    "totalElements": 2,
    "totalPages": 1,
    "currentPage": 0
  },
  "totalElements": 2,
  "totalPages": 1,
  "currentPage": 0
}
```

## Seguridad

Esta aplicación no implementa seguridad a nivel de autenticación/autorización. En un entorno de producción, se recomienda implementar:

- Autenticación mediante JWT o Spring Security
- HTTPS para la comunicación segura
- Validación de entrada para prevenir inyección SQL

## Contribuir

Si deseas contribuir a este proyecto, puedes hacerlo a través de pull requests o reportando issues.

## Licencia

Este proyecto está licenciado bajo la licencia MIT.

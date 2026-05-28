# API de Analítica de Aprendizaje — TdeA

**API de Analítica de Aprendizaje para Detección Temprana y Retroalimentación Personalizada**  
Tecnológico de Antioquia — Trabajo de Grado

---

## Restricciones importantes

> Esta API **NO** implementa:
> - Autenticación
> - Autorización
> - Gestión de Cursos (sin entidad Curso administrable)
> - Gestión de Estudiantes (sin entidad Estudiante CRUD)
>
> Los estudiantes se referencian mediante el campo `referenciaEstudiante` (identificador externo/seudonimizado).

---

## Stack tecnológico

| Tecnología | Versión |
|---|---|
| Java | 21 |
| Spring Boot | 3.2.5 |
| PostgreSQL | 16 |
| Flyway | incluido en Spring Boot |
| OpenAPI/Swagger | springdoc 2.5.0 |
| Maven | 3.9+ |
| Docker | 24+ |

---

## Ejecución rápida con Docker Compose

```bash
# 1. Clonar el repositorio
git clone <url-repo>
cd feedbackSystem

# 2. Levantar base de datos y API
docker compose up --build

# 3. Verificar que la API está activa
curl http://localhost:8080/api/v1/rubricas
```

La API estará disponible en: `http://localhost:8080`  
Swagger UI: `http://localhost:8080/swagger-ui.html`  
OpenAPI JSON: `http://localhost:8080/api-docs`

---

## Ejecución local (sin Docker)

### Pre-requisitos
- Java 21
- Maven 3.9+
- PostgreSQL 16 corriendo en `localhost:5432`

### Configurar base de datos

```sql
CREATE DATABASE feedback_system_db;
CREATE USER feedback_user WITH PASSWORD 'feedback_pass';
GRANT ALL PRIVILEGES ON DATABASE feedback_system_db TO feedback_user;
```

### Ejecutar la aplicación

```bash
./mvnw spring-boot:run
```

Flyway ejecutará automáticamente las migraciones `V1` (esquema) y `V2` (datos de prueba).

---

## Estructura del proyecto

```
src/
├── main/java/co/edu/tdea/feedbacksystem/
│   ├── config/          # OpenAPI/Swagger
│   ├── controller/      # Controladores REST
│   ├── dto/
│   │   ├── request/     # DTOs de entrada
│   │   └── response/    # DTOs de salida
│   ├── entity/          # Entidades JPA
│   ├── exception/       # Manejo global de errores
│   ├── mapper/          # Conversión entidad ↔ DTO
│   ├── repository/      # Spring Data JPA
│   └── service/         # Lógica de negocio
└── main/resources/
    └── db/migration/    # Migraciones Flyway
```

---

## Endpoints principales

### Rúbricas
```
POST   /api/v1/rubricas
GET    /api/v1/rubricas
GET    /api/v1/rubricas/{id}
PUT    /api/v1/rubricas/{id}
POST   /api/v1/rubricas/{id}/versiones
POST   /api/v1/rubricas/{id}/criterios
GET    /api/v1/rubricas/{id}/criterios
```

### Plantillas de Retroalimentación
```
POST   /api/v1/plantillas
GET    /api/v1/plantillas
GET    /api/v1/plantillas/{id}
PUT    /api/v1/plantillas/{id}
```

### Actividades Evaluativas de Referencia
```
POST   /api/v1/actividades-referencia
GET    /api/v1/actividades-referencia
GET    /api/v1/actividades-referencia/{id}
PUT    /api/v1/actividades-referencia/{id}
```

### Calificaciones de Referencia
```
POST   /api/v1/calificaciones-referencia
GET    /api/v1/calificaciones-referencia/{id}
GET    /api/v1/calificaciones-referencia?referenciaEstudiante={ref}
```

### Feedback
```
POST   /api/v1/feedback
GET    /api/v1/feedback/{id}
GET    /api/v1/feedback/estudiante/{referenciaEstudiante}
GET    /api/v1/feedback/actividad/{actividadId}
POST   /api/v1/feedback/{id}/publicar
```

### Perfil Académico
```
GET    /api/v1/perfiles/{referenciaEstudiante}
POST   /api/v1/perfiles/{referenciaEstudiante}/recalcular
GET    /api/v1/perfiles/{referenciaEstudiante}/recomendaciones
```

### Reportes
```
GET    /api/v1/reportes/actividad/{actividadId}
GET    /api/v1/reportes/feedback
GET    /api/v1/reportes/coordinacion
```

### Auditoría
```
GET    /api/v1/auditoria?page=0&size=20
GET    /api/v1/auditoria/{entidad}/{entidadId}
```

---

## Ejemplos cURL

### Crear rúbrica (suma de pesos debe ser 100)
```bash
curl -X POST http://localhost:8080/api/v1/rubricas \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Rúbrica Ejemplo",
    "descripcion": "Evaluación de proyecto integrador",
    "criterios": [
      {
        "nombre": "Diseño",
        "descripcion": "Calidad del diseño de la solución",
        "peso": 40,
        "orden": 1,
        "niveles": [
          { "nombre": "Excelente", "puntajeMin": 4.5, "puntajeMax": 5.0 },
          { "nombre": "Bueno",     "puntajeMin": 3.5, "puntajeMax": 4.4 },
          { "nombre": "Básico",    "puntajeMin": 3.0, "puntajeMax": 3.4 },
          { "nombre": "Deficiente","puntajeMin": 0.0, "puntajeMax": 2.9 }
        ]
      },
      {
        "nombre": "Implementación",
        "descripcion": "Calidad de la implementación",
        "peso": 60,
        "orden": 2,
        "niveles": [
          { "nombre": "Excelente", "puntajeMin": 4.5, "puntajeMax": 5.0 },
          { "nombre": "Bueno",     "puntajeMin": 3.5, "puntajeMax": 4.4 }
        ]
      }
    ]
  }'
```

### Registrar calificación
```bash
curl -X POST http://localhost:8080/api/v1/calificaciones-referencia \
  -H "Content-Type: application/json" \
  -d '{
    "actividadEvaluativaId": "<uuid-actividad>",
    "referenciaEstudiante": "EST-2026-010",
    "valorNumerico": 4.3,
    "observacionGeneral": "Buen desempeño general"
  }'
```

### Crear y publicar feedback
```bash
# Crear feedback
curl -X POST http://localhost:8080/api/v1/feedback \
  -H "Content-Type: application/json" \
  -d '{
    "calificacionReferenciaId": "<uuid-calificacion>",
    "resumenGeneral": "Excelente trabajo final",
    "comentarios": [
      {
        "criterioId": "<uuid-criterio>",
        "comentario": "Muy buena implementación",
        "fortaleza": "Código limpio y bien documentado",
        "oportunidadMejora": "Agregar más pruebas unitarias"
      }
    ]
  }'

# Publicar feedback
curl -X POST http://localhost:8080/api/v1/feedback/<uuid-feedback>/publicar
```

### Recalcular perfil académico
```bash
curl -X POST http://localhost:8080/api/v1/perfiles/EST-2026-001/recalcular
```

---

## Formato de errores

Todos los errores siguen este formato:

```json
{
  "timestamp": "2026-05-27T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "La suma de ponderaciones de los criterios debe ser 100. Suma actual: 80",
  "path": "/api/v1/rubricas"
}
```

Para errores de validación de campos:
```json
{
  "timestamp": "2026-05-27T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Error de validación en los datos enviados",
  "path": "/api/v1/rubricas",
  "fieldErrors": [
    {
      "field": "nombre",
      "message": "El nombre de la rubrica no puede estar vacio",
      "rejectedValue": ""
    }
  ]
}
```

---

## Datos de prueba

La migración `V2` carga automáticamente:
- 1 rúbrica con 4 criterios (suma = 100%)
- 1 plantilla de retroalimentación
- 1 actividad evaluativa de referencia
- 3 calificaciones (EST-2026-001, EST-2026-002, EST-2026-003)
- 1 feedback publicado con comentarios por criterio
- 1 perfil académico básico con recomendaciones

---

## Ejecutar pruebas

```bash
# Pruebas unitarias (sin necesidad de BD)
./mvnw test

# Solo pruebas de un módulo
./mvnw test -Dtest=RubricaServiceTest

# Generar reporte de cobertura
./mvnw verify
```

---

## Variables de entorno (Docker)

| Variable | Por defecto |
|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://postgres:5432/feedback_system_db` |
| `SPRING_DATASOURCE_USERNAME` | `feedback_user` |
| `SPRING_DATASOURCE_PASSWORD` | `feedback_pass` |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | `validate` |
| `SERVER_PORT` | `8080` |

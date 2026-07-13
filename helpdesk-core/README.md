# Helpdesk Core — Backend

API REST en Java para la gestión de clientes, equipos y tickets de soporte
técnico. Modela un caso real de soporte técnico Nivel 1: cada cliente tiene
uno o más equipos asignados, y cada equipo puede tener uno o más tickets de
incidencias reportadas.

**🔗 Demo en vivo:** https://helpdesk-core-pbgv.onrender.com/clientes
*(el plan gratuito de Render "duerme" el servidor tras 15 min de inactividad;
la primera petición puede tardar ~30-50 segundos en responder)*

**🖥️ Frontend que consume esta API:** https://helpdesk-core-one.web.app
([repo](https://github.com/FerSierra87/helpdesk-core-frontend))

## Modelo de datos

```
Cliente (1) ───< (N) Equipo (1) ───< (N) Ticket
```

- Un **Cliente** puede tener varios **Equipos** asignados (notebooks,
  monitores, impresoras, etc.)
- Un **Equipo** puede tener varios **Tickets** (incidencias reportadas sobre
  ese equipo en particular)
- La relación se modela con claves foráneas reales en PostgreSQL
  (`equipos.cliente_id`, `tickets.equipo_id`), no solo en el código Java

## Stack

- **Java 21**
- **Spring Boot 3.5.x** (Web, Data JPA, Validation)
- **PostgreSQL** (hosteado en [Supabase](https://supabase.com))
- **Lombok** (reduce código repetitivo de getters/setters)
- **Docker** (multi-stage build para el despliegue)
- **Render** (hosting del backend)

## Endpoints

### Clientes

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/clientes` | Lista todos los clientes |
| `GET` | `/clientes/{id}` | Busca un cliente por ID |
| `POST` | `/clientes` | Crea un cliente nuevo |
| `PUT` | `/clientes/{id}` | Actualiza un cliente existente |
| `DELETE` | `/clientes/{id}` | Elimina un cliente |

### Equipos

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/equipos` | Lista todos los equipos |
| `GET` | `/equipos/{id}` | Busca un equipo por ID |
| `GET` | `/equipos/cliente/{clienteId}` | Lista los equipos de un cliente |
| `POST` | `/equipos` | Crea un equipo (requiere `cliente.id` existente) |
| `PUT` | `/equipos/{id}` | Actualiza un equipo existente |
| `DELETE` | `/equipos/{id}` | Elimina un equipo |

### Tickets

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/tickets` | Lista todos los tickets |
| `GET` | `/tickets/{id}` | Busca un ticket por ID |
| `GET` | `/tickets/equipo/{equipoId}` | Lista los tickets de un equipo |
| `GET` | `/tickets/estado/{estado}` | Filtra por estado (`abierto` / `resuelto`) |
| `POST` | `/tickets` | Crea un ticket (requiere `equipo.id` existente) |
| `PUT` | `/tickets/{id}/resolver` | Marca un ticket como resuelto |
| `DELETE` | `/tickets/{id}` | Elimina un ticket |

### Ejemplo de body para crear un ticket

```json
{
  "descripcion": "El usuario no puede iniciar sesión desde ayer",
  "categoria": "Accesos",
  "prioridad": "media",
  "equipo": { "id": 1 }
}
```

## Correrlo en local

### Requisitos
- JDK 21
- Una base de datos PostgreSQL (local, o gratis en [Neon](https://neon.com)/[Supabase](https://supabase.com))

### Pasos

1. Cloná el repo y abrilo en tu IDE (recomendado: IntelliJ IDEA)

2. Configurá `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://TU_HOST:5432/postgres
   spring.datasource.username=TU_USUARIO
   spring.datasource.password=${DB_PASSWORD}

   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
   spring.jpa.show-sql=true

   server.port=${PORT:8080}
   ```

3. Definí la variable de entorno `DB_PASSWORD` con tu contraseña real
   (en IntelliJ: *Edit Configurations → Environment variables*)

4. Corré la clase `HelpdeskCoreApplication` — Hibernate crea las tablas
   automáticamente en el primer arranque

5. Probá con Postman o `curl`:
   ```bash
   curl http://localhost:8080/clientes
   ```

## Despliegue (Docker + Render)

El proyecto incluye un `Dockerfile` multi-stage:

1. **Etapa de build**: compila el proyecto con Maven sobre una imagen con JDK 21
2. **Etapa final**: copia solo el `.jar` resultante a una imagen liviana
   (`eclipse-temurin:21-jre-alpine`)

En Render, el servicio está configurado como tipo **Docker**, apuntando a este
`Dockerfile`, con la variable de entorno `DB_PASSWORD` cargada en el panel de
Render (nunca en el código ni en el repo).

> ⚠️ Nota técnica: la conexión a Supabase usa el **Session Pooler**
> (`*.pooler.supabase.com`) en vez de la conexión directa, porque esta última
> usa IPv6 y no es alcanzable desde la red de Render.

## Notas de seguridad

- La contraseña de la base de datos nunca está en el código — se inyecta por
  variable de entorno (`${DB_PASSWORD}`).
- CORS está configurado explícitamente (`CorsConfig.java`) para aceptar
  únicamente el frontend autorizado, no cualquier origen.

## Posibles mejoras a futuro

- Autenticación (JWT) para proteger los endpoints
- Paginación en los listados (`/clientes`, `/equipos`, `/tickets`)
- Documentación interactiva con Swagger/OpenAPI
- Validaciones más estrictas con mensajes de error personalizados
- Tests unitarios e de integración

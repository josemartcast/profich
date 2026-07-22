# ProFich

ProFich es una aplicación independiente de registro horario para un pequeño negocio de hostelería. El proyecto nace para cubrir una necesidad real y, al mismo tiempo, servir como proyecto de portfolio.

Esta primera entrega deja una base limpia y compilable. Todavía no implementa el sistema completo de fichajes.

## Arquitectura

El repositorio es un monorepo con dos aplicaciones independientes:

```text
profich/
├── android-app/   Aplicación nativa para la tablet del establecimiento
├── backend/       API REST y reglas de negocio
└── docs/          Diseño y planes de implementación
```

### Android

- Kotlin y Jetpack Compose.
- Material 3 y Navigation Compose.
- Estado de pantalla gestionado con `ViewModel` y `StateFlow`.
- Paquetes iniciales separados en `presentation`, `domain` y `data`.
- Pantalla principal y navegación mínima a una pantalla informativa.
- Identificador de aplicación: `com.josemartcast.profich`.

### Backend

- Java 21 y Spring Boot 4.1.
- Maven Wrapper.
- Spring Web MVC, Validation, Security y Data JPA.
- H2 en memoria para desarrollo y pruebas.
- Configuración de seguridad provisional que permite las peticiones mientras se diseña la autenticación real.
- Endpoint mínimo de comprobación: `GET /api/status`.

Respuesta esperada:

```json
{
  "service": "profich-backend",
  "status": "UP"
}
```

## Alcance del MVP

El MVP previsto incluirá:

- Gestión y desactivación lógica de empleados.
- Identificación del empleado mediante PIN personal.
- Entrada, inicio de pausa, fin de pausa y salida.
- Varias sesiones en un mismo día para admitir turnos partidos.
- Consulta del estado actual y del historial propio.
- Resúmenes diarios y mensuales.
- Incidencias y correcciones justificadas sin sobrescribir el registro original.
- Historial de auditoría.
- Sincronización desde una tablet cuando se recupere la conexión.
- Exportación de registros.

No forman parte de esta base inicial la autenticación completa, el almacenamiento offline, la auditoría, las exportaciones ni el modelo de dominio definitivo.

## Reglas de fichaje

El estado de cada empleado seguirá esta máquina de estados:

```text
FUERA_DE_JORNADA --CLOCK_IN-----> TRABAJANDO
TRABAJANDO       --BREAK_START--> EN_PAUSA
EN_PAUSA         --BREAK_END----> TRABAJANDO
TRABAJANDO       --CLOCK_OUT----> FUERA_DE_JORNADA
```

Las demás transiciones se rechazarán. Los turnos partidos se representarán mediante varias sesiones de trabajo y las pausas pertenecerán a una sesión. El backend será la autoridad para validar cada evento y asignar su hora oficial. Los registros originales no se modificarán silenciosamente: cualquier corrección futura deberá conservar el dato original, el motivo y quién la autorizó.

## Puesta en marcha

### Requisitos

- JDK 21.
- Android Studio o Android SDK con la plataforma Android 37.0.
- Variable `ANDROID_HOME` apuntando al SDK para compilar desde terminal.

No es necesario instalar Gradle ni Maven globalmente; ambos proyectos incluyen sus wrappers.

### Aplicación Android

En Windows:

```powershell
cd android-app
.\gradlew.bat test assembleDebug
```

El APK de desarrollo se genera en `android-app/app/build/outputs/apk/debug/`.

### Backend

En Windows:

```powershell
cd backend
.\mvnw.cmd verify
.\mvnw.cmd spring-boot:run
```

Con el backend iniciado, el estado del servicio estará disponible en `http://localhost:8080/api/status`.

## Próximos pasos

1. Definir el modelo de eventos de fichaje y sus invariantes.
2. Diseñar empleados, dispositivos y sesiones de trabajo.
3. Implementar los casos de uso del backend con pruebas.
4. Conectar la app Android con la API.
5. Añadir autenticación por roles y PIN protegido.
6. Incorporar persistencia offline, sincronización y resolución de conflictos.
7. Añadir correcciones auditadas y exportación de registros.
8. Preparar PostgreSQL, Docker y el despliegue del entorno real.

## Documentación

- [Diseño de la base inicial](docs/superpowers/specs/2026-07-22-profich-foundation-design.md)
- [Plan de implementación](docs/superpowers/plans/2026-07-22-profich-foundation.md)

## Licencia

Este proyecto se distribuye bajo la [licencia MIT](LICENSE).

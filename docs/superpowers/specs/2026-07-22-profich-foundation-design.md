# Diseño de la base inicial de ProFich

## Objetivo

Preparar ProFich como un monorepo profesional y compilable con una aplicación
Android nativa y un backend independiente. Esta fase solo establece una base
limpia para el desarrollo posterior del registro horario.

## Alcance de esta fase

- Monorepo con `android-app/` y `backend/`.
- Aplicación Android mínima con Kotlin, Jetpack Compose, Material 3,
  Navigation Compose y ViewModel.
- Backend mínimo con Spring Boot, Java 21 y Maven.
- Documentación raíz en español.
- Configuración de desarrollo con H2.
- Pruebas mínimas que validen el arranque y la estructura creada.
- Commits separados por entregable.

Quedan expresamente fuera la autenticación completa, el fichaje real, la
persistencia del dominio, la sincronización sin conexión, la auditoría, las
incidencias, las correcciones y las exportaciones.

## Arquitectura del monorepo

```text
profich/
├── android-app/
│   ├── app/
│   ├── gradle/
│   ├── build.gradle.kts
│   └── settings.gradle.kts
├── backend/
│   ├── src/main/java/com/josemartcast/profich/
│   ├── src/main/resources/
│   ├── src/test/
│   └── pom.xml
├── docs/
├── .editorconfig
├── .gitignore
└── README.md
```

Android y backend mantienen ciclos de construcción independientes. No se
añade un orquestador raíz porque todavía no existe una necesidad real que lo
justifique.

## Aplicación Android

### Herramientas y compatibilidad

- Identificador: `com.josemartcast.profich`.
- Android Gradle Plugin 9.3.0.
- Gradle 9.5.0.
- Kotlin integrado en AGP y plugin de Compose compatible.
- Compose BOM 2026.06.00.
- `compileSdk` y `targetSdk`: 37.
- `minSdk`: 26, suficiente para tabletas Android 8 o posteriores.
- Java 21 para ejecutar Gradle; bytecode de la aplicación en Java 17.

La compilación Android requiere Android SDK Platform 37. El repositorio no
versiona `local.properties` ni una ruta local al SDK.

### Organización

La primera versión usa un único módulo `app`. Los paquetes se separan por
responsabilidad sin convertir todavía cada capa en un módulo Gradle:

```text
com.josemartcast.profich
├── data
├── domain
└── presentation
    ├── navigation
    ├── home
    └── about
```

`data` contendrá en el futuro implementaciones de repositorios y fuentes de
datos. `domain` contendrá modelos y casos de uso independientes de Android.
`presentation` contiene Compose, navegación y estado de pantalla.

### Interfaz mínima

La aplicación muestra una pantalla de inicio con el nombre ProFich, el estado
de esta fase y un acceso a una pantalla informativa. La segunda pantalla
permite volver. Esta navegación demuestra que la estructura funciona sin
simular todavía fichajes ni datos.

El estado inicial se expone desde un ViewModel como un valor inmutable. No hay
llamadas HTTP, base de datos local ni inyección de dependencias en esta fase.

## Backend

### Herramientas

- Java 21.
- Spring Boot 4.1.0.
- Maven y Maven Wrapper.
- Spring Web MVC.
- Bean Validation.
- Spring Security.
- Spring Data JPA.
- H2 en memoria para desarrollo y pruebas.

### Organización

```text
com.josemartcast.profich
├── ProFichApplication
├── config
├── shared
└── status
```

Solo se implementa un endpoint `GET /api/status` con una respuesta fija que
permite comprobar que el servicio está disponible. No se crean todavía
entidades `Employee`, `TimeEvent` ni repositorios JPA ficticios.

Spring Security se configura de forma provisional para permitir el endpoint
de estado y desactivar la creación de una autenticación funcional. La clase y
la documentación indican que esta política se sustituirá al desarrollar la
autenticación.

La configuración H2 usa una base en memoria y `ddl-auto: validate`, de modo
que el arranque no cree esquemas de dominio inexistentes de forma silenciosa.
La consola web de H2 permanece desactivada.

## Dominio previsto del registro horario

Aunque esta fase no lo implementa, el README establece estas reglas como
contrato para el desarrollo posterior:

- Estados: `FUERA_DE_JORNADA`, `TRABAJANDO` y `EN_PAUSA`.
- Transiciones válidas:
  - `FUERA_DE_JORNADA -> TRABAJANDO` mediante entrada.
  - `TRABAJANDO -> EN_PAUSA` mediante inicio de pausa.
  - `EN_PAUSA -> TRABAJANDO` mediante fin de pausa.
  - `TRABAJANDO -> FUERA_DE_JORNADA` mediante salida.
- Eventos previstos: `CLOCK_IN`, `BREAK_START`, `BREAK_END` y `CLOCK_OUT`.
- Los eventos originales serán inmutables.
- La hora del servidor será la referencia al sincronizar.
- Un día podrá contener varias sesiones para soportar turnos partidos.
- Las correcciones futuras conservarán el original y exigirán motivo.

## Errores y configuración

Android no oculta errores de configuración del SDK: el README explica los
prerrequisitos y Gradle informa si falta Platform 37. El backend usa la
respuesta estándar de Spring para rutas inexistentes; no se añade todavía un
manejador global porque no existe dominio que lo necesite.

Los secretos y las rutas locales quedan fuera de Git. No se incluyen claves,
usuarios reales ni credenciales de producción.

## Estrategia de pruebas

- Android: prueba unitaria del estado inicial del ViewModel. Las pruebas de UI
  quedan preparadas como dependencia, pero no requieren emulador en esta fase.
- Backend: prueba de carga del contexto y prueba MVC de `GET /api/status`.
- Verificación final: `gradlew.bat test` para Android cuando esté instalado el
  SDK 37 y `mvnw.cmd verify` para backend.

La implementación de comportamiento nuevo seguirá ciclos de prueba fallida,
implementación mínima y prueba correcta. Los archivos generados y de
configuración no necesitan una prueba previa específica.

## Documentación y commits

El README raíz explica propósito, arquitectura, requisitos, comandos de
arranque, alcance MVP, reglas de fichaje, exclusiones y próximos pasos. Los
commits de esta fase se separan en:

1. Diseño aprobado.
2. Estructura y configuración comunes.
3. Base Android.
4. Base backend.
5. Documentación final y ajustes de verificación.

## Criterios de aceptación

- El repositorio conserva su licencia MIT y no contiene secretos.
- Android y backend están separados y documentados.
- El identificador común es `com.josemartcast.profich`.
- Android sincroniza sus dependencias y compila con SDK 37 instalado.
- Backend completa `mvnw.cmd verify` con Java 21.
- No se implementa ninguna funcionalidad excluida de esta fase.
- El árbol de trabajo queda limpio después de los commits.

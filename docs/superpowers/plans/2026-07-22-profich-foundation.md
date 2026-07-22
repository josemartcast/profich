# ProFich Foundation Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Crear una base profesional y compilable de ProFich como monorepo con una aplicación Android Compose y un backend Spring Boot independiente.

**Architecture:** Android y backend se construyen de forma independiente dentro de un mismo repositorio. Android usa un único módulo `app` organizado en `data`, `domain` y `presentation`; el backend mantiene paquetes por función y solo expone un endpoint técnico de estado.

**Tech Stack:** Android Gradle Plugin 9.3.0, Gradle 9.5.0, Kotlin/Compose, Compose BOM 2026.06.00, Navigation Compose 2.9.8, Java 21, Spring Boot 4.1.0, Maven, H2, JUnit 5.

## Global Constraints

- El identificador común debe ser `com.josemartcast.profich`.
- Android usa `compileSdk = 37`, `targetSdk = 37`, `minSdk = 26` y bytecode Java 17.
- El backend usa Java 21 y Spring Boot 4.1.0.
- No implementar autenticación completa, fichajes, persistencia del dominio, offline, auditoría ni exportaciones.
- Los secretos, rutas locales, artefactos de construcción y bases locales no se versionan.
- Cada comportamiento propio se crea con prueba fallida primero; archivos generados y configuración se verifican mediante sincronización o compilación.

---

### Task 1: Configuración común del monorepo

**Files:**
- Create: `.editorconfig`
- Modify: `.gitignore`

**Interfaces:**
- Consumes: repositorio Git existente.
- Produces: reglas comunes de formato e ignorados usadas por ambos proyectos.

- [ ] **Step 1: Crear `.editorconfig`**

Definir UTF-8, LF, nueva línea final, espacios y cuatro espacios para Java/Kotlin/XML; dos espacios para YAML/JSON.

- [ ] **Step 2: Completar `.gitignore`**

Añadir entradas específicas para Android (`local.properties`, `.externalNativeBuild/`, `.cxx/`, `captures/`, `*.apk`, `*.aab`) y H2 (`*.mv.db`, `*.trace.db`) sin eliminar las reglas existentes.

- [ ] **Step 3: Verificar la configuración**

Run: `git diff --check`
Expected: exit 0 sin errores de espacios.

- [ ] **Step 4: Commit**

```text
chore: prepare monorepo configuration
```

### Task 2: Base Android Compose

**Files:**
- Create: `android-app/settings.gradle.kts`
- Create: `android-app/build.gradle.kts`
- Create: `android-app/gradle.properties`
- Create: `android-app/gradle/libs.versions.toml`
- Create: `android-app/gradle/wrapper/gradle-wrapper.properties`
- Create: `android-app/gradlew`
- Create: `android-app/gradlew.bat`
- Create: `android-app/gradle/wrapper/gradle-wrapper.jar`
- Create: `android-app/app/build.gradle.kts`
- Create: `android-app/app/proguard-rules.pro`
- Create: `android-app/app/src/main/AndroidManifest.xml`
- Create: `android-app/app/src/main/res/values/strings.xml`
- Create: `android-app/app/src/main/res/values/themes.xml`
- Create: `android-app/app/src/main/java/com/josemartcast/profich/MainActivity.kt`
- Create: `android-app/app/src/main/java/com/josemartcast/profich/presentation/ProFichApp.kt`
- Create: `android-app/app/src/main/java/com/josemartcast/profich/presentation/navigation/ProFichDestination.kt`
- Create: `android-app/app/src/main/java/com/josemartcast/profich/presentation/home/HomeUiState.kt`
- Create: `android-app/app/src/main/java/com/josemartcast/profich/presentation/home/HomeViewModel.kt`
- Create: `android-app/app/src/main/java/com/josemartcast/profich/presentation/home/HomeScreen.kt`
- Create: `android-app/app/src/main/java/com/josemartcast/profich/presentation/about/AboutScreen.kt`
- Create: `android-app/app/src/main/java/com/josemartcast/profich/presentation/theme/ProFichTheme.kt`
- Create: `android-app/app/src/main/java/com/josemartcast/profich/data/package-info.java`
- Create: `android-app/app/src/main/java/com/josemartcast/profich/domain/package-info.java`
- Create: `android-app/app/src/test/java/com/josemartcast/profich/presentation/navigation/ProFichDestinationTest.kt`
- Create: `android-app/app/src/test/java/com/josemartcast/profich/presentation/home/HomeViewModelTest.kt`

**Interfaces:**
- Consumes: Android SDK Platform 37 and JDK 21.
- Produces: `ProFichApp()`, `ProFichDestination`, `HomeViewModel.uiState: StateFlow<HomeUiState>`.

- [ ] **Step 1: Crear la configuración Gradle y el módulo `app`**

Usar AGP 9.3.0, Gradle 9.5.0, plugin Compose 2.3.21, Compose BOM 2026.06.00, Activity Compose 1.13.0, Lifecycle 2.10.0 y Navigation 2.9.8. Aplicar Kotlin integrado de AGP, sin `org.jetbrains.kotlin.android`.

- [ ] **Step 2: Generar Gradle Wrapper**

Run: Gradle 9.5 `wrapper --gradle-version 9.5.0 --distribution-type bin`
Expected: scripts y JAR del wrapper creados en `android-app/`.

- [ ] **Step 3: Escribir primero las pruebas de navegación y estado**

```kotlin
class ProFichDestinationTest {
    @Test fun `home is the start destination`() {
        assertEquals(ProFichDestination.Home.route, ProFichDestination.start.route)
    }

    @Test fun `destinations have distinct routes`() {
        assertNotEquals(ProFichDestination.Home.route, ProFichDestination.About.route)
    }
}
```

```kotlin
class HomeViewModelTest {
    @Test fun `initial state identifies the project foundation`() {
        val state = HomeViewModel().uiState.value
        assertEquals("ProFich", state.title)
        assertEquals("Base del proyecto preparada", state.status)
    }
}
```

- [ ] **Step 4: Ejecutar las pruebas y comprobar RED**

Run: `gradlew.bat testDebugUnitTest`
Expected: FAIL porque `ProFichDestination`, `HomeViewModel` y `HomeUiState` todavía no existen.

- [ ] **Step 5: Implementar el modelo mínimo de navegación y estado**

```kotlin
sealed interface ProFichDestination {
    val route: String
    data object Home : ProFichDestination { override val route = "home" }
    data object About : ProFichDestination { override val route = "about" }
    companion object { val start: ProFichDestination = Home }
}
```

```kotlin
data class HomeUiState(
    val title: String = "ProFich",
    val status: String = "Base del proyecto preparada",
)
```

```kotlin
class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
}
```

- [ ] **Step 6: Ejecutar las pruebas y comprobar GREEN**

Run: `gradlew.bat testDebugUnitTest`
Expected: 3 tests, 0 failures.

- [ ] **Step 7: Implementar la interfaz Compose mínima**

Crear `MainActivity`, `ProFichApp`, `HomeScreen`, `AboutScreen` y `ProFichTheme`. `ProFichApp` contiene un `NavHost` que comienza en `Home`, navega a `About` y permite volver. No añadir servicios, repositorios ni datos ficticios.

- [ ] **Step 8: Compilar Android**

Run: `gradlew.bat test assembleDebug`
Expected: BUILD SUCCESSFUL cuando Android SDK Platform 37 está instalado. Si falta el SDK, registrar el error exacto y verificar al menos que el wrapper y la resolución de plugins funcionan.

- [ ] **Step 9: Commit**

```text
feat(android): add minimal Compose application
```

### Task 3: Base Spring Boot

**Files:**
- Create: `backend/pom.xml`
- Create: `backend/.mvn/wrapper/maven-wrapper.properties`
- Create: `backend/mvnw`
- Create: `backend/mvnw.cmd`
- Create: `backend/src/main/java/com/josemartcast/profich/ProFichApplication.java`
- Create: `backend/src/main/java/com/josemartcast/profich/config/SecurityConfig.java`
- Create: `backend/src/main/java/com/josemartcast/profich/shared/package-info.java`
- Create: `backend/src/main/java/com/josemartcast/profich/status/StatusController.java`
- Create: `backend/src/main/java/com/josemartcast/profich/status/StatusResponse.java`
- Create: `backend/src/main/resources/application.yml`
- Create: `backend/src/test/java/com/josemartcast/profich/ProFichApplicationTests.java`
- Create: `backend/src/test/java/com/josemartcast/profich/status/StatusControllerTest.java`

**Interfaces:**
- Consumes: Java 21 and Maven 3.9.16.
- Produces: `GET /api/status -> {"service":"profich-backend","status":"UP"}`.

- [ ] **Step 1: Crear `pom.xml`, aplicación, configuración y Maven Wrapper**

Usar el parent `spring-boot-starter-parent:4.1.0` y dependencias Web, Validation, Security, Data JPA, H2 y Test. Configurar el compilador para Java 21.

- [ ] **Step 2: Crear primero la prueba del endpoint**

```java
@WebMvcTest(StatusController.class)
@Import(SecurityConfig.class)
class StatusControllerTest {
    @Autowired MockMvc mockMvc;

    @Test
    void returnsServiceStatus() throws Exception {
        mockMvc.perform(get("/api/status"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.service").value("profich-backend"))
            .andExpect(jsonPath("$.status").value("UP"));
    }
}
```

- [ ] **Step 3: Ejecutar la prueba y comprobar RED**

Run: `mvnw.cmd -Dtest=StatusControllerTest test`
Expected: FAIL porque `StatusController` todavía no existe.

- [ ] **Step 4: Implementar la respuesta y el controlador mínimos**

```java
public record StatusResponse(String service, String status) {}
```

```java
@RestController
@RequestMapping("/api/status")
class StatusController {
    @GetMapping
    StatusResponse getStatus() {
        return new StatusResponse("profich-backend", "UP");
    }
}
```

- [ ] **Step 5: Ejecutar la prueba y comprobar GREEN**

Run: `mvnw.cmd -Dtest=StatusControllerTest test`
Expected: 1 test, 0 failures.

- [ ] **Step 6: Añadir prueba de contexto y configuración H2**

Crear `ProFichApplicationTests` con `@SpringBootTest` y `contextLoads()`. Configurar H2 en memoria, consola desactivada y `spring.jpa.hibernate.ddl-auto: validate`.

- [ ] **Step 7: Verificar el backend completo**

Run: `mvnw.cmd verify`
Expected: 2 tests, 0 failures; BUILD SUCCESS.

- [ ] **Step 8: Commit**

```text
feat(backend): add Spring Boot service foundation
```

### Task 4: README y verificación integral

**Files:**
- Modify: `README.md`

**Interfaces:**
- Consumes: comandos y estructura reales de Tasks 1-3.
- Produces: guía raíz en español para desarrollar y ejecutar ambos proyectos.

- [ ] **Step 1: Reescribir el README raíz**

Incluir descripción, arquitectura, árbol del monorepo, requisitos, comandos de Android y backend, endpoint de estado, alcance MVP, estados y transiciones de fichaje, fuera de alcance y próximos pasos.

- [ ] **Step 2: Verificar requisitos documentados**

Confirmar que el README diferencia el alcance final del MVP de lo implementado en esta fase y no afirma que autenticación, offline, auditoría o exportación ya existan.

- [ ] **Step 3: Ejecutar comprobaciones frescas**

Run: `backend\mvnw.cmd -f backend\pom.xml verify`
Expected: BUILD SUCCESS, 2 tests, 0 failures.

Run: `android-app\gradlew.bat -p android-app test assembleDebug`
Expected: BUILD SUCCESS si existe SDK 37; en caso contrario, documentar el requisito bloqueante sin afirmar que Android compiló.

Run: `git diff --check`
Expected: exit 0.

Run: `git status --short`
Expected: solo `README.md` antes del commit.

- [ ] **Step 4: Commit**

```text
docs: document ProFich foundation and roadmap
```

- [ ] **Step 5: Inspección final**

Run: `git status --short --branch`
Expected: árbol limpio y rama `main` por delante de `origin/main` con los commits locales de esta fase.

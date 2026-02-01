# GitHub Copilot instructions for procycle 🚴‍♂️

## Quick summary
- Small Spring Boot web app (Spring Boot 4.0.2, Java 17) that serves Thymeleaf views from `src/main/resources/templates`.
- No external services configured (no DB, no external APIs). Primary components are very small and colocated under `com.niko.procycle`.

---

## Key files & responsibilities 🔧
- `src/main/java/com/niko/procycle/ProcycleApplication.java` — app entrypoint (Spring Boot).
- `src/main/java/com/niko/procycle/HomeController.java` — simple MVC controller. Returns the view name `"home"` mapped to `templates/home.html`.
- `src/main/resources/templates/home.html` — Thymeleaf template used by `HomeController`.
- `src/main/java/com/niko/procycle/Cyclist.java` — domain POJO (getters only). Contains a small `main` method used for quick local debugging.
- `src/main/java/com/niko/procycle/GameService.java` — intended service for game logic; currently a minimal/stub implementation (manage `ArrayList<Cyclist>`).
- `pom.xml` — Maven build, includes `spring-boot-starter-thymeleaf` and `spring-boot-starter-webmvc`. Java target is set via `<java.version>17</java.version>`.
- `src/test/java/com/niko/procycle/ProcycleApplicationTests.java` — basic context load smoke test.

---

## Build, run, test (developer commands) ✅
- Use the project wrapper to match CI: `./mvnw` (Linux/macOS) or `mvnw.cmd` (Windows).
- Run locally: `./mvnw spring-boot:run` → app available at `http://localhost:8080/`.
- Run tests: `./mvnw test`.
- Create a runnable JAR: `./mvnw -DskipTests package` then `java -jar target/procycle-*.jar`.

> Tip: rely on `./mvnw` in automation and CI to avoid host Maven/JDK mismatch.

---

## Project-specific conventions & patterns ✨
- Keep code under package `com.niko.procycle` and follow the existing small-file layout (controllers, domain, services in same package).
- Controllers return view names (string) mapped to `templates/*` — update template files when changing view names.
- `Cyclist` is a plain POJO; prefer writing unit tests for logic in POJOs rather than relying on ad-hoc `main()` output.
- `GameService` is currently a stub (manages `ArrayList<Cyclist>`). If you add DI, prefer annotating with `@Service` and use constructor injection to make it test-friendly.
- Tests use JUnit 5 (via Spring Boot). For controllers, favor `@WebMvcTest` + `MockMvc`; for service logic write plain unit tests.

---

## Integration & external surface ⚠️
- There are currently no configured external integrations (no DB, OAuth, or 3rd-party APIs). Adding any should also add configuration to `application.properties` and document required environment variables in this file.

---

## Suggested starter tasks for AI agents (clear, scoped, testable) 💡
1. Implement core `GameService` methods: add, remove, list cyclists; add unit tests for each method.
2. Add a controller endpoint to return a list of cyclists and a small Thymeleaf fragment in `home.html` to render them.
3. Improve test coverage: add unit tests for `HomeController` (use `@WebMvcTest`) and service tests (plain unit tests).

When implementing, always run `./mvnw test` and ensure the new behavior is covered by tests.

---

## Things to avoid / watch-outs ⚠️
- Do not change the top-level package name (`com.niko.procycle`) without a strong reason — many paths assume it.
- Avoid adding heavy frameworks or services unless justified. This is a small demo-style app; keep changes minimal and well-tested.
- `Cyclist.main` is an ad-hoc debug helper — avoid depending on its output in tests.

---

If any of the above is unclear or you want more conventions (commit message format, CI checks, branching rules), tell me which areas to expand and I will iterate. 🙋‍♂️
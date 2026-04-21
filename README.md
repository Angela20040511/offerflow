# OfferFlow

OfferFlow is a recruitment collaboration demo with a Spring Boot backend and a Vue 3 frontend.

## Demo Data Initialization

- Normal startup does **not** execute `schema.sql` or `data.sql` destructively.
- The backend defaults to:

```yaml
spring:
  sql:
    init:
      mode: ${SQL_INIT_MODE:never}
```

- `backend/src/main/resources/sql/data.sql` is a **manual demo reset script**.
- That script clears and recreates demo data. Do not enable it in normal daily startup if you need to preserve registered users or business records.

## When To Use `data.sql`

Use demo initialization only when you explicitly want to reset the database for showcase or testing.

Examples:

```powershell
$env:SQL_INIT_MODE="always"
cmd /d /c mvn spring-boot:run
```

After the demo data has been reloaded once, switch back to the default:

```powershell
Remove-Item Env:SQL_INIT_MODE
```

or set:

```powershell
$env:SQL_INIT_MODE="never"
```

## Resume PDF Naming

- Demo resume PDFs follow the `resumeId.pdf` convention.
- Frontend preview may add a cache-busting timestamp query string, but the backend still stores files as `resumeId.pdf`.

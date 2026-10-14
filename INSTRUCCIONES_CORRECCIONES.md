# Entrega reconstruida desde Repomix

**Aviso:** este ZIP reconstruye solo los archivos de texto presentes en el XML; NO contiene binarios originales (APK, imágenes, JAR, DOCX, PPTX). No se ha podido compilar ni ejecutar contra una base de datos real en esta revisión. No contiene el `.env` real.

## Arranque
1. Copia `backend/.env.example` a `backend/.env`. Genera `JWT_SECRET` aleatorio de al menos 32 bytes y pon credenciales nuevas.
2. Cambia `DB_TARGET=docker` o `DB_TARGET=neon`. Neon requiere URL JDBC con `sslmode=require`.
3. En Linux/macOS: `cd backend && bash scripts/start.sh`; en Windows PowerShell: `cd backend; ./scripts/start.ps1`.
4. Web local: `http://localhost:8080`. Android emulador: `http://10.0.2.2:8080/`.
5. Para un teléfono físico local, cambia `APP_BIND=0.0.0.0` y configura `http://IP_LAN_DEL_PC:8080/` en Android DEBUG **solo en red de confianza**. Para producción, usa HTTPS y compila Android release.
6. Si usas Docker y Neon, no hace falta arrancar PostgreSQL local. Si vuelves a Docker, el script arranca el servicio postgres.

## Cambios
- Selección de PostgreSQL desde `.env` a través de scripts de inicio.
- Alineadas las variables JDBC de Compose y Spring.
- Cookie secure parametrizada para desarrollo HTTP; usar `SESSION_COOKIE_SECURE=true` bajo HTTPS.
- Login rechaza cuentas inactivas y mensajes de credenciales uniformes.
- Autorización por rol supervisor en escrituras del catálogo.
- Borrado de usuarios vía POST con protección CSRF de Spring Security.
- Android sin logs de cuerpos y sin cleartext en release; backup deshabilitado.
- Docker Lambda actualizado a Java 25.

## Límites y comprobaciones pendientes
- Revisar scripts SQL y esquema `ddl-auto=validate` contra las bases reales; una BBDD Neon vacía necesita migración/inicialización antes de arrancar.
- Comprobar rutas y permisos de movimientos, riesgos de concurrencia y lógica de negocio con pruebas de integración.
- Confirmar que el backend compila y que la web y Android hacen login real.
- No distribuir el APK antiguo: recompilar Android; el APK original no se incluye en el XML.
- La protección de CSRF en formularios Thymeleaf depende de la integración Spring Security/Thymeleaf: verificar en ejecución.
- Si se expusieron credenciales en Repomix o Git, rotar las contraseñas y el secreto JWT.

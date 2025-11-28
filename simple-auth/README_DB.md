# Base de datos y configuraciÃ³n â€” SimpleAuth

Resumen rÃ¡pido: aÃ±adÃ­ el esquema SQL solicitado y un inicializador para crear la base de datos/tablas en tiempo de ejecuciÃ³n si no existen.

QuÃ© contiene:
- `src/main/resources/db/schema.sql` â€” DDL con las tablas: Roles, Personas, TutoresXAdictos, Contadores, Objetivos, Medallas, usuariosXmedallas, Contactos_Emergencia, Notificaciones.
- `src/main/java/com/example/auth/util/DBInitializer.java` â€” carga y ejecuta `schema.sql` al inicio.
- `src/main/resources/config.properties.example` â€” ejemplo de conexiÃ³n (MySQL).
 - `src/main/resources/config.properties.example` â€” ejemplo de conexiÃ³n (MySQL / H2). Default: H2 embedded.
H2 embedded (sin MySQL)
- Por defecto el proyecto ahora usa H2 embebido si `db.type` no se configura o se establece `h2`.
- Para usar H2 por defecto copia `src/main/resources/config.properties.example` a `src/main/resources/config.properties`.
Si prefieres que Maven maneje dependencias, usa los comandos que aparecen abajo â€” Maven descargarÃ¡ H2/mysql y generarÃ¡ el jar ejecutable.


1) (Opcional) Si prefieres MySQL, configura `src/main/resources/config.properties` con `db.type=mysql` y `db.url/db.user/db.password`.
1. Un servidor MySQL disponible o un motor JDBC compatible.
2. Las dependencias JDBC (MySQL driver) se gestionan con Maven (`pom.xml`)  no es necesario colocar jars manualmente en `lib/.
3) Con Maven (recomendado): ejecuta en la raÃ­z del proyecto:

```powershell
# compilar y empaquetar (crea un jar ejecutable con dependencias)
mvn -DskipTests package

# lanzar sin empaquetar (usa exec plugin)
mvn -Dexec.mainClass=com.example.auth.Main exec:java

# o ejecutar el jar empaquetado
java -jar target/simple-auth-0.1.0.jar
```

Los scripts `start.ps1` y `run.bat` ahora intentan usar Maven si estÃ¡ instalado, y si no lo encuentran vuelven al modo legacy que compila y ejecuta con el JDK incluido.

Nota importante: el proyecto ahora usa Maven para gestionar dependencias (H2, MySQL). No es necesario ni recomendado colocar jars manualmente en `lib/`.

1) Coloca el driver JDBC (mysql-connector-java-x.y.z.jar) en `lib\mysql-connector-java.jar`. Puedes renombrarlo a `mysql-connector-java.jar`.

2) Copia el archivo de ejemplo a `src/main/resources/config.properties` y ajusta credenciales si es necesario.

3) Ejecuta `start.ps1` en la raÃ­z del proyecto (PowerShell) o `run.bat` en Windows. Los scripts compilan, copian recursos y arrancan la app con el driver en clase si estÃ¡ en `lib/`.

Notas tÃ©cnicas:
- `DBInitializer` ejecuta `schema.sql` con `IF NOT EXISTS` y un `INSERT IGNORE` para roles; puedes usar el archivo para inicializar la DB manualmente con un cliente MySQL si prefieres.
- `Personas.contrasena` almacena el hash devuelto por `PasswordUtil.hash(...)` â€” NO almacenes contraseÃ±as en texto claro. Si necesitas importar datos, usa `PasswordUtil.hash` o el UI de registro.

Â¿QuÃ© puedo hacer ahora por ti?
- Puedo aÃ±adir soporte para ejecutar con SQLite/H2 embebido (no necesitarÃ¡s instalar drivers), o
- Puedo crear un pequeÃ±o helper CLI que genere hashes y cree un usuario Admin, o
- Convertir el proyecto a Maven/Gradle para gestionar dependencias (recomendado) y aÃ±adir el driver automÃ¡ticamente.

Nota importante: el proyecto usa Maven para gestionar dependencias (H2, MySQL). No es necesario ni recomendado colocar jars manualmente en `lib/`.

## CLI helper para pruebas
He aÃ±adido una herramienta de consola `com.example.auth.cli.UserCLI` para generar hashes y crear usuarios en la BD (Ãºtil para pruebas).

Ejemplos de uso (desde la raÃ­z del proyecto):

1) Generar hash:

```powershell
mvn -Dexec.mainClass=com.example.auth.cli.UserCLI exec:java -Dexec.args="hash MyPassword"
```

2) Crear usuario:

```powershell
mvn -Dexec.mainClass=com.example.auth.cli.UserCLI exec:java -Dexec.args="create admin@example.com Admin123! admin 2"
```

3) Mostrar usuario:

```powershell
mvn -Dexec.mainClass=com.example.auth.cli.UserCLI exec:java -Dexec.args="show admin@example.com"
```

La utilidad llama a `DBInitializer` antes de operar para asegurar que las tablas existen.





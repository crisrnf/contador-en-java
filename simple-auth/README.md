# Simple Java Auth (Swing + MySQL)

Proyecto simple en Java usando Swing para la interfaz gráfica y MySQL para la persistencia de usuarios (email + contraseña hasheada).

Características
- Registro de usuario con email y contraseña (hash seguro usando PBKDF2)
- Inicio de sesión con validación de contraseña
- Persistencia en MySQL (JDBC) o H2 embebido para pruebas

Requisitos
- Java 17+
- Maven (opcional: si no está disponible los scripts fallback usarán el JDK incluido)
- MySQL (o MariaDB compatible) si no quieres usar H2 embebido

Configuración rápida
1. Crear la base de datos y tabla en MySQL (desde terminal / client). Puedes usar el script incluido:

   sql/create_db.sql

   En PowerShell (ejemplo):

```powershell
mysql -u root -p < sql/create_db.sql
```

2. Configurar conexión a MySQL
- Edita `src/main/resources/config.properties` y agrega tus datos de conexión:

```properties
# Ejemplo
db.url=jdbc:mysql://localhost:3306/simple_auth?serverTimezone=UTC
db.user=root
db.password=mi_contraseña
```

3. Construir el proyecto (Maven)

```powershell
cd simple-auth
mvn clean package -DskipTests
```

4. Ejecutar la aplicación

```powershell
# Ejecutar con Maven (usa el main definido en pom.xml)
mvn -q exec:java -Dexec.mainClass=com.example.auth.Main

O bien, empaqueta y ejecuta el jar con dependencias (shade):
java -jar target/simple-auth-0.1.0-shaded.jar
```

Notas
-- Las contraseñas se almacenan usando PBKDF2 (implementación integrada en el proyecto).
- Este proyecto es un ejemplo simple para aprender — no es una solución lista para producción.

Archivos clave
- `src/main/java/com/example/auth` - código fuente
- `src/main/resources/config.properties` - configuración de la BD
- `sql/create_db.sql` - script de creación de base y tabla

Si quieres, puedo añadir ISO/UTF-8 support, validación de emails más avanzada o integración con UI moderna (JavaFX) — dime cuál prefieres.

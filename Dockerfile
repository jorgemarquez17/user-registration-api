FROM eclipse-temurin:21

# Directorio de trabajo
WORKDIR /app

# Copia el JAR generado por Gradle
COPY build/libs/user-registration-api-1.0.0.jar app.jar

#DEFINE EL VOLUMEN
VOLUME ["/app/data"]

# Expone el puerto
EXPOSE 8080

# Ejecuta la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
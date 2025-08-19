FROM eclipse-temurin:21-jdk as builder
WORKDIR /app

# Copy Maven wrapper only (optional)
COPY mvnw .

# Copy pom.xml
COPY pom.xml ./

# Download dependencies (requires Maven installed)
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build project
RUN mvn clean package -DskipTests

# Runtime image
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/target/commentService-0.0.1-SNAPSHOT.jar ./app.jar

ENTRYPOINT ["java", "-Xmx256m", "-jar", "app.jar"]
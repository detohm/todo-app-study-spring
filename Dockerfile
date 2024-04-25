FROM openjdk:21 as builder
WORKDIR /app
COPY pom.xml .
COPY mvnw .
COPY .mvn/ .mvn
RUN ./mvnw dependency:go-offline

COPY src/ ./src
RUN ./mvnw package -DskipTests

FROM openjdk:21
COPY --from=builder /app/target/todo-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]

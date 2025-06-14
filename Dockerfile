FROM gradle:8-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle bootJar

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar"]
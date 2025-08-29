#Etapa Build
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -e -DskipTests dependency:go-offline
COPY . .
RUN mvn -q -e -DskipTests package


FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar
EXPOSE 8080
ENV JAVA_OPS="-Xms128m -Xmx256m"
ENTRYPOINT [ "sh","-c","java ${JAVA_OPS} -jar /app/app.jar" ]
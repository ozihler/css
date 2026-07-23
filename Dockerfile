FROM eclipse-temurin:25-jdk AS build
WORKDIR /workspace
COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN ./mvnw -B -DskipTests dependency:go-offline
COPY src src
RUN ./mvnw -B -DskipTests package

FROM icr.io/appcafe/open-liberty:kernel-slim-java25-openj9-ubi-minimal
USER root
COPY --chown=1001:0 src/main/liberty/config/ /config/
COPY --from=build --chown=1001:0 /workspace/target/fitness-memberships.war /config/apps/
COPY --from=build --chown=1001:0 /root/.m2/repository/org/postgresql/postgresql/*/postgresql-*.jar /config/lib/
RUN features.sh
USER 1001

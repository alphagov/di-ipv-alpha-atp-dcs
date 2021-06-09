FROM gradle:jdk13@sha256:cd82e0f35ce72162ef102f639be950ff6e1c056056f1d2e99a6704dc1a57ade5 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN ./scripts/generate-key-pair-for-dev.sh
RUN gradle build --no-daemon

FROM openjdk:13-slim@sha256:dd65efeba8b205afc6798ec6d3378219d2957ddea410872ae4fb3fec02afeb5f
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/dcs-atp-service-0.0.1-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
LABEL project="di-ipv-atp-dcs"
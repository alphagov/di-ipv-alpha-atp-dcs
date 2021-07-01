FROM gradle:jdk15@sha256:ea8494d3eec55ecc7c3c9ff0c1106488711be0256cc022f48d86c31a528cc673 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
#RUN ./scripts/generate-key-pair-for-dev.sh
RUN gradle build --no-daemon

FROM openjdk:15-slim@sha256:82fc670b1757068d299fb3f860201c5c97625b5ca351f903a6de33857398eb82
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/dcs-atp-service-0.0.1-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
LABEL project="di-ipv-atp-dcs"
FROM gradle:jdk16@sha256:d31e12d105e332ec2ef1f31c20eac6d1467295487ac70e534e3c1d0ae4a0506e AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN ./scripts/generate-key-pair-for-dev.sh
RUN gradle build --no-daemon

FROM openjdk:16-slim@sha256:38f6c41a7f4901b734f4a7cfc0daa6a1995b552d7ec9517496788f6cc8090235
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
LABEL project="di-ipv-atp-dcs"
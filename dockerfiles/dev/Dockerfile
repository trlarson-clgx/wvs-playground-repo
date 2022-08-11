FROM gradle:7.4.2-jdk17 as builder
WORKDIR /app
COPY ./ ./
RUN --mount=type=cache,id=gradle,target=/home/gradle/.gradle gradle bootJar --no-daemon


FROM openjdk:17-slim-bullseye
USER root
WORKDIR /app
COPY --from=builder app/build/libs/*.jar ./app.jar

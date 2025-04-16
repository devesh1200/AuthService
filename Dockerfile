FROM openjdk:17

COPY target/AuthService.jar  /usr/app/

WORKDIR /usr/app/

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "AuthService.jar"]
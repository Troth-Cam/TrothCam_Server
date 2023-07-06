FROM openjdk:11-jdk
ARG JAR_FILE=./build/libs/trothcam-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
VOLUME ["/var/log"]
ENTRYPOINT [ "java", "-jar", "-Dspring.profiles.active=prod", "-Duser.timezone=Asia/Seoul", "/app.jar" ]
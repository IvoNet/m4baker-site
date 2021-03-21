FROM openjdk:16-jdk-alpine3.13
LABEL maintainer="Ivo Woltring - @ivonet"

ENV profile prod

ADD /target/m4baker-site.jar /opt/m4baker-site.jar

VOLUME /downloads

ENTRYPOINT ["java", "-Dspring.profiles.active=${profile}", "-jar" ,"/opt/m4baker-site.jar"]

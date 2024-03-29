FROM amazoncorretto:17.0.4-alpine3.16
LABEL maintainer="Ivo Woltring - @ivonet"

ENV profile prod

ADD /target/m4baker-site.jar /opt/m4baker-site.jar

VOLUME /downloads

ENTRYPOINT ["java", "-Dspring.profiles.active=${profile}", "-jar" ,"/opt/m4baker-site.jar"]

FROM adoptopenjdk:11-jre-hotspot

MAINTAINER remus.selea@gmail.com

VOLUME /tmp

COPY ./build/libs/* ./app.jar

EXPOSE 8321

ENTRYPOINT ["java","-jar","app.jar"]

FROM adoptopenjdk:11-jre-hotspot

MAINTAINER remus.selea@gmail.com

ENV APP_HOME=/usr/app/

WORKDIR $APP_HOME

COPY ./build/libs/* ./app.jar

EXPOSE 8321

ENTRYPOINT ["java","-jar","app.jar"]

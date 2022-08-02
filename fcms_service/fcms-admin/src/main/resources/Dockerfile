#FROM niyongsheng/image-seetaface2
FROM java:8
MAINTAINER NYS
VOLUME /tmp
ADD *.jar /app.jar
EXPOSE 8099
ENTRYPOINT [ "java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar" ]
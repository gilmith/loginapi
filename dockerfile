FROM alpine:latest
# Instalar OpenJDK 21
RUN apk --no-cache add openjdk21
ENV JAVA_HOME=/usr/lib/jvm/java-21-openjdk
ENV PATH=$PATH:$JAVA_HOME/bin
WORKDIR /app
COPY ./target/loginapi-0.0.19.jar /app
ENTRYPOINT [ "java", "-jar", "/app/loginapi-0.0.19.jar" ]

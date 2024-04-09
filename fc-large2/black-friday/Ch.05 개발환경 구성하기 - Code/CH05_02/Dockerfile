FROM ubuntu:18.04

RUN apt-get update && \
    apt-get install -y openjdk-17-jdk && \
    apt-get clean;

WORKDIR /app

COPY my-hello-world.jar /app/

CMD ["java", "-jar", "my-hello-world.jar"]
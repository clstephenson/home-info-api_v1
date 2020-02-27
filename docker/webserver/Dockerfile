FROM openjdk:8-jdk-alpine
ARG JAR_FILE=./target/webapp-0.0.1-exec.jar
COPY ${JAR_FILE} app.jar
EXPOSE 5000

## Add the wait script to the image
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.7.3/wait /wait
RUN chmod +x /wait

## Launch the wait tool and then the Spring Boot application
CMD /wait && java -jar /app.jar
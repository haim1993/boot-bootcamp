FROM java:8-jdk-alpine

# Copy config file and JAR to path
COPY  build/libs/logs-shipper-1.0-SNAPSHOT-all.jar build/resources/main/log4j2.xml /usr/app/

# Use current directory
WORKDIR /usr/app

# Renaming the JAR file
RUN mv logs-shipper-1.0-SNAPSHOT-all.jar shipper.jar


EXPOSE 8080

CMD ["java", "-Dlog4j.configurationFile=log4j2.xml", "-jar", "shipper.jar"]
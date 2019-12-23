FROM java:8-jdk-alpine

# Copy config file and JAR to path
COPY  build/libs/indexer-1.0-SNAPSHOT-all.jar build/resources/main/ /usr/app/

# Use current directory
WORKDIR /usr/app

# Renaming the JAR file
RUN mv indexer-1.0-SNAPSHOT-all.jar indexer.jar

CMD ["java", "-jar", "indexer.jar"]
# Overview

Building the JAR file:

```
./gradlew shadowJar
```

Running HAProxy container with 2 instances of the Logger Shipper:

```
docker-compose up --build
```

# Overview

The project builds and runs an HAProxy container to load balance instances of a simple server application that ships dummy logs to the logz.io platform.

### Dependencies
You must have Docker installed on your system to build and run the containers.


### How to build
Building the JAR file:

```
./gradlew shadowJar
```

Running HAProxy container with 2 instances of the Logger Shipper:

```
docker-compose up --build
```

The endpoint of the HAProxy,
[localhost:8080/boot-bootcamp](http://localhost:8080/boot-bootcamp)

# Overview

The project builds and runs an HAProxy container to load balance instances of a simple server application that ships dummy logs to the logz.io platform.


### Dependencies
You must have Docker installed on your system to build and run the containers.


***!IMPORTANT***
> To see the shipped logs in your logz.io account, you must change the TOKEN ID in the configuration file 
> Configuration file path : *logs-shipper/src/main/resources/log4j2.xml*


### Tasks
**Part 1**
- Create new repository “boot-bootcamp” in your personal GitHub account
- Create new JAVA project
- Push your project to your github repository
- Use Gradle as a build tool
- Include HTTP Server library (choose as you wish)
- Your server should have a GET /boot-bootcamp endpoint

**Part 2**
- Run boot-bootcamp as a Docker container 
- Run 2 instances of boot-bootcamp behind a load-balancer. Use docker-compose


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

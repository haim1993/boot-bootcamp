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

**Part 3**
- Integrate Jetty and JAX-RS (Jersey) as the REST API HTTP Server for your boot-bootcamp service

**Part 4**
- Integrate Guice into your boot-bootcamp project
  Create a class ServerModule that will bind the needed classes, and install the needed modules (Guice-Jersey [https://github.com/logzio/guice-jersey](https://github.com/logzio/guice-jersey))
- Make the server configurable
  Create a file server.config which will contain a json configuration file, as the following:
  ```
  {
      "port": 8080,
      "logMessage": "boot boot"
  }
  ```
  Create a class ServerConfiguration  that will hold the configuration parameters as private members
  Bind this class using guice, and use it in the appropriate places 

### How to build
Building the JAR file:

```
./gradlew shadowJar
```

Running HAProxy container with 4 instances of the Logger Shipper:

```
docker-compose --compatibility up --build
```
The number of instances can be changed in the `.env` file : REPLICAS=(num-of-instances)

The endpoint of the HAProxy,
[localhost:8080/boot-bootcamp](http://localhost:8080/boot-bootcamp)

<h1 align="center"> Email Service </h1> <br>

<p align="center">
  This service will communicate with event bus and other services
</p>


## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Requirements](#requirements)
- [Quick Start](#quick-start)
- [Improvements](#improvements)




## Introduction

This service is responsible for consuming successful subscription event and send email

## Features
* Developed using spring reactive 
* Microservice architecture
* Event driven architecture
* * Queues are treated as streams to get faster and reliable delivery and consumption
* Smart Endpoints and Dumb Pipes
* Consume successful subscription event
* Sent email

## Requirements
The application can be run locally. Containerization is in progress

### Local
* [Java 16 SDK](https://www.oracle.com/java/technologies/downloads/#java16)
* [Maven](https://downloads.apache.org/maven/maven-3/3.8.1/binaries/)
* [RabbitMQ](https://hub.docker.com/r/bitnami/rabbitmq/)

## Quick Start
Make sure your maven is pointing to JAVA_HOME and JAVA_HOME is set to Java16 JDK.

Make sure your RabbiMQ docker is up and running to see successful subscription event.

The RabbitMQ host value in the __application.yml__ file is set to `localhost`.

### Run Local
If your JAVA_HOME is set to Java16 JDK
```bash
$ mvn clean install
$ java -jar target/stream-receiver-0.0.1-SNAPSHOT.jar
```

For multiple JDK issue
```bash
$ JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-16.0.2.jdk/Contents/Home
$ export JAVA_HOME
$ mvn clean install
$ java -jar target/stream-receiver-0.0.1-SNAPSHOT.jar
```

## Improvements
* API documentation using swagger-ui and open-api docs
* 100% Unit test code coverage
* It can be implemented as a serverless architure
* Containerization
* Integration with CICD i.e. jenkins / rancher
* Metrics Expose
* Integration with jaeger / slueth / opentelemetry for better traceability
* Integration with metrics collector i.e. prometheus
* Integration with ELK stack
* Integration with grafana for better visibility, observability and alerts
* Integration test
* Automation testing or behavioral testing i.e. RobotFramework, Selenium
* Passing traceId / correlationId in event header for better traceability

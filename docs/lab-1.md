# Dockerfile

Lab Session 1 - 20 Oct, 2025

## Introduction

The goal of this lab session is to practice your skills related to
Dockerfile. It includes the instructions used inside a Dockerfile, the
commands for building and publishing an image, the architecture of
Docker Runtime, and the inspection of a running Docker container. It
also includes skills learned from other courses, such as Maven commands
from the course “Frameworks” and SQL commands from the course “PL/SQL”.
Just so you know, the Docker image produced by this session **will be
used in the subsequent lab sessions**. Please prioritize your work and
make sure it’s finished before the next lecture.

``` mermaid
%%{
    init: {
        'logLevel': 'debug',
        'theme': 'base'
    }
}%%
timeline
    title Lab Session Objectives
    1. Develop
        : Package a Java application as a JAR
        : Understand the internals of a JAR
        : Define Docker image using Dockerfile
    2. Build
        : Build image using docker-build
        : Login to a registry
        : Publish image to a registry
    3. Run
        : Understand container runtime
        : Run new containers
        : Inspect running containers
```

The lab sessions of this course are developed around [Spring
PetClinic](https://spring-petclinic.github.io), which is a list of
open-source sample applications based on the Spring stack. The Spring
PetClinic is a sample application designed to show how the Spring stack
can be used to build simple, but powerful database-oriented
applications.

To submit the answers to this lab session, please fill in your answers
directly in this document. This must be done before the beginning of the
next course.

## Exercise 1: Create a JAR

``` mermaid
graph LR
    client[Client]

    subgraph Container
        direction LR
        portin8080[Port 8080]
        subgraph JVM["Java Virtual Machine"]
            jar["Java Application (JAR)"]
        end
        env_vars["Environment\nVariables"]
    end

    client --> portin8080
    portin8080 --> jar
    env_vars --> jar
```

Create a Java Archive (JAR) file using the source code of
[spring-projects/spring-petclinic](https://github.com/spring-projects/spring-petclinic).
You can build it using Maven (`mvn`). Start the server using the `java`
command line. What do you see if you visit the URL
<http://localhost:8080> in your browser?

``` sh
# TODO: enter the commands for building and starting the Java server here
```

Could you look over the content of the JAR file? Inspect the file
entries, especially the content of the manifest.

``` sh
# TODO: paste the content of the file "META-INF/MANIFEST.MF" here
```

  

## Exercise 2: Build a Docker Image

Define a Dockerfile, which is the first step of creating a Docker image.
Create a Dockerfile based on one of the JDK images available on Docker
Hub <https://hub.docker.com/_/eclipse-temurin>. You must pick a
long-term support (LTS) version 21+. Ideally, you’ll be able to find a
small image to reduce the container size. The Java Archive (JAR) file
should be built outside of Docker via the command you used in the
previous question. Expose port 8080 of the container so that it can
accept requests from the clients. The Dockerfile should be stored under
the following directory: `${REPO_ROOT}/spring-petclinic/Dockerfile`.
Please commit the changes and push them to GitHub.

  

``` dockerfile
# Note: commit your changes to the file "spring-petclinic/Dockerfile"
```

  

Now that you have your Dockerfile, try to build your Docker images using
Docker BuildKit. You are expected to build an image that supports x86-64
architecture (`linux/amd64`). This build command should be executed from
the root directory of the Git repository `${REPO_ROOT}`.

  

``` sh
# TODO: enter the docker command here
```

  

  

## Exercise 3: Run a Container

Run a Docker container using the command `docker run`. Your container
should receive HTTP requests on port 8080. Once your Docker container is
running, copy some logs from Spring Boot to show that the application is
running.

  

``` sh
# TODO: paste your docker-run command here
```

``` sh
# TODO: paste the docker command for finding the logs
```

``` sh
# TODO: paste the logs here
```

  

  

## Exercise 4: Publish a Docker Image

Now, you need to publish your Docker image to Docker Hub under the
account “Mincong Classroom” (https://hub.docker.com/u/mincongclassroom).
The target repository should be prefixed by “spring-petclinic”, followed
by the name of your team in lowercase.

    mincongclassroom/spring-petclinic-${team}

The teacher will distribute the credentials for authenticating to the
registry during the lab session. The release version should be `1.0.0`.

``` sh
# TODO: Paste the publish command here
```

## Exercise 5: Inspect Docker container

Inspect the Docker container and find out its container ID.

``` sh
# TODO: paste the command used here
```

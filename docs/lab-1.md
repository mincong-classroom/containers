# Dockerfiles

Lab Session 1 - 28 Oct, 2024

## Introduction

The goal of this lab session is to practice your skills related to
Dockerfiles. It includes the instructions for Dockerfile, the
docker-build command, the architecture of Docker runtime, the inspection
of a running container, and the storage of a container. It also includes
skills learned from other courses, such as Maven commands from the
course “Frameworks” and SQL commands from the course “PL/SQL”. Note that
the Java image produced by this session **will be used in the lab
sessions** of the following chapters. So please prioritize your work and
ensure that it is done before the next lecture.

To submit the answers to this lab session, please fill in your answers
in this document in-place. This should be done before the beginning of
the next course.

## Exercise 1: Create a Java image

Create a Docker image to package the Spring Boot application
“weekend-server”, step by step.

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

### 1.1 Create a JAR without Docker

Create a Java Archive (JAR) file using the source code available in your
Git repository, under the directory `weekend-server/`. You can build it
using Maven (`mvn`). Please provide the command that you used below.
Then, what is the name of the JAR file produced by Maven? Once you find
it, can you start the web server using the `java` command,
e.g. `java -jar /path/to/my.jar`? What do you see if you visit the URL
<http://localhost:8080> in your browser?

> [!TIP]
> To stop the Java server, you can press keys CTRL + C.

  

    *\[answer\]*

  

  

### 1.2 Define a Dockerfile

Define a Dockerfile, which is the first step of creating a Docker image.
Create a Dockerfile based on one of the JDK images available on Docker
Hub <https://hub.docker.com/_/eclipse-temurin>. You have to pick a
version that is compatible with JDK 21, the latest long-term support
(LTS) version. There are multiple choices, ideally, you can find a small
image, which reduces the size of the container. The Java Archive (JAR)
file should be built outside of Docker via the command you used in the
previous question. Expose port 8080 of the container so that it can
accept requests from the clients. The Dockerfile should be stored under
the following directory: `${PROJECT_ROOT}/weekend-server/Dockerfile`.
Please commit the changes and push them to your Git repository.

  

    *\[answer: commit the Dockerfile to the Git repository\]*

  

  

### 1.3 Build your Docker image

Now you have your Dockerfile, try to build your Docker image using the
command `docker build` with additional options. You are expected to
build your image with the repository name as `weekend-server-${team}` in
the registry. Once your Docker image is built successfully, please paste
your command below. This build command should be executed from the root
directory of the project `${PROJECT_ROOT}`.

  

    *\[answer: docker build …\]*

  

  

Verify that your image is successfully built by inspecting the image via
the command `docker images`. Then, fill information in the table below.

  

| Field      | Value |
|:-----------|------:|
| Repository |       |
| Tag        |       |
| Image ID   |       |
| Created    |       |
| Size       |       |

  

  

### 1.4 Run your Docker image

Run your Docker image using the command `docker run`. Your container
should receive HTTP requests on port 8080. Once your Docker container is
running, copy some logs from Spring Boot to show that the application is
running.

  

    *\[Paste your command here: docker run …\]*

    *\[Paste your logs here\]*

  

  

Then, use `curl` to perform an HTTP request to the Java server and
observe the response.

  

    *\[Paste your curl command and result here\]*

  

  

### 1.5 Inspect Docker container

Inspect the docker container using the command `docker ps`. What is the
container ID of your Java container?

    *\[answer\]*

  

  

### 1.6 Building the JAR in Docker

In the previous step “1.2 Define a Dockerfile”, the JAR file wasn’t part
of the Docker build. Can you refactor the Dockerfile, so that you build
the JAR when building the Docker image?

> [!TIP]
> This is called a multi-stage build. You can use
> `COPY --from=${ANOTHER_IMAGE}` to copy the artifact built by another
> image. For example, you can copy the JAR file from a Maven image, and
> paste it to a JDK image. See more informations in
> <https://docs.docker.com/build/building/multi-stage/#name-your-build-stages>

## Exercise 2: Run a database image

In this exercise, we are going to play with MySQL database and observe
the difference in terms of lifecycle between the container and the data
(volume).

### 2.1 Run MySQL as a container

We have a MySQL database image that is ready to be used. You can run it
using the following command:

``` sh
docker run \
    --name weekend-mysql \
    --rm \
    -d \
    -p 3306:3306 \
    -e MYSQL_ALLOW_EMPTY_PASSWORD=true \
    mysql:9
```

> [!CAUTION]
> Don’t allow empty password for your MySQL Database in any
> environment. This is a serious security issue and will result in data
> leak easily. We do this here only to facilitate the lab session.

Ensure that you can visit it using the command below:

``` sh
docker exec -it weekend-mysql mysql -u root
```

  

    *\[Paste the information that you see from the MySQL shell\]*

  

### 2.1 Initialize database

Import the following SQL script into your MySQL database. You can
execute this command from the root of the Git repository:

``` sh
docker exec -i weekend-mysql mysql -u root < weekend-mysql/init.sql
```

  

Now if you connect to MySQL again using the `docker exec` command, do
you see the new data inserted there?

  

    *\[answer: show the database and tables here using SQL commands\]*

  

  

Now, complete the `init.sql` script for all the days of the week **in
English**. Please commit the changes to the Git repository directly.
Here is the French version if you need inspiration:

| Weekday   | Value                       |
|:----------|:----------------------------|
| Monday    | `Non.`                      |
| Tuesday   | `Non.`                      |
| Wednesday | `Non.`                      |
| Thursday  | `Bientôt…`                  |
| Friday    | `Presque, mais pas encore.` |
| Saturday  | `C'est le week-end !`       |
| Sunday    | `C'est le week-end !`       |

### 2.2 Data persistence

Stop the container, and start it again. What do you observe? Are your
previous modifications lost?

  

    *\[answer\]*

  

### 2.3 Volume

Use a volume to persist the data so that the data lifecycle and the
container lifecycle are decoupled. Create a volume called “mysql-data”
and mount it to the container. Then, retry the steps in exercises 2.1
and 2.2 to see whether the data are persisted. For more details, see the
instructions for creating a volume in Docker:
<https://docs.docker.com/reference/cli/docker/volume/create/>, and how
to mount a volume to a container:
<https://docs.docker.com/reference/cli/docker/container/run/#volume>.
Please describe the whole process including the commands used and your
observations.

## Clean up

If you use a computer from ESIGELEC, please stop and remove all the
running containers and volumes before leaving the lab session.

``` sh
docker stop $(docker ps -q)
```

## Going Further

Suggestion:

1.  Containerize one frontend application for the module
    “node.js/react.js”
2.  Run a MySQL database to practice your PL/SQL skills for the module
    “PL/SQL”

Provide a summary and key code snippets here and earn up to 5 points for
your lab session!

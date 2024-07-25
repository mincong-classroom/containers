# GitHub Actions

Lab Session 2 - 23 Oct, 2024

## Introduction

The goal of this lab session is to let you practice the development of a
continuous integration (CI) pipeline using GitHub Actions, including the
definition of workflow, jobs, and the usage of actions and scripts. At
the end of this session, the Docker image of your Java application
should be published to Docker Hub (<https://hub.docker.com/>).

To submit the answers to this lab session, you need to copy this
markdown document to the `reports/` directory of this Git repository and
fill in your answers. This should be done before the beginning of the
next course.

> [!IMPORTANT]
> This lab session assumes that you completed lab session
> 1, meaning that your Java application is containerized and published
> successfully to Docker Hub. If it’s not the case, please notify the
> teacher.

## Exercise 1 - Run unit tests

Replace the existing job “hello-world” with the job
“weekend-server-test” which runs the unit tests of the Maven project
“weekend-server”. The Java version should be the latest LTS: Java 21.
This job should be run for any `push` event, regardless of the branch.
For example, if a commit is pushed to the “main” branch or the
“development” branch, the job should always be executed. Please commit
the code changes to the Git repository directly. Then, take screenshots
and describe where can you find the information related to the execution
of the workflow, the status of the workflow, and the logs of the
execution of the job.

  

    *\[answer\]*

  

## Exercise 2 - Release a Docker image

Create a second job “weekend-server-release” which builds and publishes
a Docker image of the Java application to Docker Hub. The final target
should be the following repository:

``` sh
mc144/weekend-server-${TEAM}
# e.g. mc144/weekend-server-red
```

You are expected to publish two tags for each new image: the `latest`
tag and the Git commit SHA tag. This job should be executed whenever new
changes are introduced to the “main” branch. However, it should not be
executed if the event is not triggered on the “main” branch,
e.g. “development”, to avoid polluting the container repository. This
job should only be executed if the previous job “weekend-server-release”
is completed successfully. Please commit the code changes to the Git
repository directly. Also, can you prove that the updated image is
working correctly?

> Hint: to log in to Docker Hub, you can use the secrets
> `DOCKER_USERNAME` and `DOCKER_PASSWORD`, predefined by the teacher, in
> the GitHub Action “docker/login-action@v3”:

> ``` yaml
> - name: Log in to Docker Hub
>   uses: docker/login-action@v3
>   with:
>     username: ${{ secrets.DOCKER_USERNAME }}
>     password: ${{ secrets.DOCKER_PASSWORD }}
> ```

For more information, you can visit this online tutorial: [Publishing
Docker images - GitHub
Docs](https://docs.github.com/en/actions/publishing-packages/publishing-docker-images).

  

    *\[answer\]*

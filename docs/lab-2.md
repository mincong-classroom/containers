# GitHub Actions

Lab Session 2 - 23 Oct, 2024

## Introduction

The goal of this lab session is to practice the development of a
continuous integration (CI) pipeline using GitHub Actions, including the
definition of workflow, jobs, and the usage of actions and scripts. It
also allows you to practice the skills related to the registry. At the
end of this session, the Docker image of your Java application should be
published to Docker Hub under Mincong’s account `mc144`
(<https://hub.docker.com/u/mc144>) or your preferred account.

To submit the answers to this lab session, please fill in your answers
in this document in-place. This should be done before the beginning of
the next course.

> [!IMPORTANT]
> This lab session assumes that you completed lab session
> 1, meaning that your Java application is successfully containerized.
> It must be built in one single line of command, such as
> `docker build weekend-server` without invoking `mvn` or other command
> line tools. If it’s not the case, please notify the teacher.

## Exercise 1 - Run unit tests

Replace the existing job “demo-test” with the job “weekend-server-test”
which runs the unit tests of the Maven project “weekend-server”. The
existing job is defined in `${PROJECT_ROOT}/.github/workflows/app.yml`.
The Java version should be the latest LTS: Java 21. This job should be
run for any `push` event, regardless of the branch. For example, if a
commit is pushed to the “main” branch or the “development” branch, the
job should always be executed. Please commit the code changes to the Git
repository directly. Then, describe where can you find the information
related to the GitHub Actions, including the execution of the workflow,
the status of the workflow, and the logs of the execution of the job.
Please also provide links to the actual execution(s).

  

    *\[answer\]*

  

## Exercise 2 - Publish Docker image from localhost

Build and publish a Docker image for the Java application from your
terminal. This is a prerequisite for automating the publishing logic in
the CI. You can login to the registry using the following syntax:

``` sh
docker login registry-1.docker.io -u mc144
# enter password here (distributed during the course)
```

and then publish your Docker image.

  

    *\[answer\]*

  

## Exercise 3 - Release a Docker image

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
job should only be executed if the previous job “weekend-server-test” is
completed successfully. Please commit the code changes to the Git
repository directly. Also, can you prove that the updated image is
working correctly?

> [!NOTE]
> To login to Docker Hub from the GitHub Actions, you can use
> the secrets `DOCKER_USERNAME` and `DOCKER_PASSWORD`, predefined by the
> teacher via the GitHub Action “docker/login-action@v3”:
>
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

## Clean up

If you’re using the credentials of Mincong’s account, please logout from
the docker command line once you finished the report.

``` sh
docker logout registry-1.docker.io
```

## Going Further

Suggestion:

1.  Implement a CI solution to automate unit tests for one of the
    modules of ESIGELEC or your personal projects.
2.  Implement a CI solution to automate the packaging of your
    application for one of the modules of ESIGELEC or your personal
    projects.

Provide a summary and key code snippets here and earn up to 5 points for
your lab session!

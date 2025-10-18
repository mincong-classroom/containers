# Configuration and Storage

Lab Session 5 - 30 Oct, 2025

## Introduction

In this session, you are going to deploy a new AI service for the
PetClinic project so that users can have an online assistant for their
questions. The goal of this lab session is to train you in configuring
and developing workloads in Kubernetes. It lets you practice the
management of ConfigMap, the creation of Secret, and the knowledge
learned from the previous chapters.

## Exercise 1 - Create Secret

Create a Secret called “openai” in Kubernetes. This Secret should
contain one single key called “api-key”.

  

``` sh
# TODO: enter command and verification here
```

  

## Exercise 2 - Create Workload

Develop a solution for running the AI service in Kubernetes. You can
find the Spring Boot application image from Docker Hub:
[`springcommunity/spring-petclinic-genai-service`](https://hub.docker.com/r/springcommunity/spring-petclinic-genai-service)
and the related source code on GitHub
([link](https://github.com/spring-petclinic/spring-petclinic-microservices/tree/main/spring-petclinic-genai-service)).
You can also visit the section [“Integrating the Spring AI
Chatbot”](https://github.com/spring-petclinic/spring-petclinic-microservices/tree/main?tab=readme-ov-file#integrating-the-spring-ai-chatbot)
in the documentation to learn more about its setup. Most importantly,
you need to start the workload with the following configuration as an
environment variable:

``` sh
OPENAI_API_KEY='sk-...'
```

Please commit your changes to the file `k8s/lab5.microservices.yaml`.
However, DO NOT commit the API key to GitHub. You should reference the
Secret created in Exercise 1.

  

``` sh
# TODO: develop the whole solution and describe what you do here.
#   Commit the manifests changes to the file "${REPO}/k8s/lab5.microservices.yaml"
```

# Configuration

Lab Session 5 - 30 Oct, 2025

## Introduction

In this session, you are going to deploy a new AI service for the
PetClinic project so that users can have an online assistant for their
questions. The goal of this lab session is to train you in configuring
and developing workloads in Kubernetes. It lets you practice the
management of ConfigMap, the creation of Secret, and the knowledge
learned from the previous chapters.

## Exercise 1 - Create Secret

In the `dev` namespace, create a new Opaque Secret called “openai”. This
Secret should contain one single key called “api-key”. The value of the
API key is communicated on Microsoft Teams. Please verify that the
secret is created. In the validation, only use a prefix of the actual
value to prove that the resource is created.

> [!CAUTION]
> Don’t commit the API value to Git. Don’t commit the key
> directly, its base64-encoded value, or the Secret manifest (YAML).

  

``` sh
# TODO: enter command and verification here
```

  

## Exercise 2 - Create Workload

In the `dev` namespace, develop a solution for running the AI service in
Kubernetes. This feature is powered by the `genai-service` from the
Spring Community. You need to integrate this microservice into the
existing stack (api-gateway, customer-service, vets-service, etc). To do
this, you need to deploy this workload to Kubernetes as a Deployment.
Also, you need to set up the networking part to enable internal
communication between this AI service and other existing services. The
containers should use the port 8084 to accept HTTP requests. You will
also need to check the configuration of the API Gateway to ensure that
it routes the related HTTP requests to the GenAI service.

You can find the Spring Boot application image from Docker Hub:
[`springcommunity/spring-petclinic-genai-service`](https://hub.docker.com/r/springcommunity/spring-petclinic-genai-service)
and the related source code on GitHub
([link](https://github.com/spring-petclinic/spring-petclinic-microservices/tree/main/spring-petclinic-genai-service)).
You can also visit the section [“Integrating the Spring AI
Chatbot”](https://github.com/spring-petclinic/spring-petclinic-microservices/tree/main?tab=readme-ov-file#integrating-the-spring-ai-chatbot)
in the documentation to learn more about its setup. Most importantly,
you will need the following environment variable `OPENAI_API_KEY` to
start the workload:

``` sh
OPENAI_API_KEY='sk-...'
```

Please commit your changes to the file `k8s/lab-5/microservices.yaml`.

Hints:

- You should reference the Secret “openai” created in Exercise 1.
- A new version of the Pet Clinic Micoservices stack is available under
  <https://mincong.io/esigelec/lab/microservice5.yaml>
- The configuration of the API Gateway is defined under the ConfigMap
  “api-gateway-config” in the “microservice5.yaml”.
- You can verify whether the API key is referenced by the GenAI service
  by printing the value of the environment variable via a `kubectl exec`
  command.

  

``` sh
# TODO: develop the whole solution and describe what you do here.
#   Commit the manifests changes to the file "${REPO}/k8s/lab-5/microservices.yaml"
```

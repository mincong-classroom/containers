# Kubernetes Deployment

Lab Session 3 - 28 Oct, 2025

## Introduction

The goal of this lab session is to train your skills related to
ReplicaSet and Deployment. It lets you practice the definition,
creation, operations related to these resources, lets you better
understand their characteristics. This session is also a transition
toward a bigger multi-team project “Pet Clinic” using a microservice
architecture.

``` mermaid
timeline
    title Lab Session Objectives
    1. Deployment Basics
        : Create a new ReplicaSet
        : Create a new Deployment
        : Update an existing Deployment
        : Manual scaling
    2. Deployment Characteristics
        : Understand self-healing
        : Understand rollout history
        : Understand rollout safety
    3. Microservice
        : Apply teacher's predefined template
        : Develop a new Docker image for your Pet Clinic
```

## Exercise 1 - ReplicaSet

Create a new ReplicaSet with 2 replicas and the Docker image
“spring-petclinic” that you developed in Lab Session 1. Use labels
`app=spring-petclinic` and `team=${team}` for both the ReplicaSet and
the underlying Pods. The Pods should expose port 8080 to receive
incoming traffic. The container name should be `main` in the ReplicaSet.
Persist the Kubernetes manifest (YAML file) in the Git repository as
file `k8s/replicaset-petclinic.yaml`.

  

``` sh
# TODO: write the answer to file "${REPO}/k8s/replicaset-petclinic.yaml"
```

  

Now, scale the ReplicaSet to 3 replicas and verify that the operation is
successful.

  

``` sh
# TODO: write down your command and verification here
```

  

Delete one of the Pods from this ReplicaSet, how many Pods do you have
now? Why?

  

``` sh
# TODO: write down your verification and analysis here
```

  

## Exercise 2 - Deployment

Similar to Exercise 1, create a new Deployment with 2 replicas using the
Docker image “spring-petclinic”. Use labels `app=spring-petclinic` and
`team=${team}` for both the Deployment and the underlying Pods. The Pods
should expose the port 8080 to receive incoming traffic. The container
name should be `main` in the Deployment. Persist the Kubernetes manifest
(YAML file) in the Git repository as file
`k8s/deployment-petclinic.yaml`.

  

``` sh
# TODO: write the answer to file "${REPO}/k8s/deployment-petclinic.yaml"
```

  

Update the Deployment to include your team name as an environment
variable. The key should be `TEAM`, and the value should be your team
name in lowercase. Observe the rollout history.

  

``` sh
# TODO: write the answer to file "${REPO}/k8s/deployment-petclinic.yaml"
```

``` sh
# TODO: write down your commands here
```

  

Try to disrupt the Deployment by using a Docker image tag that does not
exist. What happens? Consider checking the ReplicaSets related to this
Deployment.

  

``` sh
# TODO: write down your analysis
```

  

Can you roll back to the previous version?

  

``` sh
# TODO: write down your commands here
```

  

## Exercise 3 - Microservice

Now, the Pet Clinic grows and you cannot handle everything by your team
anymore. You need to collaborate with other teams so that each team can
focus on a specific area. You decided to adopt a microservice
architecture. In the clinic, one team is responsible for the API Gateway
and the Web UI; another team is responsible for the Customer Service;
and the last one is responsible for the Veterinarian Service. Discuss
with other teams to reach an agreement on your responsibilities.

- \[F\] Frontend team: API Gateway and Web UI
- \[C\] Customer team: Customer Service
- \[V\] Veterinarian team: Veterinarian Service

If you don’t have sufficient teams in your group, please use the
templates provided by the teacher. Depending on your team, your
assignment is slightly different.

Apply the following manifest, and ensure that the whole stack is
working:

``` sh
kubectl apply -f https://mincong.io/esigelec/lab/microservice.yaml
```

  

``` sh
# TODO: write the answer to file "${REPO}/k8s/microservice.lab3.yaml"
```

  

### Exercise 3F - Create a frontend image

> [!NOTE]
> This exercise is for the Frontend team (F).

Create a new Docker image for the API Gateway and the PetClinic UI. This
is a replacement of the default image
`springcommunity/spring-petclinic-api-gateway`. The new UI should
include your clinic name in the footer of the Spring PetClinic UI, such
as “Pet Clinic: Center” or “Pet Clinic: West”. Publish your result to
Docker Hub. The target repository is:

    mincongclassroom/spring-petclinic-api-gateway-${clinic}

The Docker image should have version `3.0` (because we are in Lab
Session 3).

Then, use the images published by other teams inside your `${clinic}`
when they are ready.

### Exercise 3C - Create a customer service image

> [!NOTE]
> This exercise is for the Customer team (C).

Create a new Docker image for the Customer Service. This is a
replacement of the default image
`springcommunity/spring-petclinic-customers-service`. You need to add a
new customer in the initialization script of the database. Publish your
result to Docker Hub. The target repository is:

    mincongclassroom/spring-petclinic-customers-service-${clinic}

The Docker image should have version `3.0` (because we are in Lab
Session 3).

Then, use the images published by other teams inside your `${clinic}`
when they are ready.

### Exercise 3V - Create a veterinarian service image

> [!NOTE]
> This exercise is for the Veterinarian team (V).

Create a new Docker image for the Veterinarian Service. This is a
replacement of the default image
`springcommunity/spring-petclinic-vets-service`. You need to add a new
veterinarian in the initialization script of the database. Publish your
result to Docker Hub. The target repository is:

    mincongclassroom/spring-petclinic-vets-service-${clinic}

The Docker image should have version `3.0` (because we are in Lab
Session 3).

Then, use the images published by other teams inside your `${clinic}`
when they are ready.

# Deployment and Networking in Kubernetes

Lab Session 4 - 4 Nov, 2024

# Introduction

The goal of this lab session is to let you practice the basics of
deployment and networking in Kubernetes, which includes the creation of
a ReplicaSet, the creation of a Deployment, the ability to read and edit
Kubernetes manifests in YAML, the configuration of commonly used
networking solutions, and the operations associated to these objectives.

To submit the answers to this lab session, please fill in your answers
in this document in-place. This should be done before the beginning of
the next course.

## Prerequisite

If you are using the computer of ESIGELEC, the Docker Desktop is
preinstalled, but you need to enable the Kubernetes feature in Docker
Desktop’s settings. If you are using your personal computer, please
download Docker Desktop from the official website of Docker, Inc. and
enable the Kubernetes feature.

<https://www.docker.com/products/docker-desktop/>

Before starting the lab session, please clear all Kubernetes resources
from your Kubernetes Cluster by using the button “Reset Kubernetes
Cluster” in your Docker Desktop (Settings \> Kubernetes).

<!-- I don't add more details because they should already have the software installed. -->

## Exercise 1 - ReplicaSet

Create a new ReplicaSet with 2 replicas and the Docker image
`nginx:1.26` and call this resource `nginx`. Use labels `app:nginx`,
`team=${team}`, and `tier=frontend` for both the ReplicaSet and the
underlying pods. The pods should expose the container port 80 to receive
incoming traffic. The name of the container should also be “nginx”.
Persist the Kubernetes manifest (YAML file) in the Git repository of
your team under the `k8s` directory.

  

    *\[answer: commit to the Git repository of your team\]*

  

  

Scale this ReplicaSet to 3 replicas using `kubectl` and provide your
command below.

  

    *\[answer\]*

  

  

Delete one of the pods from this ReplicaSet, how many pods do you have
now? Why?

  

    *\[answer\]*

  

  

## Exercise 2 - Deployment

Create a new Deployment with 2 replicas using the Docker image
`weekend-server` that you built in the previous lab session and call
this deployment `weekend-server`. Use labels `app:weekend-server`,
`team=${team}`, and `tier=backend` for both the Deployment and the
underlying pods. Persist the Kubernetes manifest (YAML file) in the Git
repository of your team under the `k8s` directory.

  

    *\[answer: commit to the Git repository of your team\]*

  

  

Do you see a ReplicaSet related to your deployment? If yes, write down
the name of the ReplicaSet, else please share your thoughts. Hint: you
can use the command `kubectl get` all\` to list all the resources in
your Kubernetes cluster under the current namespace.

  

    *\[answer\]*

  

  

Scale up the deployment to 3 replicas and share the command below.

  

    *\[answer\]*

  

  

## Exercise 3 - Service

Provide a networking solution for weekend-server application so that
this deployment is exposed to other resources inside the Kubernetes
cluster. Please ensure that access is only granted for resources within
the Kubernetes cluster, access from external systems is not allowed.
Persist the Kubernetes manifest (YAML file) in the Git repository of
your team under the `k8s` directory.

  

    *\[answer: commit to the Git repository of your team\]*

  

  

Port forward to that service by listening to port 8080 of your machine
and forwarding data to port 80 of the service (`8080:80`) to verify that
the service is working as expected. Please share the kubectl command.

  

    *\[answer\]*

  

  

Use another to verify that this is working as expected. Please create a
helper pod with the image busybox using the command below. Try to use
different DNS queries to send HTTP requests to the service. Reminder:
after starting the busybox, you can use `wget` command with output to
stdout (`-O-`), followed by the URL to make an HTTP request. You can
exit the pod by pressing CTRL + D (‘D’ stands for disconnect).

``` sh
kubectl run busybox --image=busybox --restart=Never --rm -it -- /bin/sh
# wget -O- ${URL}
```

Here is a reminder of different expressions:

| Form | Expression |
|:---|:---|
| Short form | `${service}` |
| Namespace qualified | `${service}.${namespace}` |
| Service domain | `${service}.${namespace}.svc` |
| Fully qualified domain name (FQDN) | `${service}.${namespace}.svc.cluster.local` |

Please provide the commands and results below.

  

    *\[answer\]*

  

  

## Exercise 4 - Upgrade Docker Image

Upgrade the Docker image of the deployment by setting the image to
`nginx:1.27` and roll out this change. Observe the status of the Pods
and ReplicaSet. Please describe your observation here.

  

    *\[answer\]*

  

  

Inspect the revision history of your deployment.

  

    *\[answer\]*

  

  

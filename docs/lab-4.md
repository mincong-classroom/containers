# Deployment and Networking in Kubernetes

Lab Session 4 - 7 Nov, 2024

# Introduction

The goal of this lab session is to let you practice the basics of
deployment and networking in Kubernetes, which includes the creation of
a ReplicaSet, the creation of a Deployment, the ability to read and edit
Kubernetes manifests in YAML, the configuration of commonly used
networking solutions, and the operations associated to these objectives.

To submit the answers to this lab session, please fill in your answers
in this document in-place. This should be done before the beginning of
the next course.

## Exercise 1 - ReplicaSet

Create a new ReplicaSet with 2 replicas and the Docker image
`nginx:1.26` and call this resource `nginx`. Use labels
`school=esigelec`, `app=nginx`, `team=${team}`, and `tier=frontend` for
both the ReplicaSet and the underlying pods. The pods should expose the
container port 80 to receive incoming traffic. The name of the container
should also be “nginx”. Persist the Kubernetes manifest (YAML file) in
the Git repository of your team under the `k8s` directory as file
`replicaset-nginx.yaml`.

  

    *\[answer: optional. Commit code changes directly under the `k8s`
directory.\]*

  

  

Scale this ReplicaSet to 3 replicas using `kubectl` and provide your
command below.

  

    *\[answer\]*

  

  

Delete one of the pods from this ReplicaSet, how many pods do you have
now? Why?

  

    *\[answer\]*

  

  

## Exercise 2 - Deployment

Create a new Deployment with 2 replicas using the Docker image
`mincongclassroom/weekend-server-${team}` that you built in the previous
lab session and call this deployment `weekend-server`. Use labels
`school=esigelec`, `app=weekend-server`, `team=${team}`, and
`tier=backend` for both the Deployment and the underlying pods. Persist
the Kubernetes manifest (YAML file) in the Git repository of your team
under the `k8s` directory as file `deployment-weekend-server.yaml`.

In addition to the changes above, please also

- Add environment variable `APP_TEAM` in the manifest to describe your
  team for the container
- Add environment variable `APP_AUTHORS` in the manifest to describe
  yourselves as authors of the container. The format should be
  “Firstname1 LASTNAME1, Firstname2 LASTNAME2”, such as “Emmanuel
  MACRON”.
- Propagate the pod name to the container.

  

    *\[answer: optional. Commit code changes directly under the `k8s`
directory.\]*

  

  

Do you see a ReplicaSet related to your deployment? If yes, write down
the name of the ReplicaSet, else please share your thoughts. Hint: you
can use the command `kubectl get all` to list all the resources in your
Kubernetes cluster under the current namespace.

  

    *\[answer\]*

  

  

Scale up the deployment to 3 replicas and share the command below.

  

    *\[answer\]*

  

  

Upgrade the Docker image of the weekend-server deployment by setting the
image to another version that you built. Roll out this change and
observe the status of the Pods, ReplicaSet, and Deployment. Please
describe your observation here.

  

    *\[answer\]*

  

  

Inspect the revision history of your deployment.

  

    *\[answer\]*

  

  

What if you specify an image that does not exist?

    *\[answer\]*

  

  

## Exercise 3 - Service

Provide a networking solution for weekend-server application so that
this deployment is exposed to other resources inside the Kubernetes
cluster. Please ensure that access is only granted for resources within
the Kubernetes cluster, access from external systems is not allowed.
Persist the Kubernetes manifest (YAML file) in the Git repository of
your team under the `k8s` directory as file
`service-weekend-server.yaml`.

  

    *\[answer: optional. Commit code changes directly under the `k8s`
directory.\]*

  

  

Port forward to that service by listening to port 8080 of your machine
and forwarding data to your service weekend-server to verify that the
service is working as expected. Please share the kubectl command.

  

    *\[answer\]*

  

  

Use another to verify that this is working as expected. Please create a
helper pod with the image
[curlimages/curl](https://hub.docker.com/r/curlimages/curl) using the
command below. Try to use different DNS queries to send HTTP requests to
the service. Reminder: after starting the curl image, you can use `curl`
command followed by the URL to make an HTTP request. You can exit the
pod by pressing CTRL + D (‘D’ stands for disconnect).

``` sh
kubectl run curl-pod --image=curlimages/curl --restart=Never --rm -it -- /bin/sh
# curl ${URL}
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

  

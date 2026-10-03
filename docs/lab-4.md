# Kubernetes Networking

Lab Session 4 - 29 Oct, 2025

## Introduction

The goal of this lab session is to let you practice the networking
basics in Kubernetes and apply them in real-world scenarios, i.e. in
multiple environments and in cross-team collaboration.

To submit the answers to this lab session, please fill in your answers
in this document in place. This should be done before the beginning of
the next course.

## Exercise 1 - Create Namespace

Create a new namespace called `classroom`. Then, list all the existing
namespaces in the Kubernetes cluster.

## Exercise 2 - Deploy Team Info Server

Deploy and expose the classroom-specific application `team-info-server`
in the namespace `classroom` in Kubernetes.

Create a new Deployment called `team-info` for the Docker image
[`mincongclassroom/team-info-server`](https://hub.docker.com/r/mincongclassroom/team-info-server).
This is the image that you have used in Lab Session 2. The Deployment
should have 1 replica. Then expose the application as an internal
Service named `team-info` in Kubernetes on port 80. Note that the web
server may not start successfully on the first attempt, so you need to
repair it (in the same way that you fixed it in Lab Session 2). Inspect
the Pod and its logs to understand the underlying issues. You need to
store the manifest (YAML file) under the path
`k8s/lab-4/app-team-info.yaml`, which contains both the Service and the
Deployment.

Then, you need to validate that the implementation is working and
document it on this page. You need to perform the following scenarios:

1.  Switch your current context to the namespace `classroom`, use a
    temporary Pod to query the service `team-info` via an HTTP request
2.  Switch your current context to the namespace `default`, use a
    temporary Pod to query the service `team-info` via an HTTP request

In the report, you need to document how you switch the namespace; how
you verify the Kubernetes resources in that namespace; how you create a
temporary Pod; how you perform the HTTP request, especially the URL used
and its meaning for the DNS; and how you analyze the HTTP response. Did
you notice any difference for the URLs used when you are in namespace
`classroom` or `default`?

Here are some additional information:

- Source code: <https://github.com/mincong-classroom/team-info-server>
- Docker repo:
  <https://hub.docker.com/r/mincongclassroom/team-info-server>
- Command
  [`kubectl config set-context`](https://kubernetes.io/docs/reference/kubectl/generated/kubectl_config/kubectl_config_set-context/)

## Exercise 3 - PetClinic Integration

The Spring PetClinic web application already ships with an “About” page:
the header has an “about” navigation item that opens a page which
fetches team information from the API endpoint `/api/about` and displays
it. Out of the box that page shows an error, because the request is not
routed anywhere yet.

Your task is to make the About page work by configuring the
**networking** — you do not write any frontend or Java code. When users
visit the web application (<http://localhost:8080>) and open the About
page, it should display the information served by the **Team Info
Server** you deployed in Exercise 2, such as:

| Key          | Value                                         |
|:-------------|:----------------------------------------------|
| Team         | test                                          |
| Team members | Alice DOE, Bob SMITH                          |
| Source code  | https://github.com/mincong-classroom/k8s-test |

Concretely, route requests for `/api/about` from the API Gateway to the
`team-info` Service in the `classroom` namespace. You need to store the
updated manifest (YAML file) under the path
`k8s/lab-4/microservices.yaml`.

IMPORTANT: do not hard-code the information in the API Gateway. The goal
is to practice your networking skills in Kubernetes — in particular
cross-namespace Service resolution via DNS — and your understanding of
inter-service communication.

Hint: if the About page cannot load the information, use the logs to
trace the request. The API Gateway runs with verbose (DEBUG) logging —
read its logs with `kubectl logs` to see whether your request is
received and which backend route it matches, and read the Team Info
Server’s logs to check whether the request reached it.

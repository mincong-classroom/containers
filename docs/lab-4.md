# Kubernetes Networking

Lab Session 4 - 29 Oct, 2026

## Introduction

The goal of this lab session is to let you practice the networking
basics in Kubernetes and apply them in real-world scenarios with
multiple namespaces and multiple services.

Write your answers in this document, where you see `# TODO`. A `# NOTE`
points to another file of your repository, such as a Kubernetes
manifest: write it there, not in this document. Commit and push to the
branch `main`: only `main` counts. Never force-push.

## Exercise 1 - Create Namespace

Create a new namespace called `classroom`. Then, list all the existing
namespaces in the Kubernetes cluster.

  

``` sh
# TODO: write down your commands and the list of namespaces here
```

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

Then, read the file `/etc/resolv.conf` of a temporary Pod in each
namespace. What does each line mean? Why does the short name `team-info`
answer from `classroom` only?

Here are some additional information:

- Source code: <https://github.com/mincong-classroom/team-info-server>
- Docker repo:
  <https://hub.docker.com/r/mincongclassroom/team-info-server>
- Command
  [`kubectl config set-context`](https://kubernetes.io/docs/reference/kubectl/generated/kubectl_config/kubectl_config_set-context/)

  

``` sh
# NOTE: write the manifest to file "${REPO_ROOT}/k8s/lab-4/app-team-info.yaml"
# TODO: write down your commands, the HTTP responses and your analysis of the URLs here
# TODO: write down the file /etc/resolv.conf of each namespace, and your analysis here
```

## Exercise 3 - Load Balancing

Your Service `team-info` has a single Pod behind it so far. Scale the
Deployment `team-info` to 3 replicas. Then, from a temporary Pod, send
30 HTTP requests to the Service, and find out how many requests each Pod
received, from the logs of the Pods. Do it a second time: do you get the
same numbers?

In the report, explain how the Service picks the Pod of each request. Is
it round robin? Which component of your cluster makes the choice?

  

``` sh
# TODO: write down your commands, the number of requests of each Pod, and your analysis here
```

## Exercise 4 - PetClinic Integration

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

Hint: if the About page cannot load the information, read the HTTP
status and the response that it shows, then use the logs to trace the
request. In the manifest, the verbose (DEBUG) logging of the API Gateway
is in comments, the variables `LOGGING_LEVEL_*`: enable it, then read
its logs with `kubectl logs` to see whether your request is received and
which backend route it matches, and read the Team Info Server’s logs to
check whether the request reached it.

  

``` sh
# NOTE: write the updated manifest to file "${REPO_ROOT}/k8s/lab-4/microservices.yaml"
# TODO: write down what the About page showed before and after your route, and how the logs told you where the request stopped
```

## Exercise 5 - Service Troubleshooting

Break your Team Info Server in 2 ways, one after the other, and fix it
after each one:

1.  The Service `team-info` has a selector that matches no Pod.
2.  The Service `team-info` has a `targetPort` on which the container
    doesn’t listen.

Each time, what do a temporary Pod and the About page of the PetClinic
show? What does `kubectl` show about the Service, its endpoints and its
Pods?

In the report, explain how you can tell the 2 cases apart, and which
command you would run first, next time a Service doesn’t answer. Does
the About page break at once? Why?

  

``` sh
# TODO: write down your commands, what you observed in each case, and your analysis here
```

## Exercise 6 - NodePort

So far, you reached your Services from inside the cluster, or through
`kubectl port-forward`. Expose the Team Info Server outside of the
cluster with a second Service, `team-info-nodeport`, of type `NodePort`,
in the namespace `classroom`. You need to store its manifest under the
path `k8s/lab-4/service-team-info-nodeport.yaml`.

Then call it from outside of the cluster, without
`kubectl port-forward`. Can you open it in your browser? If not, why
not, and from where can you call it?

In the report, explain the path of a request to your NodePort, from the
client to the container.

  

``` sh
# NOTE: write the manifest to file "${REPO_ROOT}/k8s/lab-4/service-team-info-nodeport.yaml"
# TODO: write down your commands, the HTTP responses and your analysis here
```

## Exercise 7 - Network Policy

The PetClinic is the website of a veterinary clinic in France. Its
Customers Service keeps the names, the addresses and the phone numbers
of the owners of the pets. These are personal data under the GDPR, and
the clinic must protect them with appropriate technical and
organizational measures.

Until now, the PetClinic shared the cluster with your Team Info Server
only, in the namespace `classroom`. From now on, the cluster will host
more projects, which have nothing to do with the PetClinic. Therefore,
the clinic asks you to make sure that the Pods of the other namespaces
can no longer call the PetClinic: neither its API Gateway, nor the
services behind it, such as the Customers Service. The only exception is
the Kubernetes system, i.e. the Pods of the namespace `kube-system`,
where the IT team runs the tools of the cluster, such as the monitoring.
You need to store the manifest of your NetworkPolicy under the path
`k8s/lab-4/networkpolicy-petclinic.yaml`.

Then, you need to validate that it works and document it on this page:

1.  From a temporary Pod in the namespace `classroom`, can you still
    call the Customers Service? And the API Gateway?
2.  From a temporary Pod in the namespace `default`, and from one in the
    namespace `kube-system`?
3.  Does the PetClinic still work in your browser, the About page
    included?

In the report, explain how you know that a request is blocked by your
policy, and which component of your cluster enforces it. Your
`kubectl port-forward` comes from neither `default` nor `kube-system`:
why does it still work?

Here is some additional information:

- Kubernetes documentation: [Network
  Policies](https://kubernetes.io/docs/concepts/services-networking/network-policies/)

  

``` sh
# NOTE: write the manifest to file "${REPO_ROOT}/k8s/lab-4/networkpolicy-petclinic.yaml"
# TODO: write down your commands, the HTTP responses and your analysis here
```

# Pod in Kubernetes

Lab Session 3 - 28 Oct, 2024

## Introduction

The goal of this lab session is to let you practice the basics of pod
and the command line tool `kubectl` in Kubernetes, which includes
different ways to create a pod, the ability to read and edit Kubernetes
manifest in YAML, and operate a pod using different commands.

To submit the answers to this lab session, please fill in your answers
in this document in-place. This should be done before the beginning of
the next course.

> [!NOTE]
> You need to enable the Kubernetes feature in Docker Desktop.
> See the README of the Git repository for more details.

## Exercise 1 - Kubernetes Overview

Observe the default containers started by the system of Kubernetes using
the command `kubectl get pods --all-namespaces`. Then, try to identify
them in the cluster architecture diagram below. You don’t have to write
down the mappings between the diagram and the output of the `kubectl`
command, but you need to paste the results of the `kubectl` command
here.

![Kubernetes Architecture
https://kubernetes.io/docs/concepts/architecture/](assets/kubernetes-cluster-architecture.svg)

  

    *\[answer: paste the output of kubectl command\]*

  

  

In the diagram above, there are 2 worker nodes and one control-plane
node (master node). How many nodes do you have on your machine? What is
the name of the node? Use the command `kubectl get nodes` to find the
answer.

  

    *\[answer\]*

  

  

## Exercise 2 - Create a nginx pod (kubectl-run)

Create a pod using the `kubectl run` command with the `nginx` Docker
image, you should name the pod “nginx” and publish the container’s port
80 to the Kubernetes cluster.

  

    *\[answer\]*

  

  

Ensure that the nginx container is running. Establish a connection with
the container using the `kubectl port-forward pod/nginx 8080:80`, so
that you can access the content via the host port 8080. Then, open your
browser, visit <http://localhost:8080>, copy the content of the web page
and paste it below.

  

    *\[answer\]*

  

  

Inspect the pod and describe its characteristics in the table below.
Hint: you can use the following commands:

- `kubectl describe pod nginx`
- `kubectl get pod nginx`
- `kubectl get pod nginx -o wide` with node information

or learn more commands from `kubectl` Quick Reference.
<https://kubernetes.io/docs/reference/kubectl/quick-reference/>

| Category  | Item                                 | Value |
|:----------|:-------------------------------------|:------|
| Container | Container name                       |       |
| Container | Container port                       |       |
| Container | Container state                      |       |
| Pod       | Pod labels                           |       |
| Pod       | IP address                           |       |
| Pod       | Namespace                            |       |
| Pod       | The total number of containers       |       |
| Pod       | The number of the running containers |       |
| Node      | Node name                            |       |

Delete this pod using the `kubectl delete` command

  

    *\[answer\]*

  

  

## Exercise 3 - Create a nginx pod (kubectl-apply)

Instead of using the `kubectl run` command, now you need to write a
manifest to describe the specification of the pod in a YAML file. Copy
the official example here:
<https://kubernetes.io/docs/concepts/workloads/pods/#using-pods>. Add
label `team=${team}` to the definition, and persist the YAML file in
your Git repository under the path `${git_repo}/k8s/pod-nginx.yaml`.

Describe the full `kubectl apply` command used:

  

    *\[answer\]*

  

  

Prove that the pod is running:

  

    *\[answer\]*

  

  

## Exercise 4 - Create a Java pod

Write a Kubernetes manifest (YAML file) to create a pod for the Java
Docker image “weekend-server” created in the previous lab sessions. This
pod should also be called a “weekend-server”, running on the container
port 8080, having labels `app=weekend-server`, `school=esigelec` and
`team=${team}` (such as `team=red`). Persist the manifest in your Git
repository under the path `${git_repo}/k8s/pod-weekend-server.yaml`

## Exercise 5 - Operate the Java pod

In this exercise, you are going to use `kubectl exec` to connect to the
container and inspect it.

Connect to the pod and then use `jps -lvm` to describe the running Java
process inside the Java pod. For more information about `jps`, see
<https://docs.oracle.com/en/java/javase/21/docs/specs/man/jps.html>.
Provide the process ID (PID) and the path of the JAR inside the
container.

  

    *\[answer\]*

  

  

What is the version of Java used?

  

    *\[answer\]*

  

  

Can you inspect the logs of the Java container: what command would you
use?

  

    *\[answer: kubectl command and some lines of logs here\]*

  

  

Can you find this java container using the `kubectl get` command with a
label selector? See more information about labels and selectors at
https://kubernetes.io/docs/concepts/overview/working-with-objects/labels/

  

    *\[answer\]*

  

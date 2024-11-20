# Configuration and Storage in Kubernetes

Lab Session 5 - 20 Nov, 2024

## Introduction

The goal of this lab session is to let you practice the basics of
configuration and storage in Kubernetes, which includes the creation of
ConfigMap and Secret to configure your application, the usage of
Kubernetes Service and Java Database Connectivity (JDBC) to connect to a
stateful application, such as MySQL Database.

To submit the answers to this lab session, please fill in your answers
in this document in-place. This should be done before the end of next
week (1 Dec, 2024 at midnight).

## Exercise 1 - Setup MySQL Cluster

Use the Kubernetes manifest `k8s/mysql.yaml` to setup the MySQL cluster
as a StatefulSet. The manifest is missing some resources, please
complete them. You can inspect the status of different resources in the
Kubernetes cluster. The missing resources should be written directly to
the file `k8s/mysql.yaml`. You are expected to have one pod running
under that StatefulSet. The database `weekend_db` should initialized,
similar to what have been done in Lab Session 1. Once the setup is done,
please verify the connectivity to the MySQL Database using the pod
`mysql-client`. Share the step(s) of the verification below and commit
the configuration changes to the `k8s` directory.

> [!IMPORTANT]
> Don’t commit sensitive information into a ConfigMap,
> such as username and password. Consider using another type of resource
> for security concerns.

> [!TIP]
> Inside the MySQL client pod, you can connect to MySQL
> database using the service ClusterIP exposed by the database. See more
> details in [DNS for Services and Pods \|
> Kubernetes](https://kubernetes.io/docs/concepts/services-networking/dns-pod-service/)
> or [“ClusterIP DNS” in the previous lecture \|
> Mincong](https://mincong.io/esigelec/4).
>
>     mysql -h $DNS_FOR_POD_OR_SERVICE -u root [...]

### Answer

<!-- Please write your answer here -->

## Exercise 2 - Connect Java Application and MySQL

Connect your backend service `weekend-server` to the MySQL Database
`weekend_db`. The answer of the request “whether today is the weekend”
should come from the database, rather than being hard-coded in Java. You
are expected to provide two API endpoints for the weekend-server:

    GET /

The default (existing) endpoint answering whether today is the weekend.
The data structure of the response of this endpoint should remain the
same, but the content should be fetched from the database.

    GET /mappings

A new endpoint listing all the days of week and whether that day is the
weekend. Here is the expected structure:

``` json
{
  "1": "No.",
  "2": "No.",
  "3": "No.",
  "4": "Soon.",
  "5": "Almost, but not yet.",
  "6": "It's the weekend!",
  "7": "It's the weekend!"
}
```

In this report, please briefly describe what modifications you did to
the Java application and Kubernetes to make it work. Prove that your
solution is working correctly. For any code changes, please commit to
the related directories.

> [!TIP]
> Using any ORM framework, such as Hibernate or Java
> Persistence API (JPA), may significantly slow down your integration.
> Consider avoid using any framework, and relying directly on Java
> Database Connectivity (JDBC) to facilitate the work.

### Answer

<!-- Please write your answer here -->

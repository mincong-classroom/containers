# Configuration and Storage in Kubernetes

Lab Session 5 - 20 Nov, 2024

## Introduction

The goal of this lab session is to let you practice the basics of
configuration and storage in Kubernetes, which includes the creation of
a ConfigMap to configure your application, the creation of a persistence
volume (PV) and a persistence volume claim (PVC) for a stateful
application, such as MySQL database.

To submit the answers to this lab session, please fill in your answers
in this document in-place. This should be done before the end of next
week (1 Dec, 2024 at midnight).

## Exercise 1 - ConfigMap

A ConfigMap is an API object used to store non-confidential data in
key-value pairs. A ConfigMap allows you to decouple environment-specific
configuration from your container images so that your applications are
easily portable. For example, this can be useful for running your
application in different environments: localhost, staging, and
production. See
<https://kubernetes.io/docs/concepts/configuration/configmap/>.

Create a ConfigMap to store configuration related to database connection
for a MySQL Database. More precisely, create it with the following
key-value pairs:

| Key           | Value                             |
|:--------------|:----------------------------------|
| `DB_HOST`     | `mysql.default.svc.cluster.local` |
| `DB_PORT`     | `3306`                            |
| `DB_NAME`     | `weekend`                         |
| `DB_USERNAME` | `mysql`                           |
| `DB_PASSWORD` | `mysql`                           |

> Note that the password is created here for demonstration purposes,
> please don’t do this in real-world scenarios. You should use a Secret
> rather than a ConfigMap, or use additional (third-party) tools to keep
> your data private.

Persist the Kubernetes manifest (YAML file) in the Git repository of
your team under the `k8s` directory.

  

    *\[answer: commit to the Git repository of your team\]*

  

  

Use this ConfigMap in the Deployment of “weekend-server”, defined in the
previous lab session. This can be done using the `envFrom` instruction
to define all of the ConfigMap’s data as container environment
variables. Here is an example if you need some inspiration, or visit the
web page
https://kubernetes.io/docs/tasks/configure-pod-container/configure-pod-configmap/#configure-all-key-value-pairs-in-a-configmap-as-container-environment-variables
for more details.

``` yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: my-deployment
spec:
  containers:
  - name: my-container
    image: my-image
    envFrom:
    - configMapRef:
        name: my-configmap
```

Persist the changes of the Kubernetes manifest (YAML file) in the Git
repository of your team under the `k8s` directory.

  

    *\[answer: commit to the Git repository of your team\]*

  

  

Are pods of the deployment restarted? Do you see those environment
variables? Hint: you can use `kubectl exec ${POD} -- env` to list all
the environment variables present in the target pod (`${POD}`). Paste
the exact command used and the results of the command.

  

    *\[answer\]*

  

  

Now, your team is assigned to develop an AI service for the company.
Both the backend service “weekend-server” and the new AI service need to
connect to the MySQL Database, how would you share the configuration for
both services? Please describe your suggestion.

  

    *\[answer\]*

  

  

## Exercise 2 - Persistence Volume and MySQL

The main motivation for using a Persistent Volume (PV) in Kubernetes is
to provide a mechanism for persistent storage that is independent of the
lifecycle of individual pods. Data stored in PVs persists even if the
pods using them are deleted, restarted, or rescheduled to different
nodes. This is crucial for stateful applications like databases, where
data needs to survive beyond the lifespan of any single pod.

Apply the predefined MySQL pod specification, PV and related service
from Nanosearch to your cluster:

``` sh
kubectl apply -f https://nanosearch.io/k8s/mysql.yaml
```

And then inspect the resources created: what kinds of Kubernetes
resources do you see?

  

    *\[answer\]*

  

  

Now, connect yourself the MySQL pod, then create a database “weekend”
and table “mappings” with the following rows.

``` sh
# username (`-u`): root
# password (`-p`): you need to enter it yourself
kubectl exec ${MYSQL_POD} -it -- mysql -uroot -p
# Welcome to the MySQL monitor.  Commands end with ; or \g.
# Your MySQL connection id is 8
# Server version: 8.0.38 MySQL Community Server - GPL
#
# Copyright (c) 2000, 2024, Oracle and/or its affiliates.
#
# Oracle is a registered trademark of Oracle Corporation and/or its
# affiliates. Other names may be trademarks of their respective
# owners.
#
# Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.
#
# mysql>
```

``` sql
-- Create the 'weekend' database
CREATE DATABASE IF NOT EXISTS weekend;

-- Use the 'weekend' database
USE weekend;

-- Create the 'mappings' table
CREATE TABLE IF NOT EXISTS mappings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    weekday VARCHAR(50) NOT NULL,
    answer VARCHAR(50) NOT NULL
);

-- Insert the specified rows into the 'mappings' table
INSERT INTO mappings (weekday, answer) VALUES
('Sunday', 'Yes'),
('Monday', 'No'),
('Tuesday', 'No'),
('Wednesday', 'No'),
('Thursday', 'No'),
('Friday', 'Almost, not yet'),
('Saturday', 'Yes');
```

Now, observe the pod, its persistent volume and persistent volume claim,
then fill the information in the table below.

| Item                    | Value   |
|:------------------------|:--------|
| Pod                     | mysql-0 |
| Persistent Volume       | pv-xxx  |
| Persistent Volume Claim | pvc-xxx |

Now, delete the MySQL pod, what happens? Is the pod restarted, are the
PV and PVC deleted? Do you have a data loss? Please connect to the MySQL
pod again and check if the database, table, and rows still exist.

  

    *\[answer\]*

  

  

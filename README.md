# Assignments

Welcome to the lab sessions! This is a mono-repository for the assignments of the course "Kubernetes" for the students of [ESIGELEC](https://esigelec.fr), majoring in "Ingénierie des Services du Numérique" (ISN). It includes 2 sections, 5 lab sessions, and is composed of different key objectives as described below:

```mermaid
timeline
    section Containers
        §1 Containerization with Docker
            : Package Java application as a JAR
            : Create Docker image
            : Publish Docker image to a registry
            : Run Docker image
    section Kubernetes
        §2 Pods
            : Explore a Kubernetes cluster with kubectl
            : Create Pods in different ways
            : Operate Pods with kubectl
        §3 Deployment
            : Create a new ReplicaSet
            : Create a Deployment
            : Understand Deployment characteristics
            : Adapt microservice architecture
        §4 Networking
            : Create a Service
            : Roll out a new version of the application
            : Collaborate with other teams to develop new features
        §5 Configuration and Storage
            : Create a ConfigMap
            : Set up a new workload end-to-end
```

## Report Submission

For students attempting this course, the description of each lab session is available under the `docs/` directory, such as `lab-1.md`. For each report, you need to fill in the answers in the document in place in the answer areas. Or, you need to provide relevant code snippets in your Git repository following the instructions. The answers to each lab session will be distributed at the beginning of the next chapter, so the deadline for submitting your lab report is the beginning of the next session. The deadline for the last lab session will be Sunday, 2 Nov 2025, at midnight. Please complete everything before the deadline. All submissions after the deadline won't be taken into account.

The score of the course is calculated based on the scores of the lab sessions, where each lab session is equally weighted. Each lab session has 20 points and the final score of the module is the average of the 5 lab sessions. For example, if you have 13, 17, 15, 14, and 16 for the 5 lab sessions, then the final score will be 15. There is no exam at the end of the course.

$$
\text{Score} = \frac{13 + 17 + 15 + 14 + 16}{5} = \frac{75}{5} = 15
$$

> [!NOTE]
>
> Please do not start the lab session in advance because the content of the lab is related to the lecture, so you should follow the lecture before getting started. Also, the content of the lab may be modified.

## Installation

### Docker Desktop

Docker Desktop is required for the lab sessions of this course. If you use a computer from ESIGELEC laboratory, the Docker Desktop is preinstalled. But if you use your personal computer, you need to download the Docker Desktop from the official website of Docker, see <https://www.docker.com/products/docker-desktop/>. You can verify if the command line tool `docker` is available in your terminal and verify its version using the following commands:

```sh
type docker
#docker is /Users/minconghuang/.docker/bin/docker

docker --version
#Docker version 26.1.4, build 5650f9b
```

> [!WARNING]
> For any computer from ESIGELEC laboratory, you don't have permissions to install software yourselves. Please contact the teacher if you encounter any difficulties.

### Kubernetes

Kubernetes has many distributions. For this course, we use the Kubernetes cluster that Docker Desktop runs on your machine. It is needed from lab session 2 on: lab session 1 doesn't use it.

Lab session 2 starts by creating the cluster: see "Before you start" in [`docs/lab-2.md`](docs/lab-2.md). In short, in Docker Desktop, open the **Kubernetes** view, select **Create cluster**, choose the cluster type **kind** with one node, or **Kubeadm** if kind is not offered, and select **Create**.

You can verify if the command line tool `kubectl` is available in your terminal and verify its version using the following commands:

```sh
type kubectl
#kubectl is /usr/local/bin/kubectl

kubectl version
#Client Version: v1.36.1
#Kustomize Version: v5.8.1
#Server Version: v1.36.1
```

On Windows, replace `type kubectl` with `Get-Command kubectl` in PowerShell, or `where kubectl` in the Command Prompt. On Linux, Docker Desktop doesn't install `kubectl`: install it first, see <https://kubernetes.io/docs/tasks/tools/>. Your versions may differ: the server version is the one of your cluster.

> [!TIP]
> Use the button **Reset cluster**, in the Kubernetes settings of Docker Desktop, to clear all existing objects of the cluster. This can be useful if you want to start from scratch, especially when you use a desktop from the school or when you messed up the cluster with incorrect operations.

> [!TIP]
> To facilitate your operations, you can enable the shell completion for your OS. Visit the official guide [kubectl completion | Kubernetes](https://kubernetes.io/docs/reference/kubectl/generated/kubectl_completion/) for the detailed instructions. You can also add an alias `k` for `kubectl` so that you don't have to type the entire command.
>
> Bash / Zsh:
>
> ```sh
> alias k=kubectl
> ```
>
> PowerShell:
>
> ```powershell
> Set-Alias k kubectl
> ```

### Others

You are also expected to have these command line tools: `mvn`, `javac`, `git`, `curl`

## Discussions

If you have any questions or feedback for this course, please use the [discussions](https://github.com/orgs/mincong-classroom/discussions) of GitHub. I would love to hear from you!

# Configuration

Lab Session 5 - 30 Oct, 2026

## Introduction

In this session, you deploy a new service for the PetClinic: an AI
assistant, behind the chat box “Chat with Us!” of the website, which
answers the questions of the users on the vets, the owners and their
pets. The goal of this lab session is to train you in configuring
workloads in Kubernetes: you practice the creation of a Secret, the
management of a ConfigMap, and what you learned in the previous
chapters. Then you move the data of the service to a database, which
runs as a StatefulSet, with its own storage. At the end, you look inside
the service, to understand how an AI assistant works.

``` mermaid
timeline
    1. Secret
        : Create the Secret of the API key
    2. Workload
        : Deploy the GenAI service
        : Check it on its own
    3. Routing
        : Route the chat to the GenAI service
        : Validate the chat end to end
    4. StatefulSet
        : Deploy Qdrant, a vector database
        : Connect the GenAI service to it
        : Explore its cluster of 3 Pods
    5. Mechanism (optional)
        : Prompt engineering
        : Tools
        : Retrieval-augmented generation
        : Memory
```

Write your answers in this document, where you see `# TODO`. A `# NOTE`
points to another file of your repository, such as a Kubernetes
manifest: write it there, not in this document. Commit and push to the
branch `main`: only `main` counts. Never force-push.

This lab session starts from the stack of Lab Session 4. Pull the files
of this lab, copy your manifest of Lab 4 to the path of the answer of
this lab, and apply it:

``` sh
git pull
cp k8s/lab-4/microservices.yaml k8s/lab-5/microservices.yaml
kubectl apply -f k8s/lab-5/microservices.yaml
```

  

## Exercise 1 - Create Secret

In the `default` namespace, where the PetClinic stack runs, create a new
Opaque Secret called “openai”: a Pod can only read the Secrets of its
own namespace. This Secret should contain one single key called
“api-key”. The value of the API key is communicated on Microsoft Teams.
Please verify that the secret is created. In the validation, only use a
prefix of the actual value to prove that the resource is created.

> [!CAUTION]
> Don’t commit the API value to Git. Don’t commit the key
> directly, its base64-encoded value, or the Secret manifest (YAML).

  

``` sh
# TODO: enter command and verification here
```

  

## Exercise 2 - Create Workload

The service behind the chat box is the GenAI service: it answers the
questions of the users with a language model of OpenAI. Deploy it, and
check that it works on its own.

In your manifest `k8s/lab-5/microservices.yaml`, add a Deployment
`genai-service`, with 1 replica, whose container `main` runs the image
`mincongclassroom/spring-petclinic-genai-service:2026.1` and uses the
port 8080. Configure it like the four other services of the manifest. It
needs one more environment variable, `OPENAI_API_KEY`, whose value is
the key of the Secret `openai` of Exercise 1: reference the Secret,
don’t copy the key. Then add a Service `genai-service`, which listens on
the port 80, like the other Services, and forwards to the port 8080 of
the container.

The Deployment of the GenAI service needs three more things:

- The Spring profile `kubernetes`, and the ConfigMap `petclinic-config`,
  mounted at `/config`: the Deployments of the four other services have
  them in common.
- Its access log, enabled as in the Deployment of the API Gateway: the
  GenAI service has the same web server, Netty.
- The variable `SPRING_REACTOR_CONTEXT_PROPAGATION`, with the value
  `auto`: its traces then join the ones of the API Gateway, which
  Exercise 5 reads.

Hint: the profile, the access log and the propagation of the context are
environment variables of the container.

Apply the manifest, then follow the logs of the GenAI service until the
application has started, which takes about 30 seconds. If it does not
start, find out why with what you learned in the lectures. Now check
that the service answers through its Service, before the API Gateway
knows it, on the local port 8081, since the API Gateway already takes
8080. Send this request:

``` text
POST http://localhost:8081/chatclient
Content-Type: text/plain

Which vets are radiologists?
```

What does the GenAI service answer?

  

``` sh
# NOTE: write the manifest to file "${REPO_ROOT}/k8s/lab-5/microservices.yaml"
# TODO: write down your commands, your checks and the answer of the GenAI service here
```

  

Now open the website (<http://localhost:8080>), with the port-forward of
the API Gateway, click “Chat with Us!” at the bottom of the page, and
ask the same question. What does the chat answer? Use the access logs,
as in Lab 3, Exercise 4, to find where the request of the chat goes, and
where it stops.

  

``` sh
# TODO: write down the answer of the chat, the lines of the access logs and your analysis here
```

  

## Exercise 3 - Routing

The API Gateway receives the questions of the chat box, but sends them
nowhere. Find where its routes are configured in your manifest, and add
the one that sends the requests of the chat, the path `/api/genai/**`,
to the GenAI service. Apply the manifest, then make the API Gateway take
the new route into account. Which of the five Deployments did you
restart, and why that one only?

Then ask the chat a question on the vets and one on the owners,
e.g. “Which vets are radiologists?” and “Who owns the pet named Leo?”.
What does it answer? Find the lines of these requests in the access logs
of the API Gateway and of the GenAI service.

  

``` sh
# NOTE: write the change to file "${REPO_ROOT}/k8s/lab-5/microservices.yaml"
# TODO: write down your commands, your answer, the answers of the chat and the lines of the access logs here
```

  

## Exercise 4 - StatefulSet

The GenAI service searches the vets in a vector store. In the previous
exercises, this was done with an in-memory vector store. Now, you are
going to industrialize the solution by using a real vector database:
[Qdrant](https://qdrant.tech/), run as a StatefulSet. The detailed
instructions are specified in the sub-exercises below.

  

### 4.1 Deploy Qdrant

In a new manifest, `k8s/lab-5/qdrant.yaml`, add a StatefulSet `qdrant`,
with 3 replicas, whose container `main` runs the image
`qdrant/qdrant:v1.14.1`. Qdrant keeps its data in the directory
`/qdrant/storage` of its container: give each Pod of the StatefulSet a
volume of its own there, of 1 GiB.

Qdrant runs in its distributed mode, where its Pods are the peers of one
cluster. A Pod listens on 3 ports:

- 6333: REST API and web UI
- 6334: gRPC, which the GenAI service uses
- 6335: the peers talk to each other within the cluster

On top of that, the cluster needs 2 Services:

- `qdrant`: for the clients (ports 6333, 6334)
- `qdrant-headless`: for the cluster (port 6335), a headless Service,
  named in the `serviceName` of the StatefulSet

Also, their containers need the configuration below, to use Qdrant in
cluster mode: the Pod 0 starts the cluster, and the other Pods join it.

``` yaml
          env:
            - name: QDRANT__CLUSTER__ENABLED
              value: "true"
          command: ["/bin/sh", "-c"]
          args:
            - |
              if [ "$HOSTNAME" = "qdrant-0" ]; then
                exec ./entrypoint.sh --uri "http://qdrant-0.qdrant-headless:6335"
              else
                exec ./entrypoint.sh --bootstrap "http://qdrant-0.qdrant-headless:6335" \
                  --uri "http://$HOSTNAME.qdrant-headless:6335"
              fi
```

Apply the manifest `k8s/lab-5/qdrant.yaml`, and check with `kubectl`
what Kubernetes created: the StatefulSet, its Pods, their claims and the
volumes behind them, and the 2 Services. In which order does Kubernetes
create the Pods? Which names does it derive from your manifest, and how?
How does it differ from a Deployment, such as the one of the GenAI
service? What does each Service give?

Then open the web UI of Qdrant, at <http://localhost:6333/dashboard>,
through a port-forward to the Service `qdrant`, and import the snapshot
`k8s/lab-5/qdrant/vector_store.snapshot` as the collection
`vector_store`. How many points does the collection hold, and what is in
one of them? Click “Find Similar” on a radiologist: which vet comes
first?

Last, prepare the traces of Exercises 4.2 and 5. They need the Tracing
Server, and their export enabled in your ConfigMap, as in Lab 3,
Exercise 6. If your stack no longer has them, apply `observability.yaml`
again, set `enabled: true` under `tracing` and `export` in the ConfigMap
of your manifest, apply it, and restart the five services. Then open
Zipkin, at <http://localhost:9411>:

``` sh
kubectl apply -f https://mincong.io/esigelec/lab/3/observability.yaml
kubectl apply -f k8s/lab-5/microservices.yaml
kubectl rollout restart deployment/api-gateway deployment/customers-service deployment/visits-service deployment/vets-service deployment/genai-service
kubectl port-forward deployment/tracing-server 9411:9411
```

  

``` sh
# NOTE: write the manifest to file "${REPO_ROOT}/k8s/lab-5/qdrant.yaml"
# TODO: write down your commands, the names of the resources, your answers and what you found in the web UI here
```

  

### 4.2 Connect the GenAI Service

The image of the GenAI service chooses its vector store with the
property `spring.ai.vectorstore.type`: `simple`, the default, in the
memory of its Pod, or `qdrant`, where it searches the collection
`vector_store` that you imported, and loads nothing into it. Add these
properties to the ConfigMap of your manifest
`k8s/lab-5/microservices.yaml`, under its existing key `spring`, not in
a second one:

``` yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: petclinic-config
data:
  application.yml: |
    # ...
    spring:
      # ... the existing properties, such as sql and jpa ...
      ai:
        vectorstore:
          type: qdrant
          qdrant:
            host: qdrant
            port: 6334
```

As the API Gateway in Exercise 3, the GenAI service reads its
configuration when it starts: apply the manifest, then restart the
service.

``` sh
kubectl apply -f k8s/lab-5/microservices.yaml
kubectl rollout restart deployment/genai-service
```

Then go to the PetClinic website, and ask the chat “Which vets are
cardiologists?”. Find the trace of this question in Zipkin, and look at
its spans: which one is the search in Qdrant?

  

``` sh
# NOTE: write the change to file "${REPO_ROOT}/k8s/lab-5/microservices.yaml"
# TODO: write down the answer of the chat, and the span of the search in Qdrant here
```

  

### 4.3 Clustering

Your 3 Pods are the peers of one cluster of Qdrant. Open the web UI of
each Pod in turn, through a port-forward to the Pod, such as
`pod/qdrant-2`. Which peers does the cluster have, and which one is its
leader (`GET cluster`, in the Console)? Where are the points of the
collection `vector_store` (the tab Info of the collection)?

Then insert the vet of `k8s/lab-5/qdrant/vet-8.json` through `qdrant-2`
only, with the REST API. Which Pods have it? What does the chat answer
to “Which vets are dermatologists?”?

Draw a diagram of the topology: the GenAI service, the 2 Services of
Qdrant, `qdrant` and `qdrant-headless`, and the StatefulSet with its 3
Pods. A Mermaid diagram or a picture of a drawing, both work.

Qdrant elects the leader of its cluster with the algorithm Raft. Study
it a little, and explain what you learned: when the Pod of the leader is
deleted, does the cluster elect another one? What keeps working in the
meantime, and what would stop with 2 Pods down? Would 4 replicas be
safer than 3?

  

``` sh
# TODO: write down your commands, what you found in the web UI, your answers, the answer of the chat, your diagram and what you learned about Raft here
```

  

## Exercise 5 - GenAI Mechanism (Optional)

The chat answers on the vets and the owners of the clinic, and it can
even add a pet to an owner. How? This exercise looks inside the GenAI
service, in four topics, which are the building blocks of most AI
assistants today: the prompt, the tools, the retrieval of documents, and
the memory.

Find the answers with the observability tools first, as in Lab Sessions
3 and 4: the access logs, the logs of the services, and the traces in
Zipkin. The source code of the service,
[`spring-petclinic-genai-service`](https://github.com/mincong-classroom/spring-petclinic-microservices/tree/esigelec-2026/spring-petclinic-genai-service),
is another way to find an answer, or to confirm what you see.

The traces need the Tracing Server, and their export enabled in your
ConfigMap: you prepared them in Exercise 4.1. Open Zipkin, at
<http://localhost:9411>:

``` sh
kubectl port-forward deployment/tracing-server 9411:9411
```

  

### 5.1 Prompt Engineering

Ask the chat “Which vets are radiologists?”. In Zipkin, find the trace
of this question: add the criterion `tagQuery`, with
`http.url=/api/genai/chatclient`, as in Lab 3, Exercise 6. Which
services does the trace go through, and what does each one do? Which
spans call OpenAI, and for which models? With which temperature does the
chat model answer? How many tokens does the question cost?

The trace does not hold the text that the service sends to the model:
the logs of the GenAI service do, after the word `request:`. Besides
your question, what does the service send, and what is it for?

  

``` sh
# TODO: write down what you found in the trace and the logs, and your answers here
```

  

### 5.2 Tools

The trace of 5.1 lists the **tools** that the service offers to the
model: what are they? Now ask “Who owns the pet named Leo?”, and follow
this question through the access logs and the traces. Which tools does
the model use, and what is the sequence of the actions, from the
question to the answer? Write down your analysis, and draw this sequence
in a sequence diagram.

Finally, ask the chat when Max visited the clinic, and why. Then open
the page of its owner, Jean Coleman, in the website. Is the answer of
the chat right? Why?

  

``` sh
# TODO: write down the tools, what you found in the logs and the traces, your analysis, and your answers here
```

  

### 5.3 Retrieval-Augmented Generation (RAG)

Go back to the question of 5.1, “Which vets are radiologists?”. Which
tool did the model use, and with which criteria? Does the Vets Service
receive a request? The vets come from a **vector store** instead, in
Qdrant since Exercise 4. In the trace, what does the service do to
search it?

The snapshot that you imported in Exercise 4.1 comes from a file of the
image of the GenAI service, which the service loads in memory without
Qdrant:
[`vectorstore.json`](https://github.com/mincong-classroom/spring-petclinic-microservices/blob/esigelec-2026/spring-petclinic-genai-service/src/main/resources/vectorstore.json).
How many documents does it hold, and what is in one of them? Who
computed its vectors, and when? If the clinic hires a new vet, does the
chat know it?

  

``` sh
# TODO: write down what you found in the logs, the trace and the file, and your answers here
```

  

### 5.4 Memory

Tell the chat your name, then ask it “What is my name?”. In the traces
of the two questions, compare the tokens that the service sends to the
model: why does the number grow? Now restart the GenAI service, wait for
it to start, and ask “What is my name?” again. What does the chat
answer, and why? What does the chat box still show, and why?

  

``` sh
# TODO: write down the answers of the chat, what you found in the traces, and your analysis here
```

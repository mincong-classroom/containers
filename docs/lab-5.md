# Configuration

Lab Session 5 - 30 Oct, 2026

## Introduction

In this session, you deploy a new service for the PetClinic: an AI
assistant, behind the chat box “Chat with Us!” of the website, which
answers the questions of the users on the vets, the owners and their
pets. The goal of this lab session is to train you in configuring
workloads in Kubernetes: you practice the creation of a Secret, the
management of a ConfigMap, and what you learned in the previous
chapters. At the end, you look inside the service, to understand how an
AI assistant works.

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
    4. Mechanism, optional
        : Prompt engineering
        : Tools
        : Retrieval-augmented generation
        : Memory
```

Write your answers in this document, where you see `# TODO`. A `# NOTE`
points to another file of your repository, such as a Kubernetes
manifest: write it there, not in this document. Commit and push to the
branch `main`: only `main` counts. Never force-push.

This lab session starts from the stack of Lab Session 4. Copy your
manifest of Lab 4 to the path of the answer of this lab, and apply it:

``` sh
mkdir k8s/lab-5
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
`mincongclassroom/spring-petclinic-genai-service:2026.0` and uses the
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
  Exercise 4 reads.

In the Deployment, they go here:

``` yaml
spec:
  template:
    spec:
      containers:
        - name: main
          # ...
          env:
            - name: SPRING_PROFILES_ACTIVE
              value: kubernetes
            - name: JAVA_TOOL_OPTIONS
              value: -Dreactor.netty.http.server.accessLogEnabled=true
            - name: SPRING_REACTOR_CONTEXT_PROPAGATION
              value: auto
            # ... and OPENAI_API_KEY, from the Secret
          volumeMounts:
            - name: petclinic-config
              mountPath: /config
      volumes:
        - name: petclinic-config
          configMap:
            name: petclinic-config
```

Apply the manifest, then follow the logs of the GenAI service until the
application has started, which takes about 30 seconds. If it does not
start, find out why with what you learned in the lectures. Now check
that the service answers through its Service, before the API Gateway
knows it. The port-forward of the API Gateway takes the local port 8080:
forward the local port 8081 to the Service, then, in another terminal,
ask a question:

``` sh
kubectl port-forward service/genai-service 8081:80
```

``` sh
# Linux, macOS, Git Bash
curl -X POST localhost:8081/chatclient -H 'Content-Type: text/plain' -d 'Which vets are radiologists?'
# PowerShell
curl.exe -X POST localhost:8081/chatclient -H "Content-Type: text/plain" -d "Which vets are radiologists?"
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

  

## Exercise 4 - GenAI Mechanism

This exercise is optional: your lab session is complete without it. The
chat answers on the vets and the owners of the clinic, and it can even
add a pet to an owner. How? This exercise looks inside the GenAI
service, in four topics, which are the building blocks of most AI
assistants today: the prompt, the tools, the retrieval of documents, and
the memory.

Find the answers with the observability tools first, as in Lab Sessions
3 and 4: the access logs, the logs of the services, and the traces in
Zipkin. The source code of the service,
[`spring-petclinic-genai-service`](https://github.com/mincong-classroom/spring-petclinic-microservices/tree/esigelec-2026/spring-petclinic-genai-service),
is another way to find an answer, or to confirm what you see.

The traces need the Tracing Server, and their export enabled in your
ConfigMap, as in Lab 3, Exercise 6. If your stack no longer has them,
apply `observability.yaml` again, set `enabled: true` under `tracing`
and `export` in the ConfigMap of your manifest, apply it, and restart
the five services. Then open Zipkin, at <http://localhost:9411>:

``` sh
kubectl apply -f https://mincong.io/esigelec/lab/3/observability.yaml
kubectl apply -f k8s/lab-5/microservices.yaml
kubectl rollout restart deployment/api-gateway deployment/customers-service deployment/visits-service deployment/vets-service deployment/genai-service
kubectl port-forward deployment/tracing-server 9411:9411
```

  

### 4.1 Prompt Engineering

Ask the chat “Which vets are radiologists?”. In Zipkin, find the trace
of this question: add the criterion `tagQuery`, with
`http.url=/api/genai/chatclient`, as in Lab 3, Exercise 6. Which steps
does the question go through, and which one calls OpenAI? Which model
answers, with which temperature? How many tokens does the question cost?

The trace does not hold the text that the service sends to the model:
the logs of the GenAI service do, after the word `request:`. Besides
your question, what does the service send, and what is it for?

  

``` sh
# TODO: write down what you found in the trace and the logs, and your answers here
```

  

### 4.2 Tools

The trace of 4.1 lists the **tools** that the service offers to the
model: what are they? Now ask “Who owns the pet named Leo?”, and follow
this question through the access logs of the API Gateway, of the GenAI
service and of the Customers Service, and through its trace. In which
order do the services receive a request, and who is the client of each
one? Which tool did the model use, and why does the Customers Service
receive a request, although the API Gateway sent the question to the
GenAI service? Does the trace of the question include the request to the
Customers Service?

Finally, ask the chat when Max visited the clinic, and why. Then open
the page of its owner, Jean Coleman, in the website. Is the answer of
the chat right? Why?

  

``` sh
# TODO: write down the lines of the logs, what you found in the trace, and your answers here
```

  

### 4.3 Retrieval-Augmented Generation (RAG)

Go back to the question of 4.1, “Which vets are radiologists?”. Which
tool did the model use, and with which criteria? Does the Vets Service
receive a request? The vets come from a **vector store** instead, which
the GenAI service loads when it starts: find it in its logs. In the
trace, what does the service do to search it?

Then open the file of the vector store,
[`vectorstore.json`](https://github.com/mincong-classroom/spring-petclinic-microservices/blob/esigelec-2026/spring-petclinic-genai-service/src/main/resources/vectorstore.json).
How many documents does it hold, what is in one of them, and how many
numbers has a vector? Where is the vector store while the service runs?
If the clinic hires a new vet, does the chat know it?

  

``` sh
# TODO: write down what you found in the logs, the trace and the file, and your answers here
```

  

### 4.4 Memory

Tell the chat your name, then ask it “What is my name?”. In the traces
of the two questions, compare the tokens that the service sends to the
model: why does the number grow? Now restart the GenAI service, wait for
it to start, and ask “What is my name?” again. What does the chat
answer, and why? What does the chat box still show, and why?

  

``` sh
# TODO: write down the answers of the chat, what you found in the traces, and your analysis here
```

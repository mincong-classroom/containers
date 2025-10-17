# Kubernetes Networking

Lab Session 4 - 29 Oct, 2024

## Introduction

The goal of this lab session is to let you practice the networking
basics in Kubernetes and apply them in real-world scenarios, i.e. in
multiple environments and in cross-team collaboration.

To submit the answers to this lab session, please fill in your answers
in this document in-place. This should be done before the beginning of
the next course.

## Exercise 1 - Hello Server

Expose the demo application
[`hello-server`](https://hub.docker.com/r/mincongclassroom/hello-server)
in Kubernetes.

Create a new Deployment called `hello-server-deployment` for the
application `hello-server`
(https://hub.docker.com/r/mincongclassroom/hello-server). This is the
image that you have fixed in Lab Session 1. The Deployment should have 1
replica. Then, expose the application as an internal Service called
`hello` in Kubernetes, under the port 80. Validate that the
implementation is working and document it in this page.

## Exercise 2 - PetClinic Microservice

Under the existing stack of the Spring PetClinic (deployed in Lab
Session 3), expose the API gateway as a Service of type NodePort under
the port `30000`. Validate that the solution implementation is working
and document it in this page.

## Exercise 3 - PetClinic Environments

Create two namespaces: `prod` and `dev`. Each namespace should contain
the whole stack in microservice, including the API gateway and the
backend services.

- In the prod namespace, use version `3.0` of the Docker images, created
  in Lab Session 3. All resources should contain the label `env=prod`.
- In the dev namespace, use version `3.0` of the Docker images, created
  in Lab Session 3. Note: later on, they will be replaced by new images
  `4.x` created in this session. All resource should contain the label
  `env=dev`.

Also, add the label `clinic=${clinic}`. Is the Pet Clinic still
accessible? If not, please fix it. Describe your observations and fixes
in this page.

## Exercise 4 - Cross-team Collaboration

In this exercise, you will collaborate with other teams to improve the
Pet Clinic service together in the `dev` namespace. Each team uses the
Kubenetes cluster in your computer, but you can deploy new Docker image
to update the logic of the microservices, and update the Kubernetes
resources (Deployment, Service, …) to change your infrastructure.

## Exercise 4C - Email Support

The pet owners are managed by the `customers-service`. Currently,
registering an owner does not require an email. But this is an important
way of communication nowadays. Could you add this feature? You should
implemente the backend logic and collaborate with the frontend team for
the frontend logic.

Here are some requirements:

- In the UI, the owner registration form should contain a new field
  “Email”
- In the UI, email should be read-only when editing owner’s information
- In the UI, email should be displayed in the owner page
- In the backend, the email should be validated
- In the backend, the email information should be persisted in the
  database
- In the backend, the email should be part of the Owner Listing API

The result should be built and deployed as image `4.x` in DockerHub
(e.g. `4.0`, `4.1`, …). Don’t overwrite existing images. If you need
additional changes, push another image.

Describe what you implemented here, including key code changes, UI
changes. Also, how you validate your changes.

## Exercise 4V - Veterinarian Qualification

The pet owners needs more information from the veterinarians before
trusting them to treat their pets. Please add a field “diplomas” to
represent a list of additional diplomas that the veterinarian has. It
helps justifying his/her specialization. You can use the following
abbreviations:

- `DESV`: Diplôme d’Études Spécialisées Vétérinaires — equivalent to a
  French medical specialization.
- `CES`: Certificat d’Études Spécialisées — an older qualification, but
  still valid.
- `DIPL`: European or American “Diplomate” titles (e.g. Dipl. ECVS for
  surgery, Dipl. ECVIM for internal medicine, etc.) — these represent
  the highest level of expertise recognized internationally.

Here are some requirements

- This new field should be part of the Veterinarians Listing API
- This new field should be displayed in the Veterinarians page
- This new field should be persisted in the database

Then, create a new API to register a veterinarian, similar to the “owner
registration”.

## Exercise 4F - UI Support

Collaborate with the Customers team and the Veterinarian team to support
the new use cases: the email of the customer, and the diplomas of the
veterinarians.

Email support:

- The owner registration form should contain a new field “Email”
- Email should be read-only when editing owner’s information
- Email should be displayed in the owner page

Veterinarians support:

- Diplomas should be part of the Verterinarians Listing
- A new page should be created for registering a new Verterinarian

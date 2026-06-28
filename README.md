# MLOps Pipeline Management API

**Module:** 5COSC022W – Client-Server Architectures
**Coursework:** RESTful API Development using JAX-RS (Jersey)
**Student Name:** Dulitha Nadith Weerasinghe
**Student ID:** 20231613

---

# API Design Overview

The MLOps Pipeline Management API is a RESTful web service developed using **JAX-RS (Jersey)** and an embedded **Grizzly HTTP Server**. The API simulates a cloud-based Machine Learning Operations (MLOps) platform that allows users to manage Machine Learning Workspaces, Machine Learning Models and their Evaluation Metrics.

The application follows REST architectural principles by exposing resources through meaningful URIs, using standard HTTP methods, returning JSON representations and remaining stateless. All application data is stored in memory using `ConcurrentHashMap` and `ArrayList`, ensuring compliance with the coursework requirement of not using an external database.

---

# Technology Stack

* Java
* Maven
* JAX-RS (Jersey)
* Embedded Grizzly HTTP Server
* Jackson JSON Provider
* ConcurrentHashMap
* ArrayList
* java.util.logging.Logger

---

# Project Structure

```text
src
 └── main
     └── java
         └── reassessment
             └── csa
                 ├── model
                 ├── repository
                 ├── resource
                 ├── exception
                 ├── filter
                 └── MLOpsApplication.java
```

### Package Description

| Package    | Description                                                                                       |
| ---------- | ------------------------------------------------------------------------------------------------- |
| model      | Contains the POJO classes: MLWorkspace, MachineLearningModel and EvaluationMetric.                |
| repository | Stores all application data using ConcurrentHashMap and ArrayList.                                |
| resource   | Contains all REST resource classes and API endpoints.                                             |
| exception  | Contains custom exceptions and ExceptionMapper implementations.                                   |
| filter     | Implements request and response logging using ContainerRequestFilter and ContainerResponseFilter. |

---

# Build and Launch Instructions

## Prerequisites

* Java 11 or later
* Apache Maven

## Compile and Run

```bash
mvn clean package exec:java -Dexec.mainClass="reassessment.csa.MLOpsApplication"
```

The API starts on:

```
http://localhost:8080/api/v1
```

Stop the server by pressing **Ctrl + C**.

---

# API Endpoints

## Discovery

| Method | Endpoint | Description                                  |
| ------ | -------- | -------------------------------------------- |
| GET    | /api/v1  | Returns API metadata and available resources |

---

## Workspace Resource

| Method | Endpoint                         | Description                   |
| ------ | -------------------------------- | ----------------------------- |
| GET    | /api/v1/workspaces               | Retrieve all workspaces       |
| POST   | /api/v1/workspaces               | Create a new workspace        |
| GET    | /api/v1/workspaces/{workspaceId} | Retrieve a specific workspace |
| DELETE | /api/v1/workspaces/{workspaceId} | Delete a workspace            |

---

## Model Resource

| Method | Endpoint                       | Description                           |
| ------ | ------------------------------ | ------------------------------------- |
| GET    | /api/v1/models                 | Retrieve all models                   |
| GET    | /api/v1/models?status=TRAINING | Filter models by status               |
| POST   | /api/v1/models                 | Register a new machine learning model |

---

## Evaluation Metric Resource

| Method | Endpoint                         | Description                      |
| ------ | -------------------------------- | -------------------------------- |
| GET    | /api/v1/models/{modelId}/metrics | Retrieve evaluation history      |
| POST   | /api/v1/models/{modelId}/metrics | Register a new evaluation metric |

---

# Sample curl Commands

## 1. API Discovery

```bash
curl -X GET http://localhost:8080/api/v1 -i
```

---

## 2. Create Workspace

```bash
curl -X POST http://localhost:8080/api/v1/workspaces \
-H "Content-Type: application/json" \
-d '{"id":"WS-VISION-01","teamName":"Computer Vision Lab","storageQuotaGb":500}' -i
```

---

## 3. Register Model

```bash
curl -X POST http://localhost:8080/api/v1/models \
-H "Content-Type: application/json" \
-d '{"framework":"PyTorch","status":"TRAINING","workspaceId":"WS-VISION-01"}' -i
```

---

## 4. Trigger Dependency Validation Error

```bash
curl -X POST http://localhost:8080/api/v1/models \
-H "Content-Type: application/json" \
-d '{"framework":"Scikit-Learn","status":"TRAINING","workspaceId":"WS-INVALID-99"}' -i
```

---

## 5. Register Evaluation Metric

```bash
curl -X POST http://localhost:8080/api/v1/models/MOD-XXXX/metrics \
-H "Content-Type: application/json" \
-d '{"accuracyScore":0.942}' -i
```

---

# HTTP Status Codes

| Status Code | Description           |
| ----------- | --------------------- |
| 200         | OK                    |
| 201         | Created               |
| 204         | No Content            |
| 400         | Bad Request           |
| 403         | Forbidden             |
| 404         | Not Found             |
| 409         | Conflict              |
| 422         | Unprocessable Entity  |
| 500         | Internal Server Error |

---

# Exception Handling

The API uses custom ExceptionMapper implementations to ensure that meaningful JSON responses are returned instead of raw Java stack traces.

Implemented exception handlers include:

* WorkspaceNotEmptyException → HTTP 409 Conflict
* LinkedWorkspaceNotFoundException → HTTP 422 Unprocessable Entity
* ModelDeprecatedException → HTTP 403 Forbidden
* Global ExceptionMapper<Throwable> → HTTP 500 Internal Server Error

---

# Request and Response Logging

A custom logging filter implements both ContainerRequestFilter and ContainerResponseFilter.

The logger records:

* HTTP request method
* Request URI
* Final HTTP status code

This improves debugging and API observability.

---

# Part 1 – Setup & Discovery

## Question 1

### Explain the role of MessageBodyWriter or a JSON provider.

When a Java object (POJO) is returned from a JAX-RS resource method, the framework delegates serialization to a `MessageBodyWriter`. A JSON provider such as Jackson automatically converts Java objects into JSON by inspecting their fields and getter methods through reflection. This separates business logic from serialization logic, allowing developers to return Java objects without manually constructing JSON strings.

---

## Question 2

### Explain REST statelessness and why it improves scalability.

REST is stateless because the server does not store client session information between requests. Every request contains all information required for processing, including authentication and request parameters.

Statelessness also eliminates cross-node memory synchronization because servers do not share session state. As a result, requests can be distributed across multiple application instances using standard load balancers, making cloud-based systems easier to scale horizontally and improving availability.

---

# Part 2 – Workspace Management

## Question 1

### Explain the benefits of Cache-Control.

Adding `Cache-Control` headers allows browsers and intermediate caches to reuse responses without repeatedly contacting the server. This reduces unnecessary network traffic, lowers CPU usage, decreases server processing time and improves response performance for clients.

---

## Question 2

### Which HTTP method should be used instead of GET?

The appropriate HTTP method is **HEAD**.

HEAD performs the same validation as GET but returns only the response headers without the response body. This allows clients to verify whether a resource exists while conserving bandwidth and reducing unnecessary data transfer.

---

# Part 3 – Model Operations

## Question 1

### Why should the server generate IDs?

Allowing clients to supply resource identifiers introduces security and data integrity risks, including duplicate identifiers and Insecure Direct Object Reference (IDOR) attacks.

Generating identifiers on the server using `UUID.randomUUID()` ensures uniqueness, protects internal resources and prevents malicious manipulation.

Jakarta Bean Validation annotations such as `@NotNull`, `@Size` and `@Pattern` provide declarative validation at the model level, keeping business logic clean and maintainable. In contrast, manual `if-else` validation introduces repetitive boilerplate code, making resource classes more difficult to maintain as validation rules evolve.

---

## Question 2

### Why must URLs be encoded?

According to RFC 3986, characters such as spaces and ampersands (`&`) are reserved structural delimiters within URLs. If these characters are transmitted without encoding, the server parser interprets them as separators rather than part of the parameter value.

Clients must therefore apply percent-encoding (for example, converting spaces to `%20` and `&` to `%26`) so that query parameters are transmitted correctly and parsed without ambiguity.

---

# Part 4 – Deep Nesting

## Question

### Explain class-level and method-level @Produces.

Applying `@Produces(MediaType.APPLICATION_JSON)` at class level establishes JSON as the default response type for every resource method within the class.

When a specific method requires a different media type, it can define its own `@Produces` annotation. The JAX-RS runtime follows a **nearest-scope match**, meaning the method-level annotation overrides the class-level declaration for that particular endpoint.

---

# Part 5 – Error Handling & Logging

## Question 1

### Why should validation failures return a 4xx status instead of 5xx?

Validation failures occur because the client has submitted invalid data. Therefore, they must return a **4xx Client Error** rather than a **5xx Server Error**.

Returning a 5xx response for client-side validation problems is an architectural anti-pattern because it incorrectly signals a server failure, triggering unnecessary monitoring alerts and wasting engineering effort investigating healthy infrastructure.

---

## Question 2

### How does JAX-RS choose an Exception Mapper?

JAX-RS selects the ExceptionMapper by examining the exception inheritance hierarchy. The runtime calculates the inheritance distance between the thrown exception and each registered ExceptionMapper, selecting the mapper with the shortest inheritance path.

Consequently, a mapper registered for `LinkedWorkspaceNotFoundException` is selected instead of a generic `ExceptionMapper<Throwable>` because it provides a more specific match.

---

## Question 3

### What HTTP metadata is useful for debugging?

Two important pieces of HTTP metadata are:

* The Request URI, which identifies the resource being accessed.
* HTTP request headers, such as the Authorization header, which help identify the client and trace requests through the system.

These details are valuable for debugging, monitoring and auditing API activity.

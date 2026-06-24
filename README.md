# MLOps Pipeline Management API – JAX-RS Implementation

## Overview

This RESTful API manages Machine Learning workspaces and models. It is built with
**JAX-RS (Jersey)** and runs on an embedded Grizzly HTTP server. All data is stored
in-memory using `ConcurrentHashMap` (no external database). The API exposes
endpoints for workspaces, models, and evaluation metrics, with nested
sub-resources for metrics history. Full error handling is implemented via custom
exceptions and mappers, and a logging filter tracks all requests and responses.

## Build & Launch Instructions

1. **Prerequisites**: Java 11+ and Maven installed.
2. **Clone/Download** the project source code.
3. **Build**:

```bash
 mvn clean compile
```

4. Run the server:

```bash
mvn exec:java
```

The server starts at http://localhost:8080/api/v1.

5. Stop: Press any key in the terminal.
# Cronos Scheduler

A **robust, extensible job scheduling platform** built with **Spring Boot + Quartz**, designed to execute heterogeneous jobs such as HTTP calls, database operations, file system tasks, cache operations, messaging pipelines, and report generation.

This project demonstrates **real-world backend engineering concepts** including job orchestration, persistence, authentication, retry handling, execution logging, and Dockerized deployment.

---

## 🚀 Key Features

* 🔐 **Authentication & Authorization** (JWT-based login)
* ⏱️ **Quartz Scheduler Integration** with persistent jobs
* 🧩 **Pluggable Job Architecture** (Factory + Executor pattern)
* 📦 **Multiple Job Types Supported**
* 📝 **Execution Logs & Status Tracking**
* 🔁 **Retry & Failure Handling**
* 🐳 **Docker & Docker Compose Ready**
* 🗄️ **PostgreSQL-backed persistence**

---

## 🧠 Supported Job Types

| Job Type          | Description                                                                     |
| ----------------- | ------------------------------------------------------------------------------- |
| **HTTP**          | Executes REST API calls (GET/POST/PUT/DELETE) with headers, query params & body |
| **EMAIL**         | Sends emails via SMTP (implemented for learning flow)                           |
| **DATABASE**      | Executes SQL queries / DB operations                                            |
| **FILE_SYSTEM**   | Reads/writes files on disk                                                      |
| **CACHE**         | Cache put/get/evict operations                                                  |
| **MESSAGE_QUEUE** | Message publishing (extensible for Kafka/RabbitMQ)                              |
| **DB_TO_KAFKA**   | Streams DB data into Kafka topics                                               |
| **REPORT**        | Generates reports based on data sources                                         |
| **SCRIPT**        | Executes scripts or shell commands                                              |
| **DUMMY**         | No-op job for testing scheduler flow                                            |

---

## 🏗️ Architecture Overview

### 1️⃣ Job Creation Flow

```
Client (Postman)
   ↓
Auth Controller → JWT Token
   ↓
Job Controller (/jobs)
   ↓
JobService
   ↓
Quartz Scheduler
```

### 2️⃣ Job Execution Flow

```
Quartz Trigger
   ↓
QuartzJob
   ↓
JobExecutorFactory
   ↓
Concrete Job Executor (HTTP / DB / FILE ...)
   ↓
Execution Logs + Job Status Update
```

---

## 🧩 Design Patterns Used

* **Factory Pattern** → `JobExecutorFactory`
* **Strategy Pattern** → Each JobExecutor implementation
* **Template Method** → Common execution lifecycle
* **DTO Pattern** → Strongly typed payloads per job

---

## 📂 Project Structure

```
cronos-scheduler
│
├── controller
│   ├── AuthController
│   └── JobController
│
├── scheduler
│   ├── QuartzJob
│   ├── QuartzConfig
│   └── JobSchedulerService
│
├── executor
│   ├── JobExecutorFactory
│   ├── HttpJobExecutor
│   ├── DatabaseJobExecutor
│   ├── FileSystemJobExecutor
│   ├── CacheJobExecutor
│   └── ...
│
├── entity
│   ├── Job
│   ├── JobSchedule
│   └── ExecutionLog
│
├── dto
│   ├── JobRequestDto
│   ├── HttpJobPayload
│   └── ...
│
├── repository
│   └── JPA Repositories
│
├── security
│   ├── JwtFilter
│   ├── JwtUtil
│   └── SecurityConfig
│
├── docker
│   ├── Dockerfile
│   └── docker-compose.yml
│
└── README.md
```

---

## 🔐 Authentication API

### Login

```http
POST /auth/login
```

```json
{
  "username": "shreyash",
  "password": "password"
}
```

➡️ Returns **JWT token** (required for all job APIs)

---

## 🧪 Creating & Executing an HTTP Job

### Endpoint

```http
POST /jobs
Authorization: Bearer <JWT_TOKEN>
```

### Sample Payload

```json
{
  "jobName": "HTTP Test Job",
  "jobType": "HTTP",
  "description": "Test HTTP job execution",
  "priority": 5,
  "maxRetries": 3,
  "scheduledAt": "2026-01-18T21:30:00",
  "payload": {
    "url": "https://jsonplaceholder.typicode.com/posts",
    "method": "POST",
    "headers": {
      "Content-Type": "application/json"
    },
    "queryParams": {
      "source": "scheduler-test"
    },
    "body": {
      "title": "Cronos Scheduler",
      "body": "HTTP Job Test",
      "userId": 1
    },
    "timeoutMs": 5000
  }
}
```

✅ Job is stored, scheduled, executed, and logged

---

## 📊 Job Lifecycle States

* `CREATED`
* `SCHEDULED`
* `RUNNING`
* `COMPLETED`
* `FAILED`

Each transition is persisted and auditable.

---

## 📝 Execution Logs

Every job execution produces:

* Start time
* End time
* Duration
* Status
* Error message (if any)
* Thread info

Stored in `execution_logs` table.

---

## 🐳 Docker Setup

### Build & Run

```bash
docker compose up --build
```

Services:

* **Spring Boot App** → `localhost:8080`
* **PostgreSQL** → `5432`

---

## 🗄️ Database

* PostgreSQL
* Quartz tables auto-created
* Job & execution metadata persisted

---

## 🧪 Testing

* Manual testing via **Postman**
* End-to-end job execution validated
* Multiple executions handled safely

---

## 🎯 What This Project Demonstrates

* Real-world **scheduler design**
* Clean **extensible architecture**
* Deep understanding of **Quartz internals**
* Practical **Spring Boot backend skills**
* Production-like **logging, retries & persistence**

---

## 👤 Author

**Shreyash Ahuja**
Backend Java Developer
Focus: Spring Boot • Quartz • System Design • Scalable Systems

---

## ✅ Future Enhancements

* UI Dashboard
* Distributed Quartz clustering
* Advanced retry policies
* Cron expression builder
* Dead-letter queue

---

⭐ If you found this project valuable, feel free to star the repository.

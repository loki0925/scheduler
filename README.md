# Job Scheduler System (Spring Boot + Quartz)

## 📌 Overview

This project is a **Spring Boot–based Job Scheduler System** designed to schedule, execute, and monitor background jobs securely and reliably. It uses **Quartz Scheduler** for job orchestration, **JWT-based authentication** for security, and **persistent storage** for job metadata and execution logs. The system is built following **enterprise backend design principles** and is extensible for event-driven and microservices architectures.

---

## 🎯 Key Features

* Secure job management using **JWT authentication**
* Job scheduling using **Quartz (cron & simple triggers)**
* Persistent storage of jobs and schedules
* Detailed **execution logging** (success, failure, timestamps)
* Event publishing for job lifecycle events (Kafka-ready)
* Clean layered architecture (Controller → Service → Scheduler → DB)
* Extensible design for retries, monitoring, and clustering

---

## 🏗️ Tech Stack

* **Java 17**
* **Spring Boot**
* **Spring Security (JWT)**
* **Quartz Scheduler**
* **Spring Data JPA / Hibernate**
* **Relational Database (PostgreSQL / MySQL)**
* **Kafka (producer-ready)**
* **Maven**

---

## 📂 Project Structure

```
cronos.scheduler
│
├── controller        # REST APIs (Auth & Job management)
├── dto               # Request/Response DTOs
├── entity            # JPA entities (Job, Schedule, Logs)
├── repository        # Database repositories
├── scheduler         # Quartz Job Executor
├── service           # Business logic & scheduling logic
├── security          # JWT & Spring Security configuration
└── SchedulerApplication
```

---

## 🔐 Authentication Flow

1. User logs in using `/auth/login`
2. Server validates credentials and issues a **JWT token**
3. Client includes JWT in `Authorization: Bearer <token>` header
4. Requests are validated via `JwtAuthenticationFilter`

✔️ Stateless authentication
✔️ Scalable & secure

---

## ⏱️ Job Scheduling Flow

1. **Create Job API** is called with scheduling details
2. Job metadata is persisted in the database
3. Quartz `JobDetail` and `Trigger` are created
4. Quartz executes the job at scheduled time
5. Execution status is logged in `ExecutionLog`
6. Job lifecycle events are published (Kafka-ready)

---

## 🧾 Execution Logging

Each job execution is recorded with:

* Job ID
* Execution status (SUCCESS / FAILED)
* Start & end timestamps
* Error message (if any)

This enables **auditing, debugging, and monitoring**.

---

## 📣 Event Publishing

Job lifecycle events such as:

* JOB_STARTED
* JOB_COMPLETED
* JOB_FAILED

are published using a producer service. This enables:

* Asynchronous processing
* Integration with notification or monitoring systems
* Future Kafka consumer integration

---

## 🚀 How to Run the Project

1. Clone the repository
2. Configure database properties in `application.properties`
3. Run database migrations (if applicable)
4. Start the application:

   ```
   mvn spring-boot:run
   ```
5. Access APIs using Postman or Swagger

---

## 🔧 Configuration Highlights

* Quartz scheduler configuration
* JWT secret & expiration settings
* Database connection properties

---

## 📈 Future Enhancements

* Kafka consumers for event processing
* Retry & misfire handling using Quartz
* Pause / Resume / Cancel job APIs
* Quartz JDBC JobStore for clustering
* Spring Boot Actuator for monitoring
* Role-based access control (RBAC)
* Job execution metrics dashboard

---

## 💬 Interview Summary

> "This project is a secure job scheduling system built using Spring Boot and Quartz. It supports authenticated job creation, persistent scheduling, execution auditing, and event-driven extensibility using Kafka. The architecture follows enterprise backend best practices and is designed to scale."

---

## 👨‍💻 Author

**Shreyash Ahuja**
Java Backend Developer

---

## 📜 License

This project is for learning and demonstration purposes.


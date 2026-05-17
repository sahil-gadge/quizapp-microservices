# Quiz Application - Microservices Architecture

## 📌 Description
This project is a **Microservices-based Quiz Application** built using Spring Boot and Spring Cloud.

It is a refactored version of the monolithic quiz application.

---

## 🏗️ Architecture

Microservices involved:

- **Service Registry (Eureka Server)**
- **API Gateway**
- **Quiz Service**
- **Question Service**

---

## 🔁 Flow

Client → API Gateway → Eureka → Microservices

---

## ⚙️ Features

- Service Discovery (Eureka)
- API Gateway Routing
- Feign Client Communication
- Load Balancing (Spring Cloud LoadBalancer)
- Circuit Breaker (Resilience4j)
- Actuator Monitoring

---

## 🛠️ Tech Stack

- Java 21
- Spring Boot
- Spring Cloud
- Eureka Server
- OpenFeign
- Resilience4j
- API Gateway
- PostgreSQL
- Maven

---

## ▶️ How to Run

1. Start **Service Registry**
2. Start **Question Service**
3. Start **Quiz Service**
4. Start **API Gateway**

---

## 📡 Endpoints

Through Gateway:

- `/quiz-service/**`
- `/question-service/**`

---

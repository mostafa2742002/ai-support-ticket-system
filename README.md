# AI Support Ticket System

[![Java](https://img.shields.io/badge/Java-21-orange?logo=java)](https://www.oracle.com/java/technologies/downloads/#java21)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?logo=spring)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15+-336791?logo=postgresql)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/License-MIT-green)](LICENSE)

An AI-powered support ticket routing system built with **Spring Boot**, **Spring AI**, **Ollama**, **PostgreSQL**, and **JWT Security**.

The system automatically **classifies** incoming support tickets, **prioritizes** them, **routes** them to the correct team, **assigns** them to active support agents, and now also helps customers ask about their ticket status through a dedicated public assistant.

---

## 🎯 Why This Project?

Many support systems still rely on manual triage:

- ❌ Humans read every ticket first
- ❌ Routing decisions are slow and inconsistent
- ❌ Urgent issues may wait in queues
- ❌ Wrong team assignment increases resolution time

**This project solves it** by using AI-driven triage and automated routing.

---

## ✨ Core Features

### 👥 Public Customer Flow

- ✅ Create support ticket (no authentication required)
- ✅ Automatic AI triage after submission
- ✅ AI generates:
  - Category classification
  - Priority level
  - Assigned team
  - AI-generated summary
- ✅ Automatic assignment to available support agent
- ✅ Ask questions about an existing ticket using ticket ID + customer email

### 🔐 Internal Support Flow

Protected by JWT authentication.

- ✅ Login for support users (Agent / Admin)
- ✅ View all tickets with filters
- ✅ View ticket details with full history
- ✅ Update ticket status
- ✅ Reassign team or agent manually
- ✅ Add comments and notes
- ✅ View ticket audit trail
- ✅ Re-run AI triage manually
- ✅ Role-based access for ticket management, comments, and admin-only AI triage

---

## 📋 Example Flow

### Incoming Customer Ticket

```json
{
  "title": "Charged twice",
  "description": "My card was charged twice for premium plan.",
  "customerName": "Ahmed Mostafa",
  "customerEmail": "ahmed@email.com"
}
```

### AI Processing Output

```json
{
  "category": "BILLING",
  "priority": "HIGH",
  "assignedTeam": "BILLING",
  "aiSummary": "Duplicate billing issue for premium subscription."
}
```

### Final Routed Ticket

```json
{
  "assignedTeam": "BILLING",
  "assignedAgent": "Sara"
}
```

---

## 📸 Screenshots

The screenshots below are loaded from [docs/screenshots](docs/screenshots).

### Customer and Ticket Flow

![Customer asks about ticket status](docs/screenshots/customer%20ask%20about%20his%20ticket.png)

![Ticket after AI triage](docs/screenshots/ticket%20after%20ai%20triage.png)

### Docs and Monitoring

![Swagger documentation](docs/screenshots/swagger%20doc.png)

![Swagger documentation, second view](docs/screenshots/swagger%20doc%201.png)

![Prometheus query](docs/screenshots/promentheus%20query.png)

![Grafana charts](docs/screenshots/gravana%20charts.png)

![Health check endpoint](docs/screenshots/health%20check%20end%20point%20.png)

---

## 🔎 Current API Surface

The main endpoints currently exposed by the application are:

| Endpoint | Access | Purpose |
|----------|--------|---------|
| `POST /api/tickets` | Public | Create a new support ticket and trigger AI triage |
| `POST /api/customer/tickets/ask` | Public | Ask about ticket status and progress using ticket ID + customer email |
| `POST /api/auth/login` | Public | Authenticate internal support users and receive a JWT |
| `GET /api/tickets` | Agent/Admin | List tickets |
| `GET /api/tickets/{id}` | Agent/Admin | View a ticket in detail |
| `PATCH /api/tickets/{id}/status` | Agent/Admin | Update ticket status |
| `PATCH /api/tickets/{id}/assign-team` | Admin | Reassign the ticket team |
| `PATCH /api/tickets/{id}/assign-agent` | Admin | Reassign the ticket agent |
| `GET /api/tickets/{ticketId}/comments` | Agent/Admin | Read ticket comments |
| `POST /api/tickets/{ticketId}/comments` | Agent/Admin | Add a comment to a ticket |
| `POST /api/ai/tickets/{ticketId}/triage` | Admin | Manually re-run AI triage |

---

## 🏗️ Architecture

The project follows a **clean layered architecture** for maintainability and testability.

### Layers

| Layer | Purpose |
|-------|---------|
| **domain** | Business entities, enums, and core logic |
| **application** | Use cases and business services |
| **infrastructure** | Repositories and persistence |
| **api** | REST controllers and DTOs |
| **ai** | AI triage services and tool calling |
| **security** | JWT authentication and authorization |

### AI Capabilities

#### Structured Output

The AI doesn't return random text—it returns **structured data** mapped directly into Java objects:

- `category` - Classification (BILLING, TECHNICAL, ACCOUNT, etc.)
- `priority` - Level (LOW, MEDIUM, HIGH, CRITICAL)
- `assignedTeam` - Team routing
- `aiSummary` - Concise ticket summary

#### Tool Calling

The AI can invoke backend tools dynamically:

- 🔍 Find similar tickets (for pattern detection)
- 💬 Inspect ticket comments
- 👤 Check active support agents
- 📊 Query ticket history

---

## 🔒 Security

**JWT-based authentication** protects internal support APIs.

| Endpoint | Auth Required | Purpose |
|----------|---|---------|
| `POST /api/tickets` | ❌ No | Create ticket (public) |
| `POST /api/customer/tickets/ask` | ❌ No | Customer ticket assistant |
| `/api/tickets/**` | ✅ Yes | Ticket management and comments (internal) |
| `/api/ai/**` | ✅ Yes | AI operations (admin-only triage endpoint) |
| `/api/auth/login` | ❌ No | User login |

---

## 🛠️ Tech Stack

### Backend
- **Java 21**
- **Spring Boot** 3.x
- **Spring Web** - REST APIs
- **Spring Data JPA** - Database access
- **Spring Security** - Authentication
- **Spring AI** - AI integration
- **Spring Boot Actuator** - Health and metrics endpoints

### Database
- **PostgreSQL** 15+
- **Flyway** - Database migrations

### AI & LLM
- **Ollama** - Local LLM runtime
- **Llama 3.1** (8B) - Language model

### Testing
- **JUnit 5** - Unit testing
- **Mockito** - Mocking
- **MockMvc** - Integration testing
- **Spring Boot Test** - Test utilities

### Documentation
- **Swagger UI** - Interactive API docs

---

## 📖 Quick Start

### Prerequisites

- Java 21+
- Docker & Docker Compose
- PostgreSQL (via Docker)
- Ollama
- Maven

### 1️⃣ Clone Project

```bash
git clone https://github.com/mostafa2742002/ai-support-ticket-system.git
cd ai-support-ticket-system
```

### 2️⃣ Start PostgreSQL

```bash
docker compose up -d
```

Or manually start PostgreSQL on `localhost:5432`

### 3️⃣ Start Ollama & Pull Model

```bash
# Terminal 1: Start Ollama server
ollama serve

# Terminal 2: Pull Llama 3.1 (8B recommended)
ollama pull llama3.1:8b
```

### 4️⃣ Run Application

```bash
./mvnw spring-boot:run
```

The application starts on `http://localhost:8081`

---

## 🔓 Default Test Credentials

### Agent User
```
Username: agent
Password: agent123
```

### Admin User
```
Username: admin
Password: admin123
```

### Example Login

```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "agent", "password": "agent123"}'
```

---

## 📚 API Documentation

**Swagger/OpenAPI** documentation available at:

👉 [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)

Interactive API testing directly in your browser.

---

## 🧪 Running Tests

```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=TicketServiceTest

# Run with coverage
./mvnw test jacoco:report
```

---

## 🚀 Future Improvements

- [ ] **Async Processing** - Kafka/event-driven AI triage
- [ ] **Advanced Routing** - Round-robin, least-busy assignment strategies
- [ ] **SLA Management** - SLA-aware prioritization and escalation
- [ ] **Notifications** - Email alerts for agents and customers
- [ ] **Dashboard** - Real-time metrics and analytics
- [ ] **Observability** - Prometheus + Grafana monitoring
- [ ] **RBAC** - Role-based access control from database
- [ ] **Token Refresh** - Refresh token mechanism
- [ ] **Offline Support** - Queue tickets during AI downtime
- [ ] **Multi-language** - Support ticket translation

---

## 💡 What This Project Demonstrates

✅ Clean layered architecture in Spring Boot  
✅ Real-world REST API design  
✅ AI integration in business workflows  
✅ Public customer assistant for ticket status questions  
✅ Secure JWT authentication  
✅ Role-based authorization for internal actions  
✅ Structured database design  
✅ Repository pattern for data access  
✅ Spring AI tool calling  
✅ Exception handling & validation  
✅ Unit and integration testing  
✅ Database migrations with Flyway  

---

## 📁 Project Structure

```
ai-support-ticket-system/
├── src/main/java/com/mostafa/aisupport/
│   ├── agent/              # Agent domain logic
│   ├── ai/                 # AI services & tools
│   ├── comment/            # Comments domain
│   ├── ticket/             # Tickets domain (core)
│   ├── common/             # Shared utilities
│   │   ├── auth/           # JWT authentication
│   │   ├── config/         # Spring configuration
│   │   ├── exception/      # Exception handling
│   │   └── security/       # Security filters
│   └── AiSupportTicketSystemApplication.java
├── src/main/resources/
│   ├── application.yaml    # Application config
│   └── db/migration/       # Flyway migrations
├── docs/
│   └── screenshots/        # Drop UI screenshots here
├── src/test/               # Unit & integration tests
├── pom.xml                 # Maven configuration
└── README.md
```

---

## 🤝 Contributing

Contributions are welcome! Please:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## 📞 Author

**Mostafa Mahmoud**  
Junior Java Backend Developer

- 🐙 GitHub: [mostafa2742002](https://github.com/mostafa2742002)
- 💼 LinkedIn: [Mostafa Mahmoud Ismail](https://www.linkedin.com/in/mostafa-mahmoud-ismail/)

---

## 📜 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🙏 Acknowledgments

- [Spring AI Documentation](https://docs.spring.io/spring-ai/reference/)
- [Ollama](https://ollama.ai/)
- [Spring Boot Best Practices](https://spring.io/blog)
- Open source community

---

**Made with ❤️ by Mostafa Mahmoud**

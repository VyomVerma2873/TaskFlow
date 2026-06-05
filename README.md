# AI-Powered Task Management Portal — TaskFlow

A full-stack task management application with AI-powered task description generation, JWT authentication, and blockchain-based audit trail.

## Tech Stack

| Layer      | Technology                                      |
|------------|-------------------------------------------------|
| Backend    | Java 17, Spring Boot 3.3, Spring Security, JPA  |
| Frontend   | React 18, Vite, Tailwind CSS, React Router      |
| Database   | PostgreSQL (Supabase)                           |
| Auth       | JWT (jjwt 0.12.x), BCrypt password hashing      |
| AI         | Google Gemini 1.5 Flash API                     |
| Blockchain | Mock SHA-256 ledger (DB-backed)                 |
| API Docs   | Springdoc OpenAPI (Swagger UI)                  |

## Features

- **User Authentication**: Register, login with JWT tokens, protected routes
- **Task Management**: Create, edit, delete tasks with priority and due dates
- **Task Status Tracking**: TODO, IN_PROGRESS, DONE with inline status updates
- **AI Task Generator**: Enter a title and AI generates description, priority, and estimated time
- **Blockchain Audit Trail**: Every task event is recorded in an immutable hash chain
- **Responsive UI**: Mobile-first design with Tailwind CSS
- **Filtering**: Filter tasks by status

## Project Structure

```
├── backend/                      # Spring Boot REST API
│   └── src/main/java/com/taskmanager/
│       ├── config/               # SecurityConfig, CorsConfig
│       ├── controller/           # AuthController, TaskController, AiController, BlockchainController
│       ├── dto/                  # Request/Response DTOs with validation
│       ├── entity/               # JPA entities (User, Task, BlockchainBlock)
│       ├── exception/            # GlobalExceptionHandler + custom exceptions
│       ├── repository/           # Spring Data JPA repositories
│       ├── security/             # JwtUtils, JwtAuthenticationFilter, CustomUserDetailsService
│       └── service/              # AuthService, TaskService, AiService, BlockchainService
├── frontend/                     # React + Vite + Tailwind CSS
│   └── src/
│       ├── components/           # Navbar, TaskCard, TaskFormModal, ProtectedRoute
│       ├── context/              # AuthContext (JWT state management)
│       ├── pages/                # LoginPage, RegisterPage, DashboardPage, BlockchainPage
│       └── services/             # Axios API service with interceptors
└── README.md
```

## Setup Instructions

### Prerequisites

- Java 17+
- Maven 3.8+
- Node.js 18+
- PostgreSQL (or Supabase connection string)
- Google Gemini API key ([get one free](https://aistudio.google.com/app/apikey))

### Backend Setup

```bash
cd backend

# Set environment variables
export DATABASE_URL="jdbc:postgresql://<host>:<port>/<dbname>"
export DATABASE_USERNAME="postgres"
export DATABASE_PASSWORD="your-password"
export JWT_SECRET="your-256-bit-secret-key-at-least-32-chars"
export GEMINI_API_KEY="your-gemini-api-key"

# Build and run
./mvnw spring-boot:run
```

Backend runs on `http://localhost:8080`.

Swagger UI available at: `http://localhost:8080/swagger-ui.html`

### Frontend Setup

```bash
cd frontend

# Install dependencies
npm install

# Start dev server (proxies /api to backend)
npm run dev
```

Frontend runs on `http://localhost:5173`.

### Using Supabase

1. Create a free account at [supabase.com](https://supabase.com)
2. Create a new project and get the connection string from Settings > Database
3. Use the **Transaction** connection string for `DATABASE_URL`
4. Set `DATABASE_USERNAME` to `postgres` and `DATABASE_PASSWORD` to your database password

## Environment Variables

| Variable           | Description                          | Example                                       |
|--------------------|--------------------------------------|-----------------------------------------------|
| `DATABASE_URL`     | PostgreSQL JDBC connection string    | `jdbc:postgresql://db.xxx.supabase.co:5432/postgres` |
| `DATABASE_USERNAME`| Database username                    | `postgres`                                    |
| `DATABASE_PASSWORD`| Database password                    | `your-password`                               |
| `JWT_SECRET`       | Secret key for JWT signing (32+ chars)| `my-super-secret-key-for-jwt-at-least-32`   |
| `GEMINI_API_KEY`   | Google Gemini API key                | `AIzaSy...`                                   |
| `PORT`             | Backend server port (optional)       | `8080`                                        |

## API Endpoints

### Authentication (Public)
| Method | Endpoint               | Description        |
|--------|------------------------|--------------------|
| POST   | `/api/auth/register`   | Register new user  |
| POST   | `/api/auth/login`      | Login and get JWT  |

### Tasks (Authenticated)
| Method | Endpoint                    | Description              |
|--------|-----------------------------|--------------------------|
| POST   | `/api/tasks`                | Create a new task        |
| GET    | `/api/tasks`                | Get all user tasks       |
| GET    | `/api/tasks/{id}`           | Get single task          |
| PUT    | `/api/tasks/{id}`           | Update task              |
| DELETE | `/api/tasks/{id}`           | Delete task              |
| PATCH  | `/api/tasks/{id}/status`    | Update task status       |

### AI (Authenticated)
| Method | Endpoint                  | Description                    |
|--------|---------------------------|--------------------------------|
| POST   | `/api/ai/generate-task`   | Generate task details from title |

### Blockchain (Authenticated)
| Method | Endpoint                | Description                |
|--------|-------------------------|----------------------------|
| GET    | `/api/blockchain/chain`  | View full blockchain       |
| GET    | `/api/blockchain/validate` | Validate chain integrity |

## AI Integration

The application uses **Google Gemini 1.5 Flash** to generate task details from a title.

### How It Works
1. User enters a task title in the create task modal
2. Clicks the "AI" button
3. Frontend sends the title to `/api/ai/generate-task`
4. Backend constructs a prompt and calls the Gemini API
5. AI returns a structured JSON with description, priority, and estimated time
6. Frontend auto-fills the form fields

### Graceful Fallback
If the Gemini API is unavailable or returns an error, the backend returns a template-based fallback response so the user can still create tasks.

## Blockchain Implementation

A **mock blockchain ledger** is implemented to demonstrate the concept of immutable audit trails.

### How It Works
- Each task event (create, update, delete, status change) creates a new block
- Each block contains: index, timestamp, previous hash, data (JSON event), SHA-256 hash
- A genesis block is created when the chain is empty
- The chain is stored in PostgreSQL and can be validated via `/api/blockchain/validate`

### Why Mock?
This is a simulated blockchain (no distributed consensus or mining). It demonstrates the core concepts of hash chaining and tamper detection without the complexity of a real distributed ledger.

## Database Schema

```
┌─────────────────┐       ┌─────────────────────────┐
│     users        │       │         tasks            │
├─────────────────┤       ├─────────────────────────┤
│ id (UUID, PK)   │◄──────│ user_id (UUID, FK)      │
│ username        │       │ id (UUID, PK)           │
│ email (unique)  │       │ title                   │
│ password (hash) │       │ description             │
│ created_at      │       │ priority (LOW/MED/HIGH) │
└─────────────────┘       │ due_date                │
                          │ status (TODO/IN_PROG/..) │
                          │ created_at              │
                          │ updated_at              │
                          └─────────────────────────┘

┌─────────────────────────────┐
│     blockchain_ledger        │
├─────────────────────────────┤
│ id (UUID, PK)               │
│ index (unique, sequential)  │
│ timestamp                   │
│ previous_hash               │
│ hash (SHA-256)              │
│ data (JSON event payload)   │
│ task_id (UUID, nullable)    │
└─────────────────────────────┘
```

## Architecture Overview

```
┌──────────────┐     ┌──────────────────────────────────────────┐
│   React UI   │────►│         Spring Boot REST API             │
│  (Vite Dev)  │     │  ┌─────────┐  ┌────────┐  ┌──────────┐  │
│              │     │  │Controller│─►│ Service │─►│Repository│  │
│  Axios HTTP  │     │  └─────────┘  └────────┘  └──────────┘  │
│  JWT in LS   │     │       │            │             │       │
└──────────────┘     │  ┌────┴─────┐ ┌────┴────┐  ┌────┴─────┐ │
                     │  │JWT Filter│ │AI Service│  │PostgreSQL │ │
                     │  └──────────┘ └─────────┘  └──────────┘ │
                     │       │            │                     │
                     │  ┌────┴──────┐ ┌───┴─────────┐          │
                     │  │  BCrypt   │ │ Gemini API  │          │
                     │  └───────────┘ └─────────────┘          │
                     └──────────────────────────────────────────┘
```

## Screenshots

*Screenshots will be added after deployment.*

## Deployment

### Frontend (Vercel)
```bash
cd frontend
npm run build
# Deploy the dist/ folder to Vercel
```

### Backend (Render)
- Connect your GitHub repo to Render
- Set build command: `cd backend && ./mvnw clean package -DskipTests`
- Set start command: `java -jar backend/target/task-manager-backend-1.0.0.jar`
- Add environment variables in Render dashboard

### Docker (Alternative)
See Docker setup files in the project root.

## Assumptions

1. Users have a single role (no RBAC implemented)
2. Tasks belong to a single user (no sharing/collaboration)
3. AI generation is a best-effort feature with graceful fallback
4. Blockchain is a demonstration mock, not a production distributed system
5. JWT tokens expire after 24 hours

## License

This project was created as a take-home assignment for a Java Full Stack Developer Intern position.

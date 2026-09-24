# Inkwell — Blogging Platform (Backend)

A production-style Spring Boot REST API powering **Inkwell**, a full-stack blogging platform that serves both registered users and guests, with secure authentication, cloud image storage, and full API documentation.

**Live frontend:** [blogapp158.netlify.app](https://blogapp158.netlify.app)
**Frontend repo:** https://github.com/Unavailable-Cat/BlogApp-Frontend

---

## Overview

The backend exposes a stateless REST API for user authentication, profile management, and blog post CRUD. Guests can browse public posts and profiles without an account; writing, editing, and deleting posts requires authentication.

## Tech Stack

- **Java** + **Spring Boot**
- **Spring Security** — stateless authentication with JWT
- **Google OAuth2** — social login, in addition to email/password
- **Role-Based Access Control (RBAC)** — restricted guest access vs. authenticated user privileges
- **MySQL** — relational persistence
- **Cloudinary** — cloud storage for post cover images, with server-side upload validation
- **Bean Validation (Jakarta Validation)** — request payload validation
- **Springdoc OpenAPI (Swagger UI)** — auto-generated, interactive API documentation

## Key Features

- **Stateless JWT authentication** — no server-side session state; every request is authenticated via a signed `Bearer` token.
- **Google OAuth2 login** alongside standard email/password registration and login, both issuing the same JWT format so the frontend treats them identically.
- **Role-based access control** — guest (unauthenticated) users get read-only access to public posts and profiles; only the authenticated author of a post can edit or delete it.
- **Cloudinary image pipeline** — post cover images are uploaded to Cloudinary with server-side validation (file type/size) before the post is persisted, avoiding untrusted files ever touching disk.
- **Bean Validation** on all incoming DTOs (registration, login, post creation/update) to reject malformed requests before they reach business logic.
- **Springdoc OpenAPI / Swagger UI** for live, browsable API documentation, kept in sync with the actual controllers and DTOs.
- **MySQL** persistence via Spring Data JPA, with relations between users and their posts.

## API Summary

| Method | Path                     | Auth required | Purpose                              |
|--------|--------------------------|:--------------:|----------------------------------------|
| POST   | `/register`              | No             | Create account, returns JWT           |
| POST   | `/login`                 | No             | Authenticate, returns JWT              |
| GET    | `/oauth2/authorization/google` | No       | Kick off Google OAuth2 login          |
| GET    | `/user/my`                | Yes            | Current user's profile                |
| GET    | `/user/username`         | No             | Look up a user by username             |
| PATCH  | `/user/username`         | Yes            | Update own username                    |
| PATCH  | `/user/description`      | Yes            | Update own bio                         |
| DELETE | `/user`                   | Yes            | Delete own account                     |
| GET    | `/blog`                  | No             | List all posts                         |
| GET    | `/blog/id/{id}`          | No             | Single post detail                     |
| GET    | `/blog/username/{u}`     | No             | Posts by a given user                  |
| GET    | `/blog/my`               | Yes            | Current user's posts                   |
| POST   | `/blog`                  | Yes            | Create post (multipart, with image)    |
| PUT    | `/blog/{id}`             | Yes (owner)    | Full update (title, content, image)    |
| PATCH  | `/blog/title/{id}`       | Yes (owner)    | Update title only                      |
| PATCH  | `/blog/content/{id}`     | Yes (owner)    | Update content only                    |
| PATCH  | `/blog/image/{id}`       | Yes (owner)    | Update cover image only                |
| DELETE | `/blog/{id}`             | Yes (owner)    | Delete post                            |

Full interactive documentation is available via Swagger UI once the app is running (see below).

## Getting Started

### Prerequisites

- Java 17+ (adjust to your actual JDK version)
- Maven (or Gradle, depending on your build tool)
- A running MySQL instance
- A Cloudinary account (cloud name, API key, API secret)
- A Google Cloud OAuth2 client (client ID + secret) for social login

### Configuration

Set the following in `application.properties` / `application.yml` (or as environment variables):

```
spring.datasource.url=jdbc:mysql://localhost:3306/inkwell
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password

jwt.secret=your_jwt_signing_secret
jwt.expiration-ms=3600000   # 1 hour

spring.security.oauth2.client.registration.google.client-id=your_google_client_id
spring.security.oauth2.client.registration.google.client-secret=your_google_client_secret

cloudinary.cloud-name=your_cloud_name
cloudinary.api-key=your_api_key
cloudinary.api-secret=your_api_secret

app.frontend-url=http://localhost:5173   # used for OAuth2 redirect back to the SPA
```

### Run

```bash
mvn spring-boot:run
```

The API starts on `http://localhost:8080` by default.

### API Docs

Once running, Swagger UI is available at:

```
http://localhost:8080/swagger-ui.html
```

## Security Notes

- Passwords are hashed (e.g. BCrypt) before storage — never stored in plain text.
- JWTs are stateless and signed; the frontend sends them as `Authorization: Bearer <token>` on every authenticated request.
- Token lifetime is currently set to 1 hour; on expiry, the API returns `401`, which the frontend uses to sign the user out gracefully.
- Guest (unauthenticated) access is intentionally restricted to read-only endpoints — write operations are enforced server-side via RBAC, not just hidden in the UI.

## Deployment

Hosted on Render

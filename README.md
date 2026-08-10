# Fitness Memberships

This repository is a Maven-managed monorepo with two independently understandable modules:

- `backend`: Java 25, Jakarta EE 11, Open Liberty, PostgreSQL and Flyway.
- `frontend`: Angular 22 standalone admin UI for registering and managing memberships.

## Verify everything

```bash
./mvnw verify
```

The Maven frontend module installs its pinned Node.js and npm versions, runs the Angular tests, and creates a production build. To work on the UI with hot reload:

```bash
cd frontend
npm start
```

The Angular development server proxies `/api` to the backend at `http://localhost:9080`.

## Run locally

```bash
docker compose up --build
```

Open the admin UI at <http://localhost:4200>. The backend API remains available at <http://localhost:9080/api>.

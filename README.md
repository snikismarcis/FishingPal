# FishingPal

FishingPal is a full-stack app for fishing conditions, trip logs, and community sharing.
This repository contains the backend API, React frontend, and project documentation.

## Live Demo

`https://fishing-pal.vercel.app`

## Stack Snapshot

- Backend: Spring Boot 4, Java 21, Spring Security (JWT), Spring Data JPA
- Database: PostgreSQL
- Frontend: React + TypeScript
- Data integrations: Open-Meteo weather provider

## Core Functional Areas

- Auth and session token flow (`/api/auth/*`)
- Conditions scoring (`/conditions`) and species list (`/species`)
- Catch logs and likes (`/api/logs`)
- Community image posts (`/api/community`)
- Health endpoint (`/health`)

## What’s Next

The next steps focus on improving performance and scalability through small, measurable changes:

- **Scaling APIs:** Optimize high-traffic endpoints by reducing payload size, and minimizing repeated database queries.
- **Load balancing:** Run multiple backend instances behind a load balancer with health checks to ensure stable traffic distribution. I also plan to simulate higher traffic to better understand system behavior under load.
- **Caching strategy:** Cache responses from the Open-Meteo API to avoid repeated external requests and reduce latency.

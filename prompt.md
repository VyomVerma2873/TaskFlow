# Java Full Stack Developer Intern — Take-Home Assignment

Role

Java Full Stack Developer Intern

Assignment Title

AI-Powered Task Management Portal

## Objective

Build a small full-stack web application where users can create, manage, and track tasks.

The application should demonstrate:

● Backend API development using Java

● Frontend UI development

● Database integration

● Authentication basics

● AI-powered automation feature

● Clean architecture and deployment readiness

The assignment is intentionally scoped to be completed within 1–2 days.

## Problem Statement

Develop a lightweight task management application that allows users to:

● Register/Login

● Create and manage tasks

● Track task status

● Use AI automation to assist task creation or summarization

● Optionally integrate a blockchain layer for task verification/history

# Tech Stack Requirements

## Backend (Mandatory)

● Spring Boot

### Backend Requirements

● REST APIs

● Layered architecture

● Exception handling

● Input validation

● JWT Authentication

● Database integration

## Frontend (Mandatory)

● React + Vite +Tailwind CSS

### Frontend Requirements

● Responsive UI

● API integration

● Form validation

● Authentication flow

● State management basics

## Database

Choose one:

● PostgreSQL

● MySQL

# Core Functional Requirements

## 1. Authentication Module

### Features

● User Registration

● User Login

● JWT Token generation

● Protected APIs

## 2. Task Management Module

### Features

Users should be able to:

● Create tasks

● Edit tasks

● Delete tasks

● Mark tasks as:

○ TODO

○ IN_PROGRESS

○ DONE

● View all tasks

### Task Fields

● Title

● Description

● Priority

● Due Date

● Status

● Created Timestamp

# AI Automation Requirement (Mandatory)

Integrate one AI-powered automation feature.

## Option A — AI Task Description Generator

User enters:

● Task title

AI generates:

● Task description

● Suggested priority

● Estimated completion effort

Example:

Input:

“Prepare client presentation”

AI Output:

● Description

● Priority: HIGH

● Estimated Time: 4 hours

## Option B — AI Task Summarizer

Generate:

● Daily productivity summary

● Pending task insights

## Option C — AI Smart Suggestions

Suggest:

● Task priorities

● Duplicate task detection

● Due date risk alerts

# AI Integration Suggestions

You may use:

● OpenAI API

● Google Gemini API

● Hugging Face Inference API

Expected

● Clean prompt handling

● API integration

● Graceful fallback if AI service fails

# Bonus Requirement (Optional)

## Blockchain Integration (Bonus Points)

Implement a lightweight blockchain feature.

### Suggested Ideas

#### Option A — Immutable Task History

Store task update hashes on blockchain/mock ledger.

Example:

● Task created

● Task status updated

● Hash stored for audit trail

#### Option B — Wallet Connection

Simple wallet integration using:

● MetaMask

● Ethereum testnet

#### Option C — Smart Contract Logging

Store task completion records on blockchain.

### Bonus Tech Suggestions

● Hardhat

● Ganache

● Solidity

# Non-Functional Requirements

## Code Quality

● Clean code structure

● Proper naming conventions

● Reusable components

● Comments where necessary

## Security

● Password hashing

● JWT validation

● Environment variable usage

## UI/UX

● Simple and clean design

● Mobile responsive preferred

# Deliverables

## Mandatory Deliverables

### 1. Source Code

Upload project to:

● GitHub

### 2. README File

Include:

● Setup instructions

● Tech stack used

● Architecture overview

● API endpoints

● AI integration explanation

● Screenshots

### 3. Demo Video (3–5 mins)

Explain:

● Application flow

● AI feature

● Architecture

● Challenges faced

Upload via:

● Google Drive

### 4. Database Schema

Provide:

● ER diagram or schema screenshot

# Evaluation Criteria

| Criteria | Weightage |
|-----------|-----------|
| Backend Architecture | 25% |
| Frontend UI & Functionality | 20% |
| AI Automation Feature | 20% |
| Code Quality | 15% |
| Database Design | 10% |
| Bonus Blockchain Integration | 10% |

# Time Expectation

Estimated Completion Time:

● 1–2 days

# Optional Enhancements

Candidates may additionally implement to be considered for the application:

● Docker setup

● Role-based access

● Pagination

● Search/filter

● Unit testing

● Swagger/OpenAPI docs

# Submission Format

Please submit:

1. GitHub Repository Link

2. Demo Video Link

3. Deployment Link (if hosted)

4. Short document explaining:

○ Assumptions

○ AI workflow

○ Blockchain implementation (if attempted)

# Deployment Suggestions

## Frontend

● Vercel

● Netlify

## Backend

● Render

● Railway

## Database

● Supabase

● Neon

● Render
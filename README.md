# Tuul Backend Service

## Table of Contents

- [Overview](#overview)
- [Features](#features)
  - [Vehicle Management](#vehicle-management)
  - [User Management](#user-management)
  - [Reservation System](#reservation-system)
  - [Security Features](#security-features)
  - [System Integration](#system-integration)
- [Prerequisites](#prerequisites)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [Security](#security)
- [API Documentation](#api-documentation)
- [Contributor](#contributor)

## Overview

This service provides REST APIs for managing vehicles, users, and reservations in a vehicle sharing system. It handles vehicle pairing, ride management, and real-time vehicle status updates.

## Features

### Vehicle Management

- **Vehicle Registration**
  - Create new vehicles with unique codes
  - Set initial state of charge
  - Configure default status settings
- **Location Tracking**
  - Real-time GPS coordinates
  - Location history
  - Route tracking
- **Vehicle Status**
  - Battery level monitoring
  - Power state (on/off)
  - Lock status
  - Availability status
- **Pairing System**
  - Secure user-vehicle pairing
  - Automatic unpair on ride completion
  - Conflict prevention for multiple user attempts

### User Management

- **Authentication**
  - Firebase authentication integration
  - Token-based security
  - Role-based access control
- **User Profiles**
  - User registration and management
  - Ride history tracking
  - Active vehicle associations
- **Session Management**
  - Secure login/logout
  - Token validation
  - Session persistence

### Reservation System

- **Ride Management**
  - Start/end ride tracking
  - Duration calculation
  - Distance monitoring
- **Cost Calculation**
  - Time-based pricing
  - Dynamic rate adjustment
  - Final cost computation
- **History Tracking**
  - Complete ride history
  - Cost breakdown
  - Route visualization

### Security Features

- **Firebase Integration**
  - Secure token verification
  - User authentication
  - Role-based permissions
- **API Security**
  - CORS configuration
  - Request validation
  - Error handling
- **Data Protection**
  - Encrypted communications
  - Secure data storage
  - Privacy compliance

### System Integration

- **MongoDB Integration**
  - Scalable data storage
  - Efficient querying
  - Data persistence
- **RESTful API**
  - Standardized endpoints
  - JSON response format
  - Error handling
- **Logging System**
  - Activity tracking
  - Error logging
  - Audit trail

## Prerequisites

- Java 11 or higher
- Maven
- MongoDB
- Firebase account for authentication

## Configuration

Create an `application.properties` file in `src/main/resources` with:

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/tuul
server.port=8080
```

## Running the Application

1. Clone the repository

```bash
git clone https://github.com/yourusername/tuul-back.git
```

2. Move inside folder

```bash
cd tuul-back
```

3. Build the project

```bash
mvn clean install
```

3. Run the application

```bash
mvn spring-boot:run
```

## Security

- Authentication is handled via Firebase
- Each API endpoint requires a valid Firebase token
- Vehicle operations are restricted to paired users

## API Documentation

After starting the application, you can access the API documentation in three ways:

### Swagger UI

Access the interactive API documentation interface at:

```
http://localhost:8080/swagger-ui/index.html
```

### OpenAPI Specification

View the raw OpenAPI specification at:

```
http://localhost:8080/v3/api-docs
```

### OpenAPI YAML

Download the OpenAPI specification in YAML format at:

```
http://localhost:8080/v3/api-docs.yaml
```

#### Features of the API Documentation:

- Interactive API testing interface
- Detailed request/response schemas
- Authentication requirements
- Example requests and responses
- Models documentation
- Error responses

#### Using Swagger UI:

1. Navigate to the Swagger UI URL
2. Expand an API endpoint to see details
3. Click "Try it out" to test the endpoint
4. Add required parameters
5. Click "Execute" to send the request
6. View the response

## Contributor

### Timofey Babisashvili <br/>

![LinkedIn](https://img.shields.io/badge/LinkedIn-%230A66C2?style=flat&logo=linkedin&logoColor=white) <br/>**[@Timofey-tech](https://www.linkedin.com/in/timofey-tech)**<br/><br/>
![GitHub](https://img.shields.io/badge/GitHub-%23181717?style=flat&logo=github&logoColor=white) <br/>**[@TigerTimofey](https://github.com/TigerTimofey)** <br/><br/>
![Portfolio](https://img.shields.io/badge/Portfolio-%2316B5D8?style=flat&logo=google-chrome&logoColor=white)<br/> **[Portfolio](https://timofey-tigertimofeys-projects.vercel.app)**

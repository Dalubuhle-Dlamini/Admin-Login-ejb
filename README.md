# Admin-Login-ejb


## Overview

`Admin-Login-ejb` is a Java EE-based web application for managing users. It includes functionalities for registering, logging in, and updating user details. The application uses MySQL for data persistence and includes role-based access control (admin vs regular user).

## Technologies Used

- **Java EE 10**
  - EJB (Enterprise JavaBeans)
  - Servlets
- **MySQL**
- **JDBC (Java Database Connectivity)**
- **BCrypt** for password hashing
- **GlassFish** application server (or any other Java EE compatible server)

## Features

- **User Registration**: Allows new users to sign up with a username, email, and password.
- **User Login**: Authenticates users and checks for admin privileges.
- **Password Management**: Securely hashes passwords using BCrypt.
- **Admin Check**: Determines if the logged-in user has admin privileges.
- **Update Password**: Enables users to update their password, requiring validation against the old password.
- **Update Details**: Enables users to update their details like their name and email.

## Setup and Configuration

### Prerequisites

- **JDK 8 or higher**
- **MySQL**
- **GlassFish** or another Java EE server

### Database Setup

1. **Install MySQL** and create a database for the project:
```sql
  CREATE DATABASE ejbtest;
  CREATE TABLE users (
      id INT AUTO_INCREMENT PRIMARY KEY,
      username VARCHAR(255) NOT NULL UNIQUE,
      email VARCHAR(255) NOT NULL UNIQUE,
      password_hash VARCHAR(255) NOT NULL,
      is_admin BOOLEAN NOT NULL DEFAULT FALSE
  );
```
2. Configure database connection in com.util.stateless DBConnection.JAVA
```java
  private static String dbName = "ejbtest";
  private static String user = "your_username";
  private static String password = "your_password";
```




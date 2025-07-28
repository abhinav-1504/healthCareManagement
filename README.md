# Healthcare Appointment Booking REST API

The **Healthcare Appointment Booking REST API** is a Spring Boot application that enables patients to book, view, update, and cancel appointments with doctors, along with managing doctor profiles and specialty filtering. It includes secure JWT-based authentication and role-based access control.

---

## 🚀 Features

- **User Management**: Register and login users with JWT authentication.
- **Doctor Profiles**: Create, view, update, and filter doctors by specialty.
- **Appointment Management**: Book, list, update, and cancel appointments.
- **Security**: Role-based access (PATIENT, DOCTOR) with JWT and Spring Security.
- **Error Handling**: Standardized JSON error responses.
- **API Documentation**: Swagger UI for interactive API testing.

---

## 🛠 Technologies Used

- Java 17
- Spring Boot 3.1.5
- Spring Security + JWT (JJWT 0.12.6)
- Spring Data JPA (MySQL 8.0)
- Maven
- Swagger (Springdoc OpenAPI 2.2.0)

---

## 📦 Prerequisites

- Java 17 (JDK)
- Maven 3.8+
- MySQL 8.0 (with `healthcare_db` created)
- Postman (for API testing)

---

## 🧰 Setup Instructions

```bash
# Clone the repository
git clone https://github.com/abhinav-1504/healthCareManagement
cd healthCareManagement
```

### Configure MySQL

Create the database:
```sql
CREATE DATABASE healthcare_db;
```

Update `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/healthcare_db
spring.datasource.username=root
spring.datasource.password=your_mysql_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwt.secret=your_jwt_secret_key
jwt.expiration=86400000
```

### Build and Run

```bash
mvn clean install
mvn spring-boot:run
```

- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 🗃 Database Schema

- **User**: `id`, `username`, `password`, `roles`
- **Doctor**: `id`, `name`, `specialty`, `contact`, `user_id`
- **Appointment**: `id`, `doctor_id`, `patient_id`, `appointmentTime`, `status`

### Sample Data (`data.sql`)

- Users: `doctor1`, `doctor2`, `patient1`, `patient2`
- Doctors: `Dr. John Smith` (Cardiology), `Dr. Jane Doe` (Neurology)
- Appointments: Sample entries for testing

---

## 🔐 API Authentication

All endpoints (except `/api/auth/register` and `/api/auth/login`) require:

```
Authorization: Bearer <JWT_TOKEN>
```

---

## 📋 API Endpoints Overview

### Authentication
- `POST /api/auth/register`: Register new user (PATIENT or DOCTOR)
- `POST /api/auth/login`: Login user and get JWT

### Users
- `GET /api/users`: List all users (requires PATIENT or DOCTOR role)

### Doctors
- `POST /api/doctors`: Create profile (DOCTOR only)
- `GET /api/doctors`: List all / filter by specialty
- `GET /api/doctors/{id}`: Get doctor by ID
- `PUT /api/doctors/{id}`: Update doctor (ownership enforced)

### Appointments (PATIENT only)
- `POST /api/appointments`: Book appointment
- `GET /api/appointments`: List appointments
- `PUT /api/appointments/{id}/status`: Update status
- `DELETE /api/appointments/{id}`: Cancel appointment

---

## ❗ Common Error Responses

| Status | Message |
|--------|---------|
| 400 | Bad request or scheduling conflict |
| 401 | Unauthorized (missing or invalid token) |
| 403 | Forbidden (invalid role or ownership) |

---

## 📮 Postman Testing

1. Login as `patient1` and `doctor1`
2. Use JWT tokens in requests
3. Test booking, listing, updating, canceling appointments
4. Explore Swagger UI

---

## 🧾 Project Structure

```
healthcare-appointment-api/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/healthcare/
│   │   │       ├── config/
│   │   │       │   └── AppConfig.java
│   │   │       ├── controller/
│   │   │       │   ├── AuthController.java
│   │   │       │   ├── DoctorController.java
│   │   │       │   ├── AppointmentController.java
│   │   │       │   └── UserController.java
│   │   │       ├── dto/
│   │   │       │   ├── AuthRequest.java
│   │   │       │   ├── AuthResponse.java
│   │   │       │   ├── AppointmentRequest.java
│   │   │       │   └── AppointmentStatusRequest.java
│   │   │       ├── model/
│   │   │       │   ├── User.java
│   │   │       │   ├── Doctor.java
│   │   │       │   └── Appointment.java
│   │   │       ├── repository/
│   │   │       │   ├── UserRepository.java
│   │   │       │   ├── DoctorRepository.java
│   │   │       │   └── AppointmentRepository.java
│   │   │       ├── service/
│   │   │       │   ├── UserService.java
│   │   │       │   ├── DoctorService.java
│   │   │       │   └── AppointmentService.java
│   │   │       └── security/
│   │   │           ├── JwtUtil.java
│   │   │           ├── JwtAuthenticationFilter.java
│   │   │           └── SecurityConfig.java
│   ├── resources/
│   │   ├── application.properties
│   │   └── data.sql
├── pom.xml
├── README.md
```

---

## 🔮 Future Improvements

- Add email notifications
- Admin role for full access
- Pagination on listings
- Unit + Integration tests with JUnit & Testcontainers

---

## 📧 Contact

For issues or contributions, open a GitHub issue or contact the maintainer at `<abhinavsinghc48@gmail.com>`.

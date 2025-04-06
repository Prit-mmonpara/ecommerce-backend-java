# ecommerce-backend-java

# 🛒 ecommerce-backend-java

A Java-based backend system for an e-commerce application. Built using **Spring Boot**, **MySQL**, and **Hibernate**, it provides RESTful APIs for user management, product catalog, orders, payments, and email notifications.

---

## 🚀 Features

- 🔐 JWT Authentication & Authorization  
- 📦 Product Management (CRUD)  
- 🛒 Cart & Order Processing  
- 💳 Razorpay Payment Integration  
- 📧 Email Notifications (via Gmail SMTP)  
- 🧾 Swagger API Documentation  
- 🌐 CORS Configured for frontend integration  
- 🗃️ MySQL Database with Hibernate ORM  

---

## 🛠️ Tech Stack

- Java 17+  
- Spring Boot  
- Spring Security  
- Hibernate + JPA  
- MySQL  
- Razorpay API  
- JavaMailSender  
- Swagger (Springdoc OpenAPI)  
- Maven  

---

## ⚙️ Configuration

# Create an `application.properties` file in `src/main/resources/` with the following structure:

```properties
# Server configuration
server.port=8080

# Database configuration
spring.datasource.url=
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Hibernate settings
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# Swagger API Docs
springdoc.api-docs.path=/api-docs

# SMTP Email Configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email
spring.mail.password=your_email_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# Enable detailed logging for email debugging
logging.level.org.springframework.mail=DEBUG

# Razorpay API Credentials
razorpay.key_id=your_razorpay_key
razorpay.key_secret=your_razorpay_secret

# JWT Security Configuration
jwt.secret=your_jwt_secret_key
jwt.expirationMs=3600000

# CORS Configuration
spring.mvc.cors.allowed-origins=http://localhost:3000,http://localhost:8080
spring.mvc.cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
spring.mvc.cors.allowed-headers=Authorization,Content-Type,X-Requested-With
spring.mvc.cors.allow-credentials=true
spring.mvc.cors.max-age=3600

📦 Running the Project
Clone the repository:
git clone https://github.com/Prit-mmonpara/ecommerce-backend-java.git
cd ecommerce-backend-java

# Create and configure your application.properties.

# Run the project using Maven:
./mvnw spring-boot:run

# Access the Swagger UI:
http://localhost:8080/swagger-ui.html

📂 API Endpoints
Sample endpoints:

# POST /api/auth/register – Register a new user

# POST /api/auth/login – User login with JWT

# GET /api/products – Get list of products

# POST /api/orders – Create a new order

# POST /api/payment – Make payment via Razorpay

# GET /api-docs – API documentation (OpenAPI)

# Full list available in Swagger UI.

👨‍💻 Author
Prit M. Monpara
GitHub: @Prit-mmonpara

📄 License
This project is licensed under the MIT License.

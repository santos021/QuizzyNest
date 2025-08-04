# QuizzyNest 🎓

QuizzyNest is a dynamic full-stack Quiz Web Application where users can register, attempt quizzes, view their scores, and track progress. Admins can manage quiz categories, questions, users, and monitor activity logs.

---

## 🌐 Live Demo

> _Hosting link will be added soon_  
🔗 GitHub: [https://github.com/santos021/QuizzyNest](https://github.com/santos021/QuizzyNest)

---

## 🛠 Tech Stack

**Frontend:**  
- HTML, CSS, Bootstrap, JavaScript

**Backend:**  
- Java  
- Spring Boot  
- Spring Security  
- Spring Data JPA  
- Thymeleaf

**Database:**  
- MySQL

---

## ✨ Features

### 👥 User Features
- Register & log in securely (Spring Security)
- Choose category & take timed quiz
- View quiz history (scores with dates)
- Retake quiz
- Update password and profile

### 🔧 Admin Features
- Add/edit/delete categories
- Add/edit/delete questions (MCQs with 4 options)
- View users
- Track recent activity logs

### 📱 Responsive Design
- Mobile-optimized using Bootstrap

---

## 🚀 Getting Started

### 1. Clone Repo

```bash
git clone https://github.com/santos021/QuizzyNest.git
cd QuizzyNest

```
### 2.  Set-up MySQL Database
## Create a database named quizzynest_db and update credentials in:
```bash
# src/main/resources/application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/quizzynest_db
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD

```
### 3.  Run the App
```bash
In IDE: Run QuizzyNestApplication.java
Visit: http://localhost:1234/

```
  
### 🙌 Author
**Santos Kumar Biswal** <br>
Email-santosbiswal543@gmail.com

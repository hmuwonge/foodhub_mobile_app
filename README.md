# 🍔 FoodHub – Food Ordering & Delivery App

![GitHub](https://img.shields.io/github/license/your-username/foodhub-android)
![GitHub repo size](https://img.shields.io/github/repo-size/your-username/foodhub-android)
![Tech Stack](https://img.shields.io/badge/Tech-Kotlin%20%7C%20Jetpack%20Compose%20%7C%20MVVM-blue)
![API Backend](https://img.shields.io/badge/Backend-Ktor%20(Kotlin)-f39f37)

A modern, fully-featured **food ordering and delivery mobile application** built with **Kotlin** and **Jetpack Compose**, following the **MVVM (Model-View-ViewModel) architecture**. This app connects to a secure **Ktor-based Kotlin backend** and supports JWT authentication, social login, real-time order tracking, and seamless restaurant browsing.

> 🌐 Backend: [FoodHub Ktor API](https://github.com/your-username/foodhub-ktor-backend) (separate repo)

---

## 📱 Features

- 🔐 **Secure Authentication**
    - JWT-based login & registration
    - Social media login (Google & Facebook via Firebase)
    - Role-based access (Customer, Restaurant Owner, Admin)

- 🏪 **Restaurant & Menu Browsing**
    - Browse restaurants by category or location
    - View detailed food menus with images and pricing

- 🛒 **Cart & Checkout**
    - Add/remove items to cart
    - Real-time cart total calculation
    - Simulated payment process

- 📍 **Order Tracking**
    - Place and track active orders
    - Push notifications for order status updates (e.g., "Preparing", "Out for Delivery")

- 👤 **User Profile**
    - Manage personal details
    - View order history
    - Secure logout with session cleanup

- 🌐 **Offline Support**
    - Caching with **Room Database**
    - Smooth UX even with poor connectivity

- 🎨 **Modern UI**
    - Built entirely with **Jetpack Compose**
    - Responsive design for phones and tablets
    - Dark mode support

---

## 🛠️ Tech Stack

| Layer | Technology |
|------|------------|
| **Language** | Kotlin |
| **UI Toolkit** | Jetpack Compose |
| **Architecture** | MVVM (Model-View-ViewModel) |
| **Networking** | Retrofit, Ktor Client |
| **Authentication** | JWT, Firebase Auth (Google/Facebook) |
| **Dependency Injection** | Hilt (Dagger Hilt) |
| **Data Persistence** | Room Database |
| **Image Loading** | Coil |
| **Async Handling** | Kotlin Coroutines & Flow |
| **Navigation** | Jetpack Navigation Compose |
| **Backend API** | Ktor (Kotlin) – RESTful |
| **Push Notifications** | Firebase Cloud Messaging (FCM) |


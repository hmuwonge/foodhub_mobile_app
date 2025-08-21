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

---

## 📂 Project Structure
app/
├── data/ # Data sources, repositories, Room DB
├── network/ # Retrofit API services
├── model/ # Data classes
├── ui/
│ ├── components/ # Reusable Compose components
│ ├── screens/ # Individual screens (Home, Cart, Profile, etc.)
│ └── theme/ # Compose theme (colors, typography, shapes)
├── viewmodel/ # ViewModel classes (ViewModel + LiveData/StateFlow)
├── util/ # Helpers, constants, extensions
└── di/ # Hilt dependency injection modules


---

## 🔧 Getting Started

### 1. Prerequisites

- [Android Studio](https://developer.android.com/studio) (Hedgehog or later)
- Android SDK 34+
- Kotlin plugin enabled
- Internet connection for API calls

### 2. Clone the Repo

```bash
git clone https://github.com/your-username/foodhub-android.git
cd foodhub-android

3. Setup Backend
Ensure the Ktor backend server is running locally or deployed.

Update the base URL in:
app/src/main/java/com/yourcompany/foodhub/network/ApiService.kt
```

const val BASE_URL = "http://your-backend-url.com/api/"
```
4. Firebase Setup (For Social Login)
Go to Firebase Console
Create a new project
Register your app with package name: com.yourcompany.foodhub
Download google-services.json and place it in app/
Enable Google and Facebook authentication in Firebase Auth
5. Build & Run
Open in Android Studio → Sync Gradle → Run on emulator or device.
# 🌍 Virtual Travel Experience (VRTour)

VRTour is a premium, full-stack virtual tourism platform that allows users to experience travel destinations in interactive, responsive 3D environments. Powered by a robust Spring Boot backend and an immersive Three.js frontend, the platform integrates MongoDB Atlas for scalable data storage and Groq AI for customized travel recommendations.

---

## 🚀 Key Features

* **Interactive 3D Environments**: Explore stunning 3D travel destinations directly in your browser using Three.js, complete with smooth camera controls and collisions.
* **Groq AI Travel Planner**: Get smart, customized travel itineraries and recommendations tailored to your preferences.
* **MongoDB Atlas Database**: Scalable, lightning-fast document storage for destinations, user favorites, reviews, and bookings.
* **Premium Dark Mode UI**: Modern, glassmorphism-based design with fluid animations and responsive layout for a premium experience.
* **Secure Auth & Operations**: Complete user management, favorites saving, reviews system, and ticket booking tracking.

---

## 🛠️ Technology Stack

### Backend
* **Language/Framework**: Java / Spring Boot 3.x / 4.x
* **Build Tool**: Gradle
* **Database**: MongoDB Atlas (Cloud Database)
* **AI Integration**: Groq API (LLaMA 3.3 model)
* **Security**: Spring Security & Token-based Authentication

### Frontend
* **Core**: HTML5 / CSS3 (Vanilla Custom Styles) / ES6+ JavaScript
* **3D Library**: Three.js (GLTFLoader for GLB models)
* **Web Server**: `http-server` static hosting

---

## 📂 Project Structure

```
virtual travel experience/
├── backend/                   # Spring Boot Backend REST API
│   ├── src/                   # Source code (Java)
│   ├── .env                   # Environment variables (IGNORED)
│   ├── .env.example           # Example environment template
│   ├── build.gradle           # Backend dependencies and configuration
│   └── gradlew.bat            # Gradle wrapper executable
│
├── frontend/                  # Responsive 3D Web Frontend
│   ├── assets/                # Images, models, and audio assets
│   ├── components/            # Reusable Web UI components
│   ├── pages/                 # Frontend pages (Dashboard, Explore, Bookings)
│   ├── css/                   # Vanilla styling and design tokens
│   ├── js/                    # Three.js 3D rendering and api integrations
│   └── index.html             # Entry point
│
├── start-servers.bat          # Shell script to start both servers in parallel
└── README.md                  # Comprehensive documentation
```

---

## ⚙️ Setup and Installation

### 1. Prerequisites
* **Java 17 or higher** installed
* **Node.js** installed (for static hosting server)
* A **MongoDB Atlas** account and database
* A **Groq AI** API Key

### 2. Environment Variables Configuration
Create a `.env` file inside the `backend/` directory (you can copy [`.env.example`](file:///c:/Users/DELL/Desktop/virtual%20travel%20experience/backend/.env.example)) and configure your credentials:

```ini
# MongoDB Configuration
MONGODB_URI=your_mongodb_atlas_connection_string
MONGODB_DATABASE=vrtourism

# Groq AI Configuration
GROQ_API_KEY=your_groq_api_key
GROQ_MODEL=llama-3.3-70b-versatile

# Server Configuration
SERVER_PORT=8080
```

---

## 🏃 Launching the Application

To start both the Backend API and the Frontend Server automatically, simply double-click the **`start-servers.bat`** script at the root directory, or run it in your terminal:

```bash
.\start-servers.bat
```

This will spin up two separate console windows:
1. **Spring Boot Backend**: Accessible at [http://localhost:8080/api](http://localhost:8080/api)
2. **Three.js Frontend**: Accessible at [http://localhost:3000](http://localhost:3000)

---

## 🛡️ License

This project is open-source and available under the MIT License.

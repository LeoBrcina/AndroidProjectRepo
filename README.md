# 📘 Formula 1 Data — Android Application for Standings, Results & Race Insights

**Author:** Leo Brcina  
**University:** Algebra University College (Software Engineering Thesis)  
**Year:** 2025  

---

## 🚀 Overview

**Formula 1 Data** is a full-featured Android application built in **Kotlin**.  
It provides an intuitive and modern interface for exploring historical Formula 1 seasons, results, driver standings, constructor standings, race schedules, and race details.

The application integrates the **Ergast Developer API** and includes extensive offline capabilities using **Room database caching**, background workers, and scheduled notifications.

This project demonstrates mastery of Android development, including:

- Modern **MVVM architecture**
- Room-based **persistent caching**
- **BroadcastReceivers**, **AlarmManager**, and **NotificationManager**
- Year-based dynamic data loading
- A complete **Content Provider** implementation
- Background **WorkManager** syncing
- Clean UI with fragments, navigation, and adapters

The app is designed to be fast, responsive, and accurate — even offline — while minimizing API calls to comply with Ergast usage guidelines.

---

## 🧠 High-Level Architecture

The repository is structured as a modular, production-grade Android application:

### `Formula 1 Data (Android App)`
- **MVVM Architecture (Model–View–ViewModel)**
- **Room Database** for caching:
  - Drivers
  - Constructors
  - Driver Standings
  - Constructor Standings
  - Race Schedules
  - Race Results
- **Repository layer** for unified API + cache access
- **Retrofit API client** for Ergast endpoints
- **Coroutines** for asynchronous data loading
- **Shared ViewModel (YearViewModel)** for season selection propagation
- **AlarmManager + BroadcastReceiver** for recurring notifications
- **WorkManager** for background season syncing
- **Content Provider** for data exposure to external apps

The entire app runs offline once cached and refreshes only when needed.

---

## 🧩 Core Features

### 📊 Formula 1 Data Browsing

- **Driver Standings** for any selected season  
- **Constructor Standings** with positions, points, wins  
- **Race Calendar** with circuit details  
- **Race Results** including drivers, teams, grid, and finishing order  
- **Driver details** (bio, nationality, permanent number, etc.)  
- **Constructor details** (stats, origin, championships, etc.)

All features automatically update when switching seasons.

---

### 🧰 Caching & Offline Capability

- Every dataset (drivers, constructors, standings, races, results) is **cached permanently** using Room
- Unique **synthetic primary keys** ensure no cross-season data collisions
- A **cache integrity system** checks data completeness to prevent partial/broken caches
- Caching reduces Ergast API usage significantly

---

### 📅 Year Selection System

A global **year selector** in the toolbar allows changing seasons (1950–2024).  
It updates:

- Standings  
- Race Calendar  
- Race Results  
- Driver/Constructor details  

All fragments use a shared `YearViewModel`, guaranteeing instant and synchronized updates.

---

### 🔔 Recurring Notifications

Implemented using:

- **AlarmManager**
- **BroadcastReceiver**
- **NotificationManager**

Features:

- Fires automatically every hour  
- First trigger after 1 minute  
- Network availability check  
- POST_NOTIFICATIONS permission handling  

---

### 🔁 Background Season Syncing (WorkManager)

A background worker automatically:

1. Iterates through seasons 1950–2024  
2. Downloads and caches standings & races  
3. Runs hourly  
4. Works even if the app is closed  

This ensures the user always has offline, up-to-date data.

---

### 🧩 Content Provider

Implements a custom **F1 Content Provider** for:

- Secure external data access  
- URI-based queries  
- Table-by-table exposure  
- Fully functional for academic evaluation  

---

## 📂 Project Structure

```bash
Formula1Data/
├── app/
│   ├── java/hr/algebra/formula1data/
│   │   ├── api/                # Retrofit Ergast API client
│   │   ├── database/           # Room DB, DAOs, entities
│   │   ├── model/              # Kotlin data models
│   │   ├── repository/         # Repository logic
│   │   ├── ui/
│   │   │   ├── activities/
│   │   │   ├── fragments/      # Standings, races, details
│   │   │   ├── adapters/       # RecyclerView adapters
│   │   │   └── viewmodels/
│   │   ├── worker/             # Background season caching
│   │   ├── provider/           # ContentProvider implementation
│   │   ├── notifications/      # Alarm + BroadcastReceiver
│   │   └── utils/              # Helpers, constants, converters
│   │
│   └── res/                    # Layouts, drawables, themes
│
└── build.gradle.kts            # Kotlin DSL Gradle build
```

---

## 📡 Data Sources

The application uses the following:

### 🟦 **Ergast Developer API**  
For all historical Formula 1 data:

- Drivers  
- Constructors  
- Standings  
- Race schedules  
- Race results  
- Season metadata  

The app requests only essential data thanks to aggressive caching.

---

# 🏗 System Architecture

```mermaid
graph TD
    A[Ergast API] --> B[Repository (Retrofit)]
    B --> C[Room Cache]
    C --> D[ViewModels]
    D --> E[Fragments/UI]

    F[AlarmManager] --> G[BroadcastReceiver] --> H[Notifications]

    I[WorkManager] --> J[Season Sync Worker] --> C

    C --> K[Content Provider]
```

---

## 🔌 Key API Endpoints Used

```http
GET /{year}/driverStandings.json
GET /{year}/constructorStandings.json
GET /{year}.json
GET /{year}/{round}/results.json
GET /drivers/{driverId}.json
GET /constructors/{constructorId}.json
```

---

## ⚙️ Android Implementation Details

### 🏛 MVVM Layer Breakdown

- **ViewModels**
  - Standings  
  - Race Calendar  
  - Driver details  
  - Constructor details  
  - Race results  
  - YearViewModel (global state)

- **Repository**
  - Fetch-from-API-with-cache fallback
  - Decides when to return DB vs network
  - Stores all fetched data into Room

- **Room Database**
  - Entities for every dataset
  - Composite/synthetic primary keys
  - DAOs for each domain
  - Migrations handled automatically

---

### 🔔 Notification System

- **Alarm triggers every hour**
- Receiver checks network availability
- Shows dynamic F1-themed notification
- Uses `NotificationChannel` on Android 8+

---

### 🔁 Background Worker

- Syncs each season from 1950–2024
- Runs indefinitely
- Handles API rate limitations
- Writes to Room on completion

---

### 📅 Year Selector Logic

- AlertDialog with year dropdown  
- Value stored in **SharedPreferences**  
- Shared `YearViewModel` updates fragments instantly  
- All fragments observe year changes  

---

### 🗂 Content Provider

Enables external querying of:

- Driver standings  
- Constructor standings  
- Race calendar  
- Race results  

URI example:

```
content://hr.algebra.formula1data.provider/driverStandings
```

---

## 🧾 .gitignore Highlights

```gitignore
/build/
.gradle/
local.properties
*.iml
*.apk
*.aab
captures/
# IDE files
.idea/
# Cache
app/src/main/assets/fastf1_cache/
```

---

## 🚧 Future Improvements

- Add driver/constructor images  
- Offline-first UI for cached seasons  
- Theme toggle (light/dark)  
- Add circuit layout images  
- Implement search across seasons  
- Predictive insights using ML (optional)  

---

### 🛡️ Security Considerations

This Android application:

- Stores all data locally
- Uses no authentication
- Makes only **public API calls**
- Exposes data via a controlled Content Provider
- Requires no dangerous permissions except POST_NOTIFICATIONS

Since the app operates entirely on-device and uses public APIs, no external security risks exist.

---

## 🙏 Acknowledgements

- **Ergast Developer API** for historical F1 data  
- **Android Jetpack** libraries  
- **Retrofit**, **Room**, **Coroutines**  
- Algebra University College — project supervision  

---

## 📜 License

This project is for educational and academic use.  
A formal license (e.g., MIT) may be added.

---

## 📌 Repository

https://github.com/LeoBrcina/AndroidProjectRepo

**Built with speed, precision, and a passion for Formula 1. 🏁**

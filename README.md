# 📘 Formula 1 Data — Android Application for Standings, Results & Race Insights

**Author:** Leo Brcina  
**University:** Algebra University College (Software Engineering Thesis)  
**Year:** 2025  

---

## 🚀 Overview

**Formula 1 Data** is a full-featured Android application built in **Kotlin**.  
It provides an intuitive and modern interface for exploring historical Formula 1 seasons, results, driver standings, constructor standings, race schedules, and race details.

The application integrates the **NOW DEPRECATED Ergast Developer API** and includes extensive offline capabilities using **Room database caching**, background workers, and scheduled notifications.

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
- **Race Calendar** with circuit and round information  
- **Race Results** including drivers, teams, grid, and finishing order  
- **Driver details** (bio, nationality, permanent number, etc.)  
- **Constructor details** (origin, stats, championships, etc.)  

All features automatically update when switching seasons.

---

### 🧰 Caching & Offline Capability

- Every dataset (drivers, constructors, standings, races, results) is **cached permanently** using Room  
- Unique **synthetic primary keys** ensure no cross-season data collisions  
- A **cache integrity system** checks data completeness to prevent partial/broken caches  
- Caching dramatically reduces Ergast API usage and speeds up the app  

---

### 📅 Year Selection System

A global **year selector** in the toolbar allows changing seasons (1950–2024). It updates:

- Standings  
- Race Calendar  
- Race Results  
- Driver/Constructor details  

All fragments use a shared `YearViewModel`, guaranteeing instant and synchronized updates across the app.

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
- Android 12+ exact alarm permission handling  

---

### 🔁 Background Season Syncing (WorkManager)

A background worker automatically:

1. Iterates through seasons 1950–2024  
2. Downloads and caches standings & races  
3. Runs on a recurring schedule  
4. Works even if the app is closed  

This ensures the user always has offline, up-to-date data.

---

### 🧩 Content Provider

Implements a custom **F1 Content Provider** for:

- Secure external data access  
- URI-based queries  
- Table-by-table exposure (standings, races, results, etc.)  
- Fully functional for academic evaluation and Android coursework requirements  

---

## 📂 Project Structure

```bash
Formula1Data/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/hr/algebra/formula1data/
│   │   │   │   ├── api/            # Retrofit Ergast API client
│   │   │   │   ├── database/       # Room DB, DAOs, entities
│   │   │   │   ├── model/          # Kotlin data models
│   │   │   │   ├── repository/     # Repository logic (cache + network)
│   │   │   │   ├── ui/
│   │   │   │   │   ├── activities/
│   │   │   │   │   ├── fragments/  # Standings, races, details
│   │   │   │   │   ├── adapters/   # RecyclerView adapters
│   │   │   │   │   └── viewmodels/
│   │   │   │   ├── worker/         # Background season caching
│   │   │   │   ├── provider/       # ContentProvider implementation
│   │   │   │   ├── notifications/  # Alarm + BroadcastReceiver
│   │   │   │   └── utils/          # Helpers, constants, converters
│   │   │   └── res/                # Layouts, drawables, themes
│   │   └── test/                   # Unit tests (if any)
│   │
│   ├── AndroidManifest.xml
│   └── build.gradle.kts
│
├── build.gradle.kts                # Project-level Gradle config
├── settings.gradle.kts
└── gradle.properties
```

---

## 📡 Data Sources

The application uses the following:

### 🟦 Ergast Developer API  
For all historical Formula 1 data:

- Drivers  
- Constructors  
- Standings  
- Race schedules  
- Race results  
- Season metadata  

All requests are made over HTTPS and are read-only.

---

# 🏗 System Architecture

The overall flow of data and responsibilities can be summarized as:

```text
[Ergast REST API]
        │
        ▼
[Retrofit API Service]
        │
        ▼
[Repository Layer] ─── checks cache, decides DB vs network
        │
        ▼
[Room Database (Entities + DAOs)]
        │
        ▼
[ViewModels]
        │
        ▼
[Fragments / Activities (UI)]
```

Background and system components:

```text
[AlarmManager] ──▶ [BroadcastReceiver] ──▶ [NotificationManager]

[WorkManager] ──▶ [SeasonSyncWorker] ──▶ [Room Database]

[Room Database] ──▶ [ContentProvider] ──▶ [External Apps (if any)]
```

This architecture ensures:

- Clear separation of concerns  
- Offline-first behavior  
- Lifecycle-aware UI updates  
- Minimal and controlled API usage  

---

## 🔌 Key Ergast Endpoints Used

```http
GET /{year}/driverStandings.json
GET /{year}/constructorStandings.json
GET /{year}.json
GET /{year}/{round}/results.json
GET /drivers/{driverId}.json
GET /constructors/{constructorId}.json
```

These endpoints are wrapped by Retrofit interfaces and consumed via the repository layer.

---

## ⚙️ Android Implementation Details

### 🏛 MVVM Layer Breakdown

- **ViewModels**
  - Driver Standings ViewModel  
  - Constructor Standings ViewModel  
  - Race Calendar ViewModel  
  - Race Results ViewModel  
  - Driver Details ViewModel  
  - Constructor Details ViewModel  
  - `YearViewModel` (global season state)

- **Repository**
  - Central access point for all data  
  - Fetches from Room when data is available and valid  
  - Falls back to Ergast when cache is missing or incomplete  
  - Persists new data into Room entities  

- **Room Database**
  - Entities for:
    - Drivers  
    - Constructors  
    - Driver standings  
    - Constructor standings  
    - Races  
    - Race results  
  - Synthetic primary keys (e.g., `season_driverId`) to avoid conflicts between seasons  
  - DAOs for all read/write operations  

---

### 🔔 Notification System

- Uses **AlarmManager** with an exact repeating alarm  
- BroadcastReceiver:
  - Checks network connectivity  
  - Builds and shows an F1-themed notification  
- Uses **NotificationChannel** for Android 8+  
- Requests `POST_NOTIFICATIONS` permission on modern Android versions  

---

### 🔁 Background Worker

- Implemented using **WorkManager**  
- Periodically:
  - Iterates season by season  
  - Downloads standings and race calendars  
  - Saves data into Room  
- Designed to be resilient and not require the app to be in the foreground  

---

### 📅 Year Selector Logic

- The selected year is chosen via an **AlertDialog** (or toolbar selector)  
- The choice is stored in **SharedPreferences**  
- `YearViewModel` holds the active season as `LiveData`  
- All fragments observe `YearViewModel`; when the year changes:
  - Fragments re-query cached data  
  - If needed, repository fetches from Ergast and updates Room  
- Ensures consistent season context across the entire app  

---

### 🗂 Content Provider

- Exposes selected tables through a custom `ContentProvider`  
- Supports query operations using URIs, e.g.:

```text
content://hr.algebra.formula1data.provider/driverStandings
content://hr.algebra.formula1data.provider/constructorStandings
content://hr.algebra.formula1data.provider/races
content://hr.algebra.formula1data.provider/results
```

- Implemented primarily for educational and project specification purposes  

---

## 🧾 .gitignore Highlights

```gitignore
# Build and IDE
/.idea/
/build/
/app/build/
*.iml

# Local configuration
local.properties

# Gradle
/.gradle/

# Logs
*.log

# Android profiling & captures
/captures/

# Misc
.DS_Store
```

---

## 🚧 Future Improvements

- Add driver and constructor images and flags  
- Implement theme toggle (light / dark / AMOLED)  
- Add circuit layout maps and country flags  
- Integrate simple predictions or trend indicators per season  
- Add search and filtering for drivers, teams, and races  
- Export data via CSV/Share functionality  

---

### 🛡️ Security Considerations

This Android application:

- Uses only **public, read-only** Ergast endpoints  
- Stores data locally in a private app-specific Room database  
- Exposes a controlled subset of data via Content Provider for demonstration purposes  
- Requests only essential permissions:
  - `POST_NOTIFICATIONS`  
  - (Network state if used for checks)  

The app never transmits or stores user-sensitive data. All communication is limited to publicly available motorsport information.

In a real production scenario, additional measures such as analytics opt-in, telemetry logging policies, and stricter permission handling could be added, but they are not required for this academic project.

---

## 🙏 Acknowledgements

- **Ergast Developer API** for comprehensive historical Formula 1 data  
- **Android Jetpack** libraries (ViewModel, LiveData, Room, Navigation, WorkManager)  
- **Retrofit**, **OkHttp**, and **Kotlin Coroutines**  
- Algebra University College – teaching staff and mentors  

---

## 📜 License

This project is provided for educational and research purposes.  
A formal license (e.g., MIT) can be added to the repository root if needed.

---

## 📌 Repository

https://github.com/LeoBrcina/AndroidProjectRepo

**Built with speed, precision, and a passion for Formula 1. 🏁**

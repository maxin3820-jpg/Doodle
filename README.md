# Doodle - Minimalist Task Manager

A lightweight, offline-first task management application for Android built with modern Android development practices.

## 🚀 Get Your APK - Super Easy!

### 🎯 Easiest Way: GitHub Actions (Recommended!)
**No Java, No Android SDK needed - builds in the cloud!**

1. Open: **`GET_APK_EASY.txt`** and follow 6 simple steps
2. GitHub builds your APK automatically (takes ~10 minutes)
3. Download and install on your phone!

**Total time:** 15-20 minutes | **Your effort:** Just upload code

### 🔧 Alternative: Build Locally
If you prefer to build on your computer:

```cmd
check-requirements.bat  # Step 1: Check setup
build.bat              # Step 2: Build APK
install.bat            # Step 3: Install
```

**See:** `START_HERE.txt` and `QUICK_START.md` for local build guide.

## Features

- **Extremely Fast**: Opens instantly to the tasks screen
- **Offline First**: All data stored locally, works completely offline
- **No Distractions**: No login, no cloud sync, no ads
- **Auto-Save**: Every action saves immediately
- **Customizable**: Theme, colors, fonts, and typography
- **Material 3 Design**: Modern, beautiful UI with smooth animations

## Technology Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose (Material 3)
- **Architecture**: MVVM
- **Database**: Room (SQLite)
- **State Management**: ViewModel + StateFlow
- **Navigation**: Navigation Compose
- **Dependency Injection**: Hilt
- **Min SDK**: 26 (Android 8.0)
- **Target SDK**: 34

## Project Structure

```
app/
├── data/
│   ├── database/       # Room database, entities, DAOs
│   ├── model/          # Data models
│   ├── repository/     # Repository layer
│   └── settings/       # DataStore settings
├── di/                 # Hilt dependency injection modules
├── ui/
│   ├── components/     # Reusable UI components
│   ├── navigation/     # Navigation graph
│   ├── screens/        # Screen composables
│   ├── theme/          # Material theme configuration
│   └── viewmodel/      # ViewModels
└── MainActivity.kt     # Main entry point
```

## Building the App

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK 34

### Build Steps

1. Open the project in Android Studio
2. Sync Gradle files
3. Build the project: `Build → Make Project`
4. Run on device/emulator: `Run → Run 'app'`

### Command Line Build

```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease

# Run tests
./gradlew test
./gradlew connectedAndroidTest
```

## Usage

### Tasks Screen
- View all active tasks
- Tap checkbox to complete a task
- Long press a task to edit or delete
- Tap FAB (+) to add a new task

### Completed Screen
- View all completed tasks
- Uncheck to move back to active tasks

### Settings
- Access from gear icon in Tasks screen
- Customize theme (Light/Dark/System)
- Choose accent color (Blue, Green, Purple, Orange, Red, Pink)
- Select background color
- Change font family and size
- All changes apply instantly

## Testing

The project includes:
- Unit tests for ViewModels and Repository
- Instrumentation tests for Room DAO
- UI tests for Compose screens

Run tests:
```bash
./gradlew test           # Unit tests
./gradlew connectedAndroidTest  # Instrumentation tests
```

## Architecture

### MVVM Pattern
- **Model**: Room entities and data classes
- **View**: Jetpack Compose UI
- **ViewModel**: State management with StateFlow

### Data Flow
1. UI observes StateFlow from ViewModel
2. ViewModel calls Repository methods
3. Repository interacts with Room DAO
4. Changes flow back through StateFlow to UI

### Key Features
- **Single Source of Truth**: Room database
- **Unidirectional Data Flow**: UI → ViewModel → Repository → Database
- **Reactive UI**: Compose recomposes on state changes
- **Dependency Injection**: Hilt provides dependencies

## License

This project is open source and available for educational purposes.

## Philosophy

Doodle follows the philosophy of:
- **Speed**: Open and start using immediately
- **Simplicity**: No unnecessary features or complexity
- **Privacy**: All data stays on your device
- **Reliability**: Auto-save ensures you never lose data
- **Beauty**: Clean, modern Material 3 design

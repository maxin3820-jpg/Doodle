# Doodle Setup Guide

## Quick Start

### Prerequisites
1. **Android Studio** - Hedgehog (2023.1.1) or later
2. **JDK 17** - Ensure JAVA_HOME is set
3. **Android SDK 34** - Install via Android Studio SDK Manager

### Opening the Project

1. Launch Android Studio
2. Click "Open" and navigate to the Doodle folder
3. Wait for Gradle sync to complete
4. If prompted, accept any SDK installation requests

### First Build

1. Connect an Android device or start an emulator (API 26+)
2. Click the "Run" button (▶) or press Shift+F10
3. Select your target device
4. Wait for the app to build and install

### Building from Command Line

#### Windows (PowerShell/CMD)
```cmd
cd c:\Users\zc\Desktop\othr\Doodle
gradlew.bat assembleDebug
```

#### Build Commands
```cmd
# Debug build
gradlew.bat assembleDebug

# Release build (requires signing config)
gradlew.bat assembleRelease

# Run unit tests
gradlew.bat test

# Run instrumentation tests
gradlew.bat connectedAndroidTest

# Clean build
gradlew.bat clean build
```

## Important Notes

### Launcher Icons
The project includes placeholder launcher icons. For production, you should:
1. Design proper icons using Android Studio's Image Asset Studio
2. Go to: File → New → Image Asset
3. Choose "Launcher Icons (Adaptive and Legacy)"
4. Upload your icon design
5. Generate all required densities

### App Signing (Release Build)
For release builds, you need to configure signing:

1. Create a keystore:
```cmd
keytool -genkey -v -keystore doodle-release.keystore -alias doodle -keyalg RSA -keysize 2048 -validity 10000
```

2. Add to `app/build.gradle.kts`:
```kotlin
android {
    signingConfigs {
        create("release") {
            storeFile = file("path/to/doodle-release.keystore")
            storePassword = "your_store_password"
            keyAlias = "doodle"
            keyPassword = "your_key_password"
        }
    }
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            // ... existing config
        }
    }
}
```

## Troubleshooting

### Gradle Sync Failed
- Check internet connection
- Invalidate Caches: File → Invalidate Caches / Restart
- Update Gradle: Settings → Build, Execution, Deployment → Gradle

### Compilation Errors
- Ensure you're using JDK 17
- Clean project: Build → Clean Project
- Rebuild: Build → Rebuild Project

### Device Not Detected
- Enable USB Debugging on device
- Install USB drivers (Windows)
- Check: Run → Edit Configurations → ensure deployment target is correct

### App Crashes on Launch
- Check Logcat in Android Studio
- Verify minimum SDK is 26 (Android 8.0)
- Ensure all dependencies downloaded correctly

## Project Structure Overview

```
Doodle/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/doodle/app/
│   │   │   │   ├── data/          # Data layer
│   │   │   │   ├── di/            # Dependency injection
│   │   │   │   ├── ui/            # UI layer
│   │   │   │   ├── MainActivity.kt
│   │   │   │   └── DoodleApplication.kt
│   │   │   ├── res/               # Resources
│   │   │   └── AndroidManifest.xml
│   │   ├── test/                  # Unit tests
│   │   └── androidTest/           # Instrumentation tests
│   └── build.gradle.kts           # App module build config
├── build.gradle.kts               # Project build config
├── settings.gradle.kts            # Project settings
└── gradle.properties              # Gradle properties
```

## Next Steps

1. **Test the app** - Run on different devices/emulators
2. **Create proper icons** - Replace placeholder icons
3. **Customize** - Adjust colors, themes as needed
4. **Add signing** - Configure release signing
5. **Test thoroughly** - Run all unit and UI tests

## Common Development Tasks

### Add a New Dependency
Edit `app/build.gradle.kts` and add to dependencies block:
```kotlin
implementation("package:name:version")
```
Then sync Gradle.

### Change App Name
Edit `app/src/main/res/values/strings.xml`:
```xml
<string name="app_name">Your Name</string>
```

### Change Package Name
Use Android Studio: Right-click package → Refactor → Rename

### Database Migration
If you modify Room entities, update version in `AppDatabase.kt`:
```kotlin
@Database(entities = [...], version = 2)
```
And provide migration strategy.

## Support

For issues or questions:
- Check Android Studio Logcat for error messages
- Review the README.md for architecture details
- Examine test files for usage examples

Happy coding! 🚀

# Building Doodle Without Android Studio

## Prerequisites

### 1. Install Java Development Kit (JDK) 17

**Option A: Download from Oracle/OpenJDK**
- Download JDK 17 from: https://adoptium.net/temurin/releases/?version=17
- Install and set JAVA_HOME environment variable
- Add to PATH: `%JAVA_HOME%\bin`

**Option B: Using Chocolatey (Windows)**
```cmd
choco install openjdk17
```

**Option C: Using winget**
```cmd
winget install EclipseAdoptium.Temurin.17.JDK
```

**Verify installation:**
```cmd
java -version
```
Should show: openjdk version "17.x.x"

### 2. Install Android Command Line Tools

**Download Android SDK Command Line Tools:**
1. Go to: https://developer.android.com/studio#command-line-tools-only
2. Download "commandlinetools-win-XXXXX_latest.zip"
3. Extract to: `C:\Android\cmdline-tools`
4. Rename extracted folder to "latest"
   - Final path: `C:\Android\cmdline-tools\latest`

**Set Environment Variables:**
```cmd
setx ANDROID_HOME "C:\Android"
setx PATH "%PATH%;%ANDROID_HOME%\cmdline-tools\latest\bin;%ANDROID_HOME%\platform-tools"
```

**Install SDK Components:**
```cmd
cd C:\Android\cmdline-tools\latest\bin

# Accept licenses
sdkmanager --licenses

# Install required components
sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"
sdkmanager "extras;google;google_play_services"
```

## Building the APK

### Step 1: Navigate to Project Directory
```cmd
cd c:\Users\zc\Desktop\othr\Doodle
```

### Step 2: Build Debug APK
```cmd
gradlew.bat assembleDebug
```

The APK will be generated at:
`app\build\outputs\apk\debug\app-debug.apk`

### Step 3: Build Release APK (Unsigned)
```cmd
gradlew.bat assembleRelease
```

## Installing the APK

### Option 1: Using ADB (Android Device)

**Install ADB:**
It comes with platform-tools (already installed above)

**Enable USB Debugging on your phone:**
1. Settings → About Phone → Tap "Build Number" 7 times
2. Settings → Developer Options → Enable "USB Debugging"
3. Connect phone via USB

**Install APK:**
```cmd
adb devices
adb install app\build\outputs\apk\debug\app-debug.apk
```

### Option 2: Transfer APK to Phone
1. Copy `app-debug.apk` to your phone
2. Open file manager on phone
3. Tap the APK file
4. Allow installation from unknown sources if prompted
5. Install

## Creating a Signed Release APK

### Step 1: Create Keystore
```cmd
keytool -genkey -v -keystore doodle-release.keystore -alias doodle -keyalg RSA -keysize 2048 -validity 10000
```

Follow prompts and remember your passwords!

### Step 2: Sign the APK

**Manual Signing:**
```cmd
# Build unsigned release APK first
gradlew.bat assembleRelease

# Sign it
jarsigner -verbose -sigalg SHA256withRSA -digestalg SHA-256 -keystore doodle-release.keystore app\build\outputs\apk\release\app-release-unsigned.apk doodle

# Zipalign
zipalign -v 4 app\build\outputs\apk\release\app-release-unsigned.apk app\build\outputs\apk\release\app-release-signed.apk
```

## Troubleshooting

### Error: "JAVA_HOME is not set"
```cmd
# Check if Java is installed
java -version

# Set JAVA_HOME (replace path with your JDK installation)
setx JAVA_HOME "C:\Program Files\Eclipse Adoptium\jdk-17.0.x"
```

### Error: "SDK location not found"
Create file: `local.properties` with:
```
sdk.dir=C\:\\Android
```

### Error: "Failed to find Build Tools"
```cmd
sdkmanager "build-tools;34.0.0"
```

### Gradle Build Failed
```cmd
# Clean build
gradlew.bat clean

# Try again
gradlew.bat assembleDebug
```

### Internet Connection Issues
Gradle needs to download dependencies on first build. Ensure:
- Stable internet connection
- No firewall blocking Gradle
- Proxy settings configured if needed

## Quick Build Script

Save this as `build.bat` in the Doodle folder:
```batch
@echo off
echo Building Doodle APK...
echo.

REM Check Java
java -version
if errorlevel 1 (
    echo ERROR: Java not found. Please install JDK 17.
    pause
    exit /b 1
)

echo.
echo Starting Gradle build...
call gradlew.bat assembleDebug

if errorlevel 1 (
    echo.
    echo BUILD FAILED!
    pause
    exit /b 1
)

echo.
echo BUILD SUCCESSFUL!
echo.
echo APK Location:
echo %CD%\app\build\outputs\apk\debug\app-debug.apk
echo.
pause
```

## File Sizes

Expected APK sizes:
- **Debug APK**: ~5-8 MB
- **Release APK**: ~3-5 MB (after ProGuard optimization)

## Testing Without Device

### Using Android Emulator from Command Line

**Create an emulator:**
```cmd
sdkmanager "system-images;android-34;google_apis;x86_64"
avdmanager create avd -n Pixel6 -k "system-images;android-34;google_apis;x86_64"
```

**Run emulator:**
```cmd
emulator -avd Pixel6
```

**Install APK to emulator:**
```cmd
adb install app\build\outputs\apk\debug\app-debug.apk
```

## Next Steps After Building

1. **Test thoroughly** on real device
2. **Create proper app icons** (see icon guide below)
3. **Sign for release** when ready for distribution
4. **Optimize** by enabling ProGuard/R8

## Resources

- Android Developer: https://developer.android.com
- Gradle Documentation: https://docs.gradle.org
- APK Signing: https://developer.android.com/studio/publish/app-signing

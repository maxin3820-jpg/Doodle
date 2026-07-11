# Quick Start Guide - Build Doodle Without Android Studio

## Step-by-Step Setup (15 minutes)

### Step 1: Install Java (JDK 17)

**Using winget (Recommended for Windows 10/11):**
```cmd
winget install EclipseAdoptium.Temurin.17.JDK
```

**Or download manually:**
- Visit: https://adoptium.net/temurin/releases/?version=17
- Download Windows x64 .msi installer
- Run installer (use default options)

**Verify:**
```cmd
java -version
```

### Step 2: Install Android SDK (Minimal)

**Download Command Line Tools:**
1. Visit: https://developer.android.com/studio#command-line-tools-only
2. Click "Download Command Line Tools for Windows"
3. Extract the zip file

**Setup SDK:**
```cmd
# Create Android folder
mkdir C:\Android
cd C:\Android

# Extract downloaded cmdline-tools here
# You should have: C:\Android\cmdline-tools\

# Create required structure
mkdir cmdline-tools\latest
# Move all files from cmdline-tools to cmdline-tools\latest

# Set environment variable
setx ANDROID_HOME "C:\Android"

# Add to PATH (restart CMD after this)
setx PATH "%PATH%;C:\Android\cmdline-tools\latest\bin;C:\Android\platform-tools"
```

**Install Required Components:**
Open a NEW Command Prompt (after setting PATH):
```cmd
cd C:\Android\cmdline-tools\latest\bin

# Accept licenses (type 'y' multiple times)
sdkmanager --licenses

# Install build tools
sdkmanager "platform-tools"
sdkmanager "platforms;android-34"
sdkmanager "build-tools;34.0.0"
```

### Step 3: Configure Project

Edit `local.properties` in Doodle folder:
```
sdk.dir=C\:\\Android
```

### Step 4: Build APK

```cmd
cd c:\Users\zc\Desktop\othr\Doodle

# Run the build script
build.bat
```

Wait 5-10 minutes for first build (downloads dependencies).

### Step 5: Install on Phone

**Enable USB Debugging on your phone:**
1. Go to Settings → About Phone
2. Tap "Build Number" 7 times
3. Go back to Settings → Developer Options
4. Enable "USB Debugging"
5. Connect phone via USB

**Install:**
```cmd
install.bat
```

Or manually:
1. Copy `app\build\outputs\apk\debug\app-debug.apk` to your phone
2. Open it and install

## Done! 🎉

The app should now be installed on your phone.

## Common Issues

### "Java is not recognized"
- Restart Command Prompt after installing Java
- Or set JAVA_HOME manually:
  ```cmd
  setx JAVA_HOME "C:\Program Files\Eclipse Adoptium\jdk-17.0.x"
  ```

### "SDK location not found"
- Edit `local.properties` and set correct SDK path
- Make sure path uses double backslashes: `C\:\\Android`

### Build takes forever
- First build downloads ~500MB of dependencies
- Requires good internet connection
- Subsequent builds are much faster (~30 seconds)

## Available Scripts

```cmd
build.bat    # Build debug APK
install.bat  # Install APK to connected device
test.bat     # Run unit tests
clean.bat    # Clean build artifacts
```

## Minimal Requirements

- **OS**: Windows 10 or 11
- **RAM**: 4GB minimum (8GB recommended)
- **Disk Space**: 5GB for SDK + 1GB for project
- **Internet**: Required for first build only

## What Gets Installed?

- **JDK 17** (~300 MB) - Java compiler
- **Android SDK** (~2-3 GB) - Build tools
- **Gradle dependencies** (~500 MB) - Build system

Total: ~4 GB

## Alternative: Pre-built APK

If you just want to test the app:
1. Run `build.bat` on any Windows PC with Java + Android SDK
2. Share the generated APK file
3. Install directly on phone

No need to set up build environment on every device!

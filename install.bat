@echo off
echo ================================
echo    Doodle APK Installer
echo ================================
echo.

REM Check if APK exists
if not exist "app\build\outputs\apk\debug\app-debug.apk" (
    echo [ERROR] APK not found!
    echo.
    echo Please build the APK first by running: build.bat
    echo.
    pause
    exit /b 1
)

echo APK found: app-debug.apk
echo.

REM Check if ADB is available
adb version >nul 2>&1
if errorlevel 1 (
    echo [ERROR] ADB not found!
    echo.
    echo ADB is required to install the APK.
    echo Install Android SDK platform-tools or:
    echo.
    echo Manual installation:
    echo 1. Copy app\build\outputs\apk\debug\app-debug.apk to your phone
    echo 2. Open the file on your phone
    echo 3. Allow installation from unknown sources if prompted
    echo 4. Tap Install
    echo.
    pause
    exit /b 1
)

echo Checking for connected devices...
adb devices
echo.

REM Install APK
echo Installing Doodle...
adb install -r app\build\outputs\apk\debug\app-debug.apk

if errorlevel 1 (
    echo.
    echo Installation failed!
    echo.
    echo Make sure:
    echo - USB debugging is enabled on your device
    echo - Device is connected and authorized
    echo - You have allowed installation permissions
    echo.
) else (
    echo.
    echo ================================
    echo Installation successful!
    echo You can now launch Doodle
    echo ================================
    echo.
)

pause

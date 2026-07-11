@echo off
color 0A
echo.
echo ========================================
echo    DOODLE SETUP ASSISTANT
echo ========================================
echo.
echo This script will help you set up the build environment.
echo.
pause

REM Check Java
echo [1/3] Checking Java installation...
echo.
java -version >nul 2>&1
if errorlevel 1 (
    echo [X] Java not found!
    echo.
    echo Please install JDK 17:
    echo.
    echo Option 1: Using winget
    echo    winget install EclipseAdoptium.Temurin.17.JDK
    echo.
    echo Option 2: Download from
    echo    https://adoptium.net/temurin/releases/?version=17
    echo.
    echo After installation, restart this script.
    echo.
    pause
    exit /b 1
) else (
    echo [OK] Java is installed
    java -version
    echo.
)

REM Check Android SDK
echo [2/3] Checking Android SDK...
echo.

if "%ANDROID_HOME%"=="" (
    echo [X] ANDROID_HOME not set
    echo.
    echo Do you have Android SDK installed? (Y/N)
    set /p HAS_SDK="> "
    
    if /i "%HAS_SDK%"=="Y" (
        echo.
        echo Please enter the path to your Android SDK:
        echo Example: C:\Android
        set /p SDK_PATH="> "
        
        if exist "!SDK_PATH!" (
            echo sdk.dir=!SDK_PATH:\=\\! > local.properties
            echo.
            echo [OK] SDK path saved to local.properties
        ) else (
            echo.
            echo [X] Path not found: !SDK_PATH!
        )
    ) else (
        echo.
        echo Please install Android SDK:
        echo.
        echo 1. Download from:
        echo    https://developer.android.com/studio#command-line-tools-only
        echo.
        echo 2. Extract to C:\Android
        echo.
        echo 3. Run this setup again
        echo.
        echo See BUILD_WITHOUT_ANDROID_STUDIO.md for detailed instructions
        echo.
        pause
        exit /b 1
    )
) else (
    echo [OK] ANDROID_HOME is set to: %ANDROID_HOME%
    echo sdk.dir=%ANDROID_HOME:\=\\% > local.properties
    echo.
)

REM Summary
echo [3/3] Setup Summary
echo.
echo Java: OK
echo Android SDK: Configured
echo.
echo ========================================
echo    READY TO BUILD!
echo ========================================
echo.
echo Next steps:
echo 1. Run: build.bat
echo 2. Wait for build to complete
echo 3. Install APK on your phone
echo.
echo First build may take 5-10 minutes (downloads dependencies)
echo.
pause

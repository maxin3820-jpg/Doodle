@echo off
echo ================================
echo    Doodle APK Builder
echo ================================
echo.

REM Check if Java is installed
echo [1/4] Checking Java installation...
java -version >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Java not found!
    echo.
    echo Please install JDK 17 from:
    echo https://adoptium.net/temurin/releases/?version=17
    echo.
    pause
    exit /b 1
)
echo [OK] Java is installed
echo.

REM Check if ANDROID_HOME is set
echo [2/4] Checking Android SDK...
if "%ANDROID_HOME%"=="" (
    echo [WARNING] ANDROID_HOME not set
    echo Creating local.properties...
    
    REM Try common locations
    if exist "C:\Android" (
        echo sdk.dir=C:\\Android > local.properties
        echo [OK] Found SDK at C:\Android
    ) else if exist "%LOCALAPPDATA%\Android\Sdk" (
        echo sdk.dir=%LOCALAPPDATA%\\Android\\Sdk > local.properties
        echo [OK] Found SDK at %LOCALAPPDATA%\Android\Sdk
    ) else (
        echo [ERROR] Android SDK not found!
        echo.
        echo Please install Android SDK or set ANDROID_HOME
        echo See BUILD_WITHOUT_ANDROID_STUDIO.md for instructions
        echo.
        pause
        exit /b 1
    )
) else (
    echo [OK] ANDROID_HOME is set to: %ANDROID_HOME%
)
echo.

REM Build the APK
echo [3/4] Building debug APK...
echo This may take a few minutes on first run (downloading dependencies)...
echo.
call gradlew.bat assembleDebug

if errorlevel 1 (
    echo.
    echo ================================
    echo    BUILD FAILED!
    echo ================================
    echo.
    echo Check the error messages above.
    echo Common issues:
    echo - Missing SDK components
    echo - Internet connection required for first build
    echo - Java version mismatch
    echo.
    echo See BUILD_WITHOUT_ANDROID_STUDIO.md for troubleshooting
    echo.
    pause
    exit /b 1
)

echo.
echo ================================
echo    BUILD SUCCESSFUL!
echo ================================
echo.

REM Show APK location
echo [4/4] APK generated successfully!
echo.
echo Location:
echo %CD%\app\build\outputs\apk\debug\app-debug.apk
echo.
echo Size:
for %%I in (app\build\outputs\apk\debug\app-debug.apk) do echo %%~zI bytes
echo.

REM Check if device connected
echo Checking for connected devices...
adb devices 2>nul | findstr "device$" >nul
if not errorlevel 1 (
    echo.
    echo Android device detected! 
    echo.
    set /p INSTALL="Do you want to install the APK now? (Y/N): "
    if /i "%INSTALL%"=="Y" (
        echo.
        echo Installing...
        adb install -r app\build\outputs\apk\debug\app-debug.apk
        if not errorlevel 1 (
            echo.
            echo Installation successful!
            echo You can now launch Doodle on your device.
        )
    )
)

echo.
echo ================================
echo To install on your phone:
echo 1. Copy app-debug.apk to your phone
echo 2. Open the file and install
echo 3. Or use: adb install app\build\outputs\apk\debug\app-debug.apk
echo ================================
echo.
pause

@echo off
echo.
echo ========================================
echo    DOODLE BUILD REQUIREMENTS CHECK
echo ========================================
echo.

set ERRORS=0

REM Check Java
echo [1/4] Checking Java...
java -version >nul 2>&1
if errorlevel 1 (
    echo [X] FAIL: Java not found
    set /a ERRORS+=1
    echo.
    echo Install JDK 17 from:
    echo https://adoptium.net/temurin/releases/?version=17
) else (
    echo [OK] PASS: Java found
    java -version 2>&1 | findstr "version"
)
echo.

REM Check JAVA_HOME
echo [2/4] Checking JAVA_HOME...
if "%JAVA_HOME%"=="" (
    echo [!] WARNING: JAVA_HOME not set
    echo This may cause issues. Set it to your JDK installation path.
) else (
    echo [OK] PASS: JAVA_HOME = %JAVA_HOME%
)
echo.

REM Check Android SDK
echo [3/4] Checking Android SDK...
if exist "local.properties" (
    echo [OK] PASS: local.properties found
    type local.properties | findstr "sdk.dir"
) else (
    if "%ANDROID_HOME%"=="" (
        echo [X] FAIL: Android SDK not configured
        set /a ERRORS+=1
        echo.
        echo Please set ANDROID_HOME or create local.properties
        echo See BUILD_WITHOUT_ANDROID_STUDIO.md for instructions
    ) else (
        echo [OK] PASS: ANDROID_HOME = %ANDROID_HOME%
    )
)
echo.

REM Check Gradle wrapper
echo [4/4] Checking Gradle wrapper...
if exist "gradlew.bat" (
    echo [OK] PASS: Gradle wrapper found
) else (
    echo [X] FAIL: gradlew.bat not found
    set /a ERRORS+=1
)
echo.

REM Summary
echo ========================================
if %ERRORS% EQU 0 (
    echo    STATUS: READY TO BUILD! ✓
    echo ========================================
    echo.
    echo Everything looks good.
    echo Run: build.bat
) else (
    echo    STATUS: NOT READY ✗
    echo ========================================
    echo.
    echo Found %ERRORS% issue(s).
    echo Please fix the errors above.
    echo.
    echo For help, see:
    echo - QUICK_START.md
    echo - BUILD_WITHOUT_ANDROID_STUDIO.md
)
echo.

pause

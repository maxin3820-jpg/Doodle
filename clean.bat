@echo off
echo Cleaning Doodle project...
echo.

call gradlew.bat clean

if errorlevel 1 (
    echo Clean failed!
) else (
    echo Clean successful!
    echo Build artifacts removed.
)

echo.
pause

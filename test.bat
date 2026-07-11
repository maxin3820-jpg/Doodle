@echo off
echo ================================
echo    Running Doodle Tests
echo ================================
echo.

echo Running unit tests...
echo.

call gradlew.bat test

if errorlevel 1 (
    echo.
    echo ================================
    echo    TESTS FAILED!
    echo ================================
    echo.
    echo Check test reports at:
    echo app\build\reports\tests\testDebugUnitTest\index.html
    echo.
) else (
    echo.
    echo ================================
    echo    ALL TESTS PASSED!
    echo ================================
    echo.
    echo Test reports available at:
    echo app\build\reports\tests\testDebugUnitTest\index.html
    echo.
)

pause

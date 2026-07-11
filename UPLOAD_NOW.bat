@echo off
color 0A
echo.
echo ========================================
echo    UPLOAD DOODLE TO GITHUB
echo ========================================
echo.

REM Your GitHub details are already set!
set GITHUB_USER=maxin3820-jpg
set REPO_NAME=Doodle
set REPO_URL=https://github.com/maxin3820-jpg/Doodle.git

REM Check if git is installed
git --version >nul 2>&1
if errorlevel 1 (
    echo [X] Git is not installed!
    echo.
    echo Please install Git first:
    echo Run: winget install Git.Git
    echo.
    echo After installing, restart this script.
    echo.
    pause
    exit /b 1
)

echo [OK] Git is installed
echo.

REM Check if already initialized
if exist ".git" (
    echo [!] Git repository already initialized
    echo.
    goto push_changes
)

echo Step 1: Initialize Git Repository
echo.
git init
git branch -M main
echo [OK] Repository initialized
echo.

echo Step 2: Configure Git (if not already done)
echo.
git config user.name "%GITHUB_USER%" 2>nul
git config user.email "%GITHUB_USER%@users.noreply.github.com" 2>nul
echo [OK] Git configured
echo.

echo Step 3: Add all files
echo.
git add .
echo [OK] Files staged
echo.

echo Step 4: Create commit
echo.
git commit -m "Initial commit - Doodle task manager app"
if errorlevel 1 (
    echo [!] Nothing to commit or commit failed
    echo Trying to configure git user...
    git config user.name "%GITHUB_USER%"
    git config user.email "%GITHUB_USER%@users.noreply.github.com"
    git commit -m "Initial commit - Doodle task manager app"
)
echo [OK] Commit created
echo.

echo Step 5: Link to GitHub repository
echo.
echo Repository: %REPO_URL%
echo.

git remote add origin %REPO_URL% 2>nul
if errorlevel 1 (
    echo [!] Remote already exists, updating...
    git remote set-url origin %REPO_URL%
)
echo [OK] Remote repository linked
echo.

:push_changes
echo ========================================
echo    READY TO PUSH TO GITHUB!
echo ========================================
echo.
echo Repository: https://github.com/%GITHUB_USER%/%REPO_NAME%
echo.
echo IMPORTANT: When asked for password, you must use a
echo Personal Access Token (NOT your GitHub password)
echo.
echo How to get a token:
echo 1. Go to: https://github.com/settings/tokens
echo 2. Click "Generate new token (classic)"
echo 3. Give it a name: "Doodle upload"
echo 4. Check the "repo" checkbox
echo 5. Click "Generate token"
echo 6. Copy the token (starts with github_pat_...)
echo 7. Paste it here when asked for password
echo.
echo The token looks like: github_pat_XXXXX...
echo.
pause

echo.
echo Pushing to GitHub...
echo.

git push -u origin main

if errorlevel 1 (
    echo.
    echo ========================================
    echo    PUSH FAILED
    echo ========================================
    echo.
    echo Possible reasons:
    echo 1. Wrong credentials (make sure to use Personal Access Token)
    echo 2. Repository doesn't exist or is wrong
    echo 3. No internet connection
    echo.
    echo Try again? Make sure you:
    echo - Use the token from: https://github.com/settings/tokens
    echo - NOT your GitHub password
    echo.
    pause
    exit /b 1
)

echo.
echo ========================================
echo    SUCCESS! CODE IS ON GITHUB!
echo ========================================
echo.
echo Your code is now at:
echo https://github.com/%GITHUB_USER%/%REPO_NAME%
echo.
echo GitHub Actions is building your APK right now!
echo.
echo To download the APK:
echo 1. Go to: https://github.com/%GITHUB_USER%/%REPO_NAME%/actions
echo 2. Wait for green checkmark (5-10 minutes)
echo 3. Click on the workflow run
echo 4. Scroll down to "Artifacts"
echo 5. Download "doodle-debug-apk"
echo.
echo For detailed instructions, open:
echo DOWNLOAD_YOUR_APK.txt
echo.
pause

@echo off
color 0A
echo.
echo ========================================
echo    UPLOAD DOODLE TO GITHUB
echo ========================================
echo.

REM Check if git is installed
git --version >nul 2>&1
if errorlevel 1 (
    echo [X] Git is not installed!
    echo.
    echo Please install Git first:
    echo 1. Run: winget install Git.Git
    echo    OR
    echo 2. Download from: https://git-scm.com/download/win
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
    set /p REINIT="Do you want to reinitialize? This will erase git history (Y/N): "
    if /i "%REINIT%"=="Y" (
        echo Removing .git folder...
        rmdir /s /q .git
    ) else (
        echo Keeping existing repository.
        goto push_changes
    )
)

echo Step 1: Initialize Git Repository
echo.
git init
git branch -M main
echo [OK] Repository initialized
echo.

echo Step 2: Add all files
echo.
git add .
echo [OK] Files staged
echo.

echo Step 3: Create first commit
echo.
git commit -m "Initial commit - Doodle task manager app"
if errorlevel 1 (
    echo [!] Commit failed. Setting git user config...
    echo.
    echo Please enter your name:
    set /p GIT_NAME="> "
    echo Please enter your email:
    set /p GIT_EMAIL="> "
    git config user.name "%GIT_NAME%"
    git config user.email "%GIT_EMAIL%"
    git commit -m "Initial commit - Doodle task manager app"
)
echo [OK] Commit created
echo.

echo ========================================
echo    GITHUB REPOSITORY SETUP
echo ========================================
echo.
echo Before continuing, you need to:
echo.
echo 1. Go to: https://github.com
echo 2. Log in to your account (or create one)
echo 3. Click the + icon (top right)
echo 4. Select "New repository"
echo 5. Repository name: doodle-app (or your choice)
echo 6. Keep it Public or Private (your choice)
echo 7. DO NOT check "Add a README file"
echo 8. Click "Create repository"
echo.
echo After creating the repository on GitHub, come back here.
echo.
pause

echo.
echo Step 4: Link to GitHub repository
echo.
echo Please enter your GitHub username:
set /p GITHUB_USER="> "
echo.
echo Please enter your repository name (e.g., doodle-app):
set /p REPO_NAME="> "
echo.

set REPO_URL=https://github.com/%GITHUB_USER%/%REPO_NAME%.git

echo Repository URL: %REPO_URL%
echo.
set /p CONFIRM="Is this correct? (Y/N): "
if /i not "%CONFIRM%"=="Y" (
    echo Please run this script again with correct details.
    pause
    exit /b 1
)

echo.
echo Adding remote repository...
git remote add origin %REPO_URL%
if errorlevel 1 (
    echo [!] Failed to add remote. It may already exist.
    git remote set-url origin %REPO_URL%
)
echo [OK] Remote repository linked
echo.

:push_changes
echo Step 5: Push to GitHub
echo.
echo You may be asked to log in to GitHub.
echo Note: Use a Personal Access Token as password (not your GitHub password)
echo.
echo To create a token:
echo 1. Go to: https://github.com/settings/tokens
echo 2. Click "Generate new token (classic)"
echo 3. Check "repo" scope
echo 4. Copy the token and use it as password
echo.
pause

echo Pushing to GitHub...
git push -u origin main

if errorlevel 1 (
    echo.
    echo ========================================
    echo    PUSH FAILED
    echo ========================================
    echo.
    echo Common issues:
    echo 1. Wrong credentials - Use Personal Access Token
    echo 2. Repository doesn't exist on GitHub
    echo 3. Network connection issues
    echo.
    echo See GITHUB_ACTIONS_GUIDE.md for troubleshooting
    echo.
    pause
    exit /b 1
)

echo.
echo ========================================
echo    SUCCESS!
echo ========================================
echo.
echo Your code is now on GitHub!
echo.
echo GitHub Actions is building your APK now...
echo.
echo To download the APK:
echo 1. Go to: https://github.com/%GITHUB_USER%/%REPO_NAME%
echo 2. Click "Actions" tab
echo 3. Wait for build to complete (5-10 minutes)
echo 4. Download the APK from "Artifacts" section
echo.
echo For detailed instructions, see:
echo GITHUB_ACTIONS_GUIDE.md
echo.
pause

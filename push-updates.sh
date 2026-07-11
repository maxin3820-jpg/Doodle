#!/bin/bash

# Push Updates to GitHub
# Run this in Git Bash

echo "========================================"
echo "   PUSH UPDATES TO GITHUB"
echo "========================================"
echo ""

# Check if git is initialized
if [ ! -d ".git" ]; then
    echo "Initializing git repository..."
    git init
    git branch -M main
fi

# Configure git user (if not already set)
git config user.name "maxin3820-jpg" 2>/dev/null
git config user.email "maxin3820-jpg@users.noreply.github.com" 2>/dev/null

# Check if remote exists
if ! git remote | grep -q "origin"; then
    echo "Adding remote repository..."
    git remote add origin https://github.com/maxin3820-jpg/Doodle.git
else
    echo "Remote already exists, updating URL..."
    git remote set-url origin https://github.com/maxin3820-jpg/Doodle.git
fi

echo ""
echo "Adding all files..."
git add .

echo ""
echo "Creating commit..."
git commit -m "Update workflow and fix build issues"

if [ $? -ne 0 ]; then
    echo ""
    echo "Nothing to commit or commit failed."
    echo "Trying to force update..."
fi

echo ""
echo "========================================"
echo "   READY TO PUSH!"
echo "========================================"
echo ""
echo "Repository: https://github.com/maxin3820-jpg/Doodle"
echo ""
echo "IMPORTANT: When asked for password, use your"
echo "Personal Access Token (NOT your GitHub password)"
echo ""
echo "Your token: github_pat_11CBWY3HQ0XCn2iWpnWxYY_..."
echo ""
read -p "Press Enter to continue..."

echo ""
echo "Pushing to GitHub..."
git push -u origin main

if [ $? -eq 0 ]; then
    echo ""
    echo "========================================"
    echo "   SUCCESS!"
    echo "========================================"
    echo ""
    echo "Your updates are on GitHub!"
    echo ""
    echo "Check build status at:"
    echo "https://github.com/maxin3820-jpg/Doodle/actions"
    echo ""
else
    echo ""
    echo "========================================"
    echo "   PUSH FAILED!"
    echo "========================================"
    echo ""
    echo "Make sure you used the Personal Access Token"
    echo "NOT your GitHub password!"
    echo ""
fi

read -p "Press Enter to exit..."

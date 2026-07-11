# GitHub Actions - Automatic APK Build Guide

## 🎯 What This Does

GitHub Actions will automatically build your APK file in the cloud (for free!) whenever you push code to GitHub. No need to install Java or Android SDK on your computer!

## 📋 Prerequisites

- A GitHub account (free) - Sign up at https://github.com
- Git installed on your computer (or GitHub Desktop)

## 🚀 Step-by-Step Process

### Step 1: Install Git (if not installed)

**Option A: Using winget**
```cmd
winget install Git.Git
```

**Option B: Download**
- Visit: https://git-scm.com/download/win
- Download and install

**Verify installation:**
```cmd
git --version
```

### Step 2: Create a GitHub Repository

1. Go to https://github.com
2. Click the **"+"** icon (top right) → **"New repository"**
3. Fill in:
   - **Repository name**: `doodle-app` (or any name you like)
   - **Description**: "Minimalist task manager for Android"
   - **Visibility**: Choose Public or Private
   - **DO NOT** check "Add a README file"
4. Click **"Create repository"**

### Step 3: Upload Your Code to GitHub

Open Command Prompt in the Doodle folder:

```cmd
cd c:\Users\zc\Desktop\othr\Doodle

# Initialize git repository
git init

# Add all files
git add .

# Commit files
git commit -m "Initial commit - Doodle app"

# Add your GitHub repository (replace USERNAME and REPO_NAME)
git remote add origin https://github.com/USERNAME/REPO_NAME.git

# Push to GitHub
git branch -M main
git push -u origin main
```

**Example:**
If your GitHub username is `john123` and repo is `doodle-app`:
```cmd
git remote add origin https://github.com/john123/doodle-app.git
git push -u origin main
```

**Note:** You may be asked to log in to GitHub. Use your GitHub credentials.

### Step 4: GitHub Actions Builds Automatically! 🎉

Once you push the code, GitHub Actions will automatically:
1. Set up Java 17
2. Set up Android SDK
3. Build your APK
4. Make it available for download

### Step 5: Download Your APK

1. Go to your GitHub repository: `https://github.com/USERNAME/REPO_NAME`
2. Click the **"Actions"** tab at the top
3. You'll see a workflow run (green ✓ when done, takes ~5-10 minutes)
4. Click on the workflow run
5. Scroll down to **"Artifacts"** section
6. Click **"doodle-debug-apk"** to download
7. Extract the zip file
8. You'll find **`app-debug.apk`** inside!

### Step 6: Install APK on Your Phone

**Method 1: Direct Transfer**
1. Copy `app-debug.apk` to your phone
2. Open it on your phone
3. Allow installation from unknown sources if prompted
4. Install!

**Method 2: Using ADB (if you have it)**
```cmd
adb install app-debug.apk
```

## 🔄 Making Changes & Rebuilding

Whenever you want to rebuild:

```cmd
cd c:\Users\zc\Desktop\othr\Doodle

# Make your code changes...

# Add changes
git add .

# Commit changes
git commit -m "Description of your changes"

# Push to GitHub
git push

# GitHub Actions will automatically build a new APK!
```

## 📸 Visual Guide

### Where to Find Actions Tab
```
GitHub Repository Page
├── Code
├── Issues
├── Pull requests
├── Actions          ← CLICK HERE
├── Projects
└── Settings
```

### Where to Download APK
```
Actions Tab
└── Click on latest workflow run
    └── Scroll down to "Artifacts"
        └── doodle-debug-apk (Click to download)
            └── Extract ZIP
                └── app-debug.apk
```

## ⏱️ Build Time

- **First build**: ~5-10 minutes
- **Subsequent builds**: ~3-5 minutes

## 💰 Cost

**FREE!** GitHub provides 2,000 minutes/month for free accounts for public and private repositories.

## 🔧 Troubleshooting

### Problem: Git push asks for password but password doesn't work

**Solution:** GitHub no longer accepts passwords. You need a Personal Access Token:

1. Go to GitHub → Settings (your profile) → Developer settings → Personal access tokens → Tokens (classic)
2. Click "Generate new token (classic)"
3. Give it a name: "Doodle app"
4. Check "repo" scope
5. Click "Generate token"
6. Copy the token (you won't see it again!)
7. Use this token as your password when git asks

### Problem: "Permission denied (publickey)"

**Solution:** Use HTTPS instead of SSH:
```cmd
git remote set-url origin https://github.com/USERNAME/REPO_NAME.git
```

### Problem: Build fails on GitHub Actions

**Solution:** 
1. Go to Actions tab
2. Click on the failed workflow
3. Check the error logs
4. Most common issues:
   - Missing `gradlew` file (I created it for you)
   - Wrong file permissions (workflow fixes this automatically)

### Problem: Can't find the APK download

**Solution:**
1. Make sure the workflow shows a green checkmark ✓
2. Refresh the page
3. Scroll all the way down to "Artifacts" section
4. If no artifacts, check the build logs for errors

## 🎨 Alternative: Using GitHub Desktop (Easier)

If command line seems complicated:

1. Download **GitHub Desktop**: https://desktop.github.com
2. Install it
3. Sign in with your GitHub account
4. Click "Add" → "Add existing repository"
5. Select the Doodle folder
6. Click "Publish repository"
7. Push your changes with the "Push origin" button

Much easier with a GUI!

## 📱 Quick Reference

```cmd
# Initial setup (once)
cd c:\Users\zc\Desktop\othr\Doodle
git init
git add .
git commit -m "Initial commit"
git remote add origin https://github.com/USERNAME/REPO_NAME.git
git push -u origin main

# After making changes
git add .
git commit -m "Your changes description"
git push

# Download APK from:
# https://github.com/USERNAME/REPO_NAME/actions
```

## 🌟 Pro Tips

1. **Keep your repo private** if you don't want others to see the code
2. **Star your own repo** to find it easily later
3. **Watch the Actions tab** to see build progress in real-time
4. **Download APKs immediately** - artifacts expire after 90 days
5. **Use descriptive commit messages** so you know what changed

## 🔐 Security Note

The APK built by GitHub Actions will be signed with a debug key (not production-ready). For publishing to Google Play Store, you'll need to set up proper signing with secrets.

## ✅ Benefits of This Approach

✓ No need to install Java/SDK on your computer  
✓ Build works on any computer (even Mac/Linux)  
✓ Free and unlimited builds  
✓ Automatic - just push code  
✓ Can share repo with others  
✓ Version control built-in  
✓ Professional workflow  

## 📞 Need Help?

Common issues are covered in troubleshooting above. If you get stuck:
1. Check the GitHub Actions logs for specific errors
2. Make sure all files were pushed (check on GitHub web interface)
3. Verify the workflow file is at `.github/workflows/build-apk.yml`

---

## 🎉 Summary

1. Create GitHub account
2. Install Git
3. Create new repository on GitHub
4. Push code using git commands
5. Wait 5-10 minutes
6. Download APK from Actions tab
7. Install on phone
8. Done!

No Java, no Android SDK, no build tools needed on your computer! 🚀

# 🚀 Railway Deployment Guide for Qualique

## Prerequisites
- A [Railway](https://railway.app) account (free tier available)
- Git installed on your computer
- Your code pushed to a GitHub repository

---

## Step 1: Push Your Code to GitHub

```bash
# Initialize git (if not already done)
git init

# Add all files
git add .

# Commit
git commit -m "Initial commit - Ready for Railway deployment"

# Add your GitHub repository as remote
git remote add origin https://github.com/YOUR_USERNAME/qualique.git

# Push to GitHub
git push -u origin main
```

---

## Step 2: Deploy on Railway

### Option A: Deploy via Railway Dashboard (Recommended)

1. Go to [railway.app](https://railway.app) and sign in
2. Click **"New Project"**
3. Select **"Deploy from GitHub repo"**
4. Authorize Railway to access your GitHub
5. Select your `qualique` repository
6. Railway will automatically detect the Dockerfile and start building

### Option B: Deploy via Railway CLI

```bash
# Install Railway CLI
npm install -g @railway/cli

# Login to Railway
railway login

# Initialize project
railway init

# Deploy
railway up
```

---

## Step 3: Add PostgreSQL Database

1. In your Railway project dashboard, click **"+ New"**
2. Select **"Database"** → **"Add PostgreSQL"**
3. Railway will automatically create the database and set `DATABASE_URL`

---

## Step 4: Configure Environment Variables

In Railway dashboard → Your project → **Variables** tab, add:

| Variable | Value | Description |
|----------|-------|-------------|
| `JWT_SECRET` | `your-super-secret-random-key-minimum-256-bits` | **Required**: Generate a strong random key |
| `SPRING_PROFILES_ACTIVE` | `prod` | Activates production configuration |
| `H2_CONSOLE_ENABLED` | `false` | Disables H2 console in production |
| `COOKIE_SECURE` | `true` | Enables secure cookies (HTTPS) |

### Generate a Secure JWT Secret
```bash
# On Linux/Mac
openssl rand -base64 64

# On Windows PowerShell
[Convert]::ToBase64String((1..64 | ForEach-Object { Get-Random -Maximum 256 }) -as [byte[]])
```

---

## Step 5: Generate Domain

1. In Railway dashboard → Your project → **Settings**
2. Click **"Generate Domain"** to get a public URL
3. Your app will be available at `https://your-app.up.railway.app`

---

## Environment Variables Reference

### Automatically Set by Railway:
| Variable | Description |
|----------|-------------|
| `PORT` | The port your app should listen on |
| `DATABASE_URL` | PostgreSQL connection string (when you add PostgreSQL) |

### You Must Set:
| Variable | Example | Description |
|----------|---------|-------------|
| `JWT_SECRET` | `abc123...` | Secret key for JWT tokens |
| `SPRING_PROFILES_ACTIVE` | `prod` | Use production profile |

### Optional:
| Variable | Default | Description |
|----------|---------|-------------|
| `H2_CONSOLE_ENABLED` | `true` | Set to `false` in production |
| `COOKIE_SECURE` | `false` | Set to `true` for HTTPS |
| `LOG_LEVEL` | `INFO` | Application log level |

---

## Post-Deployment Checklist

- [ ] PostgreSQL database added
- [ ] `JWT_SECRET` environment variable set
- [ ] `SPRING_PROFILES_ACTIVE=prod` set
- [ ] `H2_CONSOLE_ENABLED=false` set
- [ ] Domain generated
- [ ] Test the health endpoint: `https://your-domain.up.railway.app/api/health`
- [ ] Test the website: `https://your-domain.up.railway.app`
- [ ] Login to admin panel and change the default password
- [ ] Add some products through the admin panel

---

## Troubleshooting

### Build Fails
- Check the build logs in Railway dashboard
- Ensure Java 21 is specified in the Dockerfile

### App Crashes on Start
- Check the deploy logs
- Verify `DATABASE_URL` is set (add PostgreSQL if not)
- Check `JWT_SECRET` is set

### Database Connection Issues
- Ensure PostgreSQL is added to your project
- Railway automatically links services - `DATABASE_URL` should be auto-set

### Health Check Fails
- The health endpoint is at `/api/health`
- Allow 60 seconds for the app to start

---

## ⚠️ Important: File Uploads

Railway uses **ephemeral storage** - files uploaded to the local filesystem will be **lost on every deploy/restart**.

### Current Limitation:
Product images uploaded through the admin panel are stored locally and will be lost.

### Recommended Solutions:
1. **Cloudinary** (Free tier available) - Easy image hosting
2. **AWS S3** - Scalable cloud storage
3. **Railway Volumes** (Paid) - Persistent storage on Railway

For now, use image URLs from external sources (like imgur, cloudinary) when adding products.

---

## Estimated Costs (Railway Free Tier)

- **Execution Hours**: 500 hours/month free
- **RAM**: 512MB free
- **PostgreSQL**: Included in free tier (limited)

For a small business website, the free tier should be sufficient for development/testing.

---

## Security Reminders

⚠️ **IMPORTANT**: After deployment:
1. Login to admin panel: `https://your-domain.up.railway.app/admin/login.html`
2. Default credentials: `admin` / `admin123`
3. You'll be forced to change your password on first login
4. Use a strong password with uppercase, lowercase, numbers, and special characters

---

## Support

- Railway Docs: https://docs.railway.app
- Spring Boot Docs: https://docs.spring.io/spring-boot/docs/current/reference/html/

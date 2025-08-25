# UBN Cross Checker - Distribution Guide

## Overview
This guide explains how to create and distribute the UBN Cross Checker application to end users who don't have a development environment.

## Quick Distribution Steps

1. **Build Distribution Package:**
   ```bash
   .\create-distribution.bat
   ```

2. **Create ZIP Archive:**
   - Compress the entire `dist` folder into `UBN-Cross-Checker-v1.0.zip`

3. **Distribute:**
   - Share the ZIP file with users
   - Users extract and run `UBN-Cross-Checker.bat`

## What Gets Included in Distribution

### Application Files
- `ubn-cross-checker-1.0-SNAPSHOT.jar` - Main application
- `lib\*.jar` - All dependencies (50+ JARs including JavaFX)
- `data\*` - Database and configuration files

### User Files
- `UBN-Cross-Checker.bat` - Easy launcher script
- `README.txt` - User installation guide
- `VERSION.txt` - Build information

## Distribution Package Features

### Automatic Checks
- ✅ Verifies Java 17+ is installed
- ✅ Checks for required JavaFX libraries
- ✅ Provides helpful error messages
- ✅ Automatic fallback mechanisms

### User-Friendly
- ✅ Single-click execution
- ✅ No technical knowledge required
- ✅ Clear installation instructions
- ✅ Troubleshooting guide included

## System Requirements for End Users

- **Operating System:** Windows 10 or higher
- **Java Runtime:** Java 17 or higher (from https://adoptium.net/)
- **Memory:** 512MB RAM minimum
- **Disk Space:** ~100MB for application + data

## Advanced Distribution Options

### Option 1: Self-Contained EXE (Current)
- Uses Launch4j to create `UBNChecker-1.0-SNAPSHOT.exe`
- Requires Java to be installed on target system
- Smaller download size (~40MB)

### Option 2: Full JRE Bundle (Recommended for non-technical users)
- Bundle Java Runtime with application
- No Java installation required by users
- Larger download size (~150MB)

### Option 3: Windows Installer
- Create MSI installer using WiX or similar
- Professional installation experience
- Registry integration and uninstaller

## Testing Distribution

1. **Test on clean Windows machine**
2. **Test with different Java versions**
3. **Test with/without admin privileges**
4. **Test antivirus compatibility**

## Troubleshooting Common Issues

### "Java is not installed"
- User needs Java 17+ from https://adoptium.net/
- Check PATH environment variable

### "JavaFX runtime components missing"
- Ensure all lib/*.jar files are present
- Try running batch file as administrator

### Antivirus False Positives
- Sign the executable (optional)
- Add to antivirus whitelist
- Use VirusTotal to verify clean scan

## Version Management

Each distribution includes:
- Build timestamp
- Version number
- JavaFX version info
- Java target version

## File Size Estimates

- Main JAR: ~1MB
- Dependencies: ~35MB
- JavaFX libraries: ~25MB
- **Total: ~60MB**

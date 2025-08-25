@echo off
echo Building distribution package for UBN Cross Checker...
echo.

REM Clean and build the project
echo Step 1: Cleaning and building project...
call mvn clean package
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Maven build failed!
    pause
    exit /b 1
)

REM Create distribution directory
echo Step 2: Creating distribution directory...
if exist "dist" rmdir /s /q "dist"
mkdir "dist"
mkdir "dist\lib"
mkdir "dist\data"

REM Copy main JAR
echo Step 3: Copying application JAR...
copy "target\ubn-cross-checker-1.0-SNAPSHOT.jar" "dist\"

REM Copy all dependencies
echo Step 4: Copying dependencies...
copy "target\modules\*" "dist\lib\"

REM Copy data files
echo Step 5: Copying data files...
if exist "data\*" copy "data\*" "dist\data\"

REM Create launcher script
echo Step 6: Creating launcher script...
(
echo @echo off
echo setlocal
echo.
echo echo Starting UBN Cross Checker...
echo echo.
echo.
echo REM Check Java installation
echo java -version ^>nul 2^>^&1
echo if %%ERRORLEVEL%% NEQ 0 ^(
echo     echo ERROR: Java is not installed or not in PATH!
echo     echo Please install Java 17 or higher from: https://adoptium.net/
echo     pause
echo     exit /b 1
echo ^)
echo.
echo REM Check JavaFX JARs
echo if not exist "lib\javafx-base-21.0.2-win.jar" ^(
echo     echo ERROR: JavaFX libraries not found!
echo     echo Please ensure all files are extracted properly.
echo     pause
echo     exit /b 1
echo ^)
echo.
echo REM Set JavaFX module path
echo set JAVAFX_PATH=lib\javafx-base-21.0.2-win.jar;lib\javafx-controls-21.0.2-win.jar;lib\javafx-fxml-21.0.2-win.jar;lib\javafx-graphics-21.0.2-win.jar
echo.
echo REM Run application
echo java --module-path "%%JAVAFX_PATH%%" ^^^
echo      --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base ^^^
echo      --add-opens javafx.graphics/com.sun.javafx.application=ALL-UNNAMED ^^^
echo      --add-opens java.base/java.lang.reflect=ALL-UNNAMED ^^^
echo      --add-opens java.base/java.lang=ALL-UNNAMED ^^^
echo      --add-opens java.desktop/java.awt=ALL-UNNAMED ^^^
echo      -Djava.awt.headless=false ^^^
echo      -Dprism.order=sw ^^^
echo      -Djavafx.platform=desktop ^^^
echo      -Dfile.encoding=UTF-8 ^^^
echo      -cp "ubn-cross-checker-1.0-SNAPSHOT.jar;lib\*" ^^^
echo      org.lw5hr.forms.UbnMain
echo.
echo if %%ERRORLEVEL%% NEQ 0 ^(
echo     echo Application failed to start. Error level: %%ERRORLEVEL%%
echo     pause
echo ^) else ^(
echo     echo Application closed successfully.
echo ^)
echo.
echo endlocal
) > "dist\UBN-Cross-Checker.bat"

REM Create README for users
echo Step 7: Creating user README...
(
echo # UBN Cross Checker - User Guide
echo.
echo ## System Requirements
echo - Windows 10 or higher
echo - Java 17 or higher ^(download from https://adoptium.net/^)
echo.
echo ## Installation
echo 1. Extract all files from the ZIP archive to a folder
echo 2. Ensure Java 17+ is installed on your system
echo 3. Double-click "UBN-Cross-Checker.bat" to run the application
echo.
echo ## Usage
echo - The application will open a JavaFX GUI window
echo - Use the interface to load and analyze your contest logs
echo - Results will be displayed in the application window
echo.
echo ## Troubleshooting
echo - If you get "Java is not installed": Install Java 17+ from https://adoptium.net/
echo - If you get "JavaFX runtime components missing": Try running the batch file as administrator
echo - If the window doesn't appear: Check Windows Defender/antivirus settings
echo.
echo ## Support
echo For issues or questions, contact the developer.
echo.
echo ## Version Information
echo Version: 1.0-SNAPSHOT
echo Build Date: %DATE%
echo JavaFX Version: 21.0.2
echo Java Target: 17
) > "dist\README.txt"

REM Create version info file
echo Step 8: Creating version info...
(
echo UBN Cross Checker Distribution Package
echo Build Date: %DATE%
echo Build Time: %TIME%
echo Java Target Version: 17
echo JavaFX Version: 21.0.2
echo Maven Version: 1.0-SNAPSHOT
) > "dist\VERSION.txt"

echo.
echo ========================================
echo Distribution package created successfully!
echo ========================================
echo.
echo Package contents:
dir /b "dist"
echo.
echo To distribute:
echo 1. Compress the 'dist' folder into a ZIP file
echo 2. Share the ZIP file with users
echo 3. Users should extract and run 'UBN-Cross-Checker.bat'
echo.
echo Distribution folder size:
for /f "tokens=3" %%a in ('dir "dist" /-c ^| find "File(s)"') do echo %%a bytes
echo.
pause

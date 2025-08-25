@echo off
echo Creating Self-Contained Distribution with Bundled JRE...
echo.

REM Build the project first
echo Step 1: Building project...
call mvn clean package
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Maven build failed!
    pause
    exit /b 1
)

REM Create self-contained distribution directory
echo Step 2: Creating self-contained distribution...
if exist "dist-standalone" rmdir /s /q "dist-standalone"
mkdir "dist-standalone"

REM Step 3: Setting up Java Runtime...
echo Step 3: Setting up Java Runtime...

REM Use current Java installation as bundled JRE
echo Using current Java installation as bundled JRE...

REM Check if JAVA_HOME is set, if not try to detect it
if "%JAVA_HOME%"=="" (
    echo JAVA_HOME not set, detecting from java command...
    for /f "tokens=*" %%i in ('where java 2^>nul') do (
        set "JAVA_EXE=%%i"
        goto :found_java
    )
    echo ERROR: Could not find Java installation!
    echo Please ensure Java 17+ is installed and accessible via PATH
    pause
    exit /b 1

    :found_java
    REM Extract JAVA_HOME from java.exe path (remove \bin\java.exe)
    for %%i in ("%JAVA_EXE%") do set "JAVA_HOME=%%~dpi"
    set "JAVA_HOME=%JAVA_HOME:~0,-5%"
)

echo Using Java from: %JAVA_HOME%
if not exist "%JAVA_HOME%\bin\java.exe" (
    echo ERROR: Invalid Java installation - java.exe not found
    pause
    exit /b 1
)

echo Copying Java Runtime Environment...
echo This may take a few minutes...
xcopy "%JAVA_HOME%" "dist-standalone\jre\" /E /I /Q
if %ERRORLEVEL% NEQ 0 (
    echo WARNING: Some files may not have copied, but continuing...
)

REM Verify the JRE was copied
if not exist "dist-standalone\jre\bin\java.exe" (
    echo ERROR: Java runtime copy failed
    pause
    exit /b 1
)

echo Java Runtime copied successfully!

REM Step 4: Copy application files
echo Step 4: Copying application files...
copy "target\ubn-cross-checker-1.0-SNAPSHOT.jar" "dist-standalone\"
mkdir "dist-standalone\lib"
copy "target\modules\*" "dist-standalone\lib\"

REM Copy data files
if exist "data" (
    mkdir "dist-standalone\data"
    copy "data\*" "dist-standalone\data\"
)

REM Step 5: Create launcher script
echo Step 5: Creating standalone launcher...
echo @echo off > "dist-standalone\UBN-Cross-Checker-Standalone.bat"
echo setlocal >> "dist-standalone\UBN-Cross-Checker-Standalone.bat"
echo. >> "dist-standalone\UBN-Cross-Checker-Standalone.bat"
echo echo Starting UBN Cross Checker (Standalone)... >> "dist-standalone\UBN-Cross-Checker-Standalone.bat"
echo echo. >> "dist-standalone\UBN-Cross-Checker-Standalone.bat"
echo. >> "dist-standalone\UBN-Cross-Checker-Standalone.bat"
echo REM Use bundled JRE >> "dist-standalone\UBN-Cross-Checker-Standalone.bat"
echo set JAVA_HOME=%%~dp0jre >> "dist-standalone\UBN-Cross-Checker-Standalone.bat"
echo set PATH=%%JAVA_HOME%%\bin;%%PATH%% >> "dist-standalone\UBN-Cross-Checker-Standalone.bat"
echo. >> "dist-standalone\UBN-Cross-Checker-Standalone.bat"
echo REM Check bundled Java >> "dist-standalone\UBN-Cross-Checker-Standalone.bat"
echo if not exist "%%JAVA_HOME%%\bin\java.exe" ( >> "dist-standalone\UBN-Cross-Checker-Standalone.bat"
echo     echo ERROR: Bundled Java runtime not found! >> "dist-standalone\UBN-Cross-Checker-Standalone.bat"
echo     echo Please ensure all files are extracted properly. >> "dist-standalone\UBN-Cross-Checker-Standalone.bat"
echo     pause >> "dist-standalone\UBN-Cross-Checker-Standalone.bat"
echo     exit /b 1 >> "dist-standalone\UBN-Cross-Checker-Standalone.bat"
echo ^) >> "dist-standalone\UBN-Cross-Checker-Standalone.bat"
echo. >> "dist-standalone\UBN-Cross-Checker-Standalone.bat"
echo REM Display Java version >> "dist-standalone\UBN-Cross-Checker-Standalone.bat"
echo echo Using bundled Java: >> "dist-standalone\UBN-Cross-Checker-Standalone.bat"
echo "%%JAVA_HOME%%\bin\java.exe" -version >> "dist-standalone\UBN-Cross-Checker-Standalone.bat"
echo echo. >> "dist-standalone\UBN-Cross-Checker-Standalone.bat"
echo. >> "dist-standalone\UBN-Cross-Checker-Standalone.bat"
echo REM Set JavaFX module path >> "dist-standalone\UBN-Cross-Checker-Standalone.bat"
echo set JAVAFX_PATH=lib\javafx-base-21.0.2-win.jar;lib\javafx-controls-21.0.2-win.jar;lib\javafx-fxml-21.0.2-win.jar;lib\javafx-graphics-21.0.2-win.jar >> "dist-standalone\UBN-Cross-Checker-Standalone.bat"
echo. >> "dist-standalone\UBN-Cross-Checker-Standalone.bat"
echo REM Run application >> "dist-standalone\UBN-Cross-Checker-Standalone.bat"
echo "%%JAVA_HOME%%\bin\java.exe" --module-path "%%JAVAFX_PATH%%" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base --add-opens javafx.graphics/com.sun.javafx.application=ALL-UNNAMED --add-opens java.base/java.lang.reflect=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.desktop/java.awt=ALL-UNNAMED -Djava.awt.headless=false -Dprism.order=sw -Djavafx.platform=desktop -Dfile.encoding=UTF-8 -cp "ubn-cross-checker-1.0-SNAPSHOT.jar;lib\*" org.lw5hr.forms.UbnMain >> "dist-standalone\UBN-Cross-Checker-Standalone.bat"
echo. >> "dist-standalone\UBN-Cross-Checker-Standalone.bat"
echo if %%ERRORLEVEL%% NEQ 0 ( >> "dist-standalone\UBN-Cross-Checker-Standalone.bat"
echo     echo Application failed to start. Error level: %%ERRORLEVEL%% >> "dist-standalone\UBN-Cross-Checker-Standalone.bat"
echo     pause >> "dist-standalone\UBN-Cross-Checker-Standalone.bat"
echo ^) >> "dist-standalone\UBN-Cross-Checker-Standalone.bat"
echo. >> "dist-standalone\UBN-Cross-Checker-Standalone.bat"
echo endlocal >> "dist-standalone\UBN-Cross-Checker-Standalone.bat"

REM Step 6: Create README
echo Step 6: Creating documentation...
echo # UBN Cross Checker - Standalone Distribution > "dist-standalone\README-STANDALONE.txt"
echo. >> "dist-standalone\README-STANDALONE.txt"
echo ## What's Included >> "dist-standalone\README-STANDALONE.txt"
echo This package includes everything needed to run UBN Cross Checker: >> "dist-standalone\README-STANDALONE.txt"
echo - The application itself >> "dist-standalone\README-STANDALONE.txt"
echo - All required libraries (including JavaFX) >> "dist-standalone\README-STANDALONE.txt"
echo - A complete Java Runtime Environment (JRE) >> "dist-standalone\README-STANDALONE.txt"
echo. >> "dist-standalone\README-STANDALONE.txt"
echo ## System Requirements >> "dist-standalone\README-STANDALONE.txt"
echo - Windows 10 or higher >> "dist-standalone\README-STANDALONE.txt"
echo - NO Java installation required (bundled with application) >> "dist-standalone\README-STANDALONE.txt"
echo. >> "dist-standalone\README-STANDALONE.txt"
echo ## Installation >> "dist-standalone\README-STANDALONE.txt"
echo 1. Extract ALL files from the ZIP archive to a folder >> "dist-standalone\README-STANDALONE.txt"
echo 2. Double-click "UBN-Cross-Checker-Standalone.bat" to run >> "dist-standalone\README-STANDALONE.txt"
echo 3. That's it! No additional software needed. >> "dist-standalone\README-STANDALONE.txt"

REM Step 7: Create version info
echo Step 7: Creating version info...
echo UBN Cross Checker - Standalone Distribution > "dist-standalone\VERSION-STANDALONE.txt"
echo Build Date: %DATE% >> "dist-standalone\VERSION-STANDALONE.txt"
echo Build Time: %TIME% >> "dist-standalone\VERSION-STANDALONE.txt"
echo Java Runtime: Bundled from %JAVA_HOME% >> "dist-standalone\VERSION-STANDALONE.txt"
echo JavaFX Version: 21.0.2 >> "dist-standalone\VERSION-STANDALONE.txt"

echo.
echo ========================================
echo Standalone distribution created successfully!
echo ========================================
echo.
echo Package contents:
dir /b "dist-standalone"
echo.
echo To test the standalone version:
echo   dist-standalone\UBN-Cross-Checker-Standalone.bat
echo.
pause

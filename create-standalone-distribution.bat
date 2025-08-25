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

REM Try to download a portable JRE if jlink is not available
echo Step 3: Setting up Java Runtime...

REM Check if jlink is available
jlink --version >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo Attempting to use jlink to create custom JRE...
    jlink --add-modules java.base,java.desktop,java.logging,java.sql,java.xml ^
          --output "dist-standalone\jre" ^
          --compress=2 ^
          --strip-debug ^
          --no-header-files ^
          --no-man-pages >nul 2>&1

    REM Check if JRE was actually created successfully
    if exist "dist-standalone\jre\bin\java.exe" (
        echo Custom JRE created successfully with jlink.
        goto :copy_app_files
    ) else (
        echo jlink failed (JavaFX modules not available in system JDK), using fallback...
        if exist "dist-standalone\jre" rmdir /s /q "dist-standalone\jre"
    )
)

REM Fallback: Use current Java installation
echo Using current Java installation as bundled JRE...
if "%JAVA_HOME%"=="" (
    echo JAVA_HOME not set, trying to detect Java installation...

    REM Try to find Java installation using 'where' command
    for /f "tokens=*" %%i in ('where java 2^>nul') do (
        set "JAVA_EXE=%%i"
        goto :found_java_exe
    )

    echo Could not find java.exe in PATH, trying registry...

    REM Try to find Java installation in registry
    for /f "tokens=2*" %%i in ('reg query "HKEY_LOCAL_MACHINE\SOFTWARE\JavaSoft\Java Runtime Environment" /s /v JavaHome 2^>nul ^| find "JavaHome"') do (
        set "DETECTED_JAVA=%%j"
        goto :found_java_home
    )

    REM Try alternative registry path for newer Java versions
    for /f "tokens=2*" %%i in ('reg query "HKEY_LOCAL_MACHINE\SOFTWARE\Eclipse Adoptium\JDK" /s /v Path 2^>nul ^| find "Path"') do (
        set "DETECTED_JAVA=%%j"
        goto :found_java_home
    )

    echo ERROR: Could not detect Java installation!
    echo Please either:
    echo 1. Install JDK 17+ from https://adoptium.net/
    echo 2. Set JAVA_HOME environment variable
    echo 3. Use the standard distribution instead
    pause
    exit /b 1

    :found_java_exe
    REM Extract JAVA_HOME from java.exe path
    for %%i in ("%JAVA_EXE%") do set "JAVA_HOME=%%~dpi.."
    echo Detected Java from PATH at: %JAVA_HOME%
    goto :copy_java

    :found_java_home
    set "JAVA_HOME=%DETECTED_JAVA%"
    echo Detected Java from registry at: %JAVA_HOME%
) else (
    echo Using JAVA_HOME: %JAVA_HOME%
)

:copy_java
echo Copying JRE from: %JAVA_HOME%
if not exist "%JAVA_HOME%\bin\java.exe" (
    echo ERROR: Invalid Java installation at %JAVA_HOME%
    echo java.exe not found at expected location
    pause
    exit /b 1
)

echo This may take a few minutes, copying Java Runtime Environment...
xcopy "%JAVA_HOME%" "dist-standalone\jre\" /E /I /Q /H
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Failed to copy Java runtime
    pause
    exit /b 1
)

REM Verify the JRE was copied successfully
if not exist "dist-standalone\jre\bin\java.exe" (
    echo ERROR: Java runtime copy failed - java.exe not found in destination
    pause
    exit /b 1
)

echo Java Runtime copied successfully!

:copy_app_files
REM Copy application files
echo Step 4: Copying application files...
copy "target\ubn-cross-checker-1.0-SNAPSHOT.jar" "dist-standalone\"
mkdir "dist-standalone\lib"
copy "target\modules\*" "dist-standalone\lib\"

REM Copy data files if they exist
if exist "data" (
    mkdir "dist-standalone\data"
    copy "data\*" "dist-standalone\data\"
)

REM Create launcher script that uses bundled JRE
echo Step 5: Creating standalone launcher...
(
echo @echo off
echo setlocal
echo.
echo echo Starting UBN Cross Checker ^(Standalone^)...
echo echo.
echo.
echo REM Use bundled JRE
echo set JAVA_HOME=%%~dp0jre
echo set PATH=%%JAVA_HOME%%\bin;%%PATH%%
echo.
echo REM Check bundled Java
echo if not exist "%%JAVA_HOME%%\bin\java.exe" ^(
echo     echo ERROR: Bundled Java runtime not found!
echo     echo Please ensure all files are extracted properly.
echo     pause
echo     exit /b 1
echo ^)
echo.
echo REM Display Java version for debugging
echo echo Using bundled Java:
echo "%%JAVA_HOME%%\bin\java.exe" -version
echo echo.
echo.
echo REM Set JavaFX module path
echo set JAVAFX_PATH=lib\javafx-base-21.0.2-win.jar;lib\javafx-controls-21.0.2-win.jar;lib\javafx-fxml-21.0.2-win.jar;lib\javafx-graphics-21.0.2-win.jar
echo.
echo REM Run application with bundled JRE
echo "%%JAVA_HOME%%\bin\java.exe" --module-path "%%JAVAFX_PATH%%" ^^^
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
echo     echo.
echo     echo Troubleshooting:
echo     echo - Ensure all files are extracted properly
echo     echo - Try running as administrator
echo     echo - Check antivirus settings
echo     pause
echo ^) else ^(
echo     echo Application closed successfully.
echo ^)
echo.
echo endlocal
) > "dist-standalone\UBN-Cross-Checker-Standalone.bat"

REM Create README for standalone distribution
echo Step 6: Creating standalone README...
(
echo # UBN Cross Checker - Standalone Distribution
echo.
echo ## What's Included
echo This package includes everything needed to run UBN Cross Checker:
echo - The application itself
echo - All required libraries ^(including JavaFX^)
echo - A complete Java Runtime Environment ^(JRE^)
echo.
echo ## System Requirements
echo - Windows 10 or higher
echo - NO Java installation required ^(bundled with application^)
echo.
echo ## Installation
echo 1. Extract ALL files from the ZIP archive to a folder
echo 2. Double-click "UBN-Cross-Checker-Standalone.bat" to run
echo 3. That's it! No additional software needed.
echo.
echo ## First Run
echo The first time you run the application:
echo - Windows may show a security warning - click "More info" then "Run anyway"
echo - Antivirus software may scan the files - this is normal
echo - The application may take a few seconds longer to start
echo.
echo ## Advantages
echo ✅ No Java installation required
echo ✅ Always uses correct Java version
echo ✅ No version conflicts
echo ✅ Works on any Windows machine
echo ✅ Completely portable
echo.
echo ## File Size
echo This standalone version is larger because it includes
echo a complete Java runtime, but requires no additional setup.
echo.
echo ## Troubleshooting
echo - If antivirus blocks execution: Add folder to whitelist
echo - If Windows SmartScreen appears: Click "More info" then "Run anyway"
echo - If application won't start: Try running as administrator
echo - For any issues: Ensure all files were extracted properly
echo.
echo ## Version Information
echo Build Date: %DATE%
echo Java Runtime: Bundled JRE
echo JavaFX Version: 21.0.2
echo Application Version: 1.0-SNAPSHOT
) > "dist-standalone\README-STANDALONE.txt"

REM Create version info for standalone
echo Step 7: Creating standalone version info...
(
echo UBN Cross Checker - Standalone Distribution
echo Build Date: %DATE%
echo Build Time: %TIME%
echo Distribution Type: Standalone ^(with bundled JRE^)
echo Java Runtime: Bundled
echo JavaFX Version: 21.0.2
echo Application Version: 1.0-SNAPSHOT
echo.
echo JRE Source: %JAVA_HOME%
) > "dist-standalone\VERSION-STANDALONE.txt"

echo.
echo ========================================
echo Standalone distribution created successfully!
echo ========================================
echo.
echo Package contents:
dir /b "dist-standalone"
echo.
echo JRE size:
for /f %%i in ('powershell -command "Get-ChildItem -Path 'dist-standalone\jre' -Recurse | Measure-Object -Property Length -Sum | Select-Object -ExpandProperty Sum"') do echo JRE: %%i bytes
echo.
echo Total package size:
for /f %%i in ('powershell -command "Get-ChildItem -Path 'dist-standalone' -Recurse | Measure-Object -Property Length -Sum | Select-Object -ExpandProperty Sum"') do echo Total: %%i bytes
echo.
echo To distribute:
echo 1. Compress 'dist-standalone' folder into 'UBN-Cross-Checker-Standalone.zip'
echo 2. Users extract and run 'UBN-Cross-Checker-Standalone.bat'
echo 3. NO Java installation required by users!
echo.
echo Testing the standalone version...
echo You can test it now by running: dist-standalone\UBN-Cross-Checker-Standalone.bat
echo.
pause

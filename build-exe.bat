@echo off
echo Building UBN Checker executable...

REM Set variables
set LAUNCH4J_HOME=C:\Program Files (x86)\Launch4j
set PROJECT_DIR=%~dp0
set JAR_FILE=%PROJECT_DIR%target\ubn-cross-checker-1.0-SNAPSHOT.jar
set TEMP_CONFIG_FILE=%PROJECT_DIR%temp-launch4j-config.xml
set EXE_FILE=%PROJECT_DIR%UBNChecker-1.0.exe
set TARGET_EXE=%PROJECT_DIR%target\UBNChecker-1.0.exe

REM Build the project with Maven
echo Step 1: Building the project with Maven...
call mvn clean package -DskipTests

REM Check if Maven build was successful
if %ERRORLEVEL% neq 0 (
    echo Maven build failed. Please check the errors above.
    exit /b %ERRORLEVEL%
)

REM Check if JAR file exists
if not exist "%JAR_FILE%" (
    echo JAR file not found: %JAR_FILE%
    echo Maven build may have failed or the JAR file path is incorrect.
    exit /b 1
)

REM Create a temporary Launch4j configuration file with correct paths
echo Step 2: Creating temporary Launch4j configuration file...
echo ^<?xml version="1.0" encoding="UTF-8"?^> > "%TEMP_CONFIG_FILE%"
echo ^<launch4jConfig^> >> "%TEMP_CONFIG_FILE%"
echo   ^<dontWrapJar^>false^</dontWrapJar^> >> "%TEMP_CONFIG_FILE%"
echo   ^<headerType^>console^</headerType^> >> "%TEMP_CONFIG_FILE%"
echo   ^<jar^>%JAR_FILE:\=\\%^</jar^> >> "%TEMP_CONFIG_FILE%"
echo   ^<outfile^>%TARGET_EXE:\=\\%^</outfile^> >> "%TEMP_CONFIG_FILE%"
echo   ^<errTitle^>UBN Checker Error^</errTitle^> >> "%TEMP_CONFIG_FILE%"
echo   ^<cmdLine^>--module-path "lib" --add-modules javafx.controls,javafx.fxml^</cmdLine^> >> "%TEMP_CONFIG_FILE%"
echo   ^<chdir^>.^</chdir^> >> "%TEMP_CONFIG_FILE%"
echo   ^<priority^>normal^</priority^> >> "%TEMP_CONFIG_FILE%"
echo   ^<downloadUrl^>https://adoptium.net/^</downloadUrl^> >> "%TEMP_CONFIG_FILE%"
echo   ^<supportUrl^>^</supportUrl^> >> "%TEMP_CONFIG_FILE%"
echo   ^<stayAlive^>false^</stayAlive^> >> "%TEMP_CONFIG_FILE%"
echo   ^<restartOnCrash^>false^</restartOnCrash^> >> "%TEMP_CONFIG_FILE%"
echo   ^<manifest^>^</manifest^> >> "%TEMP_CONFIG_FILE%"
echo   ^<icon^>^</icon^> >> "%TEMP_CONFIG_FILE%"
echo   ^<classPath^> >> "%TEMP_CONFIG_FILE%"
echo     ^<mainClass^>org.lw5hr.forms.UbnMain^</mainClass^> >> "%TEMP_CONFIG_FILE%"
echo     ^<cp^>lib/*^</cp^> >> "%TEMP_CONFIG_FILE%"
echo   ^</classPath^> >> "%TEMP_CONFIG_FILE%"
echo   ^<jre^> >> "%TEMP_CONFIG_FILE%"
echo     ^<path^>^</path^> >> "%TEMP_CONFIG_FILE%"
echo     ^<bundledJre64Bit^>false^</bundledJre64Bit^> >> "%TEMP_CONFIG_FILE%"
echo     ^<bundledJreAsFallback^>false^</bundledJreAsFallback^> >> "%TEMP_CONFIG_FILE%"
echo     ^<minVersion^>17^</minVersion^> >> "%TEMP_CONFIG_FILE%"
echo     ^<maxVersion^>^</maxVersion^> >> "%TEMP_CONFIG_FILE%"
echo     ^<jdkPreference^>preferJre^</jdkPreference^> >> "%TEMP_CONFIG_FILE%"
echo     ^<runtimeBits^>64/32^</runtimeBits^> >> "%TEMP_CONFIG_FILE%"
echo   ^</jre^> >> "%TEMP_CONFIG_FILE%"
echo   ^<splash^> >> "%TEMP_CONFIG_FILE%"
echo     ^<waitForWindow^>true^</waitForWindow^> >> "%TEMP_CONFIG_FILE%"
echo     ^<timeout^>60^</timeout^> >> "%TEMP_CONFIG_FILE%"
echo     ^<timeoutErr^>true^</timeoutErr^> >> "%TEMP_CONFIG_FILE%"
echo   ^</splash^> >> "%TEMP_CONFIG_FILE%"
echo   ^<versionInfo^> >> "%TEMP_CONFIG_FILE%"
echo     ^<fileVersion^>1.0.0.0^</fileVersion^> >> "%TEMP_CONFIG_FILE%"
echo     ^<txtFileVersion^>1.0^</txtFileVersion^> >> "%TEMP_CONFIG_FILE%"
echo     ^<fileDescription^>UBN Checker Application^</fileDescription^> >> "%TEMP_CONFIG_FILE%"
echo     ^<copyright^>LW5HR^</copyright^> >> "%TEMP_CONFIG_FILE%"
echo     ^<productVersion^>1.0.0.0^</productVersion^> >> "%TEMP_CONFIG_FILE%"
echo     ^<txtProductVersion^>1.0^</txtProductVersion^> >> "%TEMP_CONFIG_FILE%"
echo     ^<productName^>UBN Checker^</productName^> >> "%TEMP_CONFIG_FILE%"
echo     ^<internalName^>UBNChecker^</internalName^> >> "%TEMP_CONFIG_FILE%"
echo     ^<originalFilename^>UBNChecker.exe^</originalFilename^> >> "%TEMP_CONFIG_FILE%"
echo   ^</versionInfo^> >> "%TEMP_CONFIG_FILE%"
echo ^</launch4jConfig^> >> "%TEMP_CONFIG_FILE%"

REM Copy dependencies to lib folder
echo Step 2.5: Copying dependencies to lib folder...
if not exist "%PROJECT_DIR%lib" mkdir "%PROJECT_DIR%lib"
call mvn dependency:copy-dependencies -DoutputDirectory=lib

REM Check if Launch4j is installed
if not exist "%LAUNCH4J_HOME%\launch4j.exe" (
    echo Launch4j not found at %LAUNCH4J_HOME%
    echo Please install Launch4j or update the LAUNCH4J_HOME variable in this script.
    echo You can download Launch4j from https://launch4j.sourceforge.net/
    exit /b 1
)

REM Create the executable with Launch4j
echo Step 3: Creating the executable with Launch4j...
"%LAUNCH4J_HOME%\launch4j.exe" "%TEMP_CONFIG_FILE%"

REM Check if Launch4j was successful
if %ERRORLEVEL% neq 0 (
    echo Launch4j failed. Please check the errors above.
    exit /b %ERRORLEVEL%
)

REM Copy the executable to the project root
echo Step 4: Copying the executable to the project root...
copy /Y "%TARGET_EXE%" "%EXE_FILE%"

REM Clean up temporary files
echo Step 5: Cleaning up temporary files...
del "%TEMP_CONFIG_FILE%"

echo Build completed successfully!
echo Executable created: %EXE_FILE%
echo.
echo To run the application, double-click on UBNChecker-1.0.exe
echo.
echo If the application doesn't start, check that Java 17 or higher is installed and in your PATH.
echo You can download Java from https://adoptium.net/

exit /b 0
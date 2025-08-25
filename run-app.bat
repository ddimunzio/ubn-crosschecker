@echo off
setlocal EnableDelayedExpansion

echo Starting UBN Cross Checker...
echo.

REM Check if JavaFX JARs exist
echo Checking JavaFX dependencies...
if not exist "target\modules\javafx-base-21.0.2-win.jar" (
    echo ERROR: JavaFX base JAR not found!
    pause
    exit /b 1
)
if not exist "target\modules\javafx-controls-21.0.2-win.jar" (
    echo ERROR: JavaFX controls JAR not found!
    pause
    exit /b 1
)
if not exist "target\modules\javafx-fxml-21.0.2-win.jar" (
    echo ERROR: JavaFX FXML JAR not found!
    pause
    exit /b 1
)
if not exist "target\modules\javafx-graphics-21.0.2-win.jar" (
    echo ERROR: JavaFX graphics JAR not found!
    pause
    exit /b 1
)
echo JavaFX JARs found successfully.
echo.

echo Approach 1: Using JavaFX module path...
REM Set JavaFX module path - Windows-specific JARs
set JAVAFX_PATH=target\modules\javafx-base-21.0.2-win.jar;target\modules\javafx-controls-21.0.2-win.jar;target\modules\javafx-fxml-21.0.2-win.jar;target\modules\javafx-graphics-21.0.2-win.jar

java --module-path "%JAVAFX_PATH%" ^
     --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base ^
     --add-opens javafx.graphics/com.sun.javafx.application=ALL-UNNAMED ^
     --add-opens java.base/java.lang.reflect=ALL-UNNAMED ^
     --add-opens java.base/java.lang=ALL-UNNAMED ^
     --add-opens java.desktop/java.awt=ALL-UNNAMED ^
     -Djava.awt.headless=false ^
     -Dprism.order=sw ^
     -Djavafx.platform=desktop ^
     -Dfile.encoding=UTF-8 ^
     -cp "target\classes;target\modules\*" ^
     org.lw5hr.forms.UbnMain

if %ERRORLEVEL% EQU 0 (
    echo Application started successfully with module path approach.
    goto :end
)

echo Approach 1 failed with error level: %ERRORLEVEL%
echo.
echo Approach 2: Using fat JAR...

java --add-opens java.base/java.lang.reflect=ALL-UNNAMED ^
     --add-opens java.base/java.lang=ALL-UNNAMED ^
     --add-opens java.desktop/java.awt=ALL-UNNAMED ^
     -Djava.awt.headless=false ^
     -Dprism.order=sw ^
     -Djavafx.platform=desktop ^
     -Dfile.encoding=UTF-8 ^
     -jar target\ubn-cross-checker-1.0-SNAPSHOT-jar-with-dependencies.jar

if %ERRORLEVEL% EQU 0 (
    echo Application started successfully with fat JAR approach.
    goto :end
)

echo Approach 2 failed with error level: %ERRORLEVEL%
echo.
echo Approach 3: Simple classpath approach...

java -Djava.awt.headless=false ^
     -Dprism.order=sw ^
     -Dfile.encoding=UTF-8 ^
     -cp "target\classes;target\modules\*" ^
     org.lw5hr.forms.UbnMain

if %ERRORLEVEL% EQU 0 (
    echo Application started successfully with simple classpath approach.
    goto :end
)

echo All approaches failed. Error level: %ERRORLEVEL%
echo.
echo Debugging information:
echo Java version:
java -version
echo.
echo Available JavaFX JARs in target\modules:
dir target\modules\javafx-*.jar /b
echo.
pause

:end
endlocal

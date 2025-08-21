@echo off
setlocal

echo Starting UBN Cross Checker...

REM Set JavaFX module path - only include JavaFX JARs
set JAVAFX_PATH=target\modules\javafx-base-21.0.2-win.jar;target\modules\javafx-controls-21.0.2-win.jar;target\modules\javafx-fxml-21.0.2-win.jar;target\modules\javafx-graphics-21.0.2-win.jar

REM Run the application with proper JavaFX module configuration
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

if %ERRORLEVEL% NEQ 0 (
    echo Error running application. Error level: %ERRORLEVEL%
    echo.
    echo Trying alternative approach...
    echo.

    REM Fallback: Try with jar-with-dependencies
    java --add-opens java.base/java.lang.reflect=ALL-UNNAMED ^
         --add-opens java.base/java.lang=ALL-UNNAMED ^
         --add-opens java.desktop/java.awt=ALL-UNNAMED ^
         -Djava.awt.headless=false ^
         -Dprism.order=sw ^
         -Djavafx.platform=desktop ^
         -Dfile.encoding=UTF-8 ^
         -jar target\ubn-cross-checker-1.0-SNAPSHOT-jar-with-dependencies.jar

    if %ERRORLEVEL% NEQ 0 (
        echo Both approaches failed. Error level: %ERRORLEVEL%
        pause
    ) else (
        echo Application started successfully with fallback method.
    )
) else (
    echo Application started successfully.
)

endlocal

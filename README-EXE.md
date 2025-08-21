# UBN Checker - Windows Executable Build Guide

This guide explains how to build and run the UBN Checker application as a Windows executable (.exe) file.

## Prerequisites

Before you can build the executable, you need to have the following software installed:

1. **Java Development Kit (JDK)** - Version 17 or higher
   - Download from: [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://adoptium.net/)
   - Make sure JAVA_HOME environment variable is set correctly

2. **Maven** - Version 3.6 or higher
   - Download from: [Maven](https://maven.apache.org/download.cgi)
   - Make sure Maven is added to your PATH

3. **Launch4j** - Cross-platform Java executable wrapper
   - Download from: [Launch4j](https://launch4j.sourceforge.net/)
   - Default installation path is `C:\Program Files (x86)\Launch4j`

## Building the Executable

### Automatic Build (Recommended)

1. Open a command prompt in the project root directory
2. Run the build script:
   ```
   build-exe.bat
   ```
3. If successful, the executable will be created as `UBNChecker-1.0.exe` in the project root directory

### Manual Build

If you prefer to build the executable manually, follow these steps:

1. Build the project with Maven:
   ```
   mvn clean package -DskipTests
   ```

2. Create a Launch4j configuration file (save as `temp-config.xml`):
   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <launch4jConfig>
     <dontWrapJar>false</dontWrapJar>
     <headerType>gui</headerType>
     <jar>target\ubn-cross-checker-1.0-SNAPSHOT.jar</jar>
     <outfile>target\UBNChecker-1.0.exe</outfile>
     <errTitle>UBN Checker Error</errTitle>
     <cmdLine></cmdLine>
     <chdir>.</chdir>
     <priority>normal</priority>
     <downloadUrl>https://adoptium.net/</downloadUrl>
     <supportUrl></supportUrl>
     <stayAlive>false</stayAlive>
     <restartOnCrash>false</restartOnCrash>
     <manifest></manifest>
     <icon></icon>
     <classPath>
       <mainClass>org.lw5hr.forms.UbnMain</mainClass>
     </classPath>
     <jre>
       <path></path>
       <bundledJre64Bit>false</bundledJre64Bit>
       <bundledJreAsFallback>false</bundledJreAsFallback>
       <minVersion>17</minVersion>
       <maxVersion></maxVersion>
       <jdkPreference>preferJre</jdkPreference>
       <runtimeBits>64/32</runtimeBits>
     </jre>
     <splash>
       <waitForWindow>true</waitForWindow>
       <timeout>60</timeout>
       <timeoutErr>true</timeoutErr>
     </splash>
     <versionInfo>
       <fileVersion>1.0.0.0</fileVersion>
       <txtFileVersion>1.0</txtFileVersion>
       <fileDescription>UBN Checker Application</fileDescription>
       <copyright>LW5HR</copyright>
       <productVersion>1.0.0.0</productVersion>
       <txtProductVersion>1.0</txtProductVersion>
       <productName>UBN Checker</productName>
       <internalName>UBNChecker</internalName>
       <originalFilename>UBNChecker.exe</originalFilename>
     </versionInfo>
   </launch4jConfig>
   ```

3. Run Launch4j with the configuration file:
   ```
   "C:\Program Files (x86)\Launch4j\launch4j.exe" temp-config.xml
   ```

4. Copy the executable from the target directory to the project root:
   ```
   copy /Y target\UBNChecker-1.0.exe UBNChecker-1.0.exe
   ```

5. Clean up the temporary file:
   ```
   del temp-config.xml
   ```

## Running the Application

After building, you can run the application by double-clicking on `UBNChecker-1.0.exe` in the project root directory.

## Troubleshooting

### Executable Doesn't Run (No Window Appears)

If you double-click the executable and nothing happens (no window or error message appears):

1. **Check Java Installation**:
   - Open a command prompt and type: `java -version`
   - Verify that Java 17 or higher is installed and properly configured
   - If Java is not found, install Java 17+ from [Adoptium](https://adoptium.net/)

2. **Run from Command Line for Error Messages**:
   - Open a command prompt in the directory containing the executable
   - Run: `UBNChecker-1.0.exe`
   - This may show error messages that aren't visible when double-clicking

3. **Check Windows Event Viewer**:
   - Press Win+R, type `eventvwr.msc` and press Enter
   - Look under "Windows Logs" > "Application" for any Java or application errors

4. **Enable Launch4j Logging**:
   - Create a file named `l4j-debug.txt` in the same directory as the executable
   - Run the executable again
   - Check the log file for error messages

### Windows PATH Configuration Issues

The executable relies on Java being properly configured in your Windows PATH. Common issues include:

1. **Incorrect PATH Syntax**:
   - Windows PATH entries must be separated by semicolons (`;`)
   - Example of correct PATH: `C:\Program Files\Java\jdk-17\bin;C:\Other\Path`
   - Check your PATH by opening a command prompt and typing: `echo %PATH%`

2. **Setting JAVA_HOME Correctly**:
   - Set JAVA_HOME to the JDK root directory (not the bin directory)
   - Example: `JAVA_HOME=C:\Program Files\Java\jdk-17`
   - Add `%JAVA_HOME%\bin` to your PATH variable
   - Restart your command prompt after making changes

3. **Path Length Limitations**:
   - Windows has a limit on environment variable length
   - If your PATH is very long, Java might not be found
   - Consider moving Java earlier in your PATH variable

### Launch4j Not Found

If you get an error about Launch4j not being found, you may need to update the `LAUNCH4J_HOME` variable in the `build-exe.bat` script to match your installation path:

1. Open `build-exe.bat` in a text editor
2. Modify line 5 to match your Launch4j installation path:
   ```
   set LAUNCH4J_HOME=C:\Path\To\Your\Launch4j
   ```

### Java Version Issues

The executable is configured to use Java 17 or higher. If you get errors about an incompatible Java version:

1. Make sure you have JDK 17 or higher installed
2. Check that your JAVA_HOME environment variable points to the correct JDK installation
3. Verify that the Java version in your PATH is 17 or higher:
   ```
   java -version
   ```

### Missing Dependencies

If the application fails to start due to missing dependencies:

1. Make sure the Maven build completed successfully
2. Try rebuilding with the updated build script:
   ```
   build-exe.bat
   ```
3. Check that all JAR files are included in the target directory

## Distribution

To distribute the application:

1. Copy the `UBNChecker-1.0.exe` file
2. The user will need to have Java 17 or higher installed on their system

## Additional Notes

- The executable is configured to use the Java installation on the user's machine (via JAVA_HOME or PATH)
- The application will display an error message if Java is not found or is an incompatible version
- For a completely standalone executable that includes the JRE, you would need to modify the Launch4j configuration to bundle a JRE
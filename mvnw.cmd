@echo off
setlocal
set "BASE_DIR=%~dp0"
if "%BASE_DIR:~-1%"=="\" set "BASE_DIR=%BASE_DIR:~0,-1%"
java -Dmaven.multiModuleProjectDirectory="%BASE_DIR%" -classpath "%BASE_DIR%\.mvn\wrapper\maven-wrapper.jar" org.apache.maven.wrapper.MavenWrapperMain %*
endlocal

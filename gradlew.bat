@ECHO OFF
SET DIR=%~dp0
SET APP_BASE_NAME=%~n0
SET APP_HOME=%DIR%

IF NOT "%JAVA_HOME%" == "" GOTO findJavaFromJavaHome

SET JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
IF %ERRORLEVEL% == 0 GOTO init

ECHO ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH. 1>&2
EXIT /B 1

:findJavaFromJavaHome
SET JAVA_HOME=%JAVA_HOME:"=%
SET JAVA_EXE=%JAVA_HOME%/bin/java.exe

IF EXIST "%JAVA_EXE%" GOTO init

ECHO ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME% 1>&2
EXIT /B 1

:init
SET CLASSPATH=%APP_HOME%gradle/wrapper/gradle-wrapper.jar
"%JAVA_EXE%" -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*

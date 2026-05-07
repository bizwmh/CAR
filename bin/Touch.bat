@ECHO OFF

@REM ---------------------------------------------------------------------------
@REM Copyright by Wolfgang Mueller-Haas
@REM ---------------------------------------------------------------------------

setlocal

@REM ---------------------------------------------------------------------------
@REM Initialize basic parameters
@REM ---------------------------------------------------------------------------

set USER_ARGS=%*
set "CURRENT_DIR=%cd%"
cd ..
set "PARENT_DIR=%cd%"
cd %CURRENT_DIR%

@REM ---------------------------------------------------------------------------
:SETUP_JVM
@REM ---------------------------------------------------------------------------

set JAVA=java
set JAVA_OPTS=-Xms32M -Xmx128M

if exist "%JAVA_HOME%\bin\java.exe" (
	set JAVA="%JAVA_HOME%\bin\java.exe"
	if exist "%JAVA_HOME%\bin\server\jvm.dll" (
		set "JAVA_OPTS=-server"
	)
)
set JAVA_OPTS=%JAVA_OPTS% --add-modules=ALL-SYSTEM
set JAVA_OPTS=%JAVA_OPTS% --add-opens=java.base/java.lang=ALL-UNNAMED
set JAVA_OPTS=%JAVA_OPTS% --add-opens=java.base/java.util=ALL-UNNAMED
set JAVA_OPTS=%JAVA_OPTS% --add-opens=java.base/java.time=ALL-UNNAMED
set JAVA_OPTS=%JAVA_OPTS% --add-opens=java.base/java.nio=ALL-UNNAMED
set JAVA_OPTS=%JAVA_OPTS% -XX:MaxMetaspaceSize=128m
set JAVA_OPTS=%JAVA_OPTS% -Dsun.net.inetaddr.ttl=60

if "%1" == "debug" (
   set JAVA_OPTS=%JAVA_OPTS% -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005
   shift)

@REM ---------------------------------------------------------------------------
@REM Setup Classpath
@REM ---------------------------------------------------------------------------

set "CLASSPATH=C:\IDE\WORKSPACE\CAR.OSGi\lib\car-2.0.0.jar"

@REM ---------------------------------------------------------------------------
@REM Set user vars
@REM ---------------------------------------------------------------------------

@REM set USER_ARGS=%*

@REM ---------------------------------------------------------------------------
@REM LAUNCH CAR OSGi
@REM ---------------------------------------------------------------------------

echo OASE_HOME      is set to %OASE_HOME%
echo JAVA           is set to %JAVA%
echo CLASSPATH      is set to %CLASSPATH%
echo USER_ARGS      is set to %USER_ARGS%
echo:

cd %OASE_HOME%

@REM ---------------------------------------------------------------------------
:LAUNCH
@REM ---------------------------------------------------------------------------

%JAVA% %JAVA_OPTS% -cp %CLASSPATH% biz.car.io.Touch %USER_ARGS%

@REM ---------------------------------------------------------------------------
:EXIT
@REM ---------------------------------------------------------------------------
cd %CURRENT_DIR%
endlocal

exit /b %ERRORLEVEL%
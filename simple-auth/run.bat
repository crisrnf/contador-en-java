@echo off
set ROOT=%~dp0
set OUT=%ROOT%out\classes
if not exist "%OUT%" mkdir "%OUT%"

echo Copying resources...
xcopy /Y /E "%ROOT%src\main\resources\*" "%OUT%" >nul 2>&1

echo Checking for Maven (mvn) in PATH...
where mvn >nul 2>nul
if %ERRORLEVEL%==0 (
  echo Maven detected — building package and running jar
  pushd %ROOT%
  mvn -q -DskipTests package
  popd
  for /f "delims=" %%J in ('dir /b /od "%ROOT%\target\*simple-auth*.jar"') do set LASTJAR=%%J
  if exist "%ROOT%\target\%LASTJAR%" (
    echo Running %LASTJAR%
    "%ROOT%src\main\java\com\example\auth\oracleJdk-25\bin\java.exe" -jar "%ROOT%\target\%LASTJAR%"
    goto :eof
  ) else (
    echo No packaged jar found in target\ – falling back to legacy mode.
  )
) else (
  echo Maven not found – using legacy compilation/run behavior.
)

:: Legacy behavior - compile and run classes
echo Compiling java sources...
"%ROOT%src\main\java\com\example\auth\oracleJdk-25\bin\javac.exe" -d "%OUT%" (for /r "%ROOT%src\main\java" %%F in (*.java) do @echo %%F)

set LIBMYSQL=%ROOT%lib\mysql-connector-java.jar
set CP=%OUT%
if exist "%LIBMYSQL%" set CP=%CP%;%LIBMYSQL%

echo Checking for Maven (mvn) in PATH...
where mvn >nul 2>nul
if %ERRORLEVEL%==0 (
  echo Maven found. Building and running with mvn exec:java...
  pushd %ROOT%
  mvn -q clean package -DskipTests
  mvn -q exec:java -Dexec.mainClass=com.example.auth.Main
  popd
) else (
  echo Maven not found, running with java and classpath: %CP%
  "%ROOT%src\main\java\com\example\auth\oracleJdk-25\bin\java.exe" -cp "%CP%" com.example.auth.Main
)

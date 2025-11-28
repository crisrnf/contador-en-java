# Start script for SimpleAuth (PowerShell)
Param()

$root = Split-Path -Parent $MyInvocation.MyCommand.Definition
$out = Join-Path $root "out\classes"
New-Item -ItemType Directory -Force -Path $out | Out-Null

Write-Host "Copying resource files..."
Copy-Item -Force -Path (Join-Path $root 'src\main\resources\**\*') -Destination $out -Recurse -ErrorAction SilentlyContinue

Write-Host "Compiling java sources..."
& "$root\src\main\java\com\example\auth\oracleJdk-25\bin\javac.exe" -d $out (Get-ChildItem -Recurse -Filter *.java -Path "$root\src\main\java" | % FullName)

Write-Host "Checking for maven (mvn) to build the project..."
if (Get-Command mvn -ErrorAction SilentlyContinue) {
	Write-Host "mvn found — building package and running the shaded jar"
	Push-Location $root
	& mvn -q -DskipTests package
	Pop-Location
	$jar = Get-ChildItem -Path (Join-Path $root 'target') -Filter "*simple-auth*.jar" | Select-Object -Last 1
	if ($jar) {
		Write-Host "Running $($jar.FullName)"
		& "$root\src\main\java\com\example\auth\oracleJdk-25\bin\java.exe" -jar $jar.FullName
		exit
	} else {
		Write-Host "Package created but jar not found in target/. Falling back to classpath execution."
	}
} else {
	Write-Host "mvn not found — using legacy classpath/start behavior."
}

# Legacy behavior: build classpath from compiled classes and optional lib drivers
$cp = $out
$mysql = Join-Path $root 'lib\mysql-connector-java.jar'
if (Test-Path $mysql) { $cp += ";$mysql"; Write-Host "Using MySQL driver: $mysql" }

Write-Host "Checking for Maven (mvn) in PATH..."
$mvn = Get-Command mvn -ErrorAction SilentlyContinue
if ($mvn) {
	Write-Host "Maven found. Building and running using mvn exec:java..."
	Push-Location $root
	& mvn -q clean package -DskipTests
	& mvn -q exec:java -Dexec.mainClass=com.example.auth.Main
	Pop-Location
} else {
	Write-Host "Maven not found, falling back to java invocation with classpath: $cp"
	& "$root\src\main\java\com\example\auth\oracleJdk-25\bin\java.exe" -cp $cp com.example.auth.Main
}

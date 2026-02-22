@echo off
javac "@compile.list" -d bin
if %errorlevel% neq 0 (
    exit /b %errorlevel%
)
cd bin
start javaw appli.Controleur
cd ..
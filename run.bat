@echo off
javac "@compile.list" -d class
if %errorlevel% neq 0 (
    exit /b %errorlevel%
)
cd class
start javaw appli.Controleur
cd ..
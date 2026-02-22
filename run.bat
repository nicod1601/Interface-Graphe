@echo off
javac "@compile.list" -d bin
if %errorlevel% neq 0 (
    exit /b %errorlevel%
)

xcopy /E /Y /I appli\donnee bin\appli\donnee
copy /Y logo.png bin\logo.png
copy /Y Graphe.ico bin\Graphe.ico

cd bin
start javaw appli.Controleur
cd ..
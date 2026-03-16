@echo off
javac "@compile.list" -d bin
if %errorlevel% neq 0 (
    exit /b %errorlevel%
)

rem Plus de xcopy pour donnee !
copy /Y appli\logo.png bin\appli\logo.png
copy /Y appli\logo.png bin\logo.png
copy /Y Graphe.ico bin\Graphe.ico

cd bin
start javaw appli.Controleur
cd ..
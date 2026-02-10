@echo off
REM Compilation des fichiers sources
echo Compilation en cours...
javac -d class ./appli/metier/Lecture.java ./appli/metier/Sommet.java ./appli/Controleur.java

if errorlevel 1 (
    echo Erreur lors de la compilation!
    pause
    exit /b 1
)

echo Compilation reussie!
echo Lancement de l'application...
java -cp class appli.Controleur

pause

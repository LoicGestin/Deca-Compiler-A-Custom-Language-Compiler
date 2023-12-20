#! /bin/sh

# Auteur : gl29
# Version initiale : 19/12/2023

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :

cd "$(dirname "$0")"/../../../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

mvn compile && src/main/bin/decac src/test/deca/context/valid/provided/hello-world.deca && ima src/test/deca/context/valid/provided/hello-world.ass

#! /bin/sh

# Auteur : gl29
# Version initiale : 19/12/2023

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :

cd "$(dirname "$0")"/../../../../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

mvn compile
echo "Lancement des tests des programmes lexicalement corrects pour l'étape A"

passed=0
total=0

for file in src/test/deca/syntax/valid/personalTests/lexer/*.deca
do
  if test_lex $file 2>&1 \
  | grep -q -e "token recognition error"
  then
    echo "$file : FAILED"
  else
    echo "$file : PASSED"
    passed=$(expr $passed + 1)
  fi
  total=$(expr $total + 1)
done

echo "Tests passés : $passed/$total"
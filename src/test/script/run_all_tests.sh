#! /bin/sh

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :

cd "$(dirname "$0")"/../../.. || exit 1

erreur_total=0
temps_initial=$(date +%s%N)

PATH=./src/test/script/launchers:"$PATH"
RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m' # No Color

echo "==================== Compilation ===================="

# Debut du timer en secondes)
debut_time=$(date +%s%N)
echo "-> Compilation du projet"
mvn test-compile > /dev/null
fin_time=$(date +%s%N)
echo -e "-> Compilation terminée [durée : $(echo "scale=3; ($fin_time - $debut_time) / 1000000000" | bc) s]\n"

echo "====================   Etape A   ===================="

echo "==> Test du lexer"
echo "   --> Tests des programmes syntaxiquement corrects"

passed=0
total=0

for file in src/test/deca/syntax/valid/personalTests/lexer/*.deca
do
  if test_lex $file 2>&1 \
  | grep -q -e "token recognition error"
  then
    echo -ne "\r\t${RED}[FAILED]${NC}   : ${file##*/}                                \r\n"
    erreur_total=$((erreur_total + 1))
  else
    echo -ne "\r\t${GREEN}[PASSED]${NC} : ${file##*/}                                    "
    passed=$((passed + 1))
  fi
  total=$((total + 1))
done

echo -ne "\r\t[Total : $passed/$total]                                    \n"

echo "   --> Tests des programmes syntaxiquement incorrects"

failed=0
total=0

for file in src/test/deca/syntax/invalid/personalTests/lexer/*.deca
do
  if test_lex $file 2>&1 \
  | grep -q -e "token recognition error"
  then
    # print in red if the test failed and the name of the file without the path
    echo -ne "\r\t${GREEN}[FAILED]${NC}   : ${file##*/}                                    "
    failed=$((failed + 1))
  else
    echo -ne "\r\t${RED}[PASSED]${NC}     : ${file##*/}                                \r\n"
    erreur_total=$((erreur_total + 1))
  fi
  total=$((total + 1))
done

echo -ne "\r\t[Total : $failed/$total]                                    \n"

echo "==> Test du parser"
echo "   --> Tests des programmes syntaxiquement corrects"

passed=0
total=0

for file in src/test/deca/syntax/valid/personalTests/parser/*.deca
do
  if test_synt $file 2>&1 \
  | grep -q -e "org.antlr.v4.runtime.misc.ParseCancellationException"
  then
    echo -ne "\r\t${RED}[FAILED]${NC}   : ${file##*/}                                \r\n"
    erreur_total=$((erreur_total + 1))
  else
    echo -ne "\r\t${GREEN}[PASSED]${NC} : ${file##*/}                                    "
    passed=$((passed + 1))
  fi
  total=$((total + 1))
done

echo -ne "\r\t[Total : $passed/$total]                                    \n"

echo "   --> Tests des programmes syntaxiquement incorrects"

failed=0
total=0

for file in src/test/deca/syntax/invalid/personalTests/parser/*.deca
do
  if test_synt $file 2>&1 \
  | grep -q -e "org.antlr.v4.runtime.misc.ParseCancellationException"
  then
    # print in red if the test failed and the name of the file without the path
    echo -ne "\r\t${GREEN}[FAILED]${NC}  : ${file##*/}                                    "
    failed=$((failed + 1))
  else
    echo -ne "\r\t${RED}[PASSED]${NC}    : ${file##*/}                                \r\n"
    erreur_total=$((erreur_total + 1))

  fi
  total=$((total + 1))
done

echo -ne "\r\t[Total : $failed/$total]                                    \n"

echo "====================   Etape B   ===================="

echo "==> Test du contexte"

passed=0
total=0

for file in src/test/deca/context/valid/personalTests/*.deca
do
  if test_context $file 2>&1 \
  | grep -q -e "Exception"
  then
    echo -ne "\r\t${RED}[FAILED]${NC}   : ${file##*/}                                \r\n"
    erreur_total=$((erreur_total + 1))
  else
    echo -ne "\r\t${GREEN}[PASSED]${NC} : ${file##*/}                                    "
    passed=$((passed + 1))
  fi
  total=$((total + 1))
done

echo -ne "\r\t[Total : $passed/$total]                                    \n"

echo "   --> Tests des programmes incorrects"

failed=0
total=0

for file in src/test/deca/context/invalid/personalTests/*.deca
do
  if test_context $file 2>&1 \
  | grep -q -e "Exception"
  then
    # print in red if the test failed and the name of the file without the path
    echo -ne "\r\t${GREEN}[FAILED]${NC}  : ${file##*/}                                    "
    failed=$((failed + 1))
  else
    echo -ne "\r\t${RED}[PASSED]${NC}    : ${file##*/}                                \r\n"
    erreur_total=$((erreur_total + 1))

  fi
  total=$((total + 1))
done

echo -ne "\r\t[Total : $failed/$total]                                    \n"

echo "====================   Etape C   ===================="


# TODO : ajouter les tests de l'étape C


echo "====================   Synthese  ===================="

echo -e "Temps total d'exécution : $(echo "scale=3; ($(date +%s%N) - $temps_initial) / 1000000000" | bc) s"

if [ $erreur_total -eq 0 ]
then
  echo -e "${GREEN}Tous les tests ont été passés avec succès !${NC}"
  exit 0 # exit with success
else
  echo -e "${RED}Il y a eu $erreur_total erreur(s)${NC}"
  exit 1 # exit with failure
fi

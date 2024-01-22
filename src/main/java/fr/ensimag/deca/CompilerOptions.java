package fr.ensimag.deca;

import fr.ensimag.deca.codegen.codeGen;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User-specified options influencing the compilation.
 *
 * @author gl29
 * @date 01/01/2024
 */
public class CompilerOptions {
    public static final int QUIET = 0;
    public static final int INFO = 1;
    public static final int DEBUG = 2;
    public static final int TRACE = 3;
    private final List<File> sourceFiles = new ArrayList<>();
    public boolean color = false;
    public boolean nocheck = false;
    protected Step maxStep = Step.ALL;
    private int debug = 0;
    private boolean parallel = false;
    private boolean printBanner = false;
    private boolean warning = false;

    public int getDebug() {
        return debug;
    }

    public boolean getParallel() {
        return parallel;
    }

    public boolean getPrintBanner() {
        return printBanner;
    }

    public List<File> getSourceFiles() {
        return Collections.unmodifiableList(sourceFiles);
    }

    public boolean getWarning() {
        return warning;
    }

    public void parseArgs(String[] args) throws CLIException {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals("-b")) {
                printBanner = true;
            } else if (arg.equals("-p")) {
                if (maxStep == Step.ALL) {
                    maxStep = Step.PARSE;
                } else {
                    throw new UnsupportedOperationException("Option " + arg + " incompatible avec -v");
                }
            } else if (arg.equals("-v")) {
                if (maxStep == Step.ALL) {
                    maxStep = Step.VERIF;
                } else {
                    throw new UnsupportedOperationException("Option " + arg + " incompatible avec -p");
                }
            } else if (arg.equals("-n")) {
                nocheck = true;
            } else if (arg.startsWith("-r")) {
                int nombreRegistres = Integer.parseInt(args[i + 1]);
                if (nombreRegistres < 4 || nombreRegistres > 16) {
                    throw new UnsupportedOperationException("Option " + arg + " incompatible avec -p");
                }
                // - 2 car on ne compte pas R0 et R1
                codeGen.setNombreRegistres(nombreRegistres - 2);
                i++;
            } else if (arg.equals("-d")) {
                debug++;
            } else if (arg.equals("-P")) {
                parallel = true;
            } else if (arg.equals("-w")) {
                warning = true;
            } else if (arg.equals("-h")) {
                displayUsage();
                System.exit(0);
            } else if (arg.equals("--color")) {
                color = true;
            } else if (arg.startsWith("-")) {
                throw new UnsupportedOperationException("Option " + arg + " inconnue");
            } else {
                sourceFiles.add(new File(arg));
            }
        }

        Logger logger = Logger.getRootLogger();
        // map command-line debug option to log4j's level.
        switch (getDebug()) {
            case QUIET:
                break; // keep default
            case INFO:
                logger.setLevel(Level.INFO);
                break;
            case DEBUG:
                logger.setLevel(Level.DEBUG);
                break;
            case TRACE:
                logger.setLevel(Level.TRACE);
                DecacCompiler.setDebug(true);
                break;
            default:
                logger.setLevel(Level.ALL);
                break;
        }
        logger.info("Application-wide trace level set to " + logger.getLevel());

        boolean assertsEnabled = false;
        assert assertsEnabled = true; // Intentional side effect!!!
        if (assertsEnabled) {
            logger.info("Java assertions enabled");
        } else {
            logger.info("Java assertions disabled");
        }


    }

    protected void displayUsage() {
        // Prints the usage of the command-line options

        System.out.println("Usage: decac [[-p | -v] [-n] [-r X] [-d]* [-P] [-w] <fichier deca>...] | [-b]");
        System.out.println("  -b (banner)       : affiche une bannière indiquant le nom de l'équipe");
        System.out.println("  -p (parse)        : arrête decac après l'étape de construction de l'arbre, et affiche la décompilation de ce dernier (--color pour activer la coloration syntaxique)");
        System.out.println("  -v (verification) : arrête decac après l'étape de vérifications (ne produit aucune sortie en l'absence d'erreur)");
        System.out.println("  -n (no check)     : supprime les tests à l'exécution spécifiés dans les points 11.1 et 11.3 de la sémantique de Deca.");
        System.out.println("  -r X (registers)  : limite les registres banalisés disponibles à R0 ... R{X-1}, avec 4 <= X <= 16");
        System.out.println("  -d (debug)        : active les traces de debug. Répéter l'option plusieurs fois pour avoir plus de traces.");
        System.out.println("  -P (parallel)     : s'il y a plusieurs fichiers sources lance la compilation des fichiers en parallèle (pour accélérer la compilation)");
        System.out.println("  -w (warning)      : affiche les avertissements");

    }

    protected enum Step {PARSE, VERIF, ALL}
}

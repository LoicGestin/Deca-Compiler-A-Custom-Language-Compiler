package fr.ensimag.deca;

import org.apache.log4j.Logger;

import java.io.File;

/**
 * Main class for the command-line Deca compiler.
 *
 * @author gl29
 * @date 01/01/2024
 */
public class DecacMain {
    private static final Logger LOG = Logger.getLogger(DecacMain.class);

    public static void main(String[] args) {
        // example log4j message.
        LOG.info("Decac compiler started");
        boolean error = false;
        final CompilerOptions options = new CompilerOptions();
        try {
            options.parseArgs(args);
        } catch (CLIException e) {
            System.err.println("Error during option parsing:\n"
                    + e.getMessage());
            options.displayUsage();
            System.exit(1);
        }
        if (options.getPrintBanner()) {
            // affiche une bannière indiquant le nom de l'équipe
            // et les options utilisées
            System.out.println("Team: gl29");
        }
        if (options.getSourceFiles().isEmpty() && !options.getPrintBanner()) {
            options.displayUsage();
        }
        if (options.getParallel()) {
            // A FAIRE : instancier DecacCompiler pour chaque fichier à
            // compiler, et lancer l'exécution des méthodes compile() de chaque
            // instance en parallèle. Il est conseillé d'utiliser
            // java.util.concurrent de la bibliothèque standard Java.
            throw new UnsupportedOperationException("Parallel build not yet implemented");
        } else {
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                if (compiler.compile()) {
                    error = true;
                }
            }
        }
        System.exit(error ? 1 : 0);
    }
}

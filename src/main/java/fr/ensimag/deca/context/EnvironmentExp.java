package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable.Symbol;

import java.util.HashMap;

/**
 * Dictionary associating identifier's ExpDefinition to their names.
 * <p>
 * This is actually a linked list of dictionaries: each EnvironmentExp has a
 * pointer to a parentEnvironment, corresponding to superblock (eg superclass).
 * <p>
 * The dictionary at the head of this list thus corresponds to the "current"
 * block (eg class).
 * <p>
 * Searching a definition (through method get) is done in the "current"
 * dictionary and in the parentEnvironment if it fails.
 * <p>
 * Insertion (through method declare) is always done in the "current" dictionary.
 *
 * @author gl29
 * @date 01/01/2024
 */
public class EnvironmentExp {
    // A FAIRE : implémenter la structure de donnée représentant un
    // environnement (association nom -> définition, avec possibilité
    // d'empilement).
    // [DONE]

    public final HashMap<Symbol, ExpDefinition> envExp = new HashMap<>();
    final EnvironmentExp parentEnvironment;


    public EnvironmentExp(EnvironmentExp parentEnvironment) {
        this.parentEnvironment = parentEnvironment;
    }

    /**
     * Return the definition of the symbol in the environment, or null if the
     * symbol is undefined.
     */
    public ExpDefinition get(Symbol key) {
        if (envExp.containsKey(key)) {
            return envExp.get(key);
        } else if (parentEnvironment != null) {
            return parentEnvironment.get(key);
        }
        return null;
    }

    /**
     * Add the definition def associated to the symbol name in the environment.
     * <p>
     * Adding a symbol which is already defined in the environment,
     * - throws DoubleDefException if the symbol is in the "current" dictionary
     * - or, hides the previous declaration otherwise.
     *
     * @param name Name of the symbol to define
     * @param def  Definition of the symbol
     * @throws DoubleDefException if the symbol is already defined at the "current" dictionary
     */
    public void declare(Symbol name, ExpDefinition def) throws DoubleDefException {
        if (envExp.containsKey(name)) {
            throw new DoubleDefException();
        } else {
            envExp.put(name, def);
        }
    }

    public void afficher() {
        System.out.println("Affichage de l'environnement : ");
        for (Symbol key : envExp.keySet()) {
            System.out.println(key + " : " + envExp.get(key));
        }
        System.out.println("Fin de l'affichage de l'environnement.");
    }

    public static class DoubleDefException extends Exception {
        private static final long serialVersionUID = -2733379901827316441L;
    }

}

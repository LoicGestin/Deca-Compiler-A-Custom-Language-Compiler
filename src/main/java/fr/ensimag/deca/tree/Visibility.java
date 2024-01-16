package fr.ensimag.deca.tree;

import java.io.PrintStream;

/**
 * Visibility of a field.
 *
 * @author gl29
 * @date 01/01/2024
 */

public enum Visibility {
    PUBLIC,
    PROTECTED;

    public void prettyPrint(PrintStream s, String prefix, boolean b) {
        if (this == PROTECTED) {
            s.print("protected");
        }
    }
}

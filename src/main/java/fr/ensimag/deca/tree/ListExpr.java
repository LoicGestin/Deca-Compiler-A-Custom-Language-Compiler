package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * List of expressions (eg list of parameters).
 *
 * @author gl29
 * @date 01/01/2024
 */
public class ListExpr extends TreeList<AbstractExpr> {


    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractExpr e : getList()) {
            e.decompile(s);
            if (getList().indexOf(e) != getList().size() - 1) {
                s.print(", ");
            }
        }
    }
}

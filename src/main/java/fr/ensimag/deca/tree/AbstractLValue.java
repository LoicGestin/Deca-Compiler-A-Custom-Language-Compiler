package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.DAddr;

/**
 * Left-hand side value of an assignment.
 *
 * @author gl29
 * @date 01/01/2024
 */
public abstract class AbstractLValue extends AbstractExpr {
    public abstract SymbolTable.Symbol getName();

    public abstract DAddr getAddr();
}

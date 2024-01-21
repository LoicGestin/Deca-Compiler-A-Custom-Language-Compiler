package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;

/**
 * Left-hand side value of an assignment.
 *
 * @author gl29
 * @date 01/01/2024
 */
public abstract class AbstractLValue extends AbstractExpr {
    public abstract SymbolTable.Symbol getName();

    public abstract DAddr getAddr(DecacCompiler compiler);

    public abstract GPRegister getGPRegister();

    public abstract boolean isAddr(DecacCompiler compiler);

    public abstract boolean isField(DecacCompiler compiler);

}

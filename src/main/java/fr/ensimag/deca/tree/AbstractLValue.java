package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.SymbolTable;

/**
 * Left-hand side value of an assignment.
 * 
 * @author gl29
 * @date 01/01/2024
 */
public abstract class AbstractLValue extends AbstractExpr {
    public abstract SymbolTable.Symbol getName();
}

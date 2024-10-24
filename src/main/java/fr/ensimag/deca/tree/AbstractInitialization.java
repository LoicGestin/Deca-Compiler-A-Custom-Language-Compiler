package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;

/**
 * Initialization (of variable, field, ...)
 *
 * @author gl29
 * @date 01/01/2024
 */
public abstract class AbstractInitialization extends Tree {

    /**
     * Implements non-terminal "initialization" of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler     contains "env_types" attribute
     * @param t            corresponds to the "type" attribute
     * @param localEnv     corresponds to the "env_exp" attribute
     * @param currentClass corresponds to the "class" attribute (null in the main bloc).
     */
    protected abstract void verifyInitialization(DecacCompiler compiler,
                                                 Type t, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    public abstract void codeGenInit(DecacCompiler compiler, ExpDefinition var);

}

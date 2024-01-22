package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * List of declarations (e.g. int x; float y,z).
 *
 * @author gl29
 * @date 01/01/2024
 */
public class ListDeclVar extends TreeList<AbstractDeclVar> {

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclVar i : getList()) {
            i.decompile(s);
        }
    }

    /**
     * Implements non-terminal "list_decl_var" of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler contains the "env_types" attribute
     * @param localEnv its "parentEnvironment" corresponds to "env_exp_sup" attribute
     *                 in precondition, its "current" dictionary corresponds to
     *                 the "env_exp" attribute
     *                 in postcondition, its "current" dictionary corresponds to
     *                 the "env_exp_r" attribute
     */
    void verifyListDeclVariable(DecacCompiler compiler, EnvironmentExp localEnv) throws ContextualError {
        // For each declaration in the list, we verify it and add it to the environment
        for (AbstractDeclVar decl : getList()) {
            decl.verifyDeclVar(compiler, localEnv, null);
        }
    }


    public void codeGenListDeclVar(DecacCompiler compiler) {
        for (AbstractDeclVar decl : getList()) {
            decl.codeGenDeclVar(compiler);
        }
    }
}

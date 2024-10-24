package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * @author gl29
 * @date 01/01/2024
 */
public class Initialization extends AbstractInitialization {

    private AbstractExpr expression;

    public Initialization(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    @Override
    protected void verifyInitialization(DecacCompiler compiler, Type t,
                                        EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        // Vérification de la compatibilité des types
        Type type = expression.verifyRValue(compiler, localEnv, currentClass, t).getType();
        // Conversion de type implicite
        if (t.isFloat() && type.isInt()) {
            expression = new ConvFloat(expression);
            expression.verifyExpr(compiler, localEnv, currentClass);
        }
        expression.setType(type);
    }


    @Override
    public void codeGenInit(DecacCompiler compiler, ExpDefinition var) {
        codeGen.setAssignation(true);
        expression.codeGenInst(compiler);
        if (var.isAddr()) {
            compiler.addInstruction(new STORE(codeGen.getRegistreUtilise(), var.getOperand()));
        } else {
            codeGen.saveVariable();
        }

    }

    @Override
    public void decompile(IndentPrintStream s) {
        expression.decompile(s);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        expression.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expression.prettyPrint(s, prefix, true);
    }


}

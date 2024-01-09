package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.commons.lang.Validate;

/**
 * @author gl29
 * @date 01/01/2024
 */
public class Initialization extends AbstractInitialization {

    public AbstractExpr getExpression() {
        return expression;
    }

    private AbstractExpr expression;

    public void setExpression(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

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
        expression.setType(type);
        if(t.isFloat() && type.isInt()) {
            expression = new ConvFloat(expression);
            expression.verifyExpr(compiler, localEnv, currentClass);
            expression.setType(t);
        }
        else if (type == null) {
            throw new ContextualError("Exception : Incompatible types null in initialization", expression.getLocation());
        }
        else if(!type.sameType(t)) {
            throw new ContextualError("Exception : Incompatible types in initialization", expression.getLocation());
        }
    }


    @Override
    public void codeGenInit(DecacCompiler compiler, DAddr addr) {
        expression.codeGenInst(compiler);
        compiler.libererRegistre();
        compiler.addInstruction(new STORE(compiler.getRegistreLibre(), addr));

    }

    @Override
    public DVal codeGen(DecacCompiler compiler) {
        return null;
    }


    @Override
    public void decompile(IndentPrintStream s) {
        expression.decompile(s);
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        expression.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expression.prettyPrint(s, prefix, true);
    }


}

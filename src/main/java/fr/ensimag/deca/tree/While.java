package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * @author gl29
 * @date 01/01/2024
 */
public class While extends AbstractInst {
    private final AbstractExpr condition;
    private final ListInst body;

    public While(AbstractExpr condition, ListInst body) {
        Validate.notNull(condition);
        Validate.notNull(body);
        this.condition = condition;
        this.body = body;
    }

    public AbstractExpr getCondition() {
        return condition;
    }

    public ListInst getBody() {
        return body;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        Label debut_while = compiler.labelTable.addLabel("debut_while");
        Label fin_while = compiler.labelTable.addLabel("fin_while");

        compiler.addLabel(debut_while);

        getCondition().codeGenInst(compiler);

        compiler.addInstruction(new CMP(0, compiler.getRegistreLibre()));
        compiler.addInstruction(new BEQ(fin_while));

        getBody().codeGenListInst(compiler);
        compiler.addInstruction(new BRA(debut_while));

        compiler.addLabel(fin_while);
        compiler.libererRegistre();
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
                              ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        this.condition.verifyInst(compiler, localEnv, currentClass, returnType);
        this.body.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        if (DecacCompiler.getColor()) {
            s.print("while", "purple");
        } else {
            s.print("while");
        }
        s.print(" (");
        getCondition().decompile(s);
        s.println(") {");
        s.indent();
        getBody().decompile(s);
        s.unindent();
        s.print("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        condition.iter(f);
        body.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        body.prettyPrint(s, prefix, true);
    }

}

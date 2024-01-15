package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public class MethodBody extends AbstractMethodBody {
    private ListInst insts;
    private ListDeclVar declVars;
    private StringLiteral stringLiteral;

    public MethodBody(ListDeclVar declVars, ListInst insts) {
        Validate.notNull(insts);
        Validate.notNull(declVars);
        this.insts = insts;
        this.declVars = declVars;
    }

    public MethodBody(StringLiteral stringLiteral) {
        Validate.notNull(stringLiteral);
        this.stringLiteral = stringLiteral;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        if (stringLiteral != null) {
            stringLiteral.decompile(s);
        } else {
            s.println("{");
            s.indent();
            declVars.decompile(s);
            insts.decompile(s);
            s.unindent();
            s.println("}");
        }
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        if (stringLiteral != null) {
            stringLiteral.prettyPrint(s, prefix, false);
        } else {
            declVars.prettyPrint(s, prefix, false);
            insts.prettyPrint(s, prefix, true);
        }
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void verifyMethodBody(DecacCompiler compiler) throws ContextualError {
        if (stringLiteral != null) {
            stringLiteral.verifyExpr(compiler, null, null);
        } else {
            declVars.verifyListDeclVariable(compiler, null);
            insts.verifyListInst(compiler, null, null, null);
        }
    }
}

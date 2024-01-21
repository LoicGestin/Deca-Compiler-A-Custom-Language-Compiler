package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;
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
            s.print(" asm(");
            stringLiteral.decompile(s);
            s.print(")");
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
        if (stringLiteral != null) {
            stringLiteral.iter(f);
        } else {
            declVars.iter(f);
            insts.iter(f);
        }
    }

    @Override
    protected void verifyMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type returnType) throws ContextualError {
        if (stringLiteral != null) {
            stringLiteral.verifyExpr(compiler, localEnv, currentClass);
        } else {
            declVars.verifyListDeclVariable(compiler, localEnv);
            insts.verifyListInst(compiler, localEnv, currentClass, returnType);
        }
    }

    public void codeGenMethodBody(DecacCompiler compiler, ClassDefinition currentClass, EnvironmentExp envParam, Type type) {
        if (stringLiteral != null) {
            stringLiteral.codeGenPrint(compiler);
        } else {
            declVars.codeGenListDeclVar(compiler, currentClass);
            insts.codeGenListInst(compiler, currentClass);
        }
    }
}

package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public class DeclMethod extends AbstractDeclMethod {
    private final AbstractIdentifier type;
    private final AbstractIdentifier name;
    private final ListDeclParam params;
    private final AbstractMethodBody body;

    public DeclMethod(AbstractIdentifier type, AbstractIdentifier name, ListDeclParam params, AbstractMethodBody body) {
        Validate.notNull(type);
        Validate.notNull(name);
        Validate.notNull(params);
        Validate.notNull(body);
        this.type = type;
        this.name = name;
        this.params = params;
        this.body = body;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        if (DecacCompiler.getColor()) s.print("\033[0;31m");
        type.decompile(s);
        if (DecacCompiler.getColor()) s.print("\033[0m");
        s.print(" ");
        name.decompile(s);
        s.print("(");
        params.decompile(s);
        s.print(") ");
        body.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, true);
        name.prettyPrint(s, prefix, true);
        params.prettyPrint(s, prefix, true);
        body.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        name.iter(f);
        params.iter(f);
        body.iter(f);
    }

    @Override
    protected void verifyMethod(DecacCompiler compiler) throws ContextualError {
        Type t = type.verifyType(compiler);
        Signature signature = new Signature();
        for (AbstractDeclParam param : params.getList()) {
            signature.add(param.getType());
        }
        name.setDefinition(new MethodDefinition(t, name.getLocation(), signature, name.getClassDefinition().getNumberOfMethods()));
        name.getClassDefinition().incNumberOfMethods();
        name.setType(t);
    }

    @Override
    protected void verifyMethodMembers(DecacCompiler compiler) throws ContextualError {
        params.verifyListDeclParamMembers(compiler);
    }

    @Override
    protected void verifyMethodBody(DecacCompiler compiler) throws ContextualError {
        body.verifyMethodBody(compiler);
    }
}

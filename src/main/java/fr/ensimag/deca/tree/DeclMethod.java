package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public class DeclMethod extends AbstractDeclMethod {
    private final AbstractIdentifier type;
    private final AbstractIdentifier name;
    private final ListDeclParam params;
    private final EnvironmentExp envParam;
    private final AbstractMethodBody body;

    public DeclMethod(AbstractIdentifier type, AbstractIdentifier name, ListDeclParam params, AbstractMethodBody body) {
        Validate.notNull(type);
        Validate.notNull(name);
        Validate.notNull(params);
        Validate.notNull(body);
        this.type = type;
        this.name = name;
        this.params = params;
        this.envParam = new EnvironmentExp(null);
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
    protected void verifyMethod(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        Type t = type.verifyType(compiler);
        Signature signature = params.verifyListDeclParamMembers(compiler, this.envParam, currentClass);

        name.setDefinition(new MethodDefinition(t, name.getLocation(), signature, currentClass.getNumberOfMethods() + 1));
        currentClass.incNumberOfMethods();

        try {
            currentClass.getMembers().declare(name.getName(), name.getExpDefinition());
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("Exception : Method " + name.getName() + " already declared", name.getLocation());
        }
        name.verifyExpr(compiler, currentClass.getMembers(), currentClass);
    }

    @Override
    protected void verifyMethodBody(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("\t[PASSE 3] : \t Méthode " + this.name.getName());
        body.verifyMethodBody(compiler, this.envParam, currentClass, type.getType());
        LOG.debug("\t[PASSE 3] : \t [FIN]");
    }
}

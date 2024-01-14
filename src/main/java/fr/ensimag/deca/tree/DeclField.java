package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public class DeclField extends AbstractDeclField {
    private final AbstractIdentifier type;
    private final AbstractIdentifier field;
    private final AbstractInitialization initialization;

    private final Visibility visibility;

    public DeclField(AbstractIdentifier type, AbstractIdentifier field, AbstractInitialization initialization, Visibility visibility) {
        Validate.notNull(type);
        Validate.notNull(field);
        Validate.notNull(initialization);
        Validate.notNull(visibility);
        this.type = type;
        this.field = field;
        this.initialization = initialization;
        this.visibility = visibility;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        if (DecacCompiler.getColor()) {
            if (visibility == Visibility.PROTECTED) {
                s.print("protected ", "purple");
            }

        } else {
            if (visibility == Visibility.PROTECTED) {
                s.print("protected ");
            }
        }
        if (DecacCompiler.getColor()) s.print("\033[0;31m");
        type.decompile(s);
        if (DecacCompiler.getColor()) s.print("\033[0m");
        s.print(" ");
        field.decompile(s);
        if (!(initialization instanceof NoInitialization)) {
            s.print(" = ");
            initialization.decompile(s);
        }
        if (DecacCompiler.getColor()) {
            s.print(";", "orange");
        } else {
            s.println(";");
        }
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, true);
        field.prettyPrint(s, prefix, true);
        initialization.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        field.iter(f);
        initialization.iter(f);
    }

    @Override
    protected void verifyField(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void verifyFieldMembers(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void verifyFieldBody(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

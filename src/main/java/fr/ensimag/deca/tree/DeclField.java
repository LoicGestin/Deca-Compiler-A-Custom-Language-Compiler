package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public class DeclField extends AbstractDeclField{
    private AbstractIdentifier type;
    private AbstractIdentifier field;
    private AbstractInitialization initialization;

    private Visibility visibility;

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
        s.print("\033[0;35m");
        if (visibility == Visibility.PROTECTED) {
            s.print("protected ");
        }
        type.decompile(s);
        s.print(" \033[0m ");
        field.decompile(s);
        if (initialization instanceof NoInitialization)
        {
            s.println("\033[0;35m;\033[0m");
        }
        else
        {
            s.print(" = ");
            initialization.decompile(s);
            s.println("\033[0;35m;\033[0m");
        }
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Type;
import org.apache.commons.lang.Validate;

public abstract class AbstractDeclParam extends Tree {

    private AbstractIdentifier type;
    private AbstractIdentifier name;

    public AbstractDeclParam(AbstractIdentifier type, AbstractIdentifier name) {
        Validate.notNull(type);
        Validate.notNull(name);
        this.type = type;
        this.name = name;
    }

    public Type getType() {
        return type.getType();
    }

    public AbstractIdentifier getTypeIdent() {
        return type;
    }
    public AbstractIdentifier getName() {
        return name;
    }

    protected abstract void verifyParam(DecacCompiler compiler)
            throws ContextualError;

    protected abstract void verifyParamMembers(DecacCompiler compiler)
            throws ContextualError;

    protected abstract void verifyParamBody(DecacCompiler compiler)
            throws ContextualError;
}

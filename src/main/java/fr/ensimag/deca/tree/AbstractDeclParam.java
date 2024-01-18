package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import org.apache.commons.lang.Validate;

public abstract class AbstractDeclParam extends Tree {

    protected final AbstractIdentifier type;
    protected final AbstractIdentifier name;

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

    protected abstract Type verifyParam(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

}

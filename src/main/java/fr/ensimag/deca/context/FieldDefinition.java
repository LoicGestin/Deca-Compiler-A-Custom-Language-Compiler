package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.Location;
import fr.ensimag.deca.tree.Visibility;

/**
 * Definition of a field (data member of a class).
 *
 * @author gl29
 * @date 01/01/2024
 */
public class FieldDefinition extends ExpDefinition {
    private final Visibility visibility;
    private final ClassDefinition containingClass;
    private final int index;

    public FieldDefinition(Type type, Location location, Visibility visibility,
                           ClassDefinition memberOf, int index) {
        super(type, location);
        this.visibility = visibility;
        this.containingClass = memberOf;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public boolean isField() {
        return true;
    }

    @Override
    public FieldDefinition asFieldDefinition(String errorMessage, Location l)
            throws ContextualError {
        return this;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public ClassDefinition getContainingClass() {
        return containingClass;
    }

    @Override
    public String getNature() {
        return "field";
    }

    @Override
    public boolean isExpression() {
        return true;
    }

}

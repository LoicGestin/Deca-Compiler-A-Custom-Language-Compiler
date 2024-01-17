package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.Location;
import org.apache.commons.lang.Validate;

/**
 * Definition of a class.
 *
 * @author gl29
 * @date 01/01/2024
 */
public class ClassDefinition extends TypeDefinition {


    private EnvironmentExp members;
    private EnvironmentExp params;
    private ClassDefinition superClass;
    private int numberOfFields = 0;
    private int numberOfMethods = 0;
    private boolean estThis = false;

    public ClassDefinition(ClassType type, Location location, ClassDefinition superClass) {
        super(type, location);
        EnvironmentExp parent;
        if (superClass != null) {
            parent = superClass.getMembers();
        } else {
            parent = null;
        }
        members = new EnvironmentExp(parent);
        params = new EnvironmentExp(null);
        this.superClass = superClass;
    }

    public int getNumberOfFields() {
        return numberOfFields;
    }

    public void incNumberOfFields() {
        this.numberOfFields++;
    }

    public int getNumberOfMethods() {
        return numberOfMethods;
    }

    public int incNumberOfMethods() {
        numberOfMethods++;
        return numberOfMethods;
    }

    @Override
    public boolean isClass() {
        return true;
    }

    @Override
    public ClassType getType() {
        // Cast succeeds by construction because the type has been correctly set
        // in the constructor.
        return (ClassType) super.getType();
    }

    public ClassDefinition getSuperClass() {
        return superClass;
    }

    public void setSuperClass(ClassDefinition superClass) {
        this.superClass = superClass;
    }

    public EnvironmentExp getMembers() {
        return members;
    }

    public EnvironmentExp getParams() {
        return params;
    }

    public void setMembers(EnvironmentExp members) {
        this.members = members;
    }

    public void setisThis() {
        estThis = true;
    }

    public boolean isThis() {
        return estThis;
    }

}

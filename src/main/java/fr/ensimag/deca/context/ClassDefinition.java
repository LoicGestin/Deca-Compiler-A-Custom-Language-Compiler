package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.Location;

/**
 * Definition of a class.
 *
 * @author gl29
 * @date 01/01/2024
 */
public class ClassDefinition extends TypeDefinition {


    private EnvironmentExp members;
    private ClassDefinition superClass;
    private int numberOfFields = 0;
    private int numberOfMethods = 0;

    private int adressTable = 0;

    public ClassDefinition(ClassType type, Location location, ClassDefinition superClass) {
        super(type, location);
        EnvironmentExp parent;
        if (superClass != null) {
            parent = superClass.getMembers();
        } else {
            parent = null;
        }
        members = new EnvironmentExp(parent);
        this.superClass = superClass;
    }

    public int getNumberOfFields() {
        return numberOfFields;
    }
    public void incNumberOfFields() {
        this.numberOfFields++;
    }

    public void setNumberOfFields(int numberOfFields) {
        this.numberOfFields = numberOfFields;
    }

    public int getNumberOfMethods() {
        return numberOfMethods;
    }

    public void incNumberOfMethods() {
        numberOfMethods++;
    }

    public int getAdressTable() {
        return adressTable;
    }

    public void setAdressTable(int adressTable) {
        this.adressTable = adressTable;
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

    public EnvironmentExp getMembers() {
        return members;
    }

}

package fr.ensimag.ima.pseudocode;

import org.apache.commons.lang.Validate;

/**
 * Representation of a label in IMA code. The same structure is used for label
 * declaration (e.g. foo: instruction) or use (e.g. BRA foo).
 *
 * @author Ensimag
 * @date 01/01/2024
 */
public class Label extends Operand {

    @Override
    public String toString() {
        return type+typeIndex;
    }

    public Label(String type, int typeIndex) {
        super();
        String name = type+typeIndex;
        Validate.isTrue(name.length() <= 1024, "Label name too long, not supported by IMA");
        Validate.isTrue(name.matches("^[a-zA-Z][a-zA-Z0-9_.]*$"), "Invalid label name " + name);
        this.type = type;
        this.typeIndex=typeIndex;
    }

    public Label(String name) {
        super();
        Validate.isTrue(name.length() <= 1024, "Label name too long, not supported by IMA");
        Validate.isTrue(name.matches("^[a-zA-Z][a-zA-Z0-9_.]*$"), "Invalid label name " + name);
        this.type = name;
    }

    public String getType() {
        return type;
    }

    public int getTypeIndex() {
        return typeIndex;
    }

    private String type;
    private int typeIndex;
}

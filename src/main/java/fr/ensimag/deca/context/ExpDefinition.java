package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.Location;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;

/**
 * Definition associated to identifier in expressions.
 *
 * @author gl29
 * @date 01/01/2024
 */
public abstract class ExpDefinition extends Definition {

    private DAddr operand;
    private GPRegister GPRegister;

    private boolean isAddr = true;

    public ExpDefinition(Type type, Location location) {
        super(type, location);
    }

    public DAddr getOperand() {
        return operand;
    }
    public GPRegister getGPRegister() {return GPRegister;}

    public boolean isAddr() {return isAddr;}
    public void setOperand(DAddr operand) {
        this.operand = operand;
    }
    public void setGPRegister(GPRegister GPRegister) {
        this.isAddr = false;
        this.GPRegister = GPRegister;
    }

}

package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;

import java.util.Stack;

public class codeGen {
    static final Stack<GPRegister> registresLibres = new Stack<>();
    static final Stack<GPRegister> registresUtilises = new Stack<>();

    static final Stack<GPRegister> registresVariables = new Stack<>();

    static {
        for (int i = 15; i >= 2; i--) {
            registresLibres.push(Register.getR(i));
        }
    }
    static int indexPile = 0;

    public static void afficheStack(){
        System.out.println("registresLibres : ");
        for (GPRegister r : registresLibres) {
            System.out.print(r + " ");
        }
        System.out.println("\nregistresUtilises : ");
        for (GPRegister r : registresUtilises) {
            System.out.print(r + " ");
        }
        System.out.println();
    }
    public static GPRegister getCurrentRegistreLibre() {
        return registresLibres.peek();
    }
    public static GPRegister getCurrentRegistreUtilise() {
        return registresUtilises.peek();
    }

    public static GPRegister getRegistreLibre() {
        GPRegister r = registresLibres.pop();
        registresUtilises.push(r);
        return r;
    }
    public static GPRegister getRegistreUtilise() {
        GPRegister r = registresUtilises.pop();
        registresLibres.push(r);
        return r;
    }

    public static DAddr getRegistreVariable() {
        indexPile ++;
        return new RegisterOffset(indexPile, Register.GB);
    }
    public static GPRegister getGPRegisterVariable() {

        return registresLibres.peek();
    }
    public static void saveVariable(){
        GPRegister r = registresUtilises.pop();
        registresVariables.push(r);
    }
    public static boolean isGPRegisterRestant() {
        return registresLibres.size() > 2;
    }
}

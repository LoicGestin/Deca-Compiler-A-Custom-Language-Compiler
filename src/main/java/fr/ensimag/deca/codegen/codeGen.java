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
    static int indexRegistre = 0;

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

    public static void libererRegistreUtilises(int nb) {
        for (int i = 0; i < nb; i++) {
            GPRegister r = registresUtilises.pop();
            registresLibres.push(r);
        }
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
        /*if(registresLibres.size() > 2) {
            GPRegister r = registresLibres.pop();
            registresVariables.push(r);
            indexRegistre ++;
            // probleme
            return new RegisterOffset(indexRegistre, r);
        }*/
        indexPile ++;
        return new RegisterOffset(indexPile, Register.GB);
    }
}

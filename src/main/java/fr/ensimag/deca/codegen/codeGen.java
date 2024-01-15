package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;

import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class codeGen {
    static final Stack<GPRegister> registresLibres = new Stack<>();
    static final Stack<GPRegister> registresUtilises = new Stack<>();

    static final Stack<GPRegister> registresVariables = new Stack<>();

    static TreeMap<String, Integer> table = new TreeMap<>();

    static int nombreRegistres = 14;

    static int indexPile = 50;

    static int nombrePileTamporaire = 0;

    public static void setNombreRegistres(int nombreRegistres) {
        if (nombreRegistres >= 2) {
            codeGen.nombreRegistres = nombreRegistres;
        }
    }

    public static void setUpRegistres() {
        for (int i = nombreRegistres + 1; i >= 2; i--) {
            registresLibres.push(Register.getR(i));
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

    static DVal registreCourant = null;
    static boolean assignation = false;

    public static void setRegistreCourant(DVal registreCourant, DecacCompiler compiler) {
        if (assignation) {
            if (codeGen.registresLibres.empty()) {
                compiler.addInstruction(new PUSH(codeGen.getRegistreUtilise()));
                nbPush++;
            }

            compiler.addInstruction(new LOAD(registreCourant, codeGen.getRegistreLibre()));


        } else {
            codeGen.registreCourant = registreCourant;
        }
    }

    static int nbPush = 0;

    public static DVal getRegistreCourant(DecacCompiler compiler) {
        if (registreCourant == null) {
            if (nbPush > 0) {
                compiler.addInstruction(new LOAD(codeGen.getCurrentRegistreUtilise(), Register.getR(0)));
                compiler.addInstruction(new POP(codeGen.getCurrentRegistreUtilise()));
                nbPush--;
                return Register.getR(0);
            }
            else{

                return getRegistreUtilise();
            }

        } else {
            DVal dVal = registreCourant;
            registreCourant = null;
            return dVal;
        }
    }

    public static void setAssignation(boolean assignation) {
        codeGen.assignation = assignation;
    }

    public static DAddr getRegistreVariable() {
        indexPile++;
        return new RegisterOffset(indexPile, Register.GB);
    }

    public static GPRegister getGPRegisterVariable() {

        return registresLibres.peek();
    }

    public static void saveVariable() {
        GPRegister r = registresUtilises.pop();
        registresVariables.push(r);
    }

    public static void saveVariable2() {
        GPRegister r = registresLibres.pop();
        registresVariables.push(r);
    }

    // ----------------------------------------------------
    static Map<String, Integer> topNEntries;

    public static boolean isGPRegisterRestant(String s) {
        return registresLibres.size() > 2 && topNEntries.containsKey(s);
    }

    public static void genereTopNEntries() {
        if (nombreRegistres <= 2) {
            return;
        }
        topNEntries = table.entrySet()
                .stream()
                .limit(nombreRegistres - 2)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        TreeMap::new
                ));
    }


    public static void addVariableTable(String s) {
        table.merge(s, 1, Integer::sum);
    }

    public static int getValueVariableTable(String s) {
        return table.get(s);
    }

    public static boolean isRegistreLibreEmpty() {
        if (registresLibres.empty()) {
            indexPile++;
            nombrePileTamporaire++;
            return true;
        }
        return false;
    }

    public static boolean isRegistreInPile() {
        return nombrePileTamporaire > 0;
    }

    public static void afficheTable() {
        System.out.println("table : ");
        for (String s : table.keySet()) {
            System.out.println(s + " : " + table.get(s));
        }
        System.out.println();
    }

    public static void afficheStack() {
        System.out.println("registresLibres : ");
        for (GPRegister r : registresLibres) {
            System.out.print(r + " ");
        }
        System.out.println("\nregistresUtilises : ");
        for (GPRegister r : registresUtilises) {
            System.out.print(r + " ");
        }
        System.out.println("\nRegistre courant : " + registreCourant);
        System.out.println("--------------------");
        System.out.println();
    }
}

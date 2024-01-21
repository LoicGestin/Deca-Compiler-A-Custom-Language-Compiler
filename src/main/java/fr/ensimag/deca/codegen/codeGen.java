package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.DeclClass;
import fr.ensimag.deca.tree.DeclMethod;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.util.*;
import java.util.stream.Collectors;

public class codeGen {
    static final Stack<GPRegister> registresLibres = new Stack<>();
    static final Stack<GPRegister> registresUtilises = new Stack<>();
    static final Stack<GPRegister> registresVariables = new Stack<>();

    static final Stack<GPRegister> registresSauvegardes_variable = new Stack<>();
    static final Stack<GPRegister> registresSauvegardes_utlisee = new Stack<>();
    static TreeMap<String, Integer> table = new TreeMap<>();

    static Label objectLabel;
    static int nombreRegistres = 14;

    static int indexPile = 3;

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

    public static void init_registres(DecacCompiler compiler) {
        setUpRegistres();
        genereTopNEntries();
        int taille = codeGen.tableSize() + getTailleTableDesMethodes();


        if (taille != 0) {
            compiler.addInstruction(new TSTO(taille + 50)); // +50 pour laisser de la place pour les variables locales
            compiler.addInstruction(new BOV(new Label("pile_pleine")));
            compiler.addInstruction(new ADDSP(taille));
        }
    }

    public static GPRegister getCurrentRegistreLibre() {
        return registresLibres.peek();
    }

    public static GPRegister getCurrentRegistreUtilise() {
        return registresUtilises.peek();
    }

    public static GPRegister getRegistreLibre() {
        return registresUtilises.push(registresLibres.pop());
    }

    public static int tableSize() {
        int size = countEntries(table);
        int size2 = (topNEntries == null) ? 0 : countEntries(topNEntries);
        return size - size2;
    }

    public static int getIndexPile() {
        return indexPile;
    }

    private static int countEntries(Map<String, Integer> entryMap) {
        int count = 0;
        for (int value : entryMap.values()) {
            if (value > 1) {
                count++;
            }
        }
        return count;
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
            return getRegistreUtilise();

        }
        DVal dVal = registreCourant;
        registreCourant = null;
        return dVal;
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
        topNEntries = table.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(nombreRegistres - 2)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, TreeMap::new));
    }


    public static void addVariableTable(String s) {
        table.merge(s, 1, Integer::sum);
    }

    public static int getValueVariableTable(String s) {
        return table.get(s);
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

    public static void setObjectLabel(Label label) {
        objectLabel = label;
    }
    public static Label getObjectLabel() {
        return objectLabel;
    }

    public static int addIndexPile(){
        return indexPile ++;
    }

    public static  Map<String, Map<Integer, String>> tableDesMethodes;
    public static Map<String,  Map<Integer, String>> construireTableDesMethodes(List<DeclClass> classList){
        tableDesMethodes = new HashMap<>();

        for(DeclClass c : classList){
            Map<Integer, String>  table = new HashMap<>();

            if(c.getVarSuper().getName().getName().equals("Object")) {
                table.put(0, "code.Object.equals");
            }
            else {
                Map<Integer, String> parentTable = tableDesMethodes.get(c.getVarSuper().getName().getName());
                table.putAll(parentTable);
            }
            int index = table.size();
            List<DeclMethod> methodeList = c.getListDeclMethod().getList();
            for (DeclMethod declMethod : methodeList) {
                if (declMethod.isOverride())
                    table.put(declMethod.getMethodDefinition().getIndex(),"code."+ c.getClassName() + "." + declMethod.getName().getName());
                else {
                    table.put(declMethod.getMethodDefinition().getIndex() + index - 1, "code."+ c.getClassName() + "." + declMethod.getName().getName());
                }
            }
            tableDesMethodes.put(c.getClassName(), table);
        }

        return tableDesMethodes;
    }

    public static int getTailleTableDesMethodes(){
        int taille = 0;
        for(String s : tableDesMethodes.keySet()) {
            taille += tableDesMethodes.get(s).size() + 1;
        }
        return taille + 2;
    }

    public static Map<Integer,String> getTableDesMethodes(String s){
        return tableDesMethodes.get(s);
    }

    public static void protect_registres(DecacCompiler compiler){
        if (DecacCompiler.getDebug()){
            compiler.addComment("\tSauvegarde des registres");
        }

        compiler.addInstruction(new TSTO(nombreRegistres + 50)); // +50 pour laisser de la place pour les variables locales
        compiler.addInstruction(new BOV(new Label("pile_pleine")));
        compiler.addInstruction(new ADDSP(nombreRegistres));

        for (int i = 0; i < nombreRegistres; i++) {
            compiler.addInstruction(new PUSH(Register.getR(i)));
        }

    }

    public static void unprotect_registres(DecacCompiler compiler){
        if (DecacCompiler.getDebug()){
            compiler.addComment("\tRestauration des registres");
        }

        for (int i = nombreRegistres - 1; i >= 0; i--) {
            compiler.addInstruction(new POP(Register.getR(i)));
        }
        compiler.addInstruction(new SUBSP(nombreRegistres));

    }
    public static String currentMethod;
    public static void setCurrentMethod(String s){
        currentMethod = s;
    }
    public static String getCurrentMethod(){
        return currentMethod;
    }

    public static void clear_registres(DecacCompiler compiler) {
        while (!registresUtilises.empty()) {
           registresUtilises.pop();
        }
        while (!registresLibres.empty()) {
            registresLibres.pop();
        }
        setUpRegistres();
    }
}
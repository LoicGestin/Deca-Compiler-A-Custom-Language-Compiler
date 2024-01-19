package fr.ensimag.trigo;

import java.lang.Math;

public class TestUMath{

    public static void main(String args[]){
        UMath m = new UMath();

        System.out.println("Test de la bibliothèque UMath: \n\tcomputed | Expected ");
        System.out.println("Test de la fonction pow: ");
        System.out.println("\t3.5² = "+m.pow((float)3.5,2) +"\t|\t" + (float)Math.pow((float)3.5,2));
        System.out.println("\t5.32^-3 = "+m.pow((float)3.5,-3) +"\t|\t" + (float)Math.pow((float)3.5,-3));
        System.out.println("----------------------------------------------------------------------------");

        System.out.println("Test des constantes: ");
        System.out.println("\t+inf = \t" + m.POSITIVE_INFINITY + "\t|\t" + Float.POSITIVE_INFINITY);
        System.out.println("\t-inf = \t" + m.NEGATIVE_INFINITY + "\t|\t" + Float.NEGATIVE_INFINITY);
        System.out.println("\tNaN = \t" + m.NaN + "\t|\t" + Float.NaN);
        System.out.println("\tMax = \t" + m.MAX_VALUE + "\t|\t" + Float.MAX_VALUE);
        System.out.println("\tMin = \t" + m.MIN_VALUE + "\t|\t" + Float.MIN_VALUE);
        System.out.println("\tMin = \t" + m.MIN_VALUE + "\t|\t" + Float.MIN_VALUE);
        System.out.println("\tPi = \t" + m.PI + "\t|\t" + (float)Math.PI);
        System.out.println("----------------------------------------------------------------------------");

        System.out.println("Test de la fonction sig: ");
        System.out.println("\tSigne de -132: " + m.sign(-1));
        System.out.println("\tSigne de 0: " + m.sign(0));
        System.out.println("----------------------------------------------------------------------------");

        System.out.println("Test de la fonction abs: ");
        System.out.println("\t|-32.584| = " + m.abs((float)-32.584));
        System.out.println("----------------------------------------------------------------------------");

        System.out.println("Test des fonctions trigo: ");
        System.out.println("\tsin(128.32) = " + m.sin(((float)128.32)) + "\t|\t" + (float)Math.sin((float)128.32));
        System.out.println("\tcos(65.3265) = " + m.cos(((float)65.3265)) + "\t|\t" + (float)Math.cos((float)65.3265));
        System.out.println("\tasin(0.84) = " + m.asin(((float)0.84)) + "\t|\t" + (float)Math.asin((float)0.84));
        System.out.println("\tatan(1) = " + m.atan(((float)1)) + "\t|\t" + (float)Math.atan((float)1));
        System.out.println("----------------------------------------------------------------------------");

        System.out.println("Test de la fonction ulp: ");
        System.out.println("\tulp(75.61) = " + m.ulp((float)75.61) + "\t|\t" + Math.ulp((float)75.61));
        System.out.println("----------------------------------------------------------------------------");
    }
}

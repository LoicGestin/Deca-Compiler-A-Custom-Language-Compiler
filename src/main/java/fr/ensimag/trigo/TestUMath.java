package fr.ensimag.trigo;

import java.lang.Math;
import java.util.Random;

public class TestUMath{

    public static void main(String args[]){
        UMath m = new UMath();
        Random r = new Random();

        System.out.println("Test de la bibliothèque UMath: \n\tComputed | Expected ");
        System.out.println("Test de la fonction pow: ");
        System.out.println("\t3.5² = "+m.pow((float)3.5,2) +"\t|\t" + (float)Math.pow((float)3.5,2));
        System.out.println("\t1234.256497^-3 = "+m.pow((float)1234.256497,-3) +"\t|\t" + (float)Math.pow((float)1234.256497,-3));
        System.out.println("----------------------------------------------------------------------------");

        System.out.println("Test des constantes: ");
        System.out.println("\t+inf = \t" + m.POSITIVE_INFINITY + "\t|\t" + Float.POSITIVE_INFINITY);
        System.out.println("\t-inf = \t" + m.NEGATIVE_INFINITY + "\t|\t" + Float.NEGATIVE_INFINITY);
        System.out.println("\tNaN = \t" + m.NaN + "\t|\t" + Float.NaN);
        System.out.println("\tMax = \t" + m.MAX_VALUE + "\t|\t" + Float.MAX_VALUE);
        System.out.println("\tMin = \t" + m.MIN_VALUE + "\t|\t" + Float.MIN_VALUE);
        System.out.println("\tPi = \t" + 4*m.atan(1) + "\t|\t" + (float)Math.PI);
        System.out.println("----------------------------------------------------------------------------");

        System.out.println("Test de la fonction sig: ");
        System.out.println("\tSigne de -132: " + m.sign(-1));
        System.out.println("\tSigne de 0: " + m.sign(0));
        System.out.println("----------------------------------------------------------------------------");

        System.out.println("Test de la fonction abs: ");
        System.out.println("\t|-32.584| = " + m.abs((float)-32.584));
        System.out.println("----------------------------------------------------------------------------");

        System.out.println("Test de la fonctions sinus: ");
        for(int i=1;i<10;i++) {
            float f =r.nextFloat()*(float)Math.pow(2,r.nextFloat()*100)*(float)Math.pow(-1,(double)r.nextInt());
            System.out.println("\tsin("+ f +") = " + m.sin(f) + "\t|\t" + (float)Math.sin(f));
        }
        System.out.println("----------------------------------------------------------------------------");

        System.out.println("Test de la fonctions cosinus: ");
        for(int i=1;i<10;i++) {
            float f =r.nextFloat()*(float)Math.pow(2,r.nextFloat()*100)*(float)Math.pow(-1,(double)r.nextInt());
            System.out.println("\tcos("+ f +") = " + m.cos(f) + "\t|\t" + (float)Math.cos(f));
        }
        System.out.println("----------------------------------------------------------------------------");

        System.out.println("Test de la fonctions arcsinus: ");
        for(int i=1;i<10;i++) {
            float f =r.nextFloat()*(float)Math.pow(2,r.nextFloat()*100)*(float)Math.pow(-1,(double)r.nextInt());
            System.out.println("\tasin("+ f +") = " + m.asin(f) + "\t|\t" + (float)Math.asin(f));
        }
        System.out.println("----------------------------------------------------------------------------");

        System.out.println("Test de la fonctions arctangente: ");
        for(int i=1;i<10;i++) {
            float f =r.nextFloat()*(float)Math.pow(2,r.nextFloat()*100)*(float)Math.pow(-1,(double)r.nextInt());
            System.out.println("\tatan("+ f +") = " + m.atan(f) + "\t|\t" + (float)Math.atan(f));
        }
        System.out.println("----------------------------------------------------------------------------");

        System.out.println("Test de la fonction ulp: ");
        for(int i=1;i<10;i++) {
            float f =r.nextFloat()*(float)Math.pow(2,r.nextFloat()*100)*(float)Math.pow(-1,(double)r.nextInt());
            System.out.println("\tulp("+ f +") = " + m.ulp(f) + "\t|\t" + Math.ulp(f));
        }
        System.out.println("----------------------------------------------------------------------------");

        System.out.println("Comparaison cosNaif et cos: ");
        long start = System.nanoTime();
        float calc = m.cosNaif((float)1.0);
        long end = System.nanoTime();
        System.out.println("\tUMath: cosNaif(1.0) = " + calc + "\t|\t" + (end-start) + " ns");
        long start1 = System.nanoTime();
        float calc1 = m.cos((float)1.0);
        long end1 = System.nanoTime();
        System.out.println("\tUMath: cosHorner(1.0) = " + calc1 + "\t|\t" + (end1-start1) + " ns");
        long start2 = System.nanoTime();
        float calc2 = (float)Math.cos((float)1.0);
        long end2 = System.nanoTime();
        System.out.println("\tMath cos(1.0) = " + calc2 + "\t|\t" + (end2-start2) + " ns");
        System.out.println("----------------------------------------------------------------------------");
    }
}

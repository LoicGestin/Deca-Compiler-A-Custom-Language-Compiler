package fr.ensimag.trigo;

import java.io.IOException;
import java.lang.Math;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;

public class CompareDecaJava {

    public  static void main(String[] args) throws IOException {
        List<Float> floatList = Arrays.asList(0.001f, -12.3456f,0.5f, 9876543.21f, -0.000001f, 3.14159f,-876.54321f, 2.71828f, -999999.999f, 4.71239f);
        int cat=0;
        float avgDiff=0;

        Path path = Paths.get("src/test/deca/trigo/ResCompareDecaJava.txt");

        System.out.println("Comparaison sin ----------------------------------------------------------");
        for (int i=0; i<10;i++){
            float computedRes = Math.abs(Float.parseFloat(Files.readAllLines(path).get(cat+i)));
            float diff = computedRes-(float)Math.sin(floatList.get(i));

            System.out.println("Computed: "+computedRes +"\t\t\t| Expected:"+ (float)Math.sin(floatList.get(i)) + "\t\t\tDifference: " + diff);
        }
        System.out.println("==> Average difference: " + avgDiff/10 +"------------------------------------------------------\n");
        cat++;
        avgDiff=0;

        System.out.println("Comparaison cos ---------------------------------------------------------------");
        for (int i=0; i<10;i++){
            float computedRes = Math.abs(Float.parseFloat(Files.readAllLines(path).get(cat+i)));
            float diff = computedRes-(float)Math.cos(floatList.get(i));
            avgDiff += diff;
            System.out.println("Computed: "+computedRes +"\t\t\t| Expected:"+ (float)Math.cos(floatList.get(i)) + "\t\t\tDifference: " + (computedRes-Math.cos(floatList.get(i))));
        }
        System.out.println("==> Average difference: " + avgDiff/10 +"------------------------------------------------------\n");
        cat++;
        avgDiff=0;

        System.out.println("Comparaison atan --------------------------------------------------------------");
        for (int i=0; i<10;i++){
            float computedRes = Math.abs(Float.parseFloat(Files.readAllLines(path).get(cat+i)));
            float diff = computedRes-(float)Math.atan(floatList.get(i));
            avgDiff += diff;
            System.out.println("Computed: "+computedRes +"\t\t\t| Expected:"+ (float)Math.atan(floatList.get(i)) + "\t\t\tDifference: " + (computedRes-Math.atan(floatList.get(i))));
        }
        System.out.println("==> Average difference: " + avgDiff/10 +"------------------------------------------------------\n");
        cat++;
        avgDiff=0;

        System.out.println("Comparaison ULP --------------------------------------------------------------");
        for (int i=0; i<10;i++){
            float computedRes = Math.abs(Float.parseFloat(Files.readAllLines(path).get(cat+i)));
            float diff = computedRes-Math.ulp(floatList.get(i));
            avgDiff += diff;
            System.out.println("Computed: "+computedRes +"\t\t\t| Expected:"+ (float)Math.ulp(floatList.get(i)) + "\t\t\tDifference: " + (computedRes-Math.ulp(floatList.get(i))));
        }
        System.out.println("==> Average difference: " + avgDiff/10 +"------------------------------------------------------\n");
        cat++;

        floatList = Arrays.asList(
                -0.723f, 0.456f, 0.821f, -0.134f, -0.589f,
                0.675f, -0.287f, 0.932f, -0.045f, 0.213f
        );
        avgDiff=0;

        System.out.println("Comparaison asin ------------------------------------------------------------");
        for (int i=0; i<10;i++){
            float computedRes = Math.abs(Float.parseFloat(Files.readAllLines(path).get(cat+i)));
            float diff = computedRes-(float)Math.asin(floatList.get(i));
            avgDiff += diff;
            System.out.println("Computed: "+computedRes +"\t\t| Expected:"+ (float)Math.asin(floatList.get(i)) + "\t\tDifference: " + (computedRes-Math.asin(floatList.get(i))));
        }
        System.out.println("==> Average difference: " + avgDiff/10 +"------------------------------------------------------\n");
    }
}

package fr.ensimag.trigo;

import java.lang.Math;

public class TestUMath{

    public static void main(String args[]){
        UMath m = new UMath();
        
        System.out.println("3.5² = "+m.pow((float)3.5,2) +" " + (float)Math.pow((float)3.5,2));
        System.out.println("5.32^-3 = "+m.pow((float)3.5,-3) +" " + (float)Math.pow((float)3.5,-3));

        System.out.println("+inf = " + m.POSITIVE_INFINITY + " " + Float.POSITIVE_INFINITY);
        System.out.println("-inf = " + m.NEGATIVE_INFINITY + " " + Float.NEGATIVE_INFINITY);
        System.out.println("NaN = " + m.NaN + " " + Float.NaN);
        System.out.println("Max = " + m.MAX_VALUE + " " + Float.MAX_VALUE);
        System.out.println("Min = " + m.MIN_VALUE + " " + Float.MIN_VALUE);

        System.out.println("Signe de -132: " + m.sign(-1));
        System.out.println("Signe de 0: " + m.sign(0));

        System.out.println("|-32.584| = " + m.abs((float)-32.584));

        System.out.println("sin(3.12) = " + m.sin(((float)3.12)) + " " + (float)Math.sin((float)3.12));
        System.out.println("cos(65.3265) = " + m.cos(((float)65.3265)) + " " + (float)Math.cos((float)65.3265));
        System.out.println("asin(0.84) = " + m.asin(((float)0.84)) + " " + (float)Math.asin((float)0.84));
        System.out.println("atan(32.684) = " + m.atan(((float)32.684)) + " " + (float)Math.atan((float)32.684));
        System.out.println("ulp(75.61) = " + m.ulp((float)75.61) + " " + Math.ulp((float)75.61));
    }
}

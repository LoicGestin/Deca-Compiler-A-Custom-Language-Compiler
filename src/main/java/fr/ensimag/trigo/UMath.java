package fr.ensimag.trigo;
//Fichier destiné à devenir Math.decah, utilisé ici pour tester les algorithmes

public class UMath {

    float POSITIVE_INFINITY = pow(2,128);
    float NEGATIVE_INFINITY =  -this.POSITIVE_INFINITY;

    float NaN = pow(2,128)*(float)1.5;

    float MAX_VALUE = 0x1.fffffep127F;
    float MIN_VALUE = 0x0.000002p-126F;

    float PI = 4 * atan(1);

    boolean isInfinite(float f){
        return (f==POSITIVE_INFINITY || f== NEGATIVE_INFINITY);
    }

    boolean isNaN(float f){
        return f==NaN;
    }

    int sign(float f){
        if (f<0){
            return -1;
        }
        return 1;
    }

    float abs(float f){
        if (f>=0){
            return f;
        }
        else{
            return -f;
        }
    }

    float pow(float f, int exp) {
        if (exp == 0) {
            return 1;
        }
        if (exp>0) {
            return f * pow(f, exp - 1);
        }
        return pow(f,exp+1)/f;
    }

    protected float sinHornerFactor(float x2, int n, float inv_fact) {

        inv_fact = inv_fact / (2 * n * (2 * n + 1));

        if (n == 255) {
            return x2 * inv_fact;
        }

        return inv_fact - x2 * sinHornerFactor(x2, n + 1, inv_fact);
    }

    float sin(float f) {

        f= f%(2*(float)Math.PI);
        float x2 = pow(f, 2);
        float inter_res;

        if (isNaN(f) || isInfinite(f)) {
            return NaN;
        } else if (f == 0) {
            return 0;
        }

        inter_res = 1 - x2 * sinHornerFactor(x2, 1, 1);

        return f * inter_res;
    }

    protected float cosHornerFactor(float x2, int n, float inv_fact) {

        inv_fact = inv_fact/(2*n*(2*n-1));

        if (n == 255) {
            return x2 * inv_fact;
        }

        return inv_fact - x2 * cosHornerFactor(x2, n + 1, inv_fact);
    }

    float cos(float f) {

        f= f%(2*(float)Math.PI);
        float x2 = pow(f, 2);

        if (isNaN(f) || isInfinite(f)) {
            return Float.NaN;
        }

        return 1-x2*cosHornerFactor(x2, 1, 1);
    }

    float asinHornerFactor(float x2, int n, float recN) {
        recN = recN*(n-(float)0.5);

        if (n == 3) {
            System.out.print(recN/(2*n+1)+"x²)");
            for (int i=0;i<n;i++) System.out.print(")");
            return recN * x2/(2*n+1);
        }

        System.out.print(recN/(2*n+1)+"+x²(");
        return recN/(2*n+1) + x2 * asinHornerFactor(x2, n + 1,recN);
    }

    float asin(float f) {

        float x2 = pow(f, 2);
        float inter_res;

        if (isNaN(f) || abs(f) > 1) {
            return NaN;
        } else if (f == 0) {
            return 0;
        }

        System.out.print("x(1+x²(");
        inter_res = 1+x2*asinHornerFactor(x2, 1, (float)1);
        return f*inter_res;
    }

    float atanHF(float x2, int n){
        if (n==255){
            return x2/(2*n+1);
        }

        return (float)1/(2*n+1) - x2*atanHF(x2,n+1);
    }

    float atan(float f) {

        float x2 = pow(f, 2);

        if (isNaN(f)) {
            return NaN;
        } else if (f == 0) {
            return 0;
        }

        return f*atanHF(x2,0);
    }

    float ulp(float f){
        int e = -1;
        int continu = 1;
        float fabs = abs(f);
        float p=1;

        if (Float.isNaN(f))
            return f;
        if (Float.isInfinite(f))
            return POSITIVE_INFINITY;
        // This handles both +0.0 and -0.0.
        if (f == 0.0)
            return MIN_VALUE;

        while (continu==1 && e!=255) {
            e=e+1;
            if (p < fabs && fabs <= 2*p){
                continu = 0;
            }
            p=2*p;

        }
        return pow(2,e-23);
    }
}



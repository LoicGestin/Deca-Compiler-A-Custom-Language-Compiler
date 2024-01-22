package fr.ensimag.trigo;

class UMath {

    float POSITIVE_INFINITY = this.pow(2,128);
    float NEGATIVE_INFINITY =  -this.POSITIVE_INFINITY;

    float NaN = this.pow(2,128)*(float)1.5;

    float MAX_VALUE = 0x1.fffffep127F;
    float MIN_VALUE = 0x0.000002p-126F;

    float PI = (float)3.1415927;//4 * this.atan((float)1.0);

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
            return f * this.pow(f, exp - 1);
        }
        return this.pow(f,exp+1)/f;
    }

    float fact(int n){
        if (n==0){
            return 1;
        }
        return this.fact(n-1);
    }

    float sinHorner(float f2, int n, float invFact) {

        invFact = invFact / (2 * n * (2 * n + 1));

        if (n == 255) {
            return f2 * invFact;
        }

        return invFact - f2 * this.sinHorner(f2, n + 1, invFact);
    }

    float sin(float f) {

        f=f%(2*PI);
        float f2 = f*f;
        float inter_res;

        if (this.isNaN(f) || this.isInfinite(f)) {
            return NaN;
        } else if (f == 0) {
            return 0;
        }

        inter_res = 1 - f2 * this.sinHorner(f2, 1, 1);

        return f * inter_res;
    }

    float cosNaif(float f){
        float somme = (float)0.0;
        int n =0;
        while (n != 255) {
            somme = somme + this.pow(-1,n)*this.pow(f, 2*n)/this.fact(2*n);
            n=n+1;
        }
        return somme;
    }

    float cosHorner(float f2, int n, float invFact) {

        invFact = invFact/(2*n*(2*n-1));

        if (n == 255) {
            return f2 * invFact;
        }

        return invFact - f2 * this.cosHorner(f2, n + 1, invFact);
    }

    float cos(float f) {

        f=f%(2*PI);
        float f2 = f*f;

        if (this.isNaN(f) || this.isInfinite(f)) {
            return NaN;
        }

        return 1-f2*this.cosHorner(f2, 1, 1);
    }

    float asinHorner(float f2, int n, float recN) {
        recN = recN*(1-1/(2*n));

        if (n == 255) {
            return recN * f2/(2*n+1);
        }

        return recN/(2*n+1) + f2 * this.asinHorner(f2, n + 1,recN);
    }

    float asin(float f) {

        float f2 = f*f;
        float inter_res;

        if (this.isNaN(f) || this.abs(f) > 1) {
            return NaN;
        } else if (f == 0) {
            return 0;
        }

        inter_res = 1+f2*this.asinHorner(f2, 1, 1);
        return f*inter_res;
    }

    float atanHorner(float f2, int n){
        if (n==255){
            return f2/(2*n+1);
        }

        return 1/(2*n+1) - f2*this.atanHorner(f2,n+1);
    }

    float atan(float f) {

        float f2 = f*f;

        if (this.isNaN(f)) {
            return NaN;
        } else if (f == 0) {
            return 0;
        }

        return f*this.atanHorner(f2,0);
    }

    float ulp(float f){
        int e = -1;
        int continu = 1;
        float fabs = f;
        float p=1;

        if (this.isNaN(f)){
            return f;
        }
        if (this.isInfinite(f)){
            return POSITIVE_INFINITY;
        }
        // This handles both +0.0 and -0.0.
        if (f == 0.0){
            return MIN_VALUE;
        }

        if (f<0){
            fabs=-f;
        }
        while (continu==1 && e!=255) {
            e=e+1;
            if (p < fabs && fabs <= 2*p){
                continu = 0;
            }
            p=2*p;

        }

        return this.pow(2,e-23);
    }
}
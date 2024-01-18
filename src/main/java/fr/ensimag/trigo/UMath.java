package fr.ensimag.trigo;

public class UMath {

    float POSITIVE_INFINITY = pow(2,255);
    float NEGATIVE_INFINITY =  -this.POSITIVE_INFINITY;

    float NaN = pow(2,255)*(1+(float)0.5);

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
        if (exp == 1) {
            return f;
        }
        return f*pow(f, exp - 1);
    }

    protected float sinHornerFactor(float x2, int n, float inv_fact) {

        inv_fact = inv_fact / (2 * n * (2 * n + 1));

        if (n == 255) {
            return x2 * inv_fact;
        }

        return inv_fact - x2 * sinHornerFactor(x2, n + 1, inv_fact);
    }

    float sin(float f) {

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

        inv_fact = inv_fact /(n*(n+1));

        if (n == 255) {
            return x2 * inv_fact;
        }

        return inv_fact - x2 * cosHornerFactor(x2, n + 1, inv_fact);
    }

    float cos(float f) {

        f=f%(2*(float)Math.PI);
        float x2 = pow(f, 2);

        if (isNaN(f) || isInfinite(f)) {
            return Float.NaN;
        }

        return 1-cosHornerFactor(x2, 1, 1);
    }

    float asinHornerFactor(float x2, int n, float sqrt_num, float denom) {

        sqrt_num = sqrt_num * (sqrt_num + 1);
        denom = denom * 2 * n * (2 * n + 1);

        if (n == 255) {
            return pow(sqrt_num, 2) * x2 / denom;
        }

        return pow(sqrt_num, 2) / denom + x2 * asinHornerFactor(x2, n + 1, sqrt_num, denom);
    }

    float asin(float f) {
        float x2 = pow(f, 2);
        float inter_res;

        if (isNaN(f) || abs(f) > 1) {
            return Float.NaN;
        } else if (f == 0) {
            return 0;
        }

        inter_res = asinHornerFactor(x2, 1, 1, 1);
        return f*inter_res;
    }


    float atanHornerFactor(float x2, int n) {

        if (n == 255) {
            return x2 / (2 * n + 1);
        }

        return atanHornerFactor(x2, n + 1);
    }

    float atan(float f) {

        float x2 = pow(f, 2);

        if (isNaN(f)) {
            return NaN;
        } else if (f == 0) {
            return 0;
        }

        return f * atanHornerFactor(x2, 0);
    }
}
    /*
    //Restranscription du code source de la classe Math de Java
    //https://developer.classpath.org/doc/java/lang/Math-source.html, l.1014-1051
    float ulp(float f){
        if (Float.isNaN(f))
            return f;
        if (Float.isInfinite(f))
            return Float.POSITIVE_INFINITY;
        // This handles both +0.0 and -0.0.
        if (f == 0.0)
            return Float.MIN_VALUE;
        int bits = Float.floatToIntBits(f);
        final int mantissaBits = 23;
        final int exponentBits = 8;
        final int mantMask = (1 << mantissaBits) - 1;
        int mantissa = bits & mantMask;
        final int expMask = (1 << exponentBits) - 1;
        int exponent = (bits >>> mantissaBits) & expMask;

        // Denormal number, so the answer is easy.
        if (exponent == 0)
        {
            int result = (exponent << mantissaBits) | 1;
            return Float.intBitsToFloat(result);
        }

        // Conceptually we want to have '1' as the mantissa.  Then we would
        // shift the mantissa over to make a normal number.  If this underflows
        // the exponent, we will make a denormal result.
        int newExponent = exponent - mantissaBits;
        int newMantissa;
        if (newExponent > 0)
            newMantissa = 0;
        else
        {
            newMantissa = 1 << -(newExponent - 1);
            newExponent = 0;
        }
        return Float.intBitsToFloat((newExponent << mantissaBits) | newMantissa);
    }
}

     */


// End of Deca Math library

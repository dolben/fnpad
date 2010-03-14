/**
 *  fnPad: a functional programming style calculator
 *  Copyright © 2001-2010 Hank Dolben, org.dolben.fn.Pad.LICENSE
 */
package org.dolben.fn;

class ConditionalsExample {
    
    public static final String string =
        "// Here are a couple of conditional expressions.\n"+
        "// The first one means: if x is less than 0,\n"+
        "// then the result is -x, otherwise the result is x.\n"+
        "x < 0 ? -x : x  // absolute value\n"+
        "x >= 0 ? x : 0  // clip below zero\n"+
        "\n"+
        "x = -1\n"+
        "\n"+
        "// a recursive definition of the greatest common divisor\n"+
        "gcd(a,b) = ( a%b == 0 ) ? b : gcd(b,a%b)\n"+
        "//    This \u2191__________\u2191 parenthesis is for clarity.\n"+
        "\n"+
        "gcd(648,156)\n";

}

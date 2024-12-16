/**
 *  fnPad: a functional programming style calculator
 *  Copyright © 2001-2010 Hank Dolben, org.dolben.fn.Pad.LICENSE
 */
package org.dolben.fn;

class DefinitionsExample {
    
    public static final String string =
        "// A definition can be an identifier = an expression.\n"+
        "x = 3\n"+
        "\n"+
        "// Things that are defined can be used in an expression.\n"+
        "y = x+1\n"+
        "\n"+
        "// Definitions can have parameters\n"+
        "// and occur in the text after being used.\n"+
        "b = length(x,y)\n"+
        "\n"+
        "// Note that this \"b\" has a different value\n"+
        "// than \"b\" in the \"length(a,b)\" definition.\n"+
        "b\n"+
        "length(b,12)\n"+
        "\n"+
        "// There are some included functions and predefined constants.\n"+
        "atan2(y,x)*180/pi\n"+
        "\n"+
        "// Here's a function definition, length of the hypotenuse,\n"+
        "length(a,b) = sqrt(a^2+b^2)\n"+
        "\n"+
        "// and another, base 10 logarithm.\n"+
        "log(x) = ln(x)/ln(10)\n"+
        "\n"+
        "log(100); log(0.1)\n";

}

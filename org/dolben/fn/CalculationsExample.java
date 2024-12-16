/**
 *  fnPad: a functional programming style calculator
 *  Copyright © 2001-2010 Hank Dolben, org.dolben.fn.Pad.LICENSE
 */
package org.dolben.fn;

class CalculationsExample {
    
    public static final String string =
        "// A comment starts with a double slash\n"+
        "// and ends with the line.\n"+
        "3+8 // Add two numbers.\n"+
        "// fnPad inserts the wavy equals\n"+
        "// and the result which follows it\n"+
        "// when the \"Calculate\" menu item is selected.\n"+
        "\n"+
        "// Do multiple operations in an expression.\n"+
        "2+3*7\n"+
        "\n"+
        "// Parentheses override the normal precedence\n"+
        "// or associativity of operators.\n"+
        "(2+3)*7\n"+
        "\n"+
        "// Division of integers results in an integer.\n"+
        "5/2\n"+
        "\n"+
        "// Division of floating point numbers\n"+
        "// or mixed floating point and integers\n"+
        "// results in a floating point number.\n"+
        "5./2\n"+
        "\n"+
        "// An expression ends with the line or at a semicolon.\n"+
        "16./5.; 2^5\n"+
        "\n"+
        "// Note that all of these calculations are done at once.\n";

}

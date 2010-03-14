/**
 *  fnPad: a functional programming style calculator
 *  Copyright © 2001-2010 Hank Dolben, org.dolben.fn.Pad.LICENSE
 */
package org.dolben.fn;

class GraphingExample {
    
    public static final String string =
        "// To see the graph,\n"+
        "// open the left panel.\n"+
        "\n"+
        "// Solve and graph\n"+
        "// a quadratic equation.\n"+
        "// Define the coefficients.\n"+
        "a = 1; b = -2; c = 0\n"+
        "\n"+
        "// Find the discriminant and roots.\n"+
        "d; root1; root2\n"+
        "\n"+
        "// Graph the quadratic function.\n"+
        "graph(x,a*x^2+b*x+c)\n"+
        "\n"+
        "// Define the graph's bounds.\n"+
        "graph.x.min =  -5; graph.x.max =  5\n"+
        "graph.y.min = -15; graph.y.max = 15\n"+
        "\n"+
        "// Define the quadratic formula.\n"+
        "root1 = (-b-sqrt(d))/(2*a) // root with -\n"+
        "root2 = (-b+sqrt(d))/(2*a) // root with +\n"+
        "d = b^2-4*a*c // discriminant\n";

}

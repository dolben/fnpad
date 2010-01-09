/**
 *  fnPad: a functional programming style calculator
 *  Copyright (c) 2001-2010 Hank Dolben, org.dolben.fn.Pad.LICENSE
 */
package org.dolben.fn;

import java.text.DecimalFormat;

/**
 *  a formatted double
 */
public class FormatDouble {
    
    private double value;
    private DecimalFormat f;
	
	/**
	 *  makes a formatted String of its argument
	 */
    public FormatDouble( double v, DecimalFormat format ) {
        value = v;
        f = format;
    }
    
	/**
	 *  converts value to an octal string
	 *
	 *  @return value as an octal string
	 */
    public String toString( ) {
        return f.format(value);
    }

}

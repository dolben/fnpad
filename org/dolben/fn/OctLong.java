/**
 *  fnPad: a functional programming style calculator
 *  Copyright © 2001-2010 Hank Dolben, org.dolben.fn.Pad.LICENSE
 */
package org.dolben.fn;

/**
 *  an octal Long
 */
public class OctLong {
    
    private long value;
    
    /**
     *  makes an octal String of its argument
     */
    public OctLong( long v ) {
        value = v;
    }
    
    /**
     *  converts value to an octal string
     *
     *  @return value as an octal string
     */
    public String toString( ) {
        return "0"+Long.toOctalString(value);
    }

}

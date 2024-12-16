/**
 *  fnPad: a functional programming style calculator
 *  Copyright © 2001-2010 Hank Dolben, org.dolben.fn.Pad.LICENSE
 */
package org.dolben.fn;

/**
 *  a hexadecimal Long
 */
public class HexLong {
    
    private long value;
    
    /**
     *  makes a hexadecimal String of its argument
     */
    public HexLong( long v ) {
        value = v;
    }
    
    /**
     *  converts value to a hexadecimal string
     *
     *  @return value as a hexadecimal string
     */
    public String toString( ) {
        return "0x"+Long.toHexString(value);
    }

}

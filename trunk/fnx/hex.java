/**
 *  fnPad: a functional programming style calculator
 *  Copyright © 2001-2010 Hank Dolben, org.dolben.fn.Pad.LICENSE
 */
package fnx;

import org.dolben.fn.HexLong;

/**
 *  a fnPad hexadecimal string Function
 */
public class hex extends org.dolben.fn.Function {
    
    /**
     *  makes a hexadecimal String of its argument
     */
    public hex( ) {
        super(1);
    }
    
    /**
     *  converts the zeroth argument to a hexadecimal string
     *
     *  @return integer as a hexadecimal string
     */
    protected Object evaluate( ) throws Exception {
        return new HexLong(getLong(0));
    }

}

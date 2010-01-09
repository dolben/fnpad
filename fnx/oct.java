/**
 *  fnPad: a functional programming style calculator
 *  Copyright (c) 2001-2010 Hank Dolben, org.dolben.fn.Pad.LICENSE
 */
package fnx;

import org.dolben.fn.OctLong;

/**
 *  a fnPad octal string Function
 */
public class oct extends org.dolben.fn.Function {
	
	/**
	 *  makes an octal String of its argument
	 */
    public oct( ) {
        super(1);
    }
    
	/**
	 *  converts the zeroth argument to an octal string
	 *
	 *  @return integer as an octal string
	 */
    protected Object evaluate( ) throws Exception {
        return new OctLong(getLong(0));
    }

}

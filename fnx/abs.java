/**
 *  fnPad: a functional programming style calculator
 *  Copyright © 2001-2010 Hank Dolben, org.dolben.fn.Pad.LICENSE
 */
package fnx;

/**
 *  a fnPad absolute value Function
 */
public class abs extends org.dolben.fn.Function {
    
    /**
     *  makes an object that calculates the absolute value of its argument
     */
    public abs( ) {
        super(1);
    }
    
    /**
     *  calculates the absolute value of the zeroth argument
     *
     *  @return absolute value
     */
    protected Object evaluate( ) throws Exception {
        Number n = getNumber(0);
        if ( (n instanceof Double) ) {
            return new Double(Math.abs(n.doubleValue()));
        } else {
            return new Long(Math.abs(n.longValue()));
        }
    }

}

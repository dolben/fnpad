/**
 *  fnPad: a functional programming style calculator
 *  Copyright © 2001-2010 Hank Dolben, org.dolben.fn.Pad.LICENSE
 */
package fnx;

/**
 *  a fnPad arc-cosine Function
 */
public class acos extends org.dolben.fn.Function {
    
    /**
     *  makes an object that calculates the arc-cosine of its argument
     */
    public acos( ) {
        super(1);
    }
    
    /**
     *  calculates the arc-cosine of the zeroth argument
     *
     *  @return arc-cosine
     */
    protected Object evaluate( ) throws Exception {
        return new Double(Math.acos(getDouble(0)));
    }

}

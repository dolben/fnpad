/**
 *  fnPad: a functional programming style calculator
 *  Copyright © 2001-2010 Hank Dolben, org.dolben.fn.Pad.LICENSE
 */
package fnx;

/**
 *  a fnPad arc-tangent Function
 */
public class atan extends org.dolben.fn.Function {
    
    /**
     *  makes an object that calculates the arc-tangent of its argument
     */
    public atan( ) {
        super(1);
    }
    
    /**
     *  calculates the arc-tangent of the zeroth argument
     *
     *  @return arc-tangent
     */
    protected Object evaluate( ) throws Exception {
        return new Double(Math.atan(getDouble(0)));
    }

}

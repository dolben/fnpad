/**
 *  fnPad: a functional programming style calculator
 *  Copyright (c) 2001-2010 Hank Dolben, org.dolben.fn.Pad.LICENSE
 */
package fnx;

/**
 *  a fnPad arc-tangent Function with two parameters
 */
public class atan2 extends org.dolben.fn.Function {
    
    /**
     *  makes an object that calculates the arc-tangent of its arguments
     */
    public atan2( ) {
        super(2);
    }
    
    /**
     *  calculates the arc-tangent of the two arguments
     *
     *  @return arc-tangent
     */
    protected Object evaluate( ) throws Exception {
        return new Double(Math.atan2(getDouble(0),getDouble(1)));
    }

}

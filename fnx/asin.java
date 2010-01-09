/**
 *  fnPad: a functional programming style calculator
 *  Copyright (c) 2001-2010 Hank Dolben, org.dolben.fn.Pad.LICENSE
 */
package fnx;

/**
 *  a fnPad arc-sine Function
 */
public class asin extends org.dolben.fn.Function {
    
    /**
     *  makes an object that calculates the arc-sine of its argument
     */
    public asin( ) {
        super(1);
    }
    
    /**
     *  calculates the arc-sine of the zeroth argument
     *
     *  @return arc-sine
     */
    protected Object evaluate( ) throws Exception {
        return new Double(Math.asin(getDouble(0)));
    }

}

/**
 *  fnPad: a functional programming style calculator
 *  Copyright (c) 2001-2010 Hank Dolben, org.dolben.fn.Pad.LICENSE
 */
package fnx;

/**
 *  a fnPad natural logarithm Function
 */
public class ln extends org.dolben.fn.Function {
	
	/**
	 *  makes an object that calculates the natural logarithm of its argument
	 */
    public ln( ) {
        super(1);
    }
    
	/**
	 *  calculates the natural logarithm of the zeroth argument
	 *
	 *  @return natural logarithm
	 */
    protected Object evaluate( ) throws Exception {
        return new Double(Math.log(getDouble(0)));
    }
    
}

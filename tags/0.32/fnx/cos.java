/**
 *  fnPad: a functional programming style calculator
 *  Copyright (c) 2001-2010 Hank Dolben, org.dolben.fn.LICENSE
 */
package fnx;

/**
 *  a fnPad cosine Function
 */
public class cos extends org.dolben.fn.Function {
	
	/**
	 *  makes an object that calculates the cosine of its argument
	 */
    public cos( ) {
        super(1);
    }
    
	/**
	 *  calculates the cosine of the zeroth argument
	 *
	 *  @return cosine
	 *
	 *  @throws Exception when argument is not a Number
	 */
    protected Object evaluate( ) throws Exception {
        return new Double(Math.cos(getDouble(0)));
    }

}

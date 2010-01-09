/**
 *
 *  fnPad: a functional programming style calculator
 *  Copyright (c) 2001-2010 Hank Dolben, org.dolben.fn.LICENSE
 */
package fnx;

/**
 *  a fnPad square root Function
 */
public class sqrt extends org.dolben.fn.Function {
    
	/**
	 *  makes an object that calculates the square root of its argument
	 */
    public sqrt() {
        super(1);
    }
    
	/**
	 *  calculates the square root of the zeroth argument
	 *
	 *  @return square root
	 *
	 *  @throws Exception when argument is not a Number
	 */
    protected Object evaluate( ) throws Exception {
        return new Double(Math.sqrt(getDouble(0)));
    }
    
}

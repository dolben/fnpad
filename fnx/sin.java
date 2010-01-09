/**
 *  fnPad: a functional programming style calculator
 *  Copyright (c) 2001-2010 Hank Dolben, org.dolben.fn.Pad.LICENSE
 */
package fnx;

/**
 *  a fnPad sine Function
 */
public class sin extends org.dolben.fn.Function {
	
	/**
	 *  makes an object that calculates the sine of its argument
	 */
	public sin() {
		super(1);
	}
	
	/**
	 *  calculates the sine of the zeroth argument
	 *
	 *  @return sine
	 *
	 *  @throws Exception when argument is not a Number
	 */
	protected Object evaluate( ) throws Exception {
		return new Double(Math.sin(getDouble(0)));
	}
	
}

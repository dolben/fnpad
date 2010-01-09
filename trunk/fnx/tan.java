/**
 *  fnPad: a functional programming style calculator
 *  Copyright (c) 2001-2010 Hank Dolben, org.dolben.fn.Pad.LICENSE
 */
package fnx;

/**
 *  a fnPad tangent Function
 */
public class tan extends org.dolben.fn.Function {
	
	/**
	 *  makes an object that calculates the tangent of its argument
	 */
	public tan( ) {
		super(1);
	}
	
	/**
	 *  calculates the tangent of the zeroth argument
	 *
	 *  @return tangent
	 */
	protected Object evaluate( ) throws Exception {
		return new Double(Math.tan(getDouble(0)));
	}

}

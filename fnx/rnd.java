/**
 *  fnPad: a functional programming style calculator
 *  Copyright (c) 2001-2010 Hank Dolben, org.dolben.fn.Pad.LICENSE
 */
package fnx;

/**
 *  a fnPad rounding Function
 */
public class rnd extends org.dolben.fn.Function {
	
	/**
	 *  makes an object that calculates the rounding of its argument
	 */
	public rnd( ) {
		super(1);
	}
	
	/**
	 *  calculates the rounding of the zeroth argument
	 *
	 *  @return rounded number as a Long
	 */
	protected Object evaluate( ) throws Exception {
		return new Long(Math.round(getDouble(0)));
	}

}

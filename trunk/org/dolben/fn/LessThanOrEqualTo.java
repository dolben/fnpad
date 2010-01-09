/**
 *  fnPad: a functional programming style calculator
 *  Copyright (c) 2001-2010 Hank Dolben
 */
package org.dolben.fn;

/**
 *  the "<=" operation
 */
class LessThanOrEqualTo extends BinaryOperation {
	
	/**
	 *  makes a "<=" expression node for a evaluation
	 */
	public LessThanOrEqualTo( Evaluable left, Evaluable right ) {
		super(left,right,"<=");
	}
	
	/**
	 *  @return true iff left <= right
	 */
	protected Object operate( long left, long right ) {
		return new Boolean(left<=right);
	}
	
	/**
	 *  @return true iff left <= right
	 */
	protected Object operate( double left, double right ) {
		return new Boolean(left<=right);
	}
	
}

/**
 *  fnPad: a functional programming style calculator
 *  Copyright (c) 2001-2010 Hank Dolben, org.dolben.fn.Pad.LICENSE
 */
package org.dolben.fn;

abstract class BinaryOperation extends Evaluable {
	private Evaluable _left;
	private Evaluable _right;
	private String _op;

	public BinaryOperation( Evaluable left, Evaluable right, String op ) {
		_left = left;
		_right = right;
		_op = op;
	}
	
	public Object evaluate( ) throws Exception {
		Number left = getNumber(_left);
		Number right = getNumber(_right);
		if ( left instanceof Long && right instanceof Long ) {
			return operate(left.longValue(),right.longValue());
		} else {
			return operate(left.doubleValue(),right.doubleValue());
		}
	}
	
	protected abstract Object operate( long a, long b );
	
	protected abstract Object operate( double a, double b );
   
	public String toString() {
		return "("+_left+_op+_right+")";
	}
	
}

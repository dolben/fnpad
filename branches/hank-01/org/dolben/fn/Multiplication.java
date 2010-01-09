/**
 *
 *  fnPad: a functional programming style calculator
 *  Copyright (c) 2001-2010 Hank Dolben
 */
package org.dolben.fn;

/**
 *  the "*" operation
 */
class Multiplication extends BinaryOperation {
    
	/**
	 *  makes a "*" expression node for a evaluation
	 */
    public Multiplication( Evaluable left, Evaluable right ) {
        super(left,right,"*");
    }
    
	/**
	 *  @return left * right
	 */
    protected Object operate( long left, long right ) {
        return new Long(left*right);
    }
    
	/**
	 *  @return left * right
	 */
    protected Object operate( double left, double right ) {
        return new Double(left*right);
    }
    
}

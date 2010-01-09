/**
 *
 *  fnPad: a functional programming style calculator
 *  Copyright (c) 2001-2010 Hank Dolben, org.dolben.fn.LICENSE
 */
package org.dolben.fn;

class Exponentiation extends BinaryOperation {
    
    public Exponentiation( Evaluable left, Evaluable right ) {
		super(left,right,"^");
    }
    
    protected Object operate( double left, double right ) {
        return new Double(Math.pow(left,right));
    }
    
	protected Object operate( long left, long right ) {
		double lefd = left;
		double righd = right;
		return operate(lefd,righd);
	}
	
}

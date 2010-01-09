/**
 *  fnPad: a functional programming style calculator
 *  Copyright (c) 2001-2010 Hank Dolben, org.dolben.fn.LICENSE
 */
package org.dolben.fn;

class NotEqualTo extends BinaryOperation {
    
    public NotEqualTo( Evaluable left, Evaluable right ) {
        super(left,right,"!=");
    }
    
    protected Object operate( long left, long right ) {
        return new Boolean(left!=right);
    }
    
    protected Object operate( double left, double right ) {
        return new Boolean(left!=right);
    }
    
}

/**
 *  @author  Hank Dolben
 *  @version 2004 Apr 10
 *
 *  fnPad: a functional programming style calculator
 *  Copyright (c) 2001-2010 Hank Dolben, org.dolben.fn.LICENSE
 */
package org.dolben.fn;

class Subtraction extends BinaryOperation {
    
    public Subtraction( Evaluable left, Evaluable right ) {
        super(left,right,"-");
    }
    
    protected Object operate( long left, long right ) {
        return new Long(left-right);
    }
    
    protected Object operate( double left, double right ) {
        return new Double(left-right);
    }
    
}
/**
 *  fnPad: a functional programming style calculator
 *  Copyright © 2001-2010 Hank Dolben, org.dolben.fn.Pad.LICENSE
 */
package org.dolben.fn;

class RightShift extends Evaluable {
    private Evaluable _left;
    private Evaluable _right;
    
    RightShift( Evaluable left, Evaluable right ) {
        _left = left;
        _right = right;
    }
    
    public Object evaluate( ) throws Exception {
        Long left = getLong(_left);
        Long right = getLong(_right);
        return new Long( left.longValue() >> right.longValue() );
    }
    
    public String toString() {
        return "("+_left+">>"+_right+")";
    }
    
}

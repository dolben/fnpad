/**
 *  fnPad: a functional programming style calculator
 *  Copyright © 2001-2010 Hank Dolben, org.dolben.fn.Pad.LICENSE
 */
package org.dolben.fn;

class And extends Evaluable {
    private Evaluable _left;
    private Evaluable _right;
    
    And( Evaluable left, Evaluable right ) {
        _left = left;
        _right = right;
    }
    
    public Object evaluate( ) throws Exception {
        Boolean left = getBoolean(_left);
        Boolean right = getBoolean(_right);
        return new Boolean( left.booleanValue() && right.booleanValue() );
    }
    
    public String toString() {
        return "("+_left+"&&"+_right+")";
    }
    
}

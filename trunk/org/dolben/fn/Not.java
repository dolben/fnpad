/**
 *  fnPad: a functional programming style calculator
 *  Copyright © 2001-2010 Hank Dolben, org.dolben.fn.Pad.LICENSE
 */
package org.dolben.fn;

class Not extends Evaluable {
    private Evaluable _expr;
    
    Not( Evaluable expr ) {
        _expr = expr;
    }
    
    public Object evaluate( ) throws Exception {
        Boolean arg = getBoolean(_expr);
        return new Boolean(!arg.booleanValue());
    }
    
    public String toString() {
        return "!("+_expr+")";
    }
    
}

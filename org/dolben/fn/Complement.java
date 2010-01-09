/**
 *  fnPad: a functional programming style calculator
 *  Copyright (c) 2001-2010 Hank Dolben, org.dolben.fn.LICENSE
 */
package org.dolben.fn;

class Complement extends Evaluable {
    private Evaluable _expr;
    
    Complement( Evaluable expr ) {
        _expr = expr;
    }
    
    public Object evaluate( ) throws Exception {
        Long a = getLong(_expr);
        return new Long(~a.longValue());
    }
    
    public String toString() {
        return "~("+_expr+")";
    }
    
}

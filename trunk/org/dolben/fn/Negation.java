/**
 *  @author  Hank Dolben
 *  @version 2004 Apr 10
 *
 *  fnPad: a functional programming style calculator
 *  Copyright (c) 2001-2010 Hank Dolben, org.dolben.fn.LICENSE
 */
package org.dolben.fn;

class Negation extends Evaluable {
    private Evaluable _expr;
    
    Negation( Evaluable expr ) {
        _expr = expr;
    }
    
    public Object evaluate( ) throws Exception {
        Number a = getNumber(_expr);
        if ( a instanceof Long ) {
            return new Long(-a.longValue());
        } else {
            return new Double(-a.doubleValue());
        }
    }
    
    public String toString() {
        return "-("+_expr+")";
    }
    
}

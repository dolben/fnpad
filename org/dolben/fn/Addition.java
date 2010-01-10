/**
 *
 *  fnPad: a functional programming style calculator
 *  Copyright (c) 2001-2010 Hank Dolben, org.dolben.fn.Pad.LICENSE
 */
package org.dolben.fn;

import java.util.List;
import java.util.LinkedList;

class Addition extends Evaluable {
    private Evaluable _left;
    private Evaluable _right;
    
    public Addition( Evaluable left, Evaluable right ) {
        _left = left;
        _right = right;
    }
    
    public Object evaluate( ) throws Exception {
        Object left = _left.evaluate();
        Object right = _right.evaluate();
        if ( left instanceof Number && right instanceof Number ) {
            Number a = (Number)left;
            Number b = (Number)right;
            if ( a instanceof Long && b instanceof Long ) {
                return new Long(a.longValue()+b.longValue());
            } else {
                return new Double(a.doubleValue()+b.doubleValue());
            }
        } else if ( left instanceof String ) {
            return (String)left+right;
        } else if ( right instanceof String ) {
            return left+(String)right;
        } else if ( left instanceof List<?> && right instanceof List<?> ) {
            List<Object> concatenation = new LinkedList<Object>((List<?>)right);
            concatenation.addAll((List<?>)left);
            return concatenation;
        } else {
            throw new Exception(
                "types of '"+_left+"' and '"+_right+"' are not compatible"
            );
        }
    }
    
}

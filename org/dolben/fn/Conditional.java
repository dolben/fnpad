/**
 *  fnPad: a functional programming style calculator
 *  Copyright © 2001-2010 Hank Dolben, org.dolben.fn.Pad.LICENSE
 */
package org.dolben.fn;

class Conditional extends Evaluable {
    private Evaluable _test;
    private Evaluable _trueExpr;
    private Evaluable _falseExpr;
    
    Conditional( Evaluable test, Evaluable trueExpr, Evaluable falseExpr ) {
        _test = test;
        _trueExpr = trueExpr;
        _falseExpr = falseExpr;
    }
    
    public Object evaluate( ) throws Exception {
        Boolean test = getBoolean(_test);
        return test.booleanValue() ? 
                    _trueExpr.evaluate() :
                    _falseExpr.evaluate();
    }
    
    public String toString() {
        return "("+_test+"?"+_trueExpr+":"+_falseExpr+")";
    }
    
}

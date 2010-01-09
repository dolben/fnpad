/**
 *  fnPad: a functional programming style calculator
 *  Copyright (c) 2001-2010 Hank Dolben, org.dolben.fn.LICENSE
 */
package org.dolben.fn;

import java.util.Stack;

/**
 *  represents an indexing expression, such as a[i]
 */
class Index extends Evaluable {
	
	private Evaluable _expr;  // an expression resulting in an array
	private Evaluable _index; // an index expression
    
	/**
	 *  makes an indexing node
	 */
	Index( Evaluable expr, Evaluable index ) {
		_expr = expr;
		_index = index;
	}
	
	/**
	 *  evaluates the indexing of an array
	 */
    public Object evaluate( ) throws Exception {
		return evaluate(new Stack<Long>());
    }
	
	/**
	 *  evaluates the indexing of an array
	 */
	public Object evaluate( Stack<Long> indest ) throws Exception {
		Object index = _index.evaluate();
		if ( index instanceof Long ) {
			indest.push((Long)index);
			return _expr.evaluate(indest);
		} else {
			throw new Exception("'"+_index+"' is not an integer");
		}
	}
	
	public String toString( ) {
		return _expr+"["+_index+"]";
	}

}

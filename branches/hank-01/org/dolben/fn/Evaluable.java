/**
 *
 *  fnPad: a functional programming style calculator
 *  Copyright (c) 2001-2010 Hank Dolben, org.dolben.fn.LICENSE
 */
package org.dolben.fn;

import java.util.Stack;

abstract class Evaluable {

	/**
	 *  evaluates this expression
	 *
	 *  @return the result
	 *
	 *  @throws Exception when something goes wrong with evaluation
	 */
    public abstract Object evaluate( ) throws Exception;

	/**
	 *  evaluates this expression with indexing
	 *
	 *  @return the result
	 *
	 *  @throws Exception when something goes wrong with evaluation
	 */
    public Object evaluate( Stack<Long> indest ) throws Exception {
		if ( indest.empty() ) {
			return evaluate();
		} else {
			throw new Exception("'"+this+"' is not an array");
		}
	}
	
	/**
	 *  gets the Number from evaluating an expression
	 *
	 *  @param expr the subexpression from which to get the number
	 *
	 *  @return the number
	 *
	 *  @throws Exception when the expression does not result in a number
	 */
	public Number getNumber( Evaluable expr ) throws Exception {
		Object object = expr.evaluate();
		if ( !(object instanceof Number) ) {
			throw new Exception("'"+expr+"' is not a number, in "+this);
		}
		return (Number)object;
	}
	
	/**
	 *  gets the Long from evaluating an expression
	 *
	 *  @param expr the subexpression from which to get the long
	 *
	 *  @return the long
	 *
	 *  @throws Exception when the expression does not result in a long
	 */
	public Long getLong( Evaluable expr ) throws Exception {
		Object object = expr.evaluate();
		if ( !(object instanceof Long) ) {
			throw new Exception("'"+expr+"' is not an integer, in "+this);
		}
		return (Long)object;
	}
	
	/**
	 *  gets the Boolean from evaluating an expression
	 *
	 *  @param expr the subexpression from which to get the boolean
	 *
	 *  @return the boolean
	 *
	 *  @throws Exception when the expression does not result in a boolean
	 */
	public Boolean getBoolean( Evaluable expr ) throws Exception {
		Object object = expr.evaluate();
		if ( !(object instanceof Boolean) ) {
			throw new Exception("'"+expr+"' is not a boolean, in "+this);
		}
		return (Boolean)object;
	}
		
}

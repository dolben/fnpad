/**
 *  fnPad: a functional programming style calculator
 *  Copyright (c) 2001-2010 Hank Dolben, org.dolben.fn.Pad.LICENSE
 */
package org.dolben.fn;

import java.util.List;

/**
 *  a derivation interface for fnPad functions written in Java
 *  </p><p>
 *  A derived class
 *  <ul>
 *    <il>has a constructor that specifies the number of paramaters</il>
 *    <il>implements evaluate() with no parameters</il>
 *    <il>uses getDouble() or getNumber() to get its arguments</il>
 *  </ul>
 */
public abstract class Function extends Symbol {

	private int          _params; // the number of parameters
	private List<Object> _args;   // the arguments during a "call"
	
	/**
	 *  sets the Symbol name to the (derived) class name
	 *  and saves the number of parameters
	 *
	 *  @param params the number of parameters to the derived class "function"
	 */
	public Function( int params ) {
		setName(this.getClass().getName());
		_params = params;
	}
	
	/**
	 *  implements Symbol's evaluate,
	 *  checks that there are the correct number of arguments,
	 *  saves the list, and calls the derived class's evaluate()
	 *
	 *  @param arguments a list of arguments
	 *
	 *  @return the result of the derived class's evaluation
	 *
	 *  @throws Exception when there is an incorrect number of arguments
	 */
	public Object evaluate( List<Evaluable> arguments ) throws Exception {
		if ( arguments.size() != _params ) {
			throw new Exception(
				"number of arguments of '"+this+"' is not equal to "+_params
			);
		}
		_args = evaluateArguments(arguments);
		return evaluate();
	}
	
	/**
	 *  evaluates the derived class's function
	 *
	 *  @return the result of the evaluation
	 *
	 *  @throws Exception
	 */
	protected abstract Object evaluate( ) throws Exception;
	
	/**
	 *  gets the double value of an argument
	 *
	 *  @param index which argument (starts at zero)
	 *
	 *  @throws Exception when the argument is not a Number
	 */
	protected double getDouble( int index ) throws Exception {
		return getNumber(index).doubleValue();
	}
	
	/**
	 *  gets an argument which is a Number
	 *
	 *  @param index which argument (starts at zero)
	 *
	 *  @return the argument Number
	 *
	 *  @throws Exception when the argument is not a Number
	 */
	protected Number getNumber( int index ) throws Exception {
		Object object = _args.get(_params-index-1);
		if ( !(object instanceof Number) ) {
			throw new Exception(
				"'"+object+"' is not a number, argument of '"+this+"'"
			);
		}
		return (Number)object;
	}
	
	/**
	 *  gets an argument which is a Long
	 *
	 *  @param index which argument (starts at zero)
	 *
	 *  @return the argument Long
	 *
	 *  @throws Exception when the argument is not a Long
	 */
	protected long getLong( int index ) throws Exception {
		Object object = _args.get(_params-index-1);
		if ( !(object instanceof Long) ) {
			throw new Exception(
				"'"+object+"' is not an integer, argument of '"+this+"'"
			);
		}
		return ((Long)object).longValue();
	}
	
}

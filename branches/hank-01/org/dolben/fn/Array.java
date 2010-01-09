/**
 *  fnPad: a functional programming style calculator
 *  Copyright (c) 2001-2010 Hank Dolben, org.dolben.fn.Pad.LICENSE
 */
package org.dolben.fn;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 *  represents an array
 */
class Array extends Evaluable {
	
	private List<Evaluable> _elements; // the elements of the array
	
	/**
	 *  makes a new array with the given elements
	 */
	Array( List<Evaluable> elements ) {
		_elements = elements;
	}
	
	/**
	 *  evaluates the elements of the array
	 *
	 *  @return a List of the results
	 */
	public Object evaluate( ) throws Exception {
		List<Object> results = new LinkedList<Object>();
		for ( Evaluable expr : _elements ) {
			results.add(expr.evaluate());
		}
		return results;
	}
	
	/**
	 *  evaluates an element of the array
	 *
	 *  @param index the index of the element
	 *
	 *  @return the result
	 */
	public Object evaluate( Stack<Long> indest ) throws Exception {
		if ( indest.empty() ) {
			return evaluate();
		}
		Long index = indest.pop();
		Evaluable expr = _elements.get(_elements.size()-index.intValue());
		return expr.evaluate(indest);
	}

}

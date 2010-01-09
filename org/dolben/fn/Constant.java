/**
 *  fnPad: a functional programming style calculator
 *  Copyright (c) 2001-2010 Hank Dolben, org.dolben.fn.LICENSE
 */
package org.dolben.fn;

import java.util.List;
import java.util.Stack;

public class Constant extends Evaluable {
    private Object _value; // this Constant's value
	
	/**
	 *  makes a constant from any Object
	 *
	 *  @param value the value of the new Constant
	 */
    Constant( Object value ) {
        _value = value;
    }
    
	/**
	 *  makes a constant from a double
	 *
	 *  @param value the value of the new Constant
	 */
    Constant( double value ) {
        _value = new Double(value);
    }
    
	/**
	 *  makes a constant from a boolean
	 *
	 *  @param value the value of the new Constant
	 */
    Constant( boolean value ) {
        _value = new Boolean(value);
    }
    
	/**
	 *  evaluates the Constant
	 *
	 *  @return its value
	 */
    public Object evaluate( ) {
        return _value;
    }
    
	/**
	 *  evaluates the indexed Constant
	 *
	 *  @return its value
	 */
    public Object evaluate( Stack<Long> indext ) throws Exception {
        Object value = _value;
        while ( !indext.empty() ) {
            if ( value instanceof List<?> ) {
                List<?> vlist = (List<?>)value;
                Long index = indext.pop();
                value = vlist.get(vlist.size()-index.intValue());
            } else {
                throw new Exception("'"+value+"' is not an array");
            }
        }
        return value;
    }
    
	/**
	 *  converts the Constant to a String
	 *
	 *  @return the string for the value
	 */
    public String toString( ) {
        return _value.toString();
    }

}

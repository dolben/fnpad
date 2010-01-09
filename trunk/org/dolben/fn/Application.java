/**
 *  fnPad: a functional programming style calculator
 *  Copyright (c) 2001-2010 Hank Dolben, org.dolben.fn.LICENSE
 */
package org.dolben.fn;

import java.util.List;
import java.util.Stack;

/**
 *  the representation of the application of a function, bound to a symbol,
 *  to a list of arguments, i.e., a "call", e.g., f(x,y)
 */
class Application extends Evaluable {
    private Symbol          _symbol;
    private List<Evaluable> _arguments;
    
    /**
     *  makes an application with the given symbol
     *
     *  @param symbol the symbol for this application
     */
    Application( Symbol symbol ) {
        _symbol = symbol;
    }
    
    /**
     *  gets this application's symbol
     *
     *  @return the symbol
     */
    public Symbol getSymbol() {
        return _symbol;
    }
    
    /**
     *  evaluates the symbol's expression with the list of arguments
     *
     *  @return the result of the evaluation
     *
     *  @throws Exception when something goes wrong with the evaluation
     */
    public Object evaluate( ) throws Exception {
        return _symbol.evaluate(_arguments);
    }
    
    
    /**
     *  evaluates the symbol's expression with the list of arguments
     *  and stack of indices
     *
     *  @param indest a stack of indices
     *
     *  @return the result of the evaluation
     *
     *  @throws Exception when something goes wrong with the evaluation
     */
    public Object evaluate( Stack<Long> indest ) throws Exception {
        return _symbol.evaluate(_arguments,indest);
    }
    
    /**
     *  sets this application's list of arguments
     *
     *  @param arguments a list of arguments
     */
    public void setArguments( List<Evaluable> arguments ) {
        _arguments = arguments;
    }
    
    /**
     *  gets this application's list of arguments
     *
     *  @return the list of arguments
     */
    public List<Evaluable> getArguments() {
        return _arguments;
    }
    
    /**
     *  makes a string representing the function call
     *
     *  @return the string
     */
    public String toString( ) {
        String s = _symbol.toString();
        if ( _arguments.isEmpty() ) {
            return s;
        } else {
            return s+"("+_arguments.toString()+")";
        }
    }
    
}

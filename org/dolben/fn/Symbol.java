/**
 *  fnPad: a functional programming style calculator
 *  Copyright (c) 2001-2010 Hank Dolben, org.dolben.fn.Pad.LICENSE
 */
package org.dolben.fn;

import java.util.LinkedList;
import java.util.Stack;
import java.util.List;
import java.util.Iterator;

public class Symbol {
    private String           _name;
    private Stack<Evaluable> _expr;
    private List<Symbol>     _parameters;
    
    public Symbol( String name ) {
        _name = name;
        _expr = new Stack<Evaluable>();
        _parameters = new LinkedList<Symbol>();
    }
    
    public Symbol( ) {
    }
    
    /**
     *  sets the name of this symbol
     *
     *  @param name the name
     */
    public void setName( String name ) {
        _name = name;
    }
    
    /**
     *  gets the name of this symbol
     *
     *  @return the name
     */
    public String getName( ) {
        return _name;
    }
    
    /**
     *  evaluates this symbol's expression with its parameters bound to the
     *  given arguments, passing through a stack of inidices
     *
     *  @param arguments a list of expressions to be evaluated to get the arguments
     *  @param indext    a stack of index values
     *
     *  @return the result of the evaluation
     *
     *  @throws Exception when something goes wrong in the evaluation
     */
    public Object evaluate( List<Evaluable> arguments, Stack<Long> indext )
    	throws Exception {
        Evaluable expr = getBoundExpression(arguments);
        Object result = expr.evaluate(indext);
        unbindParameters();
        return result;
    }
    
    /**
     *  evaluates this symbol's expression with its parameters bound to the
     *  given arguments
     *
     *  @param arguments a list of expressions to be evaluated to get the arguments
     *  @param indext    a stack of index values
     *
     *  @return the result of the evaluation
     *
     *  @throws Exception when something goes wrong in the evaluation
     */
    public Object evaluate( List<Evaluable> arguments ) throws Exception {
        Evaluable expr = getBoundExpression(arguments);
        Object result = expr.evaluate();
        unbindParameters();
        return result;
    }
    
    private Evaluable getBoundExpression( List<Evaluable> arguments )
    	throws Exception {
        if ( _expr.empty() ) {
            throw new Exception("'"+_name+"' is undefined");
        }
        bindParameters(arguments);
        return _expr.peek();
    }
    
    protected List<Object> evaluateArguments( List<Evaluable> arguments )
    	throws Exception {
        List<Object> values = new LinkedList<Object>();
        for ( Evaluable expr : arguments ) {
            values.add(expr.evaluate());
        }
        return values;
    }
    
    private void bindParameters( List<Evaluable> arguments ) throws Exception {
        if ( _expr.size() == 1 ) {
            // if Symbol is a parameter itself, it has no parameters
            Iterator<Object> arg = evaluateArguments(arguments).iterator();
            for ( Symbol parameter : _parameters ) {
                if ( !arg.hasNext() ) {
                    throw new Exception("too few arguments to '"+_name+"'");
                }
                parameter.setExpression(new Constant(arg.next()));
            }
            if ( arg.hasNext() ) {
                throw new Exception("too many arguments to '"+_name+"'");
            }
        }
    }
    
    private void unbindParameters( ) {
        if ( _expr.size() == 1 ) {
            for ( Symbol parameter : _parameters ) {
                parameter.unsetExpression();
            }
        }
    }
   
    public void setExpression( Evaluable expr ) {
        _expr.push(expr);
    }
    
    public void unsetExpression( ) {
        _expr.pop();
    }
    
    public void setParameters( List<Symbol> parameters ) {
        _parameters = parameters;
    }
    
    public List<Symbol> getParameters( ) {
        return _parameters;
    }
    
    public String toString( ) {
        return _name;
    }

}

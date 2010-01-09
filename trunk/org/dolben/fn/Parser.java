/**
 *  fnPad: a functional programming style calculator
 *  Copyright (c) 2001-2010 Hank Dolben, org.dolben.fn.Pad.LICENSE
 */
package org.dolben.fn;

/**
 *  parser for the grammar:
 *
 *  definition -> call = expression
 *
 *  -- the rest from [Aho and Ullman 1977, example 4.6]
 *     with "call" added:
 *
 *  expression -> expression TOP term   | term
 *  term       -> term       FOP factor | factor
 *  factor     -> primary    ^   factor | primary
 *  primary    -> - primary | element
 *  element    -> ( expression ) | LITERAL | function
 *  call       -> IDENTIFIER | IDENTIFIER ( ) | IDENTIFIER ( arguments )
 *  arguments  -> expression | expression , arguments
 *
 *  TOP -> + | -
 *  FOP -> * | /
 *
 *  NOTE: exponentiation is right associative
 */

import java.util.HashMap;
import java.util.Map;
import java.util.LinkedList;
import java.util.List;

/**
 *  a recursive descent parser
 *
 *  with immediate left-recursion eliminated and left-factored,
 *  grammar is:
 *-------------------------------------------------------------------------------
 *  definition				->  call "="  conditional-expression
                         |  null
 *
 *  conditional-expression  ->  logical-expression conditional-expression'
 *  conditional-expression' ->  "?" logical-expression conditional-expression'
                            ":" logical-expression conditional-expression'
                         |  null
 *  logical-expression		->  relational-expression logical-expression'
 *  logical-expression'		->  LOP relational-expression logical-expression'
                         |  null
 *  relational-expression   ->  arithmetic-expression relational-expression'
 *  relational-expression'  ->  COP arithmetic-expression relational-expression'
                         |  null
 *  arithmetic-expression   ->  term arithmetic-expression'
 *  arithmetic-expression'  ->  TOP term arithmetic-expression'
                         |  null
 *  term					->  factor term'
 *  term'					->  FOP factor term'
                         |  null
 *  factor					->  primary "^" factor
                         |  primary
 *  primary					->  MOP primary
                         |  element "[" argument-list "]"
                         |  element
 *  element					->  LITERAL
                         |  "(" conditional-expression ")"
                         |  "{" argument-list "}"
                         |  call
 *  call					->  IDENTIFIER
                         |  IDENTIFIER "(" ")"
                         |  IDENTIFIER "(" argument-list ")"
 *  argument-list			->  conditional-expression
                         |  conditional-expression "," argument-list
 *
 *  LOP  ->  "&&" | "||"
 *  COP  ->  ">" | "<" | "==" ">=" | "<=" | "!="
 *  TOP  ->  "+" | "-" | "&" | "|" | "$"
 *  FOP  ->  "*" | "/" | "%" | "<<" | ">>"
 *  MOP  ->  "-" | "!" | "~"
 *
 *  Actually, there are two grammars:
 *    the definition and (its tail) the conditional expression
 *
 *  parseDefiniton() looks for a definition
 *  parseExpression() looks for a conditional expression
 */
class Parser {
    private int     _token;
    private Scanner _scanner;
    private Map<String,Symbol>     _symbols;
    
    Parser( ) {
        _symbols = new HashMap<String,Symbol>();
        lookup("NaN")     .setExpression(new Constant(Double.NaN));
        lookup("Infinity").setExpression(new Constant(Double.POSITIVE_INFINITY));
        lookup("pi")      .setExpression(new Constant(  Math.PI));
        lookup("e")       .setExpression(new Constant(  Math.E));
        lookup("true")    .setExpression(new Constant(true ));
        lookup("false")   .setExpression(new Constant(false));
    }
    
    public Map<String,Symbol> getSymbols( ) {
        return _symbols;
    }
    
    public Evaluable parseExpression( Scanner scanner )
        throws Exception {
        _scanner = scanner;
        advance();
        if ( _token == Scanner.END ) {
            return null;
        } else {
            Evaluable expr = getConditionalExpression();
            if ( _token == Scanner.EQUALS ) {
                while ( _token != Scanner.END ) {
                    advance();
                }
                return null;
            } else if ( _token == Scanner.END ) {
                return expr;
            } else {
                error();
                return null;
            }
        }
    }
    
    //  definition  ->  def "="  conditional-expression  |  null
    public void parseDefinition( Scanner scanner )
        throws Exception {
        _scanner = scanner;
        advance();
        if ( _token == Scanner.END ) {
            return;
        } else {
            Evaluable def = getConditionalExpression();
            if ( _token == Scanner.EQUALS ) {
                advance();
                if ( !(def instanceof Application) ) {
                    error();
                }
                Application appl = (Application)def;
                Symbol function = appl.getSymbol();
                List<Symbol> parameters = new LinkedList<Symbol>();
                for ( Object arg : appl.getArguments() ) {
                    if ( !(arg instanceof Application) ) {
                        error();
                    }
                    Application parm = (Application)arg;
                    parameters.add(parm.getSymbol());
                }
                function.setParameters(parameters);
                Evaluable expr = getConditionalExpression();
                if ( expr == null || _token != Scanner.END ) {
                    error();
                }
                function.setExpression(expr);
            } else {
                while ( _token != Scanner.END ) {
                    advance();
                }
            }
        }
    }
        
    //  condition  ->  logical-expression condition-prime
    private Evaluable getConditionalExpression( )
        throws Exception {
        Evaluable logical = getLogicalExpression();
        return getConditionalExpressionPrime(logical);
    }
    
    //  condition-prime  ->
    //      '?' logical-expression condition-prime ':' logical-expression condition-prime
    //    | null
    private Evaluable getConditionalExpressionPrime( Evaluable left )
        throws Exception {
        if ( _token == Scanner.QUESTION_MARK ) {
            advance();
            Evaluable t = getLogicalExpression();
            t = getConditionalExpressionPrime(t);
            if ( _token == Scanner.COLON ) {
                advance();
                Evaluable f = getLogicalExpression();
                f = new Conditional(left,t,f);
                return getConditionalExpressionPrime(f);
            } else {
                error();
                return null;
            }
        } else {
            return left;
        }
    }
    
    //  logical-expression  ->  relation logical-expression-prime
    private Evaluable getLogicalExpression( )
        throws Exception {
        Evaluable term = getRelationalExpression();
        return getLogicalExpressionPrime(term);
    }
    
    //  logical-expression-prime  ->  OP& relation logical-expression-prime  |  null
    private Evaluable getLogicalExpressionPrime( Evaluable left )
        throws Exception {
        switch ( _token ) {
            case Scanner.DOUBLE_AMPERSAND:
                advance();
                left = new And(left,getRelationalExpression());
                return getLogicalExpressionPrime(left);
            case Scanner.DOUBLE_BAR:
                advance();
                left = new Or(left,getRelationalExpression());
                return getLogicalExpressionPrime(left);
            default:
                return left;
        }
    }
    
    //  relation  ->  expression relation-prime
    public Evaluable getRelationalExpression( )
        throws Exception {
        Evaluable expr = getExpression();
        return getRelationalExpressionPrime(expr);
    }
    
    //  relation-prime  ->  OP< expression relation-prime  |  null
    public Evaluable getRelationalExpressionPrime( Evaluable left )
        throws Exception {
        switch ( _token ) {
            case Scanner.DOUBLE_EQUALS:
                advance();
                left = new EqualTo(left,getExpression());
                return getRelationalExpressionPrime(left);
            case Scanner.EXCLAMATION_EQUALS:
                advance();
                left = new NotEqualTo(left,getExpression());
                return getRelationalExpressionPrime(left);
            case Scanner.GREATER_THAN:
                advance();
                left = new GreaterThan(left,getExpression());
                return getRelationalExpressionPrime(left);
            case Scanner.LESS_THAN:
                advance();
                left = new LessThan(left,getExpression());
                return getRelationalExpressionPrime(left);
            case Scanner.LESS_THAN_EQUALS:
                advance();
                left = new LessThanOrEqualTo(left,getExpression());
                return getRelationalExpressionPrime(left);
            case Scanner.GREATER_THAN_EQUALS:
                advance();
                left = new GreaterThanOrEqualTo(left,getExpression());
                return getRelationalExpressionPrime(left);
            default:
                return left;
        }
    }
    
    //  expression  ->  term expression-prime
    public Evaluable getExpression( )
        throws Exception {
        Evaluable term = getTerm();
        return getExpressionPrime(term);
    }

    //  expression-prime  ->  TOP term expression-prime  |  null
    private Evaluable getExpressionPrime( Evaluable left )
        throws Exception {
        switch ( _token ) {
            case Scanner.PLUS:
                advance();
                left = new Addition(left,getTerm());
                return getExpressionPrime(left);
            case Scanner.MINUS:
                advance();
                left = new Subtraction(left,getTerm());
                return getExpressionPrime(left);
            case Scanner.AMPERSAND:
                advance();
                left = new BitAnd(left,getTerm());
                return getExpressionPrime(left);
            case Scanner.BAR:
                advance();
                left = new BitOr(left,getTerm());
                return getExpressionPrime(left);
            case Scanner.DOLLARS:
                advance();
                left = new BitXor(left,getTerm());
                return getExpressionPrime(left);
            default:
                return left;
        }
    }

    //  term  ->  factor term-prime
    private Evaluable getTerm( )
        throws Exception {
        Evaluable factor = getFactor();
        return getTermPrime(factor);
    }

    //  term-prime  ->  FOP factor term-prime  |  null
    private Evaluable getTermPrime( Evaluable left )
        throws Exception {
        switch ( _token ) {
            case Scanner.ASTERISK:
                advance();
                left = new Multiplication(left,getFactor());
                return getTermPrime(left);
            case Scanner.SLASH:
                advance();
                left = new Division(left,getFactor());
                return getTermPrime(left);
            case Scanner.PERCENT:
                advance();
                left = new Modulus(left,getFactor());
                return getTermPrime(left);
            case Scanner.DOUBLE_LESS_THAN:
                advance();
                left = new LeftShift(left,getFactor());
                return getTermPrime(left);
            case Scanner.DOUBLE_GREATER_THAN:
                advance();
                left = new RightShift(left,getFactor());
                return getTermPrime(left);
            default:
                return left;
        }
    }

    //  factor  ->  primary ^ factor  |  primary
    private Evaluable getFactor( )
        throws Exception {
        Evaluable primary = getPrimary();
        if ( _token == Scanner.CARET ) {
            advance();
            return new Exponentiation(primary,getFactor());
        } else {
            return primary;
        }
    }

    /**
     *  primary  ->  MOP primary
     *			  |  element "[" argument-list "]"
     *			  |  element
     */
    private Evaluable getPrimary( )
        throws Exception {
        switch ( _token ) {
            case Scanner.MINUS:
                advance();
                return new Negation(getPrimary());
            case Scanner.EXCLAMATION_POINT:
                advance();
                return new Not(getPrimary());
            case Scanner.TILDE:
                advance();
                return new Complement(getPrimary());
            default:
                Evaluable element = getElement();
                if ( _token == Scanner.LEFT_BRACKET ) {
                    advance();
                    List<Evaluable> indices = getArguments();
                    if ( _token == Scanner.RIGHT_BRACKET ) {
                        advance();
                        for ( int i = indices.size()-1; i >= 0; --i ) {
                            Evaluable index = indices.get(i);
                            element = new Index(element,index);
                        }
                    } else {
                        error();
                    }
                }
                return element;
        }
    }
    
    /**
     *  element  ->  literal
     *			  |  ( conditional-expression )
     *			  |  { } | { argument-list }
     *			  |  call
     */
    private Evaluable getElement()
        throws Exception {
        if ( _token == Scanner.LITERAL ) {
            Object value = _scanner.getValue();
            Evaluable constant = new Constant(value);
            advance();
            return constant;
        } else if ( _token == Scanner.LEFT_PARENTHESIS ) {
            advance();
            Evaluable expr = getConditionalExpression();
            if ( _token == Scanner.RIGHT_PARENTHESIS ) {
                advance();
            } else {
                error();
            }
            return expr;
        } else if ( _token == Scanner.LEFT_BRACE ) {
            advance();
            List<Evaluable> elements = new LinkedList<Evaluable>();
            if ( _token == Scanner.RIGHT_BRACE ) {
                advance();
            } else {
                elements = getArguments();
                if ( _token == Scanner.RIGHT_BRACE ) {
                    advance();
                } else {
                    error();
                }
            }
            return new Array(elements);
        } else {
            return getCall();
        }
    }
    
    /**
     *  call  ->  identifier  |  identifier ( )  |  identifier ( argument-list )
     *			|  e
     */
    private Application getCall()
        throws Exception {
        if ( _token == Scanner.IDENTIFIER ) {
            List<Evaluable> arguments = new LinkedList<Evaluable>();
            String name = _scanner.getString();
            advance();
            Application call = new Application(lookup(name));
            if ( _token == Scanner.LEFT_PARENTHESIS ) {
                advance();
                if ( _token == Scanner.RIGHT_PARENTHESIS ) {
                    advance();
                } else {
                    arguments = getArguments();
                    if ( _token == Scanner.RIGHT_PARENTHESIS ) {
                        advance();
                    } else {
                        error();
                        return null;
                    }
                }
            }
            call.setArguments(arguments);
            return call;
        } else {
            error();
            return null;
        }
    }
    
    //  argument-list  ->
    //    conditional-expression  |  conditional-expression , argument-list
    private LinkedList<Evaluable> getArguments( )
        throws Exception {
        LinkedList<Evaluable> arguments;
        Evaluable expr = getConditionalExpression();
        if ( _token == Scanner.COMMA ) {
            advance();
            arguments = getArguments();
        } else {
            arguments = new LinkedList<Evaluable>();
        }
        arguments.add(expr);
        return arguments;
    }

    private void advance() {
        _token = _scanner.getNextToken();
    }
    
    private void error( )
        throws Exception {
        throw new Exception("syntax error");
    }

    private Symbol lookup( String name ) {
        Symbol symbol;
        if ( _symbols.containsKey(name) ) {
            symbol = (Symbol)_symbols.get(name);
        } else {
            try {
                symbol = (Symbol)Class.forName("fnx."+name).newInstance();
            } catch (Exception e) {
                symbol = new Symbol(name);
            }
            _symbols.put(name,symbol);
        }
        return symbol;
    }
    
    private static String[] _test = {
        "234",
        "1-2-3-4",
        "2 + 3 + 4",
        "2 - 3 - 4 ",
        "2*3*4",
        "2/3/4",
        "2^3^4",
        "2*3+4",
        "2+3*4",
        "(2+3)*4",
        "2*3^4",
        "a = 2^3*4",
        "a",
        "234-a",
        "-2-3-4",
        "2^-3+4",
        "2*-3+4",
        "234.",
        "2.34",
        ".234",
        "2e34",
        "23.e4",
        "2.3E4",
        "2.3e-4",
        ".23e+4",
        "",
        "1/0",
        "-1/0",
        "0/0",
        "NaN",
        "-Infinity",
        "pi",
        "e",
        "sqrt(4)",
        "sin(pi/2)",
        "cos(pi)",
        "ln(e)",
        "atan(1)*180/pi",
        "true",
        "false",
        "length(x,y) = sqrt(x^2+y^2)",
        "length(3,4)"
    };

    public static void main( String[] arg ) {
        Parser parser = new Parser();
        try {
            for ( int i = 0; i < _test.length; ++i ) {
                StringBuffer buffer = new StringBuffer(_test[i]);
                Scanner scanner = new Scanner(buffer,0);
                parser.parseDefinition(scanner);
            }
            for ( int i = 0; i < _test.length; ++i ) {
                StringBuffer buffer = new StringBuffer(_test[i]);
                Scanner scanner = new Scanner(buffer,0);
                Evaluable expr = parser.parseExpression(scanner);
                if ( expr != null ) {
                    scanner.backin(expr.evaluate().toString());
                }
                System.out.println(expr+" <- "+buffer);
            }
        } catch ( Exception e ) {
            System.out.println(e.getMessage());
        }
    }
        
 }

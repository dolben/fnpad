/**
 *  fnPad: a functional programming style calculator
 *  Copyright (c) 2001-2010 Hank Dolben, org.dolben.fn.Pad.LICENSE
 */
package org.dolben.fn;

class Scanner {
	// tokens
	static final int ERROR					= -1;
	static final int END					=  0;
	static final int PLUS					=  1;
	static final int MINUS					=  2;
	static final int ASTERISK				=  3;
	static final int SLASH					=  4;
	static final int CARET					=  5;
	static final int IDENTIFIER				=  6;
	static final int LITERAL				=  7;
	static final int LEFT_PARENTHESIS		=  8;
	static final int RIGHT_PARENTHESIS		=  9;
	static final int COLON					= 10;
	static final int WAVY_EQUALS			= 11;  // Å
	static final int EQUALS					= 12;
	static final int PERCENT				= 13;
	static final int QUESTION_MARK			= 14;
	static final int COMMA					= 15;
	static final int GREATER_THAN			= 16;
	static final int DOUBLE_EQUALS			= 17;  // ==
	static final int DOUBLE_AMPERSAND		= 18;  // &&
	static final int DOUBLE_BAR				= 19;  // ||
	static final int EXCLAMATION_POINT		= 20;
	static final int LESS_THAN				= 21;
	static final int LESS_THAN_EQUALS		= 22;
	static final int GREATER_THAN_EQUALS	= 23;
	static final int EXCLAMATION_EQUALS		= 24;  // !=
	static final int LEFT_BRACE				= 25;
	static final int RIGHT_BRACE			= 26;
	static final int LEFT_BRACKET			= 27;
	static final int RIGHT_BRACKET			= 28;
	static final int AMPERSAND				= 29;  // &
	static final int BAR					= 30;  // |
	static final int DOLLARS				= 31;  // $
	static final int TILDE                  = 32;  // ~
	static final int DOUBLE_LESS_THAN		= 33;  // <<
	static final int DOUBLE_GREATER_THAN	= 34;  // >>
	
	static final int INTEGER				=  0;
	static final int REAL					=  1;
	static final int STRING					=  2;
	static final int HEXADECIMAL            =  3;
	static final int OCTAL                  =  4;
	
	private StringBuffer _s;  // the string being scanned
	private int _next;        // index of the next or current character scanned
	private int _last;        // scanner position before last token
	private int _first;       // index of the first character in a LITERAL or ID
	private int _token;       // the token to return
	private int _literal_type;// the type of a literal
	private int _cursor;      // the position of the cursor in the text buffer
	
	Scanner( StringBuffer s, int cursor ) {
		_s = s;
		_cursor = cursor;
		reset();
	}
	
	/**
	 *  @return the buffer
	 */
	public StringBuffer getBuffer( ) {
		return _s;
	}
	
	/**
	 *  sets the scanner to start over at the beginning of the buffer
	 */
	public void reset( ) {
		_next = 0;
		_last = 0;
	}
	
	/**
	 *  gets the adjusted cursor position
	 *
	 *  @return the cursor position
	 */
	public int getCursor( ) {
		return _cursor;
	}
	
	/**
	 *  sets the cursor position to the scan point
	 */
	public void setCursor( ) {
		_cursor = _next;
	}
	
	/**
	 *  backs the result into the text buffer
	 */
	public void backin( String s ) {
		s = " \u2248 "+s;
		_s.insert(_last,s);
		if ( _cursor >= _last ) {
			_cursor += s.length();
		}
		_next += s.length();
	}
	
	/**
	 *  deletes the results from the text buffer
	 *  adjusting the cursor position if necessary
	 */
	public void backout( ) {
		while ( more() ) {
			int last = _next;
			if ( getNextToken() == WAVY_EQUALS ) {
				int next = _next;
				while ( getNextToken() != END ) {
					next = _next;
				}
				_s.delete(last,next);
				if ( _cursor > last ) {
					if ( _cursor < next ) {
						_cursor = last;
					} else {
						_cursor -= next-last;
					}
				}
				_next = last;
			}
		}
		reset();
	}
	
	/**
	 *  @return true iff there are more characters to be scanned in the buffer
	 */
	public boolean more( ) {
		return _next < _s.length();
	}
	
	// states
	private static final int START		=  0;
	private static final int FINISH		=  1;
	private static final int DIGITS		=  2;
	private static final int FRACTION   =  3;
	private static final int ESIGN		=  4;
	private static final int EXPONENT   =  5;
	private static final int LETTERS	=  6;
	private static final int SLASHER	=  7;
	private static final int COMMENT	=  8;
	private static final int EQUAL		=  9;
	private static final int AMPERSANDS = 10;
	private static final int BARS		= 11;
	private static final int LESS		= 12;
	private static final int GREATER	= 13;
	private static final int EXCLAMATION= 14;
	private static final int LETTER 	= 15;
	private static final int QUOTED 	= 16;
	private static final int ZERO       = 17;
	private static final int HEXADIGITS = 18;
	
	// called repeatedly
	public int getNextToken( ) {
		_token = END;
		_last = _next;
		int state = START;
		while ( more() ) {
			char c = _s.charAt(_next);
			switch ( state ) {
				case START:			state = start(c);		break;
				case DIGITS:		state = digits(c);		break;
				case ZERO:          state = zero(c);        break;
				case HEXADIGITS:    state = hexadigits(c);  break;
				case FRACTION:		state = fraction(c);	break;
				case ESIGN:			state = esign(c);		break;
				case EXPONENT:		state = exponent(c);	break;
				case LETTERS:		state = letters(c);		break;
				case LETTER:		state = letter(c);		break;
				case SLASHER:		state = slasher(c);		break;
				case COMMENT:		state = comment(c);		break;
				case EQUAL:			state = equal(c);		break;
				case GREATER:		state = greater(c);		break;
				case LESS:			state = less(c);		break;
				case EXCLAMATION:   state = exclamation(c); break;
				case AMPERSANDS:	state = ampersands(c);  break;
				case BARS:			state = bars(c);		break;
				case QUOTED:		state = quoted(c);		break;
				case FINISH:		return _token;  //		break;
			}
			_next++;
		}
		return _token;
	}
	
	private int start( char c ) {
		_first = _next;
		switch ( c ) {
			case '+':   _token = PLUS;				return FINISH;
			case '-':   _token = MINUS;				return FINISH;
			case '*':   _token = ASTERISK;			return FINISH;
			case '^':   _token = CARET;				return FINISH;
			case '(':   _token = LEFT_PARENTHESIS;  return FINISH;
			case ')':   _token = RIGHT_PARENTHESIS; return FINISH;
			case '{':   _token = LEFT_BRACE;		return FINISH;
			case '}':   _token = RIGHT_BRACE;		return FINISH;
			case '[':   _token = LEFT_BRACKET;		return FINISH;
			case ']':   _token = RIGHT_BRACKET;		return FINISH;
			case ',':   _token = COMMA;				return FINISH;
			case ';':   _token = END;				return FINISH;
			case '?':   _token = QUESTION_MARK;		return FINISH;
			case '%':   _token = PERCENT;			return FINISH;
			case '$':   _token = DOLLARS;			return FINISH;
			case '~':   _token = TILDE;             return FINISH;
			case '\n':  _token = END;				return FINISH;
			case '\r':  _token = END;				return FINISH;
			case '!':   _token = EXCLAMATION_POINT; return EXCLAMATION;
			case '>':   _token = GREATER_THAN;		return GREATER;
			case '<':   _token = LESS_THAN;			return LESS;
			case '\u2248':
						_token = WAVY_EQUALS;		return FINISH;
			case '=':   _token = EQUALS;			return EQUAL;
			case '&':   _token = AMPERSAND;			return AMPERSANDS;
			case '|':   _token = BAR;				return BARS;
			case '/':   _token = SLASH;				return SLASHER;
			case ':':   _token = COLON;				return FINISH;
			case '.':   _token = LITERAL;			return FRACTION;
			case '"':   _token = LITERAL;			return QUOTED;
			case '0':   _token = LITERAL;
						_literal_type = INTEGER;    return ZERO;
			default:
				if ( Character.isDigit(c) ) {
					_token = LITERAL;
					_literal_type = INTEGER;
					return DIGITS;
				} else if ( Character.isLetter(c) || c == '_' ) {
					_token = IDENTIFIER;
					return LETTERS;
				} else if ( Character.isWhitespace(c) ) {
					return START;
				} else {
					_token = ERROR;
					return FINISH;
				}
		}
	}
	
	private int zero( char c ) {
		if ( Character.isDigit(c) ) {
			_literal_type = OCTAL;
			return DIGITS;
		} else if ( c == 'x' ) {
			_literal_type = HEXADECIMAL;
			return HEXADIGITS;
		} else if ( c == '.' ) {
			_literal_type = REAL;
			return FRACTION;
		} else {
			_next--;
			return FINISH;
		}
	}
	
	private int hexadigits( char c ) {
		if ( isHexadigit(c) ) {
			return HEXADIGITS;
		} else {
			_next--;
			return FINISH;
		}
	}
	
	private int digits( char c ) {
		if ( Character.isDigit(c) ) {
			return DIGITS;
		} else if ( c == '.' ) {
			_literal_type = REAL;
			return FRACTION;
		} else if ( c == 'e' || c == 'E' ) {
			_literal_type = REAL;
			return ESIGN;
		} else {
			_next--;
			return FINISH;
		}
	}
	
	private int fraction( char c ) {
		if ( Character.isDigit(c) ) {
			return FRACTION;
		} else if ( c == 'e' || c == 'E' ) {
			return ESIGN;
		} else {
			_next--;
			return FINISH;
		}
	}
	
	private int esign( char c ) {
		if ( c == '+' || c == '-' || Character.isDigit(c) ) {
			return EXPONENT;
		} else {
			return FINISH;
		}
	}
	
	private int exponent( char c ) {
		if ( Character.isDigit(c) ) {
			return EXPONENT;
		} else {
			_next--;
			return FINISH;
		}
	}
	
	private int equal( char c ) {
		if ( c == '=' ) {
			_token = DOUBLE_EQUALS;
			return FINISH;
		} else {
			_next--;
			return FINISH;
		}
	}
	
	private int exclamation( char c ) {
		if ( c == '=' ) {
			_token =	EXCLAMATION_EQUALS;
			return FINISH;
		} else {
			_next--;
			return FINISH;
		}
	}
	
	private int greater( char c ) {
		if ( c == '=' ) {
			_token = GREATER_THAN_EQUALS;
			return FINISH;
		} else if ( c == '>' ) {
			_token = DOUBLE_GREATER_THAN;
			return FINISH;
		} else {
			_next--;
			return FINISH;
		}
	}
	
	private int less( char c ) {
		if ( c == '=' ) {
			_token = LESS_THAN_EQUALS;
			return FINISH;
		} else if ( c == '<' ) {
			_token = DOUBLE_LESS_THAN;
			return FINISH;
		} else {
			_next--;
			return FINISH;
		}
	}
	
	private int ampersands( char c ) {
		if ( c == '&' ) {
			_token = DOUBLE_AMPERSAND;
			return FINISH;
		} else {
			_next--;
			return FINISH;
		}
	}
	
	private int bars( char c ) {
		if ( c == '|' ) {
			_token = DOUBLE_BAR;
			return FINISH;
		} else {
			_next--;
			return FINISH;
		}
	}
	
	private int slasher( char c ) {
		if ( c == '/' ) {
			_token = END;
			return COMMENT;
		} else {
			_next--;
			return FINISH;
		}
	}
	
	private int quoted( char c ) {
		if ( c == '"' ) {
			_literal_type = STRING;
			return FINISH;
		} else {
			return QUOTED;
		}
	}
	
	private int comment( char c ) {
		if ( c == '\r' || c == '\n' ) {
			return FINISH;
		} else {
			return COMMENT;
		}
	}
	
	private int letters( char c ) {
		if ( Character.isLetterOrDigit(c) || c == '_' ) {
			return LETTERS;
		} else if ( c == '.' ) {
			_token = ERROR;
			return LETTER;
		} else {
			_next--;
			return FINISH;
		}
	}
	
	private int letter( char c ) {
		if ( Character.isLetter(c) || c == '_' ) {
			_token = IDENTIFIER;
			return LETTERS;
		} else {
			_next--;
			return FINISH;
		}
	}
	
	public Object getValue() {
		switch ( _literal_type ) {
			case INTEGER:
				return new Long(getString());
			case OCTAL:
				return new Long(Long.parseLong(_s.substring(_first+1,_next),8));
			case HEXADECIMAL:
				return new Long(Long.parseLong(_s.substring(_first+2,_next),16));
			case REAL:
				return new Double(getString());
			case STRING:
				return _s.substring(_first+1,_next-1);
			default:
				return null;
		}
	}
	
	public String getString() {
		return _s.substring(_first,_next);
	}
	
	private boolean isHexadigit( char c ) {
		return Character.isDigit(c) ||
			('a' <= c && c <= 'f') || ('A' <= c && c <= 'F');
	}

}

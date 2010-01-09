/**
 *  fnPad: a functional programming style calculator
 *  Copyright (c) 2001-2010 Hank Dolben, org.dolben.fn.LICENSE
 */
package org.dolben.fn;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Shape;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.dolben.dogs.Scale;

/**
 *  Graph implements the graphing of functions - "graph(x,f(x))" produces a
 *  a graph of f(x) over the range of x from graph.x.min to graph.x.max.
 *  Multiple graphs and the drawing of axes are supported.
 *  </p><p>
 *  There is only one Graph per window. The multiple graph()s are implemented
 *  by a list of Graf (internal class) objects.
 *  </p><p>
 *  Drawing takes place when the GUI demands it through drawAll().
 *  evaluate() saves the arguments for application during auto-ranging and drawing.
 */
class Graph extends Symbol {
	
	// space in pixels between the edge of the graph panel and the content drawn
	private final static int TOP_MARGIN		= 12;
	private final static int BOTTOM_MARGIN  = 16;
	private final static int LEFT_MARGIN	= 56;
	private final static int RIGHT_MARGIN   = 24;
	
	// the colors of the graphs, used in the order in the array
	private final static Color _colors[] = {
		Color.black,
		Color.red,
		Color.green,
		Color.blue,
		Color.magenta,
		Color.orange
	};
	
	private List<GrafExpression>	_grafs;	// the Grafs, one for each application of graph()
	private GrafExpression
					    _graphing; // evaluating while graphing
	private Graphics _graphics;          // graphics context during drawAll()
	private Map<String,Symbol> _symbols; // a reference to the Parser's symbol table
	private Scale    _xs;		         // the scale of x, between pixels and values
	private Scale    _ys;		// the scale of y, between pixels and values
	private Axis	 _xa;		// the x axis
	private Axis	 _ya;		// the y axis
	private double   _rangeMin; // the minimum value of y for all Grafs
	private double   _rangeMax; // the maximum value of y for all Grafs
	
	/**
	 *  makes a Graph, should be (at most) one per window
	 */
	Graph( ) {
		super("graph");
		_grafs = new LinkedList<GrafExpression>();
	}
	
	/**
	 *  resets the Graph for a fresh evaluation of the parse tree
	 */
	public void reset( Map<String,Symbol> symbols ) {
		_grafs = new LinkedList<GrafExpression>();
		_graphing = null;
		_symbols = symbols;
		_symbols.put(getName(),this);
		_xs = null;
		_ys = null;
		_rangeMin = Double.MAX_VALUE;
		_rangeMax = Double.MIN_VALUE;
	}
	
	/**
	 *  adds an expression that results in a Graph
	 */
	public void add( Evaluable expr ) {
		if ( _grafs.size() > 0 ) {
			GrafExpression grafex = _grafs.get(0);
			if ( grafex.getExpression() == expr ) {
				return;
			}
		}
		Color color = _colors[_grafs.size()%_colors.length];
		_grafs.add(new GrafExpression(expr,color));
		return;
	}
	
	/**
	 *  overrides Symbol's evaluate() to capture the argument list for
	 *  each application of graph(), and set the scales and auto-ranging
	 *
	 *  @param arguments the list of acutal arguments for this application
	 *                   (x,f(x))
	 *
	 *  @return this Graph
	 *
	 *  @throws Exception when evaluation of function fails
	 */
	public Object evaluate( List<Evaluable> arguments ) throws Exception {
		if ( arguments.size() != 2 ) {
			throw new Exception(
				"wrong number of arguments to 'graph', not equal to 2"
			);
		}
		Object arg = arguments.get(1);
		if ( !(arg instanceof Application) ) {
			throw new Exception("'"+arg+"' is not a symbol");
		}
		Application appl = (Application)arg;
		Evaluable expr = (Evaluable)arguments.get(0);
		Graf graf = new Graf(appl.getSymbol(),expr);
		if ( _graphing == null) {
			if ( _xs == null ) {
				double min = getSymbolDouble("x.min",-1);
				double max = getSymbolDouble("x.max", 1);
				_xs = new Scale(min,max);
				_xa = new Axis(min,max);
			}
			graf.autoRange();
			double min = getSymbolDouble("y.min",_rangeMin);
			double max = getSymbolDouble("y.max",_rangeMax);
			_ys = new Scale(min,max);
			_ya = new Axis(min,max);
		} else {
			graf.draw(_graphics);
		}
		return this;
	}
	
	/**
	 *  gets the value of a give "graph" Symbol, graph.&lt;suffix&gt;,
	 *  or returns the given default if the Symbol is undefined
	 *
	 *  @param name the suffix of the Symbol name
	 *  @param d    the default value
	 *
	 *  @return the value of the Symbol or d
	 *
	 *  @throws Exception when the Symbol is undefined or the value is not a Number
	 */
	private double getSymbolDouble( String suffix, double d ) throws Exception {
		String name = "graph."+suffix;
		Symbol s = (Symbol)_symbols.get(name);
		if ( s == null ) {
			return d;
		}
		Object result = s.evaluate(new LinkedList<Evaluable>());
		if ( !(result instanceof Number) ) {
			throw new Exception("'"+name+"' is not a number");
		}
		return ((Number)result).doubleValue();
	}
	
	/**
	 *  draws the graphics including the graph of each graph()'s function,
	 *  axes, and border
	 */
	public void drawAll( Graphics g, int width, int height ) {
		if ( _grafs.size() != 0 ) {
			_graphics = g;
			Shape clip = null;
			try {
				_xs.setDomain(LEFT_MARGIN,width-RIGHT_MARGIN);
				_ys.setDomain(height-BOTTOM_MARGIN,TOP_MARGIN);
				drawGrid(g);
				g.setColor(Color.darkGray);
				int xsize = width-LEFT_MARGIN-RIGHT_MARGIN;
				int ysize = height-TOP_MARGIN-BOTTOM_MARGIN;
				g.drawRect(LEFT_MARGIN,TOP_MARGIN,xsize,ysize);
				clip = g.getClip();
				g.clipRect(LEFT_MARGIN,TOP_MARGIN,xsize+1,ysize+1);
				for ( GrafExpression grafing : _grafs ) {
					_graphing = grafing;
					_graphing.graph(g);
				}
			} catch ( Exception e ) {
				g.drawString(
					e.getMessage(),LEFT_MARGIN+8,height-BOTTOM_MARGIN-8
				);
			}
			if ( clip != null ) {
				g.setClip(clip);
			}
			_graphing = null;
		}
	}
	
	/**
	 *  draws grid lines and labels
	 */
	private void drawGrid( Graphics g ) {
		g.setFont(new Font("Helvetica",Font.PLAIN,10));
		final int TICK = 3;
		
		int x1; int y1 = _ys.getLower()+TICK;
		int x2; int y2 = _ys.getUpper();
		for ( double x = _xa.getFirst(); x <= _xs.getMax(); x += _xa.getSize() ) {
			x1 = _xs.scale(x);
			g.setColor(Color.lightGray);
			g.drawLine(x1,y1,x1,y2);
			g.setColor(Color.black);
			labelX(g,_xa.format(x),x1,y1);
		}
		x1 = _xs.scale(0.0);
		g.setColor(Color.gray);
		g.drawLine(x1,y1,x1,y2);
		
		x1 = _xs.getLower()-TICK;
		x2 = _xs.getUpper();
		for ( double y = _ya.getFirst(); y <= _ys.getMax(); y += _ya.getSize() ) {
			y1 = _ys.scale(y);
			g.setColor(Color.lightGray);
			g.drawLine(x1,y1,x2,y1);
			g.setColor(Color.black);
			labelY(g,_ya.format(y),x1,y1);
		}
		y1 = _ys.scale(0.0);
		g.setColor(Color.gray);
		g.drawLine(x1,y1,x2,y1);
	}
	
	/**
	 *  labels an x-axis tick
	 */
	private void labelX( Graphics g, String s, int x, int y ) {
		FontMetrics fm = g.getFontMetrics();
		g.drawString(s,x-fm.stringWidth(s)/2,y+fm.getAscent());
	}
	
	/**
	 *  labels a y-axis tick
	 */
	private void labelY( Graphics g, String s, int x, int y ) {
		FontMetrics fm = g.getFontMetrics();
		g.drawString(s,x-fm.stringWidth(s),y+fm.getAscent()/2-1);
	}
	
	/**
	 *  represents an axis
	 */
	private class Axis {
		
		private double _size;			// size of a division
		private double _first;			// the first tick
		private DecimalFormat _decimal; // format for axis label
		
		/**
		 *  determines values for one coordinate of a grid
		 */
		Axis( double min, double max ) {
			double range = max-min; // range of the scale
			double log10 = Math.log(10); // ln(10)
			// magnitude of range
			long magnitude = (long)Math.floor(Math.log(range)/log10-1);
			// normalized range 10 - 100
			double norm = range/Math.pow(10,magnitude);
			int normsize = 20; // the normalized division size
			if ( niceDivisions(norm/2) ) {
				normsize =  2;
			} else if ( niceDivisions(norm/5) ) {
				normsize =  5;
			} else if ( niceDivisions(norm/10) ) {
				normsize = 10;
			}
			_size = normsize*Math.pow(10,magnitude);
			_first = Math.ceil(min/_size)*_size;
			pickFormat(min,max);
		}
		
		private double getSize( ) {
			return _size;
		}
		
		private double getFirst( ) {
			return _first;
		}
		
		private String format( double value ) {
			return _decimal.format(value);
		}
		
		/**
		 *  picks a decimal format for a scale
		 */
		private void pickFormat( double min, double max ) {
			double amin = Math.abs(min);
			double amax = Math.abs(max);
			if (
				amin < 1000 && amax < 1000 &&
				(amin >= 0.1 || amax >= 0.1)
				) {
				_decimal = new DecimalFormat("##0.##");
			} else {
				_decimal = new DecimalFormat("0.##E0");
			}
		}
		
		/**
		 *  checks that the number of divisions is "nice"
		 */
		private boolean niceDivisions( double divisions ) {
			return 3 < divisions && divisions < 8;
		}
	
	}
	
	/**
	 *  an expression containing an application of graph(),
	 *  saved from one evaluation until before the next parse,
	 *  so that the graphs can be redrawn as the GUI needs
	 */
	private class GrafExpression {
		
		private Evaluable _expr;  // top level expression
		private Color     _color; // the color of this graph
		
		/**
		 *  makes a Graf with an expression
		 */
		GrafExpression( Evaluable expr, Color color ) {
			_expr = expr;
			_color = color;
		}
		
		/**
		 *  @return the expression
		 */
		private Evaluable getExpression( ) {
			return _expr;
		}
		
		/**
		 *  sets the color for graphing this expression and evalates
		 */
		private void graph( Graphics g ) throws Exception {
			g.setColor(_color);
			_expr.evaluate();
		}
	
	}
	
	/**
	 *  evaluator of graph(x,f(x)) over the range of x
	 */
	private class Graf {
		
		// number of steps-1 of x in auto-ranging
		private final static int _AUTO_STEPS = 100;
		
		private Symbol _x;		// x in graph(x,y)
		private Evaluable _y;   // y in graph(x,y)
		
		/**
		 *  makes a Graf used for auto-ranging
		 */
		Graf( Symbol x, Evaluable y ) {
			_x = x;
			_y = y;
		}
		
		/**
		 *  updates auto-ranging min and max for this Graf
		 */
		private void autoRange( ) throws Exception {
			double deltax = (_xs.getMax()-_xs.getMin())/_AUTO_STEPS;
			for ( int i = 0; i <= _AUTO_STEPS; ++i ) {
				double y = f(_xs.getMin()+i*deltax);
				if ( !Double.isInfinite(y) ) {
					if ( y < _rangeMin ) {
						_rangeMin = y;
					}
					if ( y > _rangeMax ) {
						_rangeMax = y;
					}
				}
			}
			if ( _rangeMin == _rangeMax ) {
				_rangeMin -= 1;
				_rangeMax += 1;
			}
		}
		
		/**
		 *  evaluates y, in graph(x,y), for a given x
		 */
		private double f( double x ) throws Exception {
			_x.setExpression(new Constant(x));
			double value = Double.NaN;
			Object result = _y.evaluate();
			if ( result instanceof Number ) {
				Number n = (Number)result;
				value = n.doubleValue();
			}
			_x.unsetExpression();
			return value;
		}
		
		/**
		 *  draws this Graf
		 */
		private void draw( Graphics g ) throws Exception {
			int xmin = _xs.getLower();
			int xmax = _xs.getUpper();
			int ymin = _ys.getLower();
			int ymax = _ys.getUpper();
			int x1 = xmin;
			int y1 = f(x1);
			for ( int x2 = xmin+1; x2 <= xmax; ++x2 ) {
				int y2 = f(x2);
				if ( 
					!(_ys.isNaN(y1) || _ys.isNaN(y2)) &&
					(y1 <= ymin || y2 <= ymin) && (y1 >= ymax || y2 >= ymax)
				) {
					g.drawLine(x1,y1,x2,y2);
				}
				x1 = x2;
				y1 = y2;
			}
		}
		
		/**
		 *  evaluates the Component coordinate of y, in graph(x,y),
		 *  for a given Component coordinate of x
		 */
		private int f( int x ) throws Exception {
			return _ys.scale(f(_xs.scale(x)));
		}
	
	}

}

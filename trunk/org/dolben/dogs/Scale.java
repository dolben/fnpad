/**
 *  dogs: discrete ordered generic series
 *  Copyright (c) 2001-2010 Hank Dolben, org.dolben.fn.LICENSE
 */
package org.dolben.dogs;

/**
 *  represents a mapping between ints and doubles
 */
public class Scale {
	
	private int    _lower;
	private int    _upper;
	private int    _nan;
	private double _min;
	private double _max;
	private double _lowerLimit;
	private double _upperLimit;
	private double _slope;
	private double _intercept;
	
	public Scale( int lower, int upper, double min, double max ) throws Exception {
		_nan = Integer.MIN_VALUE;
		_lower = lower;
		_upper = upper;
		_min = min;
		_max = max;
		compute();
	}
	
	public Scale( double min, double max ) throws Exception {
		_nan = Integer.MIN_VALUE;
		_lower = Integer.MIN_VALUE+1;
		_upper = Integer.MAX_VALUE;
		_min = min;
		_max = max;
		compute();
	}
	
	private void compute( ) throws Exception {
		if ( _min >= _max ) {
			throw new Exception("Scale min >= max");
		}
		_slope = (_max-_min)/(_upper-_lower);
		_intercept = -_slope*_lower+_min;
		if ( _slope > 0 ) {
			_upperLimit = scale(Integer.MAX_VALUE);
			_lowerLimit = scale(Integer.MIN_VALUE+1);
		} else {
			_upperLimit = scale(Integer.MIN_VALUE+1);
			_lowerLimit = scale(Integer.MAX_VALUE);
		}
	}
	
	public void setDomain( int lower, int upper ) throws Exception {
		_lower = lower;
		_upper = upper;
		compute();
	}
	
	public int getLower( ) {
		return _lower;
	}
	
	public int getUpper( ) {
		return _upper;
	}
	
	public double getMin( ) {
		return _min;
	}
	
	public double getMax( ) {
		return _max;
	}
	
	public boolean isNaN( int i ) {
		return i == _nan;
	}
	
	public int scale( double d ) {
		if ( Double.isNaN(d) || d < _lowerLimit || _upperLimit < d ) {
			return _nan;
		} else {
			return (int)Math.round((d-_intercept)/_slope);
		}
	}
	
	public double scale( int i ) {
		if ( isNaN(i) ) {
			return Double.NaN;
		} else {
			return _slope*i+_intercept;
		}
	}

}

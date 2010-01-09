/**
 *  fnPad: a functional programming style calculator
 *  Copyright (c) 2001-2010 Hank Dolben, org.dolben.fn.Pad.LICENSE
 */
package fnx;

import org.dolben.fn.FormatDouble;
import java.text.DecimalFormat;

/**
 *  a fnPad floating point decimal string Function
 */
public class eng extends org.dolben.fn.Function {
    
    /**
     *  makes a floating point decimal string of its argument
     */
    public eng( ) {
        super(2);
    }
    
    /**
     *  converts the zeroth argument to a floating point decimal string
     *
     *  @return integer as a floating point decimal string
     */
    protected Object evaluate( ) throws Exception {
        long places = getLong(1);
        if ( places > 15 ) {
            places = 15;
        }
        String d = "##0.";
        while ( places-- > 4 ) {
            d += "0";
        }
        return new FormatDouble(getDouble(0),new DecimalFormat(d+"#E0"));
    }

}

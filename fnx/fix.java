/**
 *  fnPad: a functional programming style calculator
 *  Copyright © 2001-2010 Hank Dolben, org.dolben.fn.Pad.LICENSE
 */
package fnx;

import org.dolben.fn.FormatDouble;
import java.text.DecimalFormat;

/**
 *  a fnPad fixed point decimal string Function
 */
public class fix extends org.dolben.fn.Function {
    
    /**
     *  makes a fixed point decimal string of its argument
     */
    public fix( ) {
        super(2);
    }
    
    /**
     *  converts the zeroth argument to a fixed point decimal string
     *
     *  @return integer as a fixed point decimal string
     */
    protected Object evaluate( ) throws Exception {
        long places = getLong(1);
        if ( places > 40 ) {
            places = 40;
        }
        String d = "";
        if ( places > 0 ) {
            d = ".";
            while ( places-- > 0 ) {
                d += "0";
            }
        }
        return new FormatDouble(getDouble(0),new DecimalFormat("##0"+d));
    }

}

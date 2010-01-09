/**
 *  fnPad: a functional programming style calculator
 *  Copyright (c) 2001-2010 Hank Dolben, org.dolben.fn.Pad.LICENSE
 */
package org.dolben.fn;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;

class GraphPanel extends JPanel {

    static final long serialVersionUID = 2;
    private Graph _graph;
    
    GraphPanel( Graph graph ) {
        _graph = graph;
        setBorder(
            BorderFactory.createBevelBorder(
                BevelBorder.LOWERED,Color.white,Color.gray
            )
        );
    }
    
    public void printAll( Graphics g ) {
        Rectangle bounds = g.getClipBounds();
        _graph.drawAll(g,bounds.width,bounds.height);
    }
    
    protected void paintComponent( Graphics g ) {
        int height = getHeight();
        int width = getWidth();
        g.setColor(Color.white);
        g.fillRect(0,0,width,height);
        _graph.drawAll(g,width,height);
    }

}

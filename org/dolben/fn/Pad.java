/**
 *  fnPad: a functional programming style calculator
 *  Copyright © 2001-2010 Hank Dolben, org.dolben.fn.Pad.LICENSE
 */
package org.dolben.fn;

import java.io.*;
import java.util.*;
import java.text.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.*;

public class Pad extends javax.swing.JFrame {
    public final static long    serialVersionUID = 1;
    private final static String VERSION   = "0.32";
    private final static int    THIS_YEAR = 2010;
    // The MIT License, http://www.opensource.org/licenses/mit-license.php
    public final static String  LICENSE =
"Copyright © 2001-"+THIS_YEAR+" Hank Dolben\n"+
"\n"+
"Permission is hereby granted, free of charge, to any person obtaining a copy\n"+
"of this software and associated documentation files (the \"Software\"), to deal\n"+
"in the Software without restriction, including without limitation the rights\n"+
"to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\n"+
"copies of the Software, and to permit persons to whom the Software is\n"+
"furnished to do so, subject to the following conditions:\n"+
"\n"+
"The above copyright notice and this permission notice shall be included in\n"+
"all copies or substantial portions of the Software.\n"+
"\n"+
"THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\n"+
"IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\n"+
"FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\n"+
"AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\n"+
"LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\n"+
"OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN\n"+
"THE SOFTWARE.";
    private final static String about =
        "fnPad version "+VERSION+"\n"+
        "http://www.dolben.org/\n"+
        "\n"+
        LICENSE;
    private final static String CHARSET   = "UTF-8";
    
    private static int news =   0;      // the number of new pads
    private static int x    = 100;      // the position of a constructed pad
    private static int y    =  50;
    private static JFileChooser fileChooser; // the file chooser
    private static List<Pad> pads;      // all the pads
    
    private JMenuItem   editCopyItem;   // "Copy" item of "Edit" menu
    private JMenuItem   editCutItem;    // "Cut" item of "Edit" menu
    private JTextArea   textPane;       // the editable text pane
    private GraphPanel  graphPanel;     // the graph panel
    private JLabel      statusLine;     // the parser status line
    private File        file;           // the text file
    private String      original;       // the original text
    private Graph       graph;          // the Graph - parse tree Symbol

    /**
     *  makes a new Pad
     */
    Pad( ) {
        initComponents();
        ++news;
        original = "";
        setTitle("Untitled_"+news);
    }
  
    /**
     *  makes a new Pad
     */
    Pad( File f ) {
        initComponents();
        readFile(f);
    }
    
    /**
     *  makes a new Pad
     */
    Pad( String content, String title ) {
        initComponents();
        original = content;
        setTitle(title);
        textPane.setText(original);
        textPane.setCaretPosition(0);
    }
    
    /**
     *  reads a text file into the text pane
     */
    private void readFile( File f ) {
        file = f;
        try {
            InputStream input = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(input,CHARSET);
            textPane.read(reader,null);
            original = textPane.getText();
            setTitle(file.getName());
        } catch ( IOException e ) {
            System.out.println(e);
        }
    }
    
    /**
     *  creates all of the components of a Pad window
     *
     *  menu bar
     *    file menu
     *    edit menu
     *    help menu
     *
     *  split pane
     *    graph panel
     *    scrolling text pane
     *      text area
     *      status line
     */
    private void initComponents() {		
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(
            new WindowAdapter() {
                public void windowClosing( WindowEvent evt ) {
                    closeFile();
                }
            }
        );

        JScrollPane textScrollPane = new JScrollPane(
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        textPane = new JTextArea();
        String fontName = textPane.getFont().getFontName();
        fontName = System.getProperty("org.dolben.fn.Pad.fontName",fontName);
        Integer fontSize = new Integer(textPane.getFont().getSize());
        fontSize = Integer.getInteger("org.dolben.fn.Pad.fontSize",fontSize);
        textPane.setFont(
            new java.awt.Font(fontName,Font.PLAIN,fontSize.intValue())
        );
        textScrollPane.setViewportView(textPane);
        
        statusLine = new JLabel(" ");
        
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new java.awt.BorderLayout());
        rightPanel.add(textScrollPane,java.awt.BorderLayout.CENTER);
        rightPanel.add(statusLine,java.awt.BorderLayout.SOUTH);
        rightPanel.setMinimumSize(new Dimension(100,100));
        
        graph = new Graph();
        graphPanel = new GraphPanel(graph);
        graphPanel.setMinimumSize(new Dimension(0,100));
        graphPanel.setPreferredSize(new Dimension(300,400));
        
        JSplitPane splitPane = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,graphPanel,rightPanel
        );
        splitPane.setOneTouchExpandable(true);
        splitPane.setContinuousLayout(true);
        splitPane.setDividerLocation(0);
        getContentPane().add(splitPane,java.awt.BorderLayout.CENTER);

        
        textPane.addCaretListener(
            new CaretListener( ) {
                public void caretUpdate( CaretEvent e ) {
                    caretChange(e);
                }
            }
        );
        
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(initFileMenu());
        menuBar.add(initEditMenu());
        menuBar.add(initHelpMenu());
        setJMenuBar(menuBar);

        setLocation(x,y);
        x = (x+20);
        y = (y+20);
        splitPane.setPreferredSize(new java.awt.Dimension(600,400));
        if ( fileChooser == null ) {
            fileChooser = new JFileChooser();
        }
        pack();
        pads.add(this);
    }
    
    /**
     *  creates the File menu
     *
     *    New (N)
     *    Open... (O)
     *    --------------------
     *    Close (W)
     *    Save (S)
     *    Save As... (shift-S)
     *    --------------------
     *    Print... (P)
     *    --------------------
     *    Close All (shift-W)
     */
    private JMenu initFileMenu( ) {
        JMenu fileMenu = new JMenu();
        fileMenu.setText("File");
        int keyMask = java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
        
        JMenuItem item;
        
        item = new JMenuItem("New");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,keyMask));
        item.addActionListener(
            new ActionListener() {
                public void actionPerformed( ActionEvent evt ) {
                    newFile();
                }
            }
        );
        fileMenu.add(item);
        
        item = new JMenuItem("Open...");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,keyMask));
        item.addActionListener(
            new ActionListener() {
                public void actionPerformed( ActionEvent evt ) {
                    openFile();
                }
            }
        );
        fileMenu.add(item);
        
        fileMenu.add(new JSeparator());
        
        item = new JMenuItem("Close");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,keyMask));
        item.addActionListener(
            new ActionListener() {
                public void actionPerformed( ActionEvent evt ) {
                    closeFile();
                }
            }
        );
        fileMenu.add(item);
        
        item = new JMenuItem("Save");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,keyMask));
        item.addActionListener(
            new ActionListener() {
                public void actionPerformed( ActionEvent evt ) {
                    saveFile();
                }
            }
        );
        fileMenu.add(item);
        
        item = new JMenuItem("Save As...");
        item.setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_S,keyMask+KeyEvent.SHIFT_DOWN_MASK)
        );
        item.addActionListener(
            new ActionListener() {
                public void actionPerformed( ActionEvent evt ) {
                    saveAsFile();
                }
            }
        );
        fileMenu.add(item);
        
        fileMenu.add(new JSeparator());
        
        item = new JMenuItem("Print...");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,keyMask));
        item.addActionListener(
            new ActionListener() {
                public void actionPerformed( ActionEvent evt ) {
                    printFile();
                }
            }
        );
        fileMenu.add(item);
        
        fileMenu.add(new JSeparator());
        
        item = new JMenuItem("Close All");
        item.setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_W,keyMask+KeyEvent.SHIFT_DOWN_MASK)
        );
        item.addActionListener(
            new ActionListener() {
                public void actionPerformed( ActionEvent evt ) {
                    closeAllFile();
                }
            }
        );
        fileMenu.add(item);
        
        return fileMenu;
    }
    
    /**
     *  creates the Edit menu
     *
     *    Cut (X)
     *    Copy (C)
     *    Paste (V)
     *    Delete
     *    Select All (A)
     *    --------------
     *    Calculate (K)
     *    Back Out (B)
     */
    private JMenu initEditMenu( ) {
        JMenu editMenu = new JMenu("Edit");
        int keyMask =
            java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
        
        editCutItem = new JMenuItem("Cut");
        editCutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,keyMask));
        editCutItem.addActionListener(
            getAction(textPane,DefaultEditorKit.cutAction)
        );
        editCutItem.setEnabled(false);
        editMenu.add(editCutItem);
        
        editCopyItem = new JMenuItem("Copy");
        editCopyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,keyMask));
        editCopyItem.addActionListener(
            getAction(textPane,DefaultEditorKit.copyAction)
        );
        editCopyItem.setEnabled(false);
        editMenu.add(editCopyItem);
        
        JMenuItem pasteItem = new JMenuItem("Paste");
        pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,keyMask));
        pasteItem.addActionListener(
            getAction(textPane,DefaultEditorKit.pasteAction)
        );
        editMenu.add(pasteItem);
        
        JMenuItem deleteItem = new JMenuItem("Delete");
        deleteItem.addActionListener(
            getAction(textPane,DefaultEditorKit.deletePrevCharAction)
        );
        editMenu.add(deleteItem);
        
        JMenuItem allItem = new JMenuItem("Select All");
        allItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,keyMask));
        allItem.addActionListener(
            getAction(textPane,DefaultEditorKit.selectAllAction)
        );
        editMenu.add(allItem);
        
        editMenu.add(new JSeparator());
        
        JMenuItem calcItem = new JMenuItem("Calculate");
        calcItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K,keyMask));
        calcItem.addActionListener(
            new ActionListener() {
                public void actionPerformed( ActionEvent evt ) {
                    calculate();
                }
            }
        );
        editMenu.add(calcItem);
        
        JMenuItem backOutItem = new JMenuItem("Back Out");
        backOutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B,keyMask));
        backOutItem.addActionListener(
            new ActionListener() {
                public void actionPerformed( ActionEvent evt ) {
                    backout();
                }
            }
        );
        editMenu.add(backOutItem);
        
        return editMenu;
    }
    
    /**
     *  creates the Help menu
     *
     *    About
     *    ------------
     *    Calculations
     *    Definitions
     *    Conditionals
     *    Graphing
     */
    private JMenu initHelpMenu( ) {
        JMenu helpMenu = new JMenu();
        helpMenu.setText("Help");
        
        JMenuItem helpAboutItem = new JMenuItem("About");
        helpAboutItem.addActionListener(
            new ActionListener( ) {
                public void actionPerformed( ActionEvent evt ) {
                    showAbout(evt);
                }
            }
        );
        helpMenu.add(helpAboutItem);
        
        helpMenu.add(new JSeparator());
        
        JMenuItem example;
        example = new JMenuItem("Calculations");
        example.addActionListener(
            new ActionListener() {
                public void actionPerformed( ActionEvent evt ) {
                    new Pad(CalculationsExample.string,"Calculations").setVisible(true);
                }
            }
        );
        helpMenu.add(example);
        
        example = new JMenuItem("Definitions");
        example.addActionListener(
            new ActionListener() {
                public void actionPerformed( ActionEvent evt ) {
                    new Pad(DefinitionsExample.string,"Definitions").setVisible(true);
                }
            }
        );
        helpMenu.add(example);
        
        example = new JMenuItem("Conditionals");
        example.addActionListener(
            new ActionListener() {
                public void actionPerformed( ActionEvent evt ) {
                    new Pad(ConditionalsExample.string,"Conditionals").setVisible(true);
                }
            }
        );
        helpMenu.add(example);
        
        example = new JMenuItem("Graphing");
        example.addActionListener(
            new ActionListener() {
                public void actionPerformed( ActionEvent evt ) {
                    new Pad(GraphingExample.string,"Graphing").setVisible(true);
                }
            }
        );
        helpMenu.add(example);
                
        return helpMenu;
    }
    
    /**
     *  handles a selection change in the text
     *  by enabling or disabling Edit menu items
     */
    private void caretChange( CaretEvent e ) {
        boolean nonzero = e.getDot() != e.getMark();
        editCutItem.setEnabled(nonzero);
        editCopyItem.setEnabled(nonzero);
    }
    
    /**
     *  gets the named action (if it exists) for a text component
     */
    private Action getAction( JTextComponent textComponent, String name ) {
        Action[] actionsArray = textComponent.getActions();
        for ( int i = 0; i < actionsArray.length; i++ ) {
            Action a = actionsArray[i];
            if ( a.getValue(Action.NAME).equals(name) ) {
                return a;
            }
        }
        return null;
    }    
    
    /**
     *  closes all the files and exits unless the user cancels a close
     */
    private void closeAllFile( ) {
        while ( !pads.isEmpty() ) {
            Pad pad = (Pad)pads.get(0);
            if ( !pad.closeFile() ) {
                return;
            }
            pad.setVisible(false);
        }
        System.exit(0);
    }
    
    /**
     *  gets a file and saves the text pane contents to it
     */
    private void saveAsFile( ) {
        if ( fileChooser.showSaveDialog(textPane) == JFileChooser.APPROVE_OPTION ) {
            file = fileChooser.getSelectedFile();
            saveFile();
            setTitle(file.getName());
        }
    }
    
    /**
     *  saves the text pane contents to the file,
     *  getting a file if there isn't one
     */
    private void saveFile( ) {
        if ( file == null ) {
            saveAsFile();
        } else try {
            OutputStream output = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(output,CHARSET);
            original = textPane.getText();
            writer.write(original);
            writer.close();
        } catch ( IOException e ) {
            System.out.println(e);
        }
    }
    
    /**
     *  makes a new pad
     */
    private static void newFile( ) {
        new Pad().setVisible(true);
    }
    
    /**
     *  gets a file and opens it
     */
    private void openFile( ) {
        if (
            fileChooser.showOpenDialog(textPane) == JFileChooser.APPROVE_OPTION
        ) {
            File f = fileChooser.getSelectedFile();
            for ( Pad p : pads ) {
                if ( f.equals(p.file) ) {
                    p.toFront();
                    return;
                }
            }
            if ( file == null && textPane.getText().length() == 0 ) {
                readFile(f);
            } else {
                new Pad(f).setVisible(true);
            }
        }
    }
    
    /**
     *  prints the graph panel
     */
    private void printFile( ) {
        java.awt.Toolkit kit = java.awt.Toolkit.getDefaultToolkit();
        Properties props = new Properties();
        java.awt.PrintJob job = kit.getPrintJob(this,getTitle(),props);
        if ( job != null ) {
            java.awt.Graphics g = job.getGraphics();
            graphPanel.printAll(g);
            g.dispose();
            job.end();
        }
    }
    
    /**
     *  performs the evaluation of all of the expressions in the text pane
     */
    private void calculate( ) {
        Parser parser = new Parser();
        graph.reset(parser.getSymbols());
        Scanner scanner = backout();
        try {
            statusLine.setText(" ");
            while ( scanner.more() ) {
                parser.parseDefinition(scanner);
            }
            DecimalFormat engineering = new DecimalFormat("##0.#E0");
            DecimalFormat fixed = new DecimalFormat("##0.0###");
            scanner.reset();
            while ( scanner.more() ) {
                Evaluable expr = parser.parseExpression(scanner);
                if ( expr != null ) {
                    Object result = expr.evaluate();
                    if ( result != null ) {
                        String s = results(expr,result,engineering,fixed);
                        if ( s != null ) {
                            scanner.backin(s);
                        }
                    }
                }
            }
        } catch ( Throwable e ) {
            String msg = e.getMessage();
            if ( msg == null ) {
                msg = "probable stack overflow";
            }
            statusLine.setText("ERROR: "+msg);
            scanner.setCursor();
            graph.reset(parser.getSymbols());
        }
        graphPanel.repaint();
        textPane.setText(scanner.getBuffer().toString());
        textPane.setCaretPosition(scanner.getCursor());
    }
    
    /**
     *  returns a string of an expression result
     */
    private String results(
        Evaluable expr, Object result, DecimalFormat engineering, DecimalFormat fixed
    ) throws Exception {
        if ( result instanceof Graph ) {
            graph.add(expr);
            return null;
        }
        if ( result instanceof Double ) {
            Double d = (Double)result;
            if ( d.isNaN() || d.isInfinite() ) {
                return d.toString();
            }
            double a = Math.abs(d.doubleValue());
            if ( a == 0.0 || ( 0.1 < a && a < 1000 ) ) {
                return fixed.format(d);
            }
            return engineering.format(d);
        }
        if ( result instanceof String ) {
            return "\""+result+"\"";
        }
        if ( result instanceof List<?> ) {
            String s = "";
            boolean first = true;
            for ( Object next : (List<?>)result ) {
                if ( first ) {
                    first = false;
                } else {
                    s = ", "+s;
                }
                s = results(expr,next,engineering,fixed)+s;
            }
            s = "{ "+s+" }";
            return s;
        }
        return result.toString();
    }
    
    /**
     *  backs the results out of the text
     */
    private Scanner backout( ) {
        StringBuffer buffer = new StringBuffer(textPane.getText());
        Scanner scanner = new Scanner(buffer,textPane.getCaretPosition());
        scanner.backout();
        textPane.setText(buffer.toString());
        textPane.setCaretPosition(scanner.getCursor());
        return scanner;
    }
    
    /**
     *  closes the pad, first checking for save
     */
    private boolean closeFile( ) {
        if ( !textPane.getText().equals(original) ) {
            int option = JOptionPane.showConfirmDialog(
                this,"Save this modified pad?",getTitle(),
                JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE
            );
            if ( option == JOptionPane.YES_OPTION ) {
                saveFile();
            } else if ( option == JOptionPane.CANCEL_OPTION ) {
                return false;
            }
        }
        pads.remove(this);
        dispose();
        if ( pads.isEmpty() ) {
            System.exit(0);
        }
        return true;
    }
    
    /**
     *  displays the About message dialog
     */
    private void showAbout( ActionEvent evt ) {
        JOptionPane.showMessageDialog(
            this,about,"About",JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    /**
     *  @param args the command line arguments, path names
     */
    public static void main( String args[] ) {
        System.out.print(about);
        pads = new LinkedList<Pad>();
        for ( int i = 0; i < args.length; ++i ) {
            File f = new File(args[i]);
            if ( f.canRead() ) {
                new Pad(f).setVisible(true);
            } else {
                System.err.println("can't read '"+f+"'");
            }
        }
        if ( pads.isEmpty() ) {
            newFile();
        }
    }

}
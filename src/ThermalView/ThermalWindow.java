package ThermalView;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Window;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class ThermalWindow extends JFrame
{
	public Container container;
	public ArrayList <JPanel> components;
	
    public ThermalWindow()
    {
        
        setPreferredSize( new Dimension(300, 300 ));
        setResizable( false );
        setLocationRelativeTo( null );
        setLayout( new GridLayout(8,8) );

        container = getContentPane();
        components = new ArrayList < JPanel >();
        JPanel temp = null;

        // Populating Arraylist object.
        for ( int i = 0; i < 64; i++ )
        {
            temp = new JPanel();
            temp.setSize( 100,100 );
            components.add( temp );
            container.add(temp);
        }
        setTitle("Thermal Window");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setVisible( true );

        // Accessing components via index.
//        components.get( 5 ).setBackground( Color.BLUE );
//        components.get( 8 ).setBackground( Color.GREEN );
    }
    
    public static void main(String[] args){
    	ThermalWindow thermalWindow = new ThermalWindow();
    }
}
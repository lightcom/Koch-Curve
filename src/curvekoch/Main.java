package curvekoch;


import java.awt.BorderLayout;
import javax.swing.JFrame;

import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 
 */
public class Main {
    public static void main(String[] args) {
        final JFrame fr = new JFrame("Koch curve");
        fr.setSize(600, 400);
        fr.setLayout(new BorderLayout());
        
        final Koch fractal = new Koch(3);
        
        JSpinner spinner = new JSpinner( new SpinnerNumberModel( 3,0,7,1 ) );
        spinner.addChangeListener( new ChangeListener() {
          @Override
          public void stateChanged( ChangeEvent e ) {
            JSpinner spinner = ( JSpinner ) e.getSource();
            SpinnerModel spinnerModel = spinner.getModel();
            fractal.setN((int) spinnerModel.getValue());
          }
        });
        
        fr.getContentPane().add(BorderLayout.NORTH,spinner);
        fr.getContentPane().add(BorderLayout.CENTER,fractal);
        fr.setResizable(false);
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fr.setVisible(true);
    }
 
}

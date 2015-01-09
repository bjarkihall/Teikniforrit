package teikniforrit;

import javax.swing.*;

public class TeikniForrit
{   
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI(); 
            }
        });
    }
    //Keyrir forritið af stað: býr til viðmót og sýnir það.
    private static void createAndShowGUI() {
        System.out.println("Created GUI on EDT? "+
        SwingUtilities.isEventDispatchThread());
        JFrame f = new MainFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        f.setMinimumSize(f.getSize());
        f.setVisible(true);
    } 
}
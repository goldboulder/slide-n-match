
package slidenmatch;

import java.text.DecimalFormat;
import javax.swing.JFrame;


public class SlideNMatch extends JFrame{

    public int fieldPanelHeight = 900;
    public int fieldPanelWidth = 900;
    public DecimalFormat commas = new DecimalFormat("###,###,###");
    public SlideNMatch(){
        setTitle("SlideNMatch");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(new MenuPanel(this));
        pack();
        //centers the frame on the screen
        setLocationRelativeTo(null);
        setVisible(true);
}

    public static void main(String[] args) {
        new SlideNMatch();
    }
    
}

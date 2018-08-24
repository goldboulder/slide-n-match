
package slidenmatch;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MakerPanel extends JPanel implements ActionListener{

    public SlideNMatch frame;
    public FieldPanel fieldPanel;
    
    public JPanel optionPanel;
    public JPanel proportiesPanel;
    public JPanel buttonPanel;
    public JPanel savePanel;
    public JButton sizePlusButton;
    public JButton sizeMinusButton;
    public JButton[] blockButtons;
    public JTextField saveTextField;
    public JButton saveButton;
    public JPanel quitResetPanel;
    public JButton resetButton;
    public JButton quitButton;
    public BufferedImage background;
    
    public final int optionPanelWidth = 350;

    
    
    
    public MakerPanel(SlideNMatch frame, String levelName){
        this.frame = frame;
        try {
            background = ImageIO.read(new File("pictures/Background/Wood.png"));
        }
        catch (IOException ex) {
            
        }
        setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
        
        makeFieldPanel(levelName);
        optionPanel = new JPanel();
        proportiesPanel = new JPanel();
        buttonPanel = new JPanel();
        savePanel = new JPanel();
        quitResetPanel = new JPanel();
        
        sizePlusButton = new JButton("Increase grid size");
        sizePlusButton.addActionListener(this);
        sizePlusButton.setActionCommand("Plus");
        sizeMinusButton = new JButton("Decrease grid size");
        sizeMinusButton.addActionListener(this);
        sizeMinusButton.setActionCommand("Minus");
        resetButton = new JButton("reset");
        resetButton.addActionListener(this);
        resetButton.setActionCommand("Reset");
        quitButton = new JButton("quit");
        quitButton.addActionListener(this);
        quitButton.setActionCommand("Quit");
        
        blockButtons = new JButton[16];
        for (int i = 0; i < blockButtons.length; i++){
            blockButtons[i] = new JButton();
            blockButtons[i].addActionListener(this);
            blockButtons[i].setActionCommand(Integer.toString(i));
        }
        
        blockButtons[0].setIcon(new ImageIcon(fieldPanel.redBlock));
        blockButtons[1].setIcon(new ImageIcon(fieldPanel.blueBlock));
        blockButtons[2].setIcon(new ImageIcon(fieldPanel.yellowBlock));
        blockButtons[3].setIcon(new ImageIcon(fieldPanel.greenBlock));
        blockButtons[4].setIcon(new ImageIcon(fieldPanel.orangeBlock));
        blockButtons[5].setIcon(new ImageIcon(fieldPanel.purpleBlock));
        blockButtons[6].setIcon(new ImageIcon(fieldPanel.brownBlock));
        blockButtons[7].setIcon(new ImageIcon(fieldPanel.pinkBlock));
        blockButtons[8].setIcon(new ImageIcon(fieldPanel.limeBlock));
        blockButtons[9].setIcon(new ImageIcon(fieldPanel.lightBlueBlock));
        blockButtons[10].setIcon(new ImageIcon(fieldPanel.lavenderBlock));
        blockButtons[11].setIcon(new ImageIcon(fieldPanel.tanBlock));
        blockButtons[12].setIcon(new ImageIcon(fieldPanel.blackBlock));
        blockButtons[13].setIcon(new ImageIcon(fieldPanel.whiteBlock));
        blockButtons[14].setText("Delete");
        blockButtons[15].setText("Select");
        
        
        saveTextField = new JTextField(16);
        saveButton = new JButton("Save");
        saveButton.addActionListener(this);
        saveButton.setActionCommand("Save");
        
        proportiesPanel.add(sizePlusButton);
        proportiesPanel.add(sizeMinusButton);
        
        for (int i = 0; i < blockButtons.length; i++){
            buttonPanel.add(blockButtons[i]);
        }
        
        quitResetPanel.add(resetButton);
        quitResetPanel.add(quitButton);
        
        savePanel.add(saveTextField);
        savePanel.add(saveButton);
        
        optionPanel.add(proportiesPanel);
        optionPanel.add(buttonPanel);
        optionPanel.add(savePanel);
        optionPanel.add(quitResetPanel);
        
        
        add(fieldPanel);
        add(optionPanel);
        
        optionPanel.setLayout(new BoxLayout(optionPanel,BoxLayout.Y_AXIS));
        proportiesPanel.setLayout(new BoxLayout(proportiesPanel,BoxLayout.X_AXIS));
        buttonPanel.setLayout(new GridLayout(4,4));
        //savePanel.setLayout(new BoxLayout(savePanel,BoxLayout.X_AXIS));
        fieldPanel.setPreferredSize(new Dimension(frame.fieldPanelWidth,frame.fieldPanelHeight));
        optionPanel.setPreferredSize(new Dimension(optionPanelWidth,frame.fieldPanelHeight));
        proportiesPanel.setPreferredSize(new Dimension(optionPanelWidth,100));
        savePanel.setPreferredSize(new Dimension(optionPanelWidth,200));
        buttonPanel.setPreferredSize(new Dimension(optionPanelWidth,optionPanelWidth));
        
        optionPanel.setOpaque(false);
        proportiesPanel.setOpaque(false);
        buttonPanel.setOpaque(false);
        savePanel.setOpaque(false);
        quitResetPanel.setOpaque(false);
        
        setVisible(true);
    }
    
    @Override
    public void paintComponent(Graphics g){
        g.setColor(new Color(255,255,255,20));
       
        g.drawImage(background, 0, 0, frame.fieldPanelWidth + optionPanelWidth, frame.fieldPanelHeight, null);
        g.fillRect(0, 0, frame.fieldPanelWidth + optionPanelWidth, frame.fieldPanelHeight);
    }
    
    public void makeFieldPanel(String levelName){
        if (levelName.equals(""))
            fieldPanel = new FieldPanel(frame,null,"Blank",0);
        else
            fieldPanel = new FieldPanel(frame,null,levelName,0);
            fieldPanel.gameMode = "Maker";
        
    }
    
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(frame.fieldPanelWidth + optionPanelWidth, frame.fieldPanelHeight);
    }
    
    
    
    public void save(){
        
        if (saveTextField.getText().equals("")){
            JOptionPane.showMessageDialog(frame,"Choose a name for your level first","",JOptionPane.PLAIN_MESSAGE);
            return;
        }
                
        
        fieldPanel.levelName = saveTextField.getText();
        boolean continueWithSave = false;
        
        //if file already exists, show warning
        String str = saveTextField.getText();
        File f = new File("levels/" + str + ".txt");
        if (f.exists()){
            int n = JOptionPane.showConfirmDialog(frame,str + " already exists. Overwrite?","",JOptionPane.YES_NO_OPTION);
            if (n == 0)
                continueWithSave = true;
        }
        else{
            continueWithSave = true;
        }
        
        if (!continueWithSave)
            return;
        
        
        try {
            fieldPanel.saveLevel();
        }
        catch (FileNotFoundException ex) {
            System.out.println("Someting went wrong saving");
        }
        System.out.println("Saved!");
        
    }
    
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Plus": fieldPanel.increaseGridSize(); break;
            case "Minus": fieldPanel.decreaseGridSize(); break;
            case "Reset": fieldPanel.loadLevel("Blank"); break;
            case "Quit": 
                MenuPanel m = new MenuPanel(frame);
                frame.setContentPane(m);
                frame.getContentPane().revalidate();
                frame.pack();
            break;
            case "0": fieldPanel.makerButtonSelected = 1; break;
            case "1": fieldPanel.makerButtonSelected = 2; break;
            case "2": fieldPanel.makerButtonSelected = 3; break;
            case "3": fieldPanel.makerButtonSelected = 4; break;
            case "4": fieldPanel.makerButtonSelected = 5; break;
            case "5": fieldPanel.makerButtonSelected = 6; break;
            case "6": fieldPanel.makerButtonSelected = 7; break;
            case "7": fieldPanel.makerButtonSelected = 8; break;
            case "8": fieldPanel.makerButtonSelected = 9; break;
            case "9": fieldPanel.makerButtonSelected = 10; break;
            case "10": fieldPanel.makerButtonSelected = 11; break;
            case "11": fieldPanel.makerButtonSelected = 12; break;
            case "12": fieldPanel.makerButtonSelected = 0; break;
            case "13": fieldPanel.makerButtonSelected = -1; break;
            case "14": fieldPanel.makerButtonSelected = -2; break;
            case "15": fieldPanel.makerButtonSelected = -3; break;
            case "Save": save();
            default: 
        }
        
        
        
        
        
        
        
    }
    
}

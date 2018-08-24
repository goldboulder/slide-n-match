
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
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class MenuPanel extends JPanel implements ActionListener{
    public SlideNMatch frame;
    
    public JPanel titlePanel;
    public JLabel titleLabel;
    
    public JPanel instructionPanel;
    public JLabel instructionLabel;
    
    public JPanel modePanel;
    public JPanel puzzlePanel;
    public JPanel freePlayPanel;
    public JLabel puzzleLabel;
    public JLabel freePlayLabel;
    public JLabel messageLabel;
    public JLabel puzzleInfoLabel;
    
    public JButton puzzleButton;
    public JPanel blankPanel;
    public JPanel blankPanel2;
    
    public JTextField puzzleTextField;
    public JButton makerButton;
    
    public JButton freePlayButton;
    public JLabel currentLevelLabel;
    public JLabel recordLabel;
    public JButton resetButton;
    
    public BufferedImage background;
    public int currentFreePlayLevel;
    public int currentFreePlayScore;
    public int recordFreePlayLevel;
    public int recordFreePlayScore;
    public int currentChallengeLevel;
    public int currentChallengeScore;
    
    
    public JPanel challengePanel;
    public JLabel challengeLabel;
    public JLabel challengeLevelLabel;
    public JButton challengeContinueButton;
    public JButton challengeNewButton;
    public JTextField challengeLevelTextField;
    public JLabel challengeInstructionLabel;
    
    
    
    public MenuPanel(SlideNMatch frame){
        this.frame = frame;
        
        currentFreePlayLevel = 0;
        currentFreePlayScore = 0;
        recordFreePlayLevel = 0;
        recordFreePlayScore = 0;
        currentChallengeLevel = 0;
        currentChallengeScore = 0;
        
        
        try {
            background = ImageIO.read(new File("pictures/Background/SlideNMatch Background.png"));
        }
        catch (IOException ex) {
            System.out.println("Image not found");
        }
        
        try{
            Scanner sc = new Scanner(new File("save data.txt"));
            currentFreePlayLevel = sc.nextInt();
            currentFreePlayScore = sc.nextInt();
            recordFreePlayLevel = sc.nextInt();
            recordFreePlayScore = sc.nextInt();
            currentChallengeLevel = sc.nextInt();;
            currentChallengeScore = sc.nextInt();;
            
        }
        catch(FileNotFoundException e){
            
        }
        
        
        
        
        
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        
        titlePanel = new JPanel();
        titleLabel = new JLabel("SlideN'Match");
        instructionPanel = new JPanel();
        instructionLabel = new JLabel("Select a game mode");
        modePanel = new JPanel();
        puzzlePanel = new JPanel();
        freePlayPanel = new JPanel();
        puzzleInfoLabel = new JLabel("Enter a level name to play or edit");
        puzzleButton = new JButton("Play Level");
        puzzleButton.addActionListener(this);
        puzzleButton.setActionCommand("Puzzle");
        makerButton = new JButton("Make Level");
        makerButton.addActionListener(this);
        makerButton.setActionCommand("Maker");
        blankPanel = new JPanel();
        blankPanel2 = new JPanel();
        puzzleTextField = new JTextField(16);
        freePlayButton = new JButton("Free Play");
        if (currentFreePlayLevel > 1)
            freePlayButton.setText("Continue");
        freePlayButton.addActionListener(this);
        freePlayButton.setActionCommand("Free Play");
        currentLevelLabel = new JLabel("Current Level: " + currentFreePlayLevel + "    Score: " + frame.commas.format(currentFreePlayScore));
        recordLabel = new JLabel("Record Level: " + recordFreePlayLevel + "    Score: " + frame.commas.format(recordFreePlayScore));
        resetButton = new JButton("Reset Progress");
        resetButton.addActionListener(this);
        resetButton.setActionCommand("Reset Progress");
        puzzleLabel = new JLabel("Puzzle Mode");
        freePlayLabel = new JLabel("Marathon Mode");
        messageLabel = new JLabel("");
        
        challengePanel = new JPanel();
        challengeLabel = new JLabel("Challenge Mode");
        challengeLevelLabel = new JLabel("level: " + currentChallengeLevel);
        challengeContinueButton = new JButton("Continue");
        challengeNewButton = new JButton("New");
        challengeLevelTextField = new JTextField();
        challengeInstructionLabel = new JLabel("Select a level to play");
        
        challengeLevelTextField.setText("1");
        
        challengePanel.add(challengeLabel);
        challengePanel.add(challengeLevelLabel);
        challengePanel.add(challengeContinueButton);
        challengePanel.add(challengeNewButton);
        challengePanel.add(challengeLevelTextField);
        challengePanel.add(challengeInstructionLabel);
        challengePanel.setLayout(new BoxLayout(challengePanel,BoxLayout.Y_AXIS));
        
        challengeContinueButton.addActionListener(this);
        challengeNewButton.addActionListener(this);
        challengeContinueButton.setActionCommand("Challenge Continue");
        challengeNewButton.setActionCommand("Challenge New");
        challengeLevelTextField.setColumns(5);
        challengeLevelTextField.setMaximumSize(new Dimension(100,16));
        
        
        freePlayPanel.add(freePlayLabel);
        freePlayPanel.add(freePlayButton);
        freePlayPanel.add(currentLevelLabel);
        freePlayPanel.add(recordLabel);
        freePlayPanel.add(resetButton);
        
        
        puzzlePanel.add(puzzleLabel);
        puzzlePanel.add(puzzleButton);
        puzzlePanel.add(puzzleTextField);
        puzzlePanel.add(makerButton);
        puzzlePanel.add(puzzleInfoLabel);
        
        
        titlePanel.add(titleLabel);
        instructionPanel.add(instructionLabel);
        
        modePanel.add(puzzlePanel);
        modePanel.add(freePlayPanel);
        modePanel.add(challengePanel);
        
        blankPanel.add(messageLabel);
        
        add(titlePanel);
        add(instructionPanel);
        add(blankPanel2);
        add(modePanel);
        add(blankPanel);
        
        
        
        blankPanel.setPreferredSize(new Dimension(1100,300));
        blankPanel2.setPreferredSize(new Dimension(1100,200));
        modePanel.setLayout(new GridLayout(1,2));
        puzzlePanel.setLayout(new BoxLayout(puzzlePanel,BoxLayout.Y_AXIS));
        puzzleTextField.setMaximumSize(new Dimension(200,25));
        freePlayPanel.setLayout(new BoxLayout(freePlayPanel,BoxLayout.Y_AXIS));
        titlePanel.setPreferredSize(new Dimension(1100,150));
        instructionPanel.setPreferredSize(new Dimension(1100,50));
        modePanel.setPreferredSize(new Dimension(1100,200));
        
        titleLabel.setFont(new Font("Snap ITC", Font.BOLD, 50));
        instructionLabel.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 30));
        messageLabel.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 30));
        
        titlePanel.setOpaque(false);
        instructionPanel.setOpaque(false);
        modePanel.setOpaque(false);
        freePlayPanel.setOpaque(false);
        blankPanel.setOpaque(false);
        blankPanel2.setOpaque(false);
        puzzlePanel.setOpaque(false);
        challengePanel.setOpaque(false);
        
        setVisible(true);
    }
    
    @Override
    public void paintComponent(Graphics g){
        g.setColor(new Color(255,255,255));
       
        g.drawImage(background, 0, 0, 1100, 900, null);
        //g.fillRect(0, 0, 1100, 900);
    }
    
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Puzzle")){
            String levelName = puzzleTextField.getText();
            File f = new File("levels/" + levelName + ".txt");
            GamePanel g;
            if (f.exists()){
                g = new GamePanel(frame,currentFreePlayLevel,currentFreePlayScore,levelName);
                frame.setContentPane(g);
                frame.getContentPane().revalidate();
                frame.pack();
            }
            else{
                messageLabel.setText("File " + puzzleTextField.getText() + ".txt not found");
                frame.getContentPane().revalidate();
                frame.pack();
            }
            
        }
        if (e.getActionCommand().equals("Free Play")){
            GamePanel g;
            
            if (currentFreePlayLevel == 1){
                //System.out.println("Reset!");
                g = new GamePanel(frame,currentFreePlayLevel,currentFreePlayScore,"New Free Play");
            }
            else
                g = new GamePanel(frame,currentFreePlayLevel,currentFreePlayScore,"Free Play");
            
            frame.setContentPane(g);
            frame.getContentPane().revalidate();
            frame.pack();
        }
        
        if (e.getActionCommand().equals("Reset Progress")){//confermation****************
            
            int n = JOptionPane.showConfirmDialog(frame,"Really delete your progress?","",JOptionPane.YES_NO_OPTION);
            if (n != 0)
                return;
            
            currentFreePlayLevel = 1;
            currentFreePlayScore = 0;
            
            currentLevelLabel.setText("Current Level: " + currentFreePlayLevel + "    Score: " + frame.commas.format(currentFreePlayScore));
            recordLabel.setText("Record Level: " + recordFreePlayLevel + "    Score: " + frame.commas.format(recordFreePlayScore));
            //update save data
            try{
                    PrintWriter file = new PrintWriter("save data.txt");
                    file.println(1);
                    file.println(0);
                    file.println(recordFreePlayLevel);
                    file.println(recordFreePlayScore);
                    file.println(currentChallengeLevel);
                    file.println(currentChallengeScore);
                    file.close();
                }
                catch(FileNotFoundException ex){
                    
                }
            
            frame.getContentPane().revalidate();
            frame.pack();
        }
        
        if (e.getActionCommand().equals("Maker")){
            String levelName = puzzleTextField.getText();
            File f = new File("levels/" + levelName + ".txt");
            MakerPanel m;
            if (f.exists())
                m = new MakerPanel(frame,puzzleTextField.getText());
            else
                m = new MakerPanel(frame,"Blank");
            frame.setContentPane(m);
            frame.getContentPane().revalidate();
            frame.pack();
            
        }
        
        if (e.getActionCommand().equals("Challenge Continue")){
                GamePanel g = new GamePanel(frame,currentChallengeLevel,currentChallengeScore,"Challenge Continue");
                //System.out.println(currentChallengeLevel);
                frame.setContentPane(g);
                frame.getContentPane().revalidate();
                frame.pack();
        }
        if (e.getActionCommand().equals("Challenge New")){
            int level = 1;
            try{
                level = Integer.parseInt(challengeLevelTextField.getText());
                if (level <= 0)
                    throw new Exception();
                GamePanel g = new GamePanel(frame,level,0,"Challenge New");
                frame.setContentPane(g);
                frame.getContentPane().revalidate();
                frame.pack();
            }
            catch (Exception ex){
                messageLabel.setText("Level needs to be a positive integer");
            }
            
        }
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1100,900);
    }
    
    
    
}

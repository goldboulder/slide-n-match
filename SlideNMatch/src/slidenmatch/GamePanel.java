/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slidenmatch;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener{
    public SlideNMatch frame;
    public FieldPanel fieldPanel;
    public JPanel statsPanel;
    
    public JPanel scorePanel;
    public JLabel scoreLabel;
    
    public JPanel levelPanel;
    public JLabel levelLabel;
    
    public JPanel resetPanel;
    public JButton resetButton;
    
    public JPanel quitPanel;
    public JButton quitButton;
    
    public final int statsPanelWidth = 200;
    
    public BufferedImage background;
    
    
    public int score;
    public int level;
    public int scoreThisLevel;
    
    public int moves;
    public String mode = "";

    
    
    public GamePanel (SlideNMatch frame, int level, int score, String name){
        //System.out.println("Game Panel Constructor" + score);
        this.frame = frame;
        try {
            background = ImageIO.read(new File("pictures/Background/Wood.png"));
        }
        catch (IOException ex) {
            
        }
        
        moves = 0;
        this.score = score;
        this.scoreThisLevel = scoreThisLevel;
        if (level > 0)
            this.level = level;
        else
            this.level = 1;
        
        setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
        if (name.equals("New Free Play")){
            
            mode = "Free Play";
            fieldPanel = new FieldPanel(this.frame,this,level,"Free Play");
        }
        else if (name.equals("Free Play")){
            mode = "Free Play";
            fieldPanel = new FieldPanel(this.frame,this,"Free Play",level);
        }
        else if (name.equals("Challenge New")){
            mode = "Challenge";
            fieldPanel = new FieldPanel(this.frame,this,level,"Challenge Continue");
        }
        else if (name.equals("Challenge Continue")){
            mode = "Challenge";
            fieldPanel = new FieldPanel(this.frame,this,"Challenge Continue",level);
        }
        else{
            mode = "Puzzle";
            fieldPanel = new FieldPanel(this.frame,this,name,level);
        }
        
        
        
        statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel,BoxLayout.Y_AXIS));
        
        fieldPanel.setPreferredSize(new Dimension(frame.fieldPanelWidth,frame.fieldPanelHeight));
        statsPanel.setPreferredSize(new Dimension(statsPanelWidth,frame.fieldPanelHeight));
        
        scorePanel = new JPanel();
        
        
        
        
        levelPanel = new JPanel();
        if (name.equals("New Free Play") || name.equals("Free Play") || name.startsWith("Challenge")){
            levelLabel = new JLabel("Level: " + Integer.toString(level));
            scoreLabel = new JLabel("Score: " + frame.commas.format(score));
        }
        else{
            levelLabel = new JLabel(name);
            scoreLabel = new JLabel("Moves: " + Integer.toString(moves));
        }
        
        levelLabel.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 20));
        levelLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 20));
        scoreLabel.setForeground(Color.WHITE);
        
        resetPanel = new JPanel();
        resetButton = new JButton("reset");
        resetButton.addActionListener(this);
        resetButton.setActionCommand("reset");
        
        quitPanel = new JPanel();
        quitButton = new JButton("quit");
        quitButton.addActionListener(this);
        quitButton.setActionCommand("quit");
        
        
        
        resetPanel.add(resetButton);
        quitPanel.add(quitButton);
        
        levelPanel.add(levelLabel);
        scorePanel.add(scoreLabel);
        
        statsPanel.add(levelPanel);
        statsPanel.add(scorePanel);
        statsPanel.add(resetPanel);
        statsPanel.add(quitPanel);
        
        statsPanel.setOpaque(false);
        scorePanel.setOpaque(false);
        levelPanel.setOpaque(false);
        resetPanel.setOpaque(false);
        quitPanel.setOpaque(false);
        
        
        add(fieldPanel);
        add(statsPanel);
        
        repaint();
        setVisible(true);
    }
    
    @Override
    public void paintComponent(Graphics g){
        g.setColor(new Color(255,255,255,20));
       
        g.drawImage(background, 0, 0, frame.fieldPanelWidth + statsPanelWidth, frame.fieldPanelHeight, null);
        g.fillRect(0, 0, frame.fieldPanelWidth + statsPanelWidth, frame.fieldPanelHeight);
    }
    
    public void nextLevel(){
        level ++;
        levelLabel.setText("Level: " + Integer.toString(level));
        fieldPanel.constructFieldPanel(level,mode);
        //System.out.println("Next Level " + mode);
        scoreThisLevel = 0;
        fieldPanel.xGridSelected = -10;
        fieldPanel.yGridSelected = -10;
        
        
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(frame.fieldPanelWidth + statsPanelWidth, frame.fieldPanelHeight);
    }
    
    public void addMatchToScore(int total, int numbered){
        int pointsGained = (int)determinePoints(total,numbered);
        score += pointsGained;
        scoreThisLevel += pointsGained;
        scoreLabel.setText("Score: " + frame.commas.format(score));
        frame.getContentPane().revalidate();
        
    }
    
    public void addMove(){
        moves ++;
        scoreLabel.setText("Moves: " + Integer.toString(moves));
        frame.getContentPane().revalidate();
        //System.out.println(moves);
    }
    
    public double determinePoints(int totalTiles,int numberedTiles){
        return 0.8*sprt(level+2)*numberedTiles*sprt(numberedTiles+1) * totalTiles*sprt(totalTiles - 1);
    }
    
    public static double sprt(double number)
    {
        if (number == 1) return 1;
        if (number < 1) return 0;
        
        double low = (1.0);
        double high = 143.016087935746;
    
    
        double ans = (low + high) / 2;
    
        for (int i = 0; i < 60; i++)
        {
            if (Math.pow(ans,ans) <= number)
                low = ans;
            else
                high = ans;
            ans = (low + high) / 2;
        }
        return ans;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("reset")){
            if (fieldPanel.gameMode.equals("Free Play") || fieldPanel.gameMode.startsWith("Challenge")){
                score -= scoreThisLevel;
                fieldPanel.constructFieldPanel(level,mode);
                scoreThisLevel = 0;
                scoreLabel.setText("Score: " + Integer.toString(score));
                frame.getContentPane().revalidate();
                if (fieldPanel.gameMode.equals("Challenge")){
                    score = 0;
                    updateChallengeStats();
                }
            }
            
            else if (fieldPanel.gameMode.equals("Puzzle")){
                fieldPanel.loadLevel(fieldPanel.levelName);
                moves = 0;
                scoreLabel.setText("Moves: " + Integer.toString(moves));
                frame.getContentPane().revalidate();
            }
            
            
        }
        
        
        
        if (e.getActionCommand().equals("quit")){
            //System.out.println(fieldPanel.gameMode);
            if (fieldPanel.gameMode.equals("Free Play")){
                
                try {
                    //System.out.println(fieldPanel.gameMode);
                    fieldPanel.saveLevel();
                }
                catch (FileNotFoundException ex) {
                    
                }
                
                updateHighScore();
                
                
                MenuPanel m = new MenuPanel(frame);
                frame.setContentPane(m);
                frame.getContentPane().revalidate();
                frame.pack();
            }
            
            else if (fieldPanel.gameMode.equals("Puzzle")){
                MenuPanel m = new MenuPanel(frame);
                frame.setContentPane(m);
                frame.getContentPane().revalidate();
                frame.pack();
            }
            
            else if (fieldPanel.gameMode.equals("Maker")){
                MenuPanel m = new MenuPanel(frame);
                frame.setContentPane(m);
                frame.getContentPane().revalidate();
                frame.pack();
            }
            
            else if (fieldPanel.gameMode.startsWith("Challenge")){
                try {
                    //System.out.println(fieldPanel.gameMode);
                    fieldPanel.saveLevel();
                }
                catch (FileNotFoundException ex) {
                    
                }
                
                MenuPanel m = new MenuPanel(frame);
                //System.out.println(fieldPanel.level + " <<<< " + score);
                m.currentChallengeLevel = fieldPanel.level;
                m.challengeLevelLabel.setText("Level: " + fieldPanel.level);
                m.currentChallengeScore = score;
                frame.setContentPane(m);
                frame.getContentPane().revalidate();
                frame.pack();
                updateChallengeStats();
            }
            
            
            
            
        }
    }
    
    public void updateChallengeStats(){
        int currentFreePlayLevel = 0;
        int currentFreePlayScore = 0;
        int recordFreePlayLevel = 0;
        int recordFreePlayScore = 0;
        
        
        try{
            Scanner sc = new Scanner(new File("save data.txt"));
            currentFreePlayLevel = sc.nextInt();
            currentFreePlayScore = sc.nextInt();
            recordFreePlayLevel = sc.nextInt();
            recordFreePlayScore = sc.nextInt();
            
        }
        catch(FileNotFoundException ex){
                    
        }
        //System.out.println(score + "  " + recordFreePlayScore);
        try{
            PrintWriter file = new PrintWriter("save data.txt");
            file.println(currentFreePlayLevel);
            file.println(currentFreePlayScore);
            file.println(recordFreePlayLevel);
            file.println(recordFreePlayScore);
            
            file.println(fieldPanel.level);
            file.println(score);
            //System.out. println(fieldPanel.level + " , " + score);
            
            file.close();
        }
        catch(FileNotFoundException ex){
            
        }
    }
    
    public void updateHighScore(){
        int recordFreePlayLevel = 0;
        int recordFreePlayScore = 0;
        int challengeLevel = 0;
        int challengeScore = 0;
        
        try{
            Scanner sc = new Scanner(new File("save data.txt"));
            sc.nextInt();
            sc.nextInt();
            recordFreePlayLevel = sc.nextInt();
            recordFreePlayScore = sc.nextInt();
            challengeLevel = sc.nextInt();
            challengeScore = sc.nextInt();
        }
        catch(FileNotFoundException ex){
                    
        }
        //System.out.println(score + "  " + recordFreePlayScore);
        try{
            PrintWriter file = new PrintWriter("save data.txt");
            file.println(level);
            file.println(score);
            if (level > recordFreePlayLevel)
                file.println(level);
            else
                file.println(recordFreePlayLevel);
            if (score > recordFreePlayScore)
                file.println(score);
            else
                file.println(recordFreePlayScore);
            
            file.println(challengeLevel);
            file.println(challengeScore);
            
            file.close();
        }
        catch(FileNotFoundException ex){
            
        }
    }
    
}

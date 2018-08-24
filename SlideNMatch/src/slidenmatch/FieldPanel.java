/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slidenmatch;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;


public class FieldPanel extends JPanel implements ActionListener, KeyListener, MouseListener{
    
    public SlideNMatch frame;
    public GamePanel parent;
    
    public int panelWidth;
    public int panelHeight;
    
    public int level;
    public int lengthOfGrid;
    public int numColors;
    public int xGridSelected;
    public int yGridSelected;
    public Block[][] blockMap;
    public int blockSize;
    public int fontSize;
    public Color backgroundColor = new Color(128,128,128,130);
    public boolean inSlidingAnimation;
    public Timer movementTimer;
    public Timer completeTimer;
    public Font blockFont;
    public int numBlocksMatched;
    public int numNumberedBlocksMatched;
    public double slidingState;
    public double slidingSpeed;
    public int matchAnimationState;
    public int maxMatchAnimationState;
    public int flashTransparency;
    
    public Color black = new Color(0,0,0);
    public Color red = new Color(255,0,0);
    public Color blue = new Color(0,0,255);
    public Color yellow = new Color(255,255,0);
    public Color green = new Color(0,150,0);
    public Color orange = new Color(255,128,0);
    public Color purple = new Color(130,0,150);
    public Color brown = new Color(114,56,27);
    public Color pink = new Color(255,20,147);
    public Color lime = new Color(0,255,0);
    public Color lightBlue = new Color(47,239,249);
    public Color lavender = new Color(255,102,255);
    public Color tan = new Color(201,140,114);
    
    public BufferedImage background;
    
    public BufferedImage whiteBlock;
    public BufferedImage blackBlock;
    public BufferedImage redBlock;
    public BufferedImage blueBlock;
    public BufferedImage yellowBlock;
    public BufferedImage greenBlock;
    public BufferedImage orangeBlock;
    public BufferedImage purpleBlock;
    public BufferedImage brownBlock;
    public BufferedImage pinkBlock;
    public BufferedImage limeBlock;
    public BufferedImage lightBlueBlock;
    public BufferedImage lavenderBlock;
    public BufferedImage tanBlock;
    
    public boolean[][] blackBlockMatrix;
    public boolean[][] freeBlockMatrix;
    public boolean[][] checkerMatrix;
    public int checkXPosition;
    public int checkYPosition;
    public String levelName;
    public String gameMode;
    
    public int makerButtonSelected;
    
    
    public FieldPanel(SlideNMatch frame, JPanel parent, int level, String gameMode){
        
        this.parent = (GamePanel) parent;
        makerButtonSelected = -1;
        this.frame = frame;
        constructorThings();
        loadImages();
        constructFieldPanel(level,gameMode);
        
        
    }
    
    public FieldPanel (SlideNMatch frame, JPanel parent, String name, int currentLevel){
        this.level = currentLevel;
        this.numColors = levelToNumColors(level);
        this.parent = (GamePanel) parent;
        this.frame = frame;
        
        constructorThings();
        loadImages();
        loadLevel(name);
        
    }
    
    public void constructorThings(){
        this.panelWidth = this.frame.fieldPanelWidth;
        this.panelHeight = this.frame.fieldPanelHeight;
        xGridSelected = -10;
        yGridSelected = -10;
        inSlidingAnimation = false;
        numBlocksMatched = 0;
        numNumberedBlocksMatched = 0;
        matchAnimationState = 0;
        maxMatchAnimationState = 10;
        slidingState = 0;
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);
        addMouseListener(this);
        movementTimer = new Timer(30,this);
        movementTimer.setActionCommand("movement");
        movementTimer.start();
        completeTimer = new Timer(300,this);
        completeTimer.setActionCommand("complete");
    }
    
    public void loadImages(){
        try {
            background = ImageIO.read(new File("pictures/Background/Stone.png"));
            
            whiteBlock = ImageIO.read(new File("pictures/Block/White Block.png"));
            blackBlock = ImageIO.read(new File("pictures/Block/Black Block.png"));
            redBlock = ImageIO.read(new File("pictures/Block/Red Block.png"));
            blueBlock = ImageIO.read(new File("pictures/Block/Blue Block.png"));
            yellowBlock = ImageIO.read(new File("pictures/Block/Yellow Block.png"));
            greenBlock = ImageIO.read(new File("pictures/Block/Green Block.png"));
            orangeBlock = ImageIO.read(new File("pictures/Block/Orange Block.png"));
            purpleBlock = ImageIO.read(new File("pictures/Block/Purple Block.png"));
            brownBlock = ImageIO.read(new File("pictures/Block/Brown Block.png"));
            pinkBlock = ImageIO.read(new File("pictures/Block/Pink Block.png"));
            limeBlock = ImageIO.read(new File("pictures/Block/Lime Block.png"));
            lightBlueBlock = ImageIO.read(new File("pictures/Block/Light Blue Block.png"));
            lavenderBlock = ImageIO.read(new File("pictures/Block/Lavender Block.png"));
            tanBlock = ImageIO.read(new File("pictures/Block/Tan Block.png"));
            
            
        }
        catch (IOException ex) {
            System.out.println("Couldn't find image!");
        }
    }
    
    public void constructFieldPanel(int level, String gameMode){
        //System.out.println("Level: " + level);
        this.level = level;
        int gridSize = levelToGridSize(level);
        int gridSize2 = gridSize * gridSize;
        constructFieldPanel(gridSize,gridSize,levelToNumColors(level),numDeadBlocks(gridSize2),blankBlocks(gridSize2),unNumberedBlocks(gridSize2),numberedBlocks(gridSize2),gameMode);
    }
    
    public void constructFieldPanel(int numRows, int lengthOfGrid, int numColors, int numDeadBlocks,int numBlankBlocks, int unNumberedBlocks, int[] numNumberedBlocks, String gameMode){
        if (gameMode.equals("Free Play"))
            levelName = "Free Play Save";
        if (gameMode.startsWith("Challenge")){
            levelName = "Challenge Continue";
        }
        
        this.gameMode = gameMode;
        this.lengthOfGrid = numRows;
        this.lengthOfGrid = lengthOfGrid;
        
        // add a few white blocks in the higher levels(30?)
        blockSize = panelWidth/lengthOfGrid;
        this.numColors = numColors;
        
        fontSize = getFontSize(blockSize);
        blockFont = new Font("Arial Rounded MT Bold", Font.BOLD, fontSize);
        slidingState = 0;
        slidingSpeed = blockSize/4.0;
        blockMap = new Block[numRows][lengthOfGrid];
        blackBlockMatrix = new boolean[numRows][lengthOfGrid];
        freeBlockMatrix = new boolean[numRows][lengthOfGrid];
        checkerMatrix = new boolean[numRows][lengthOfGrid];
        
        int[] set = getRandomSet(numRows*lengthOfGrid);
        
        int numOfNumberedBlocks = numNumberedBlocks.length;
        if (numDeadBlocks + unNumberedBlocks + numOfNumberedBlocks > numRows * lengthOfGrid)
            throw new RuntimeException("Too many blocks to fit in the grid!");
        
        int blocksPlaced = 0;
        while (numDeadBlocks > 0){
            int row = rowIndex(set[blocksPlaced]);
            int column = columnIndex(set[blocksPlaced]);
            blockMap[row][column] = new Block(this,0,0,row,column);
            numDeadBlocks --;
            blocksPlaced ++;
        }
        
        while (unNumberedBlocks > 0){
            int row = rowIndex(set[blocksPlaced]);
            int column = columnIndex(set[blocksPlaced]);
            blockMap[row][column] = new Block(this,getRandomColor(),0,row,column);
            unNumberedBlocks --;
            blocksPlaced ++;
        }
        
        while (numBlankBlocks > 0){
            int row = rowIndex(set[blocksPlaced]);
            int column = columnIndex(set[blocksPlaced]);
            blockMap[row][column] = new Block(this,-1,0,row,column);
            numBlankBlocks --;
            blocksPlaced ++;
        }
        
        while (numOfNumberedBlocks > 0){
            int row = rowIndex(set[blocksPlaced]);
            int column = columnIndex(set[blocksPlaced]);
            blockMap[row][column] = new Block(this,getRandomColor(),numNumberedBlocks[numOfNumberedBlocks - 1],row,column);
            numOfNumberedBlocks --;
            blocksPlaced ++;
        }
        
        antiStuck();
        repaint();
        
        try {
            saveLevel();
        }
        catch (FileNotFoundException ex) {
            
        }
        
        if (gameMode.equals("Free Play"))
            parent.updateHighScore();
        
        
    }
    
    
    public int levelToGridSize(int level){
        return 4 + (int)Math.sqrt(level);
    }
    
    public int levelToNumColors(int level){
        
        int colors = 1 + (int)(Math.pow(level,0.48));
        if (colors > 11) colors = 11;
        return colors;
    }
    
    public int numDeadBlocks(int gridSize){
        double percent = 0.15+(level*0.0005);
        if (percent > 0.25) percent = 0.25;
        return (int)(gridSize * percent);
        
    }
    
    public int blankBlocks(int gridSize){
        if (level < 30)
            return 0;
        double percent = 0.0005*(level-30);
        if (percent > 0.1) percent = 0.1;
        return (int) (gridSize * percent);
    }
    
    public int unNumberedBlocks(int gridSize){
        return (int)(gridSize * 0.22 - blankBlocks(gridSize));
    }
    
    public int[] numberedBlocks(int gridSize){
        double max = maxNumberForBlocks();
        double min = minNumberForBlocks();
        int[] ans = new int[(int)(gridSize*0.20)];
        for (int i = 0; i < ans.length; i++){
            ans[i] = (int)(Math.random() * (max - min) + min);
        }
        return ans;
    }
    
    public double minNumberForBlocks(){
        return (Math.pow(level,0.45)/10.0) + 1;
    }
    
    public double maxNumberForBlocks(){
        return Math.pow(level+1,0.45);
    }
    
    public int columnIndex(int index){
        return index % lengthOfGrid;
    }
    
    public int rowIndex(int index){
        return index / lengthOfGrid;
    }
    
    public void loadLevel (String name){
        //System.out.println("Free Play Load");
        levelName = name;
        if (name.equals("Free Play")){
            gameMode = "Free Play";
            name = "Free Play Save";
            levelName = "Free Play Save";
        }
        else if (name.equals("Blank")){
            gameMode = "Maker";
            name = "Blank";
            levelName = "Blank";
            
        }
        else if (name.equals("Challenge Continue")){
            //stuff@@@@@@@@@@@@@@@@@@@@@@@
            
            gameMode = "Challenge";
            levelName = "Challenge Continue";
            //System.out.println(level);
        }
        
        else{
            gameMode = "Puzzle";
        }
        
        try{
            Scanner sc = new Scanner(new File("levels/" + name + ".txt"));
            lengthOfGrid = Integer.parseInt(sc.nextLine());
            lengthOfGrid = lengthOfGrid;
            String[] tokens;
            String[] subTokens;
            blockMap = new Block[lengthOfGrid][lengthOfGrid];
            for (int y = 0; y < lengthOfGrid; y++){
                tokens = sc.nextLine().split(" ");
                for (int x = 0; x < lengthOfGrid; x++){
                    if (tokens[x].equals("x"))
                        continue;
                    
                    if (tokens[x].equals("w")){
                        blockMap[x][y] = new Block(this, -1, 0, x, y);
                        continue;
                    }
                    
                    if (tokens[x].equals("b")){
                        blockMap[x][y] = new Block(this, 0, 0, x, y);
                        continue;
                    }
                    //System.out.println(x + " " + y);
                    //System.out.println(tokens[x]);
                    subTokens = tokens[x].split(":");
                    //System.out.println(subTokens[0] + "   " + subTokens[1]);
                    blockMap[x][y] = new Block(this, Integer.parseInt(subTokens[0]), Integer.parseInt(subTokens[1]), x, y);
                     
                    
                }
            }
            if (gameMode.equals("Free Play")){
                parent.scoreThisLevel = sc.nextInt();
            }
            
        }
        catch(FileNotFoundException e){
            System.out.println("file not found");
        }
        
        blockSize = panelWidth/lengthOfGrid;
        //System.out.println(lengthOfGrid);
        fontSize = getFontSize(blockSize);
        blockFont = new Font("Arial Rounded MT Bold", Font.BOLD, fontSize);
        
        slidingSpeed = blockSize/4.0;
        
        xGridSelected = -10;
        yGridSelected = -10;
        repaint();
        
    }
    
    public void deleteBlock(int x, int y){
        blockMap[x][y] = null;
    }
    
    //@@@@@@@@@@@@@@@
    public void saveLevel() throws FileNotFoundException{
        //System.out.println(levelName);
        PrintWriter file = new PrintWriter("levels/" + levelName + ".txt");
        file.println(lengthOfGrid);
        for (int y = 0; y < lengthOfGrid; y++){
            for (int x = 0; x < lengthOfGrid; x++){
                if (blockMap[x][y] != null)
                    file.print(blockMap[x][y].toString() + " ");
                else
                    file.print("x ");
            }
            file.println("");
        }
        
        if (gameMode.equals("Free Play") || gameMode.equals("Challenge"))
            file.print(parent.scoreThisLevel);
        else
            file.print("0");
        
        file.close();
    }
    
    
    @Override
    public void paintComponent(Graphics g){
        g.setColor(backgroundColor);
        g.drawImage(background, 0, 0, panelWidth, panelHeight, null);
        g.fillRect(0, 0, panelWidth, panelHeight);
        
        // draw grid lines
        if (gameMode.equals("Maker"))
            g.setColor(black);
        int linePosition;
        for (int i = 1; i < lengthOfGrid; i++){
            linePosition = gridXToScreenX(i);
            g.drawLine(linePosition, 0, linePosition, panelHeight);
            g.drawLine(0,linePosition, panelWidth, linePosition);
        }
        
        // paint each block
        for (int i = 0; i < lengthOfGrid; i++){
            for (int j = 0; j < lengthOfGrid; j++)
                if (blockMap[i][j] != null)
                    blockMap[i][j].paint(g);
        }
        
        // paint highlighted block
        if (!inSlidingAnimation)
            paintHighlight(g);

    }
    
    // paints a white border around the selected space
    public void paintHighlight(Graphics g){
        int lineThickness = (blockSize) / 50 + 1;
        g.setColor(Color.WHITE);
        int x = gridXToScreenX(xGridSelected);
        int y = gridYToScreenY(yGridSelected);
        
        g.fillRect(x-lineThickness, y-lineThickness, 2*lineThickness, blockSize+lineThickness);
        g.fillRect(x-lineThickness, y-lineThickness, blockSize+lineThickness, 2*lineThickness);
        g.fillRect(x+blockSize-lineThickness, y-lineThickness, 2*lineThickness, blockSize+2*lineThickness);
        g.fillRect(x-lineThickness, y+blockSize-lineThickness, blockSize+lineThickness, 2*lineThickness);


    }
    
    
    public void matchResult(){
        
        if (numBlocksMatched >= 3){
            for (int i = 0; i < lengthOfGrid; i++){
                for (int j = 0; j < lengthOfGrid; j++){
                    if (blockMap[i][j] != null && blockMap[i][j].matched){
                        blockMap[i][j].match();
                        matchAnimationState = maxMatchAnimationState;
                        if (blockMap[i][j] != null)
                            blockMap[i][j].isNumbered = (blockMap[i][j].number > 0);
                    }
                }
            }
            if (gameMode.equals("Free Play") || gameMode.startsWith("Challenge")){
                parent.addMatchToScore(numBlocksMatched,numNumberedBlocksMatched);
                if (!hasNumberedBlocks()){
                    completeTimer.start();
                }
            }
            if (gameMode.equals("Puzzle")){
                
                if (blockMap[xGridSelected][yGridSelected] != null){
                    xGridSelected = -10;
                    yGridSelected = -10;
                }
                
                
                if (!hasNumberedBlocks())
                    completeTimer.start();
            }
        }
        
        for (int i = 0; i < lengthOfGrid; i++){
            for (int j = 0; j < lengthOfGrid; j++){
                if (blockMap[i][j] != null)
                    blockMap[i][j].matched = false;
            }
        }
        
        
        
        numBlocksMatched = 0;
        numNumberedBlocksMatched = 0;
    }
    
    // determines if there are still numbered blocks in the field
    public boolean hasNumberedBlocks(){
        for (int i = 0; i < lengthOfGrid; i++){
            for (int j = 0; j < lengthOfGrid; j++){
                if (blockMap[i][j] != null && blockMap[i][j].isNumbered && blockMap[i][j].color > 0){
                    return true;
                }
            }
        }
        return false;
    }
    
    public int getFontSize(int blockHeight){
        return blockHeight / 2;
    }
    
    public int getRandomColor(){
        return (int)(Math.random()*numColors + 1);
    }
    
    public int mouseXToGridX(int mX){
        return (int)(1.0 * mX * lengthOfGrid /panelWidth);
    }
    
    public int mouseYToGridY(int mY){
        return (int)(1.0 * mY * lengthOfGrid /panelHeight);
    }
    
    public int gridXToScreenX(int gridX){
        return (int)(panelWidth * (1.0*gridX/lengthOfGrid));
    }
    public int gridYToScreenY(int gridY){
        return (int)(panelHeight * (1.0*gridY/lengthOfGrid));
    }
    
    public static int[] getRandomSet(int size){
        int[] array = new int[size];
        
        for (int i = 0; i < size; i ++){
            array[i] = i;
        }
        
        for (int i = 0; i < size; i ++){
            swap(array,(int)(Math.random()*size),i);
        }
        
        return array;
    }
    
    public static void swap(int[] arr, int a, int b){
        int temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }
    
    public void increaseGridSize(){
        if (lengthOfGrid > 100)
            return;
        
        Block[][] newBlockMap = new Block[lengthOfGrid+1][lengthOfGrid+1];
        for (int y = 0; y < lengthOfGrid; y++){
            for (int x = 0; x < lengthOfGrid; x++){
                newBlockMap[x][y] = blockMap[x][y];
            }
        }
        blockMap = newBlockMap;
        lengthOfGrid ++;
        blockSize = panelWidth/lengthOfGrid;
        fontSize = getFontSize(blockSize);
        blockFont = new Font("Arial Rounded MT Bold", Font.BOLD, fontSize);
        for (int y = 0; y < lengthOfGrid; y++){
            for (int x = 0; x < lengthOfGrid; x++){
                if (blockMap[x][y] != null){
                    blockMap[x][y].screenXPosition = gridXToScreenX(x);
                    blockMap[x][y].screenYPosition = gridYToScreenY(y);
                }
            }
        }
        repaint();
        
    }
    
    public void decreaseGridSize(){
        if (lengthOfGrid < 2)
            return;
        
        Block[][] newBlockMap = new Block[lengthOfGrid-1][lengthOfGrid-1];
        for (int y = 0; y < lengthOfGrid-1; y++){
            for (int x = 0; x < lengthOfGrid-1; x++){
                newBlockMap[x][y] = blockMap[x][y];
            }
        }
        blockMap = newBlockMap;
        lengthOfGrid --;
        blockSize = panelWidth/lengthOfGrid;
        fontSize = getFontSize(blockSize);
        blockFont = new Font("Arial Rounded MT Bold", Font.BOLD, fontSize);
        for (int y = 0; y < lengthOfGrid; y++){
            for (int x = 0; x < lengthOfGrid; x++){
                if (blockMap[x][y] != null){
                    blockMap[x][y].screenXPosition = gridXToScreenX(x);
                    blockMap[x][y].screenYPosition = gridYToScreenY(y);
                }
            }
        }
        repaint();
    }
    
    
    
    public void antiStuck(){
        ////////////////////////////////////rare bug?
        do{
        checkXPosition = (int)(Math.random() * lengthOfGrid);
        checkYPosition = (int)(Math.random() * lengthOfGrid);
        }
        while(blockMap[checkXPosition][checkYPosition] != null && blockMap[checkXPosition][checkYPosition].color == 0);
        
        
        getBlackBlockMap();
        freeScan(checkXPosition,checkYPosition);
        checkScan(checkXPosition,checkYPosition,"up");
    }
    
    public void getBlackBlockMap(){
        blackBlockMatrix = new boolean[lengthOfGrid][lengthOfGrid];
        for (int i = 0; i < lengthOfGrid; i++){
            for (int j = 0; j < lengthOfGrid; j++){
                if (blockMap[j][i] != null && blockMap[j][i].color == 0){
                    blackBlockMatrix[j][i] = true;
                }
            }
        }
        
    }
    
    public void freeScan(int x, int y){
        if (freeBlockMatrix[x][y] == true)
            return;
        freeBlockMatrix[x][y] = true;
        
        
            if (blackBlockMatrix[x][y] == false){
                try{freeScan(x+1,y);} catch(IndexOutOfBoundsException e){};
                try{freeScan(x-1,y);} catch(IndexOutOfBoundsException e){}
                try{freeScan(x,y+1);} catch(IndexOutOfBoundsException e){}
                try{freeScan(x,y-1);} catch(IndexOutOfBoundsException e){}
            }
        
    }
    
    public void checkScan(int x, int y, String direction){
        try{
            if (checkerMatrix[y][x] == true)
                return;
            checkerMatrix[y][x] = true;
            if (freeBlockMatrix[y][x] == false){
                switch (direction){
                    case "left": blockMap[y][x+1] = null;/*System.out.println("Deleted block at " + y + " , " + (x+1));*/ break;
                    case "right": blockMap[y][x-1] = null;/*System.out.println("Deleted block at " + y + " , " + (x-1));*/ break;
                    case "up": blockMap[y+1][x] = null;/*System.out.println("Deleted block at " + (y+1) + " , " + x);*/ break;
                    case "down": blockMap[y-1][x] = null;/*System.out.println("Deleted block at " + (y-1) + " , " + x);*/ break;
                    
                    
                }
                //System.out.println("deleted block at " + x + "," + y + " " + direction);
                
                freeScan(y,x);
            }
            
        }
        catch(IndexOutOfBoundsException e){
            return;
        }
        checkScan(x+1,y,"right");
        checkScan(x-1,y,"left");
        checkScan(x,y+1,"down");
        checkScan(x,y-1,"up");
    }
    

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("movement")){
            //System.out.println(matchAnimationState);
            if (matchAnimationState > 0){
                matchAnimationState --;
                flashTransparency = (int)(255 * Math.sin(Math.PI/2*matchAnimationState/maxMatchAnimationState));
                if (matchAnimationState == 0){
                    for (int i = 0; i < lengthOfGrid; i++){
                        for (int j = 0; j < lengthOfGrid; j++){
                            if (blockMap[i][j] != null)
                                blockMap[i][j].justMatched = false;
                        }
                    }
                    repaint();
                }
            }
            
            if (inSlidingAnimation){
                slidingState += slidingSpeed;
                Block selectedBlock = blockMap[xGridSelected][yGridSelected];
                switch(selectedBlock.direction){
                    case "up": selectedBlock.screenYPosition -= slidingSpeed; break;
                    case "right": selectedBlock.screenXPosition += slidingSpeed; break;
                    case "left": selectedBlock.screenXPosition -= slidingSpeed; break;
                    case "down": selectedBlock.screenYPosition += slidingSpeed; break;
                }
                if (slidingState >= blockSize){
                    slidingState = 0;
                    selectedBlock.screenXPosition = gridXToScreenX(selectedBlock.gridXPosition);
                    selectedBlock.screenYPosition = gridYToScreenY(selectedBlock.gridYPosition);
                    inSlidingAnimation = false;
                    selectedBlock.move(selectedBlock.direction);
                    repaint();
                }
            }
            if (inSlidingAnimation || matchAnimationState != 0)
                repaint();
        }
        else if (e.getActionCommand().equals("complete")){
            //System.out.println(gameMode);
            completeTimer.stop();
            if (gameMode.equals("Free Play")){
                JOptionPane.showMessageDialog(frame,"Level Complete!","",JOptionPane.PLAIN_MESSAGE);
                parent.nextLevel();
            }
            else if (gameMode.equals("Puzzle")){
                JOptionPane.showMessageDialog(frame,"Puzzle Complete!","",JOptionPane.PLAIN_MESSAGE);
                MenuPanel m = new MenuPanel(frame);
                frame.setContentPane(m);
                frame.getContentPane().revalidate();
                frame.pack();
            }
            else if (gameMode.startsWith("Challenge")){
                JOptionPane.showMessageDialog(frame,"Level Complete!","",JOptionPane.PLAIN_MESSAGE);
                MenuPanel m = new MenuPanel(frame);
                frame.setContentPane(m);
                frame.getContentPane().revalidate();
                frame.pack();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameMode.equals("Puzzle") || gameMode.equals("Free Play")  || gameMode.startsWith("Challenge")){
            try{
                if (!inSlidingAnimation && matchAnimationState <= 0){
                    if (gameMode.equals("Puzzle"))
                        parent.addMove();
                    if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W){
                            blockMap[xGridSelected][yGridSelected].move("up");
                        }
                    if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D){
                            blockMap[xGridSelected][yGridSelected].move("right");
                        }
                    if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A){
                            blockMap[xGridSelected][yGridSelected].move("left");
                        }
                    if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S){
                            blockMap[xGridSelected][yGridSelected].move("down");
                        }
                }
            }
            catch(IndexOutOfBoundsException | NullPointerException h){

            }
            
        }
        
        else if (gameMode.equals("Maker")){
            try{
                if (blockMap[xGridSelected][yGridSelected] != null){
                    if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W){
                        blockMap[xGridSelected][yGridSelected].increaseNumber();
                    }
                    else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S){
                        blockMap[xGridSelected][yGridSelected].decreaseNumber();
                    }
                    repaint();
                }
            }
            catch(IndexOutOfBoundsException ex){
                
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = mouseXToGridX(e.getX());
        int y = mouseYToGridY(e.getY());
        //System.out.println(x + "  " + y);
        Block selectedBlock = blockMap[x][y];
        if (!inSlidingAnimation && !gameMode.equals("Maker")){
            
            if(selectedBlock != null && selectedBlock.color != 0){
                xGridSelected = x;
                yGridSelected = y;
                repaint();
            }
        }
        
        if (gameMode.equals("Maker")){
            if(selectedBlock != null && makerButtonSelected == -3){
                xGridSelected = x;
                yGridSelected = y;
            }
            
            if (makerButtonSelected == -2){
                blockMap[x][y] = null;
                xGridSelected = -10;
                yGridSelected = -10;
            }
            if (makerButtonSelected > -2){
                blockMap[x][y] = new Block(this,makerButtonSelected,0,x,y);
            }
            repaint();
        }
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        requestFocusInWindow();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    
    
    
}

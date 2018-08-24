package slidenmatch;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class Block{
    public FieldPanel fieldPanel;
    public int color;
    public boolean isNumbered;
    public int number;
    public double screenXPosition;
    public double screenYPosition;
    public int gridXPosition;
    public int gridYPosition;
    public boolean matched;
    public boolean justMatched;
    public String direction;
    
    
    
    
    public Block(FieldPanel fieldPanel, int color, int number, int x, int y){
        this.fieldPanel = fieldPanel;
        this.color = color;
        this.number = number;
        this.isNumbered = (number > 0);
        this.gridXPosition = x;
        this.gridYPosition = y;
        this.matched = false;
        this.justMatched = false;
        screenXPosition = fieldPanel.gridXToScreenX(gridXPosition);
        screenYPosition = fieldPanel.gridYToScreenY(gridYPosition);
        direction = "up";
        
    }
    
    
    public void paint(Graphics g) {
        
        
        BufferedImage image = intToColorImage(color);
        g.drawImage(image,(int)screenXPosition,(int)screenYPosition, fieldPanel.blockSize+1, fieldPanel.blockSize+1,null);
        
        if (isNumbered && color > 0){
            g.setColor(Color.BLACK);
            
            g.setFont(fieldPanel.blockFont);
            if (number >= 100)
                g.drawString(Integer.toString(number), (int)screenXPosition, (int)screenYPosition+(int)(fieldPanel.blockSize*0.66));
            else if (number >= 10)
                g.drawString(Integer.toString(number), (int)screenXPosition+(int)(fieldPanel.blockSize*0.16), (int)screenYPosition+(int)(fieldPanel.blockSize*0.66));
            else
                g.drawString(Integer.toString(number), (int)screenXPosition+(int)(fieldPanel.blockSize*0.33), (int)screenYPosition+(int)(fieldPanel.blockSize*0.66));
        }
        
        if (justMatched){
            g.setColor(new Color(255,255,255,fieldPanel.flashTransparency));
            g.fillRect((int)screenXPosition-1,(int)screenYPosition-1, fieldPanel.blockSize+2, fieldPanel.blockSize+2);
        }
        
    }
    
    public void move(String dir){
        fieldPanel.inSlidingAnimation = true;
        direction = dir;
        try{
            switch(dir){
                case "up":
                    if(fieldPanel.blockMap[gridXPosition][gridYPosition-1] == null){
                        
                        gridYPosition --;
                        fieldPanel.blockMap[gridXPosition][gridYPosition] = this;
                        fieldPanel.blockMap[gridXPosition][gridYPosition+1] = null;
                        fieldPanel.slidingState = 0;
                    }
                    else{
                        checkForMatches();
                        fieldPanel.inSlidingAnimation = false;
                    }
                
                
                break;
                case "right":
                    if(fieldPanel.blockMap[gridXPosition+1][gridYPosition] == null){
                        
                        gridXPosition ++;
                        fieldPanel.blockMap[gridXPosition][gridYPosition] = this;
                        fieldPanel.blockMap[gridXPosition-1][gridYPosition] = null;
                        //move(dir);//for animation
                        fieldPanel.slidingState = 0;
                    }
                    else{
                        checkForMatches();
                        fieldPanel.inSlidingAnimation = false;
                    }
                
                
                
                break;
                case "left":
                    if(fieldPanel.blockMap[gridXPosition-1][gridYPosition] == null){
                        
                        gridXPosition --;
                        fieldPanel.blockMap[gridXPosition][gridYPosition] = this;
                        fieldPanel.blockMap[gridXPosition+1][gridYPosition] = null;
                        fieldPanel.slidingState = 0;
                    }
                    else{
                        checkForMatches();
                        fieldPanel.inSlidingAnimation = false;
                    }
                break;
                
                case "down":
                    if(fieldPanel.blockMap[gridXPosition][gridYPosition+1] == null){
                       
                       gridYPosition ++;
                       fieldPanel.blockMap[gridXPosition][gridYPosition] = this;
                       fieldPanel.blockMap[gridXPosition][gridYPosition-1] = null;
                       fieldPanel.slidingState = 0;
                    }
                    else{
                        checkForMatches();
                        fieldPanel.inSlidingAnimation = false;
                    }
                
                
                break;
                default:
                    throw new RuntimeException("Invalid direction");
            }
        }
        catch(IndexOutOfBoundsException e){
            checkForMatches();
            fieldPanel.inSlidingAnimation = false;
        }
        
        fieldPanel.xGridSelected = gridXPosition;
        fieldPanel.yGridSelected = gridYPosition;
        fieldPanel.matchResult();
        
    }
    

    public void match(){
        justMatched = true;
        if (isNumbered)
            number --;
        if (fieldPanel.gameMode.equals("Free Play") || fieldPanel.gameMode.startsWith("Challenge")){
            color = fieldPanel.getRandomColor();
        }
        else if (fieldPanel.gameMode.equals("Puzzle")){
            if (number <= 0){
                fieldPanel.deleteBlock(gridXPosition,gridYPosition);
                
            }
        }
    }
    
    public void checkForMatches(){//recursive
        if (color > 0){
            matched = true;
            fieldPanel.numBlocksMatched ++;
            if (isNumbered) fieldPanel.numNumberedBlocksMatched ++;

            Block topBlock = null, rightBlock = null, leftBlock = null, bottomBlock = null;

            try{
                topBlock = fieldPanel.blockMap[gridXPosition][gridYPosition-1];
            }
            catch(IndexOutOfBoundsException e){}
            try{
                rightBlock = fieldPanel.blockMap[gridXPosition+1][gridYPosition];
            }
            catch(IndexOutOfBoundsException e){}
            try{
                leftBlock = fieldPanel.blockMap[gridXPosition-1][gridYPosition];
            }
            catch(IndexOutOfBoundsException e){}
            try{
                bottomBlock = fieldPanel.blockMap[gridXPosition][gridYPosition+1];
            }
            catch(IndexOutOfBoundsException e){}

            if (topBlock != null && topBlock.color == color && !topBlock.matched){
                topBlock.checkForMatches();
            }
            if (rightBlock != null && rightBlock.color == color && !rightBlock.matched){
                rightBlock.checkForMatches();
            }
            if (leftBlock != null && leftBlock.color == color && !leftBlock.matched){
                leftBlock.checkForMatches();
            }
            if (bottomBlock != null && bottomBlock.color == color && !bottomBlock.matched){
                bottomBlock.checkForMatches();
            }
        }
    }
    
    public void increaseNumber(){
        number ++;
        isNumbered = true;
    }
    
    public void decreaseNumber(){
        if(isNumbered){
            number --;
            if (number == 0)
                isNumbered = false;
        }
    }
    
    
    public BufferedImage intToColorImage(int num){
        switch(num){
            case -1: return fieldPanel.whiteBlock;
            case 0: return fieldPanel.blackBlock;
            case 1: return fieldPanel.redBlock;
            case 2: return fieldPanel.blueBlock;
            case 3: return fieldPanel.yellowBlock;
            case 4: return fieldPanel.greenBlock;
            case 5: return fieldPanel.orangeBlock;
            case 6: return fieldPanel.purpleBlock;
            case 7: return fieldPanel.brownBlock;
            case 8: return fieldPanel.pinkBlock;
            case 9: return fieldPanel.limeBlock;
            case 10: return fieldPanel.lightBlueBlock;
            case 11: return fieldPanel.lavenderBlock;
            case 12: return fieldPanel.tanBlock;
            default: return null;
            
        }
    }
    
    @Override
    public String toString(){
        switch (color){
            case 0: return "b";
            case -1: return "w";
            default: return Integer.toString(color) + ":" + Integer.toString(number);
        }
    }

    
    
}

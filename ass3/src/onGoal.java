
import java.awt.Image;
import javax.swing.ImageIcon;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Insigma
 */
public class onGoal {
    private  int x;
    private  int y;
    private  Image image;
    
    /**
     * Constructor for OnGoal
     * @precondition x and y are real positive integers
     */
    public onGoal(int x, int y) {
        ImageIcon ii = new ImageIcon("./resources/onGoal.png");
        image = ii.getImage();
        this.x = x;
        this.y = y;
    }
    
    /**
     * Returns onGoal Image
     */
    public Image getImg() {
        return image;
    }
    
    /**
     * getter for X coordinate
     * returns x
     */
    public int getX() {
        return x;
    }
    
    
    /**
     * Getter for Y Co ordinate
     * return y
     */
    public int getY() {
        return y;
    }
}

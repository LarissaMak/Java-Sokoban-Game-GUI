

import java.awt.Image;
import javax.swing.ImageIcon;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author kirachen
 */
public class Goal {
    private int x;
    private int y;
    private Image image;
    
    /**
     * Constructor for Goal
     * @param x
     * @param y
     */
    public Goal(int x, int y) {
        ImageIcon ii = new ImageIcon("./resources/goal.png");
        image = ii.getImage();
        this.x = x;
        this.y = y;
    }
    
    /**
     * returns png image
     * @return
     */
    public Image getImg() {
        return image;
    }
    
    /**
     * returns x coordinate
     * @return
     */
    public int getX() {
        return x;
    }
    
    /**
     * returns y coordinate
     * @return
     */
    public int getY() {
        return y;
    }
    
    /**
     * returns goal coordinates as string
     */
    @Override
    public String toString() {
        return "Goal{" + "x=" + x + ", y=" + y + '}';
    }
    
    
}

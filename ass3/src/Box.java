
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
public class Box {
    private int x;
    private int y;
    private Image image;
    
    /**
     * Constructor for box
     * @param x
     * @param y
     */
    public Box(int x, int y) {
        ImageIcon ii = new ImageIcon("./resources/crate.png");
        image = ii.getImage();
        this.x = x;
        this.y = y;
    }
    
    /**
     * REturns the png file for box
     * @return
     */
    public Image getImg() {
        return image;
    }
    
    /**
     * Returns x coordinate
     * @return
     */
    public int getX() {
        return x;
    }
    
    /**
     * Returns y coordinate 
     * @return
     */
    public int getY() {
        return y;
    }
}
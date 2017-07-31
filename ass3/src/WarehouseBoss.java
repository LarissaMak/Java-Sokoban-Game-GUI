
import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * WarehouseBoss is the main point of this project which will 
 * save instances that other classes will access
 * 
 */
public class WarehouseBoss {

    private static final int SCREEN_SIZE_X = 1100; //fixed X screen size variable
    private static final int SCREEN_SIZE_Y = 740; //fixed Y screen size variable

    private JFrame jFrame; //our main JFrame will be stored here
    private JPanel menuScreen; //our current jPanel will be stored here
    private char[][] oldMap; //stores current map to be reused for restart
    
    private MenuScreen menuScreenClass; 

    /**Getter method allows other classes to retrieve menuScreen
     * returns menuScreen 
     * @return MenuScreen 
     */
    public MenuScreen getMenuScreenClass() {
        return menuScreenClass;
    }

    /**Setter method allows other classes to set menuScreenClass
     * sets the menuscreen 
     * @param menuScreenClass 
     */
    public void setMenuScreenClass(MenuScreen menuScreenClass) {
        this.menuScreenClass = menuScreenClass;
    }
    
    /**Setter method allows other classes to set oldMap
     * sets the old map to save previous state 
     * @param oldMap 
     */
    public void setOldMap(char[][] oldMap) {
        this.oldMap = oldMap;
    }
    
    /**Getter method allows other classes to retrieve old map
     * retreives the old map 
     * @return getOldMap
     */
    public char[][] getOldMap() {
        return oldMap;
    }
    
    /**Getter method allows other classes to retrieve jFrame
     * retrieves the JFrame
     * @return JFrame
     */
    public JFrame getJFrame() {
        return jFrame;
    }

    /**Setter method allows other classes to set jFrame
     * Sets the JFrame 
     * @param jFrame 
     */
    public void setJFrame(JFrame jFrame) {
        this.jFrame = jFrame;
    }

    /**Setter method allows other classes to set jFrame
     * initialize the vairables of JFrame to show up 
     */
    public void setJFrame() {
        jFrame = new JFrame();
        jFrame.setResizable(false);
        jFrame.setSize(SCREEN_SIZE_X, SCREEN_SIZE_Y);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //closes when clicking "X"
        jFrame.setTitle("WAREHOUSE BOSS!");
    }

    /**Getter method allows other classes to retrieve menuScreen
     * retrieves menuScreen 
     * @return JPanel menuScreen
     */
    public JPanel getMenuScreen() {
        return menuScreen;
    }

    /**Setter method allows other classes to set menuScreen
     * Sets the menuScreen 
     * @param menuScreen 
     */
    public void setMenuScreen(JPanel menuScreen) {
        this.menuScreen = menuScreen;
    }


    /**
     * the starting point of program that initalizes warehouseboss instance and menusceren  
     * @param args      taken from stdin
     * @throws IOException
     * @throws UnsupportedAudioFileException
     * @throws LineUnavailableException 
     */
    public static void main(String args[]) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        WarehouseBoss warehouseBoss = new WarehouseBoss();
        warehouseBoss.setJFrame();
        MenuScreen myMenuScreen = new MenuScreen();
        myMenuScreen.showMenuScreen(warehouseBoss);
    }
}
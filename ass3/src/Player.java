import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;

public class Player {

    private int dx;
    private int dy;
    private int x;
    private int y;
    private Image image;
    private WarehouseBoss warehouseBoss;
    private int unit = 50;
    
    /**
     * Constructor for Player
     * @param warehouseBoss
     * @param x
     * @param y
     */
    public Player(WarehouseBoss warehouseBoss, int x, int y) {
        this.warehouseBoss = warehouseBoss;
        ImageIcon ii = new ImageIcon("./resources/player.png");
        image = ii.getImage();
        this.x = x;
        this.y = y;

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
     * returns png for player
     * @return
     */
    public Image getImage() {
        return image;
    }

    /**
     * Logic for when a player moves. Contains cases depending on the players adjacent tile
     * @param e
     * @param map
     * @return
     * @throws LineUnavailableException
     * @throws IOException
     * @throws UnsupportedAudioFileException
     */
    public boolean keyPressed(KeyEvent e, char[][] map) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        //AUDIO: Plays player move music 
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("./resources/music/player_move.wav").getAbsoluteFile());
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();

        int key = e.getKeyCode();

        int prevX = x / unit;
        int prevY = y / unit;

        int moveX = 0;
        int moveY = 0;

        if (key == KeyEvent.VK_A) {
            
            // x -= 20;
            moveX--;
        }

        if (key == KeyEvent.VK_D) {
            // x += 20;
            moveX++;
        }

        if (key == KeyEvent.VK_W) {
            // y -= 20;
            moveY--;
        }

        if (key == KeyEvent.VK_S) {
            // y += 20;
            moveY++;
        }

        //this is the item that that player wants to move/interact with
        char c = map[x / unit + moveX][y / unit + moveY];

        //Perform different things according to what type that item is
        boolean saveMap = true; //tells Board whether to save this map for Undo 
        switch (c) {
            case '#': //Wall
                //=== AUDIO: plays music to indicate invalid player move ===
                audioInputStream = AudioSystem.getAudioInputStream(new File("./resources/music/invalid_move.wav").getAbsoluteFile());
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
                saveMap = false;
                break;
            case '_': //Empty space, move character onto that block
                if (map[prevX][prevY] == '@') {
                    map[prevX + moveX][prevY + moveY] = 'C';
                    map[prevX][prevY] = 'G';
                    x += moveX * unit;
                    y += moveY * unit;
                } else {
                    map[prevX + moveX][prevY + moveY] = 'C';
                    map[prevX][prevY] = '_';
                    x += moveX * unit;
                    y += moveY * unit;
                }
                break;
            case 'G': //Goal, move character onto that spot
                if (map[prevX][prevY] == '@') {
                    map[prevX + moveX][prevY + moveY] = '@';
                    map[prevX][prevY] = 'G';
                    x += moveX * unit;
                    y += moveY * unit;
                } else {
                    map[prevX + moveX][prevY + moveY] = '@';
                    map[prevX][prevY] = '_';
                    x += moveX * unit;
                    y += moveY * unit;
                }
                break;
            case 'B': //Crate, move character onto that spot and push crate according to arrow keys pressed
                audioInputStream = AudioSystem.getAudioInputStream(new File("./resources/music/crate_move.wav").getAbsoluteFile());
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
                if (key == KeyEvent.VK_D) {
                    if (map[prevX + 2][prevY] == '_') {
                        map[prevX + 2][prevY] = 'B';
                        if (map[prevX][prevY] == '@') {
                            map[prevX + moveX][prevY + moveY] = 'C';
                            map[prevX][prevY] = 'G';
                            x += moveX * unit;
                            y += moveY * unit;
                        } else {
                            map[prevX + 2][prevY] = 'B';
                            map[prevX + moveX][prevY + moveY] = 'C';
                            map[prevX][prevY] = '_';
                            x += moveX * unit;
                            y += moveY * unit;
                        }
                    } else if (map[prevX + 2][prevY] == 'G') {
                        map[prevX + 2][prevY] = 'X';
                        if (map[prevX][prevY] == '@') {
                            map[prevX + moveX][prevY + moveY] = 'C';
                            map[prevX][prevY] = 'G';
                            x += moveX * unit;
                            y += moveY * unit;
                        } else {
                            map[prevX + 2][prevY] = 'X';
                            map[prevX + moveX][prevY + moveY] = 'C';
                            map[prevX][prevY] = '_';
                            x += moveX * unit;
                            y += moveY * unit;
                        }
                    }
                } else if (key == KeyEvent.VK_A) {
                    if (map[prevX - 2][prevY] == '_') {
                        map[prevX - 2][prevY] = 'B';
                        if (map[prevX][prevY] == '@') {
                            map[prevX + moveX][prevY + moveY] = 'C';
                            map[prevX][prevY] = 'G';
                            x += moveX * unit;
                            y += moveY * unit;
                        } else {
                            map[prevX - 2][prevY] = 'B';
                            map[prevX + moveX][prevY + moveY] = 'C';
                            map[prevX][prevY] = '_';
                            x += moveX * unit;
                            y += moveY * unit;
                        }
                    } else if (map[prevX - 2][prevY] == 'G') {
                        map[prevX - 2][prevY] = 'X';
                        if (map[prevX][prevY] == '@') {
                            map[prevX + moveX][prevY + moveY] = 'C';
                            map[prevX][prevY] = 'G';
                            x += moveX * unit;
                            y += moveY * unit;
                        } else {
                            map[prevX - 2][prevY] = 'X';
                            map[prevX + moveX][prevY + moveY] = 'C';
                            map[prevX][prevY] = '_';
                            x += moveX * unit;
                            y += moveY * unit;
                        }
                    }
                } else if (key == KeyEvent.VK_S) {
                    if (map[prevX][prevY + 2] == '_') {
                        map[prevX][prevY + 2] = 'B';
                        if (map[prevX][prevY] == '@') {
                            map[prevX + moveX][prevY + moveY] = 'C';
                            map[prevX][prevY] = 'G';
                            x += moveX * unit;
                            y += moveY * unit;
                        } else {
                            map[prevX][prevY + 2] = 'B';
                            map[prevX + moveX][prevY + moveY] = 'C';
                            map[prevX][prevY] = '_';
                            x += moveX * unit;
                            y += moveY * unit;
                        }
                    } else if (map[prevX][prevY + 2] == 'G') {
                        map[prevX][prevY + 2] = 'X';
                        if (map[prevX][prevY] == '@') {
                            map[prevX + moveX][prevY + moveY] = 'C';
                            map[prevX][prevY] = 'G';
                            x += moveX * unit;
                            y += moveY * unit;
                        } else {

                            map[prevX + moveX][prevY + moveY] = 'C';
                            map[prevX][prevY] = '_';
                            x += moveX * unit;
                            y += moveY * unit;
                        }
                    }
                } else if (key == KeyEvent.VK_W) {
                    if (map[prevX][prevY - 2] == '_') {
                        map[prevX][prevY - 2] = 'B';
                        map[prevX][prevY - 2] = 'B';
                        if (map[prevX][prevY] == '@') {
                            map[prevX + moveX][prevY + moveY] = 'C';
                            map[prevX][prevY] = 'G';
                            x += moveX * unit;
                            y += moveY * unit;
                        } else {
                            map[prevX][prevY - 2] = 'B';
                            map[prevX + moveX][prevY + moveY] = 'C';
                            map[prevX][prevY] = '_';
                            x += moveX * unit;
                            y += moveY * unit;
                        }
                    } else if (map[prevX][prevY - 2] == 'G') {
                        map[prevX][prevY - 2] = 'X';
                        if (map[prevX][prevY] == '@') {
                            map[prevX + moveX][prevY + moveY] = 'C';
                            map[prevX][prevY] = 'G';
                            x += moveX * unit;
                            y += moveY * unit;
                        } else {
                            map[prevX][prevY - 2] = 'X';
                            map[prevX + moveX][prevY + moveY] = 'C';
                            map[prevX][prevY] = '_';
                            x += moveX * unit;
                            y += moveY * unit;
                        }
                    }
                }
                break;

        }
        return saveMap;
    }
}



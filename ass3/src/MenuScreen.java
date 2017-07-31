
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Makes Jpanel for menu screen. 
 * Will open to Board screen which is another JPanel.
 * Sets that into our warehouseBoss JFrame
 * 
 */
public class MenuScreen extends JPanel {

    private boolean isMusicOn; //flag to see whether music is on or not
    private boolean isChallenger; //checks if its hard elvel
    private AudioInputStream audioInputStream; //instance of input stream
    private Clip clip; //instance of clip
    JButton musicBtn; //button for music button

    /**constructor for this class
     * 
     * sets isMusicOn to initially be true
     */
    public MenuScreen() {
        this.isMusicOn = true; //music is always on at first
    }

    /**Shows our menu screen and sets it within JFrame
     * 
     * @param warehouseBoss
     * @throws IOException
     * @throws UnsupportedAudioFileException
     * @throws LineUnavailableException 
     */
    public void showMenuScreen(WarehouseBoss warehouseBoss) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        //======= Set up our panel and frame =======
        //Create inner welcome screen (to be placed within our main JFrame)
        JPanel menuScreen = new JPanel();
        menuScreen.setBackground(Color.black);
        //menuScreen.addKeyListener(new TAdapter());
        //Initialise outer jFrame (this is used to contain our menu screen)
        final JFrame jFrame = warehouseBoss.getJFrame();
        jFrame.add(menuScreen); //add our JPanel into JFrame

        //======== Set up our welcome screen =======
        menuScreen.setLayout(null); //add our boxLayout to menuScreen

        //Plays music on a loop
        playGameMusic();

        //Make button for playing/stopping music
        musicBtn = new JButton();
        ImageIcon imageIconMusic = new ImageIcon(new ImageIcon("./resources/music.png").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        musicBtn.setIcon(imageIconMusic);

        musicBtn.setBorder(BorderFactory.createEmptyBorder());
        menuScreen.add(musicBtn);

        musicBtn.setBounds(970, 590, 100, 100);
        //Add event handling to toggle music on and off
        musicBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { //when clicked on...
                try {
                    if (isMusicOn) { //music is on, mute it
                        clip.stop();
                        isMusicOn = false;
                        ImageIcon imageIconMusic = new ImageIcon(new ImageIcon("./resources/no_music.png").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
                        //musicBtn.setLocation(700, 700);
                        musicBtn.setIcon(imageIconMusic);
                    } else {//music is off, unmute it
                        clip.start();
                        isMusicOn = true;
                        ImageIcon imageIconMusic = new ImageIcon(new ImageIcon("./resources/music.png").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
                        //musicBtn.setLocation(700, 700);
                        musicBtn.setIcon(imageIconMusic);
                    }
                } catch (Exception ex) {
                    System.out.println("Error with stopping/starting sound.");
                    ex.printStackTrace();
                    System.exit(1);
                }
            }
        });

        //Add logo
        JButton b1 = new JButton(new ImageIcon(new ImageIcon("./resources/logo.png").getImage().getScaledInstance(500, 450, Image.SCALE_DEFAULT)));
        b1.setBorder(BorderFactory.createEmptyBorder());
        b1.setFocusable(false);
        menuScreen.add(b1);
        b1.setBounds(315, 25, 500, 420);

        //Add invisible component to pad out nodes
        menuScreen.add(javax.swing.Box.createRigidArea(new Dimension(0, 20))); //makes invisible box 20units tall

        //Sets the Cursor to our Custom Cursor
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage("./resources/cursor.png");
        Cursor c = toolkit.createCustomCursor(image, new Point(jFrame.getX(), jFrame.getY()), "img");
        jFrame.setCursor(c);

        //Make intial start button below logo        
        JButton startBtn = new JButton();
        ImageIcon imageIconStart = new ImageIcon(new ImageIcon("./resources/start.png").getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT));
        startBtn.setIcon(imageIconStart);
        startBtn.setBorder(BorderFactory.createEmptyBorder());
        menuScreen.add(startBtn);
        startBtn.setBounds(466, 380, 200, 200);

        //start button hover
        JButton startBtnFlash = new JButton();
        ImageIcon imageIconStartFlash = new ImageIcon(new ImageIcon("./resources/start_selected.png").getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT));
        startBtnFlash.setIcon(imageIconStartFlash);
        startBtnFlash.setBorder(BorderFactory.createEmptyBorder());
        menuScreen.add(startBtnFlash);
        startBtnFlash.setBounds(466, 380, 200, 200);
        startBtnFlash.setVisible(false);

        //make sound and blink when hover over quit
        startBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            /**
             *  inner class to set flash
             * @param evt 
             */
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                //startBtnFlash.setBounds(466, 380, 200, 200);
                startBtnFlash.setVisible(true);
                //sound 
                try {
                    audioInputStream = AudioSystem.getAudioInputStream(new File("./resources/music/menu_select.wav").getAbsoluteFile());
                } catch (UnsupportedAudioFileException ex) {
                    Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    clip = AudioSystem.getClip();
                } catch (LineUnavailableException ex) {
                    Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    clip.open(audioInputStream);
                } catch (LineUnavailableException ex) {
                    Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
                }
                clip.start();
            }

            /** takes in event and sets flash off
             * 
             * @param evt 
             */
            public void mouseExited(java.awt.event.MouseEvent evt) {
                startBtnFlash.setVisible(false);
            }
        });

        //---- Buttons for difficulty settings -----
        JButton normBtn = new JButton();
        ImageIcon imageIconNorm = new ImageIcon(new ImageIcon("./resources/casual.png").getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT));
        normBtn.setIcon(imageIconNorm);
        normBtn.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        normBtn.setAlignmentY(java.awt.Component.CENTER_ALIGNMENT);
        normBtn.setBorder(BorderFactory.createEmptyBorder());
        menuScreen.add(normBtn);

        normBtn.setBounds(346, 390, 200, 200);
        normBtn.setVisible(false);
        //Add event handling for toggling between easy and hard mode
        normBtn.addActionListener(new ActionListener() {
            @Override
            /**
             * inner class to check on action done
             */
            public void actionPerformed(ActionEvent e) {
                isChallenger = false;

                try {
                    //when clicked on...
                    //Play selection sound
                    AudioInputStream audioInputStream = null;
                    audioInputStream = AudioSystem.getAudioInputStream(new File("./resources/music/menu_choose.wav").getAbsoluteFile());
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.start();
                    //Show main game screen
                    jFrame.add(new Board(warehouseBoss, isChallenger));

                    //Hide our current menu screen
                    menuScreen.setVisible(false);
                    //Store frame and panel in warehouseBoss fo later retrival
                    warehouseBoss.setJFrame(jFrame);
                    warehouseBoss.setMenuScreen(menuScreen);
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                    Logger.getLogger(MenuScreen.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        audioInputStream.close();
                    } catch (IOException ex) {
                        Logger.getLogger(MenuScreen.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        JButton challengerBtn = new JButton();
        ImageIcon imageIconChal = new ImageIcon(new ImageIcon("./resources/expert.png").getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT));
        challengerBtn.setIcon(imageIconChal);
        challengerBtn.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        challengerBtn.setAlignmentY(java.awt.Component.CENTER_ALIGNMENT);
        challengerBtn.setBorder(BorderFactory.createEmptyBorder());
        menuScreen.add(challengerBtn);

        challengerBtn.setBounds(596, 390, 200, 200);
        challengerBtn.setVisible(false);
        //Add event handling for toggling between easy and hard mode
        challengerBtn.addActionListener(new ActionListener() {
            @Override
            /**
             * inner class to check on whats done
             */
            public void actionPerformed(ActionEvent e) {
                isChallenger = true;

                try {
                    //when clicked on...
                    //Play selection sound

                    AudioInputStream audioInputStream = null;
                    audioInputStream = AudioSystem.getAudioInputStream(new File("./resources/music/menu_choose.wav").getAbsoluteFile());
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.start();
                    //Show main game screen
                    jFrame.add(new Board(warehouseBoss, isChallenger));

                    //Hide our current menu screen
                    menuScreen.setVisible(false);
                    //Store frame and panel in warehouseBoss fo later retrival
                    warehouseBoss.setJFrame(jFrame);
                    warehouseBoss.setMenuScreen(menuScreen);
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                    Logger.getLogger(MenuScreen.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        audioInputStream.close();
                    } catch (IOException ex) {
                        Logger.getLogger(MenuScreen.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        //Add event handling for start btn to show game/board screen
        startBtn.addActionListener(new ActionListener() {
            @Override
            /**
             * inner class to check on whats done and sets relevant buttons visible
             */
            public void actionPerformed(ActionEvent e) {
                normBtn.setVisible(true);
                challengerBtn.setVisible(true);
                startBtn.setVisible(false);

            }
        });

        //QUIT BUTTON
        //Make quit button below start btn initial state        
        JButton quitBtn = new JButton();
        ImageIcon imageIconQuit = new ImageIcon(new ImageIcon("./resources/quit.png").getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT));
        quitBtn.setIcon(imageIconQuit);
        quitBtn.setBorder(BorderFactory.createEmptyBorder());
        menuScreen.add(quitBtn);
        quitBtn.setBounds(465, 540, 200, 200); //530
        //quit button hover
        JButton quitBtnFlash = new JButton();
        ImageIcon imageIconQuitFlash = new ImageIcon(new ImageIcon("./resources/quit_selected.png").getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT));
        quitBtnFlash.setIcon(imageIconQuitFlash);
        quitBtnFlash.setBorder(BorderFactory.createEmptyBorder());
        menuScreen.add(quitBtnFlash);

        //make sound and blink when hover over quit
        quitBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                //quitBtnFlash.setBounds(465, 510, 200, 200);
                //quitBtn.setVisible(false);
                //sound 
                try {
                    audioInputStream = AudioSystem.getAudioInputStream(new File("./resources/music/menu_select.wav").getAbsoluteFile());
                } catch (UnsupportedAudioFileException ex) {
                    Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    clip = AudioSystem.getClip();
                } catch (LineUnavailableException ex) {
                    Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    clip.open(audioInputStream);
                } catch (LineUnavailableException ex) {
                    Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
                }
                clip.start();
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                quitBtn.setVisible(true);
            }
        });

        //Add event handling for start btn to show game/board screen
        quitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //souond for quit menu clicked
                try {
                    audioInputStream = AudioSystem.getAudioInputStream(new File("./resources/music/menu_quit.wav").getAbsoluteFile()); //LM
                } catch (UnsupportedAudioFileException ex) {
                    Logger.getLogger(MenuScreen.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MenuScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
                Clip clip = null;
                try {
                    clip = AudioSystem.getClip();
                } catch (LineUnavailableException ex) {
                    Logger.getLogger(MenuScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    clip.open(audioInputStream);
                } catch (LineUnavailableException ex) {
                    Logger.getLogger(MenuScreen.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MenuScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
                clip.start();
                System.exit(1);
            }
        });

        jFrame.setVisible(true); //Show our main JFrame
    }

    /**plays game music on a loop
     * 
     * @throws LineUnavailableException
     * @throws IOException
     * @throws UnsupportedAudioFileException 
     */
    public void playGameMusic() throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        audioInputStream = null;
        audioInputStream = AudioSystem.getAudioInputStream(new File("./resources/music/game.wav").getAbsoluteFile());
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);

        Runnable helloRunnable = new Runnable() {
            /**
             * inner class to be run on a loop
             */
            public void run() {
                try {
                    if (isMusicOn) {
                        audioInputStream = AudioSystem.getAudioInputStream(new File("./resources/music/game.wav").getAbsoluteFile());
                        clip = AudioSystem.getClip();
                        clip.open(audioInputStream);
                        clip.start();
                    }
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                    Logger.getLogger(MenuScreen.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        audioInputStream.close();
                    } catch (IOException ex) {
                        Logger.getLogger(MenuScreen.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(helloRunnable, 0, 28, TimeUnit.SECONDS);
    }
}


//rollback 1 gif animation working including player 
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author kirachen
 */
public class Board extends JPanel implements ActionListener {

    /**
     * Creates new form Board
     *
     */
    public static ArrayList<Goal1> allGoals;
    public static int flag = 0;
    private boolean resetFlag = false;

    public static final char WALL = '#';
    public static final char CHARACTER = 'C';
    public static final char SPACE = '_';
    public static final char BLOCK = 'B';
    public static final char GOAL = 'G';

    // COG = Character on goal
    public static final char COG = '@';
    // BOG = Block on goal
    public static final char BOG = 'X';

    int isFirstTime = 0;
    char[][] map;
    ArrayList<char[][]> prevMoveMaps;
    private char[][] oldMap;
    private static boolean isChallenger;

    private Timer timer;
    private Player p;
    private final int DELAY = 10;
    private WarehouseBoss warehouseBoss;
    private int unit = 50;
    //screen sizes
    //int x = (getWidth() - unit*10) / 2;
    //int y = (getHeight() - unit*10) / 2;
    private static int x = 150;
    private static int y = 100;
    private boolean isComplete = false;
    private boolean playLvlCompleteAudio = true;
    private int count;
    private ArrayList<char[][]> mapList = gatherAllMaps();

    private JButton nextLvlBtn;
    private JButton resLvlBtn;
    private JButton quitLvlBtn;
    private JButton undoLvlBtn;
    private JLabel wasdImage;

    private static boolean isTutorial = true;
    //animation
    JLabel WASDlabel;
    JLabel playerLabel = new JLabel();
    ;
    ImageIcon imageIconPlayer = new ImageIcon(new ImageIcon("./resources/player_anim.gif").getImage().getScaledInstance(unit, unit, Image.SCALE_DEFAULT));

    //audio
    AudioInputStream audioInputStream;
    Clip clip;
    
    /**
     * Constructor for Board
     * @param warehouseBoss
     * @param isChallenger
     */
    public Board(WarehouseBoss warehouseBoss, boolean isChallenger) {
        addKeyListener(new TAdapter());
        setBackground(Color.BLACK);
        setFocusable(true);
        requestFocusInWindow();
        setVisible(true);

        this.warehouseBoss = warehouseBoss;
        this.isChallenger = isChallenger;

        timer = new Timer(DELAY, this);
        timer.start();

        map = tut1Map();
        oldMap = duplicateMap(map);
        this.warehouseBoss.setOldMap(oldMap);

        prevMoveMaps = new ArrayList<char[][]>();

        //GUI part 
        setLayout(null); //add our boxLayout to our jPanel

        nextLvlBtn = new JButton();
        ImageIcon imageIconContinue = new ImageIcon(new ImageIcon("./resources/Image11.png").getImage().getScaledInstance(250, 250, Image.SCALE_DEFAULT));
        nextLvlBtn.setIcon(imageIconContinue);
        nextLvlBtn.setBorder(BorderFactory.createEmptyBorder());
        add(nextLvlBtn);
        nextLvlBtn.setVisible(false);

        quitLvlBtn = new JButton();
        ImageIcon imageIconQuit = new ImageIcon(new ImageIcon("./resources/quit_ingame.png").getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT));
        quitLvlBtn.setIcon(imageIconQuit);
        quitLvlBtn.setBorder(BorderFactory.createEmptyBorder());
        add(quitLvlBtn);

        resLvlBtn = new JButton();
        ImageIcon imageIconRestart = new ImageIcon(new ImageIcon("./resources/restart_ingame.png").getImage().getScaledInstance(150, 160, Image.SCALE_DEFAULT));
        resLvlBtn.setIcon(imageIconRestart);
        resLvlBtn.setBorder(BorderFactory.createEmptyBorder());
        add(resLvlBtn);

        undoLvlBtn = new JButton();
        ImageIcon imageIconUndo = new ImageIcon(new ImageIcon("./resources/undo.png").getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT));
        undoLvlBtn.setIcon(imageIconUndo);
        undoLvlBtn.setBorder(BorderFactory.createEmptyBorder());
        add(undoLvlBtn);

        WASDlabel = new JLabel();
        ImageIcon imageIconWASD = new ImageIcon(new ImageIcon("./resources/wasd_anim.gif").getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT));
        WASDlabel.setIcon(imageIconWASD);
        add(WASDlabel);
    }
    
    /**
     * returns a copy of a map
     * @param map
     * @return
     */
    public char[][] duplicateMap(char[][] map) {
        char[][] temp = new char[map.length][50]; //assume map cant be larger than 50 blocks
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                temp[i][j] = map[i][j];
            }
        }

        return temp;
    }
    
    /**
     * Adds all tutorial maps into a list
     * @return
     */
    public ArrayList<char[][]> gatherAllMaps() {
        char[][] tut1 = tut1Map();
        ArrayList<char[][]> list = new ArrayList<char[][]>();
        list.add(tut1);
        char[][] tut2 = tut2Map();
        list.add(tut2);
        char[][] tut3 = tut3Map();
        list.add(tut3);
        char[][] tut4 = tut4Map();
        list.add(tut4);
        return list;
    }

    @Override
    
    /**
     * Paints the component 
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            doDrawing(g);
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
        }

        Toolkit.getDefaultToolkit().sync();
    }
    
    /**
     * Converts the map array into pixels
     * @param g
     * @throws IOException
     * @throws UnsupportedAudioFileException
     * @throws LineUnavailableException
     */
    private void doDrawing(Graphics g) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        // if(resetFlag == true) {
        //    map = warehouseBoss.getOldMap();
        //    resetFlag = false;
        //}
        int u = 50;
        int numGoals = 0;
        boolean onGoal = false;
        boolean printAnimationOnce = true;

        Graphics2D g2d = (Graphics2D) g;
        if (allGoals != null && isFirstTime == 0) {
            for(Iterator<Goal1> iter = allGoals.iterator(); iter.hasNext();) {
                Goal1 g1 = iter.next();
                map[g1.x][g1.y] = 'G';
            }
            isFirstTime = 1;
            System.out.println(allGoals.size());
        }
        
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {

                if (map[i][j] == SPACE) {
                    Tile n = new Tile(u * i - 10, u * j);
                    g2d.drawImage(n.getImg(), n.getX() + x, n.getY() + y, unit, unit, this);
                } else if (map[i][j] == WALL) {
                    Wall w = new Wall(u * i - 10, u * j);
                    g2d.drawImage(w.getImg(), w.getX() + x, w.getY() + y, unit, unit, this);
                    //System.out.println("X: "+x);
                    //System.out.println("getWidth(): " + getWidth());

                    int x = (getWidth() - unit * 10) / 2;
                } else if (map[i][j] == GOAL) {
                    Goal goal = new Goal(u * i - 10, u * j);
                    g2d.drawImage(goal.getImg(), goal.getX() + x, goal.getY() + y, unit, unit, this);
                    if (allGoals == null) {
                        numGoals++;
                    } else {
                        numGoals = allGoals.size();
                    }
                } else if (map[i][j] == BLOCK) {
                    Box b = new Box(u * i - 10, u * j);
                    g2d.drawImage(b.getImg(), b.getX() + x, b.getY() + y, unit, unit, this);
                } else if (map[i][j] == 'X') {
                    /*audioInputStream = AudioSystem.getAudioInputStream(new File("./resources/music/crate_on_goal.wav").getAbsoluteFile());
                     clip = AudioSystem.getClip();
                     clip.open(audioInputStream);
                     clip.start(); */
                    onGoal o = new onGoal(u * i - 10, u * j);
                    //Goal goal = new Goal(u*i, u*j);
                    //g2d.drawImage(o.getImg(), o.getX(), o.getY(), this);
                    g2d.drawImage(o.getImg(), o.getX() + x, o.getY() + y, unit, unit, this);
                } else if (map[i][j] == 'C' || map[i][j] == '@') {
                    if (map[i][j] == '@') {
                        onGoal = true;
                    }
                    p = new Player(warehouseBoss, u * i, u * j);
                }
            }

        }
        //placing quit reset undo buttons on gameplay screen
        quitLvlBtn.setBounds(640, 480, 150, 150);
        resLvlBtn.setBounds(790, 480, 150, 150);
        undoLvlBtn.setBounds(940, 480, 150, 150);

        //player animation
        playerLabel.setIcon(imageIconPlayer);
        add(playerLabel);
        playerLabel.setBounds(p.getX() + 3 * unit - 10, p.getY() + 2 * unit, unit, unit);

        //show wasd only in tutorial levels
        if (isTutorial == true) { //Not tutorial 
            WASDlabel.setBounds(790, 350, 150, 150);
        } else {
            WASDlabel.setVisible(false);
        }

        if (numGoals == 0 && onGoal == false) {
            //==== AUDIO: level cleared ====
            isComplete = true;
            if (playLvlCompleteAudio == true) { //play this audio once.
                audioInputStream = AudioSystem.getAudioInputStream(new File("./resources/music/level_cleared.wav").getAbsoluteFile());
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
                playLvlCompleteAudio = false;
            }
            nextLvlBtn.setBounds(750, 80, 250, 250);
            nextLvlBtn.setVisible(true);    //Show "next level" prompt

            //if(resetFlag == true) {
            //map = mapList.get(mapList.size()-1);
            // resetFlag = false;
            //}
            //return;
            //timer.stop();
            // repaint();
            // warehouseBoss.showLevelCompleteScreen(warehouseBoss);
        }

        requestFocusInWindow();
    }
    /**
     * repaints map when a key is pressed 
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (flag == 0) {
            //printMap(map);
            flag++;
        }
        repaint();
    }
    
    /**
     * design for tutorial 1
     * @return
     */
    public char[][] tut1Map() {
        /* #########
        *  #CB    G#
        *  #########
         */
        char[][] tut1 = new char[9][3];
        for (int i = 0; i < tut1.length; i++) {
            for (int j = 0; j < tut1[i].length; j++) {
                if (i == 0 || i == tut1.length - 1 || j == 0 || j == tut1[i].length - 1) {
                    tut1[i][j] = WALL;
                } else {
                    tut1[i][j] = SPACE;
                }
            }
        }

        tut1[1][1] = CHARACTER;
        tut1[2][1] = BLOCK;
        tut1[7][1] = GOAL;
        return tut1;
    }
    
    /**
     * design for tut 2
     * @return
     */
    public char[][] tut2Map() {
        //#########
        //#G   B C#
        //####### #
        //      #B#
        //      # #
        //      # #
        //      # #
        //      # #
        //      #G#
        //      ###

        char[][] tut2 = new char[][]{
            {'#', '#', '#', '0', '0', '0', '0', '0', '0'},
            {'#', 'G', '#', '0', '0', '0', '0', '0', '0'},
            {'#', '_', '#', '0', '0', '0', '0', '0', '0'},
            {'#', '_', '#', '0', '0', '0', '0', '0', '0'},
            {'#', '_', '#', '0', '0', '0', '0', '0', '0'},
            {'#', '_', '#', '0', '0', '0', '0', '0', '0'},
            {'#', 'B', '#', '#', '#', '#', '#', '#', '#'},
            {'#', 'C', 'B', '_', '_', '_', '_', 'G', '#'},
            {'#', '#', '#', '#', '#', '#', '#', '#', '#'}
        };

        //printMap(tut2);
        return tut2;

    }
    
    /**
     * design for tutorial 3
     * @return
     */
    public char[][] tut3Map() {
        char[][] tut3 = new char[][]{
            {'0', '0', '0', '0', '0', '0', '#', '#', '#'},
            {'0', '0', '0', '0', '0', '0', '#', 'C', '#'},
            {'#', '#', '#', '#', '#', '#', '#', '_', '#'},
            {'#', 'G', '_', '_', '_', '_', 'B', '_', '#'},
            {'#', '#', '#', '#', '#', '#', '#', '#', '#'},
            {'0', '0', '0', '0', '0', '0', '0', '0', '0'},
            {'0', '0', '0', '0', '0', '0', '0', '0', '0'},
            {'0', '0', '0', '0', '0', '0', '0', '0', '0'},};

        return tut3;
    }
    
    /**
     * design for tutorial 4
     * @return
     */
    public char[][] tut4Map() {
        char[][] tut4 = new char[][] {
            {'#', '#', '#', '#', '0', '#', '#', '#', '#'},
            {'#', '_', '_', '#', '0', '#', '_', '_', '#'},
            {'#', 'B', '_', '#', '#', '#', '_', '_', '#'},
            {'#', 'G', '_', '_', '_', 'G', 'B', 'C', '#'},
            {'#', '#', '#', '#', '#', '#', '_', '_', '#'},
            {'0', '0', '0', '0', '0', '#', '_', '_', '#'},
            {'0', '0', '0', '0', '0', '#', '#', '#', '#'},
            {'0', '0', '0', '0', '0', '0', '0', '0', '0'},};
    
        return tut4;
    }
    
    /**
     * helper fucntiion for printing out current map into terminal
     * @param map
     */
    public void printMap(char[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
    }
    
    /**
     * Controller in swing. reads user input
     * @author Fabrice Samonte
     *
     */
    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            //presses quit "q" button
            if (key == KeyEvent.VK_Q) {
                allGoals = null;
                try {
                    audioInputStream = AudioSystem.getAudioInputStream(new File("./resources/music/ingame_quit.wav").getAbsoluteFile());
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
                System.out.println("IM QUITTING");

                //goes back to main menu
                setVisible(false);
                //warehouseBoss.getEx().setVisible(false);
                warehouseBoss.getMenuScreen().requestFocusInWindow();
                warehouseBoss.getMenuScreen().setVisible(true);
                isFirstTime = 0;
            }

            if (key == KeyEvent.VK_R) {
                if (!isComplete) { //dont reset when game is complete
                    resetFlag = true;
                    map = duplicateMap(warehouseBoss.getOldMap());

                    //audio for reset
                    try {
                        audioInputStream = AudioSystem.getAudioInputStream(new File("./resources/music/restart.wav").getAbsoluteFile());
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
                    isFirstTime = 0;
                    return;
                }
            }

            if (key == KeyEvent.VK_U) {
                if (!isComplete) { //dont undo when game is complete
                    if (prevMoveMaps.size() > 0) {
                        map = duplicateMap(prevMoveMaps.get(prevMoveMaps.size() - 1));
                        prevMoveMaps.remove(prevMoveMaps.size() - 1);
                    } 
                    return;
                }
            }

            //presses "e" button, continue to next level
            if (key == KeyEvent.VK_E) {
                //isComplete = true;
                if (isComplete) {
                    nextLvlBtn.setVisible(false);    //Show "next level" prompt
                    playLvlCompleteAudio = true; //resets flag to play music next round
                    isComplete = false; //resets flag for next round

                    count++;
                    if (count >= mapList.size()) { //Not tutorial 
                        map = generator2();
                        warehouseBoss.setOldMap(duplicateMap(map));
                        mapList.add(oldMap);
                        prevMoveMaps.clear();
                    } else { //is a tutorial level
                        map = mapList.get(count);
                        warehouseBoss.setOldMap(duplicateMap(map));
                        System.out.println(count);
                        prevMoveMaps.clear();
                    }
                    isFirstTime = 0;
                }
            }
            /*if (key == KeyEvent."R") {
             AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("./resources/music/level_cleared.wav").getAbsoluteFile());
             Clip clip = AudioSystem.getClip();
             clip.open(audioInputStream);
             clip.start(); 
             } */
            if (key == KeyEvent.VK_ESCAPE) {
                System.exit(1);
            }

            //Process other keystrokes
            try {
                char[][] tempMap = duplicateMap(map); //For 'Undo', save curr Map
                boolean saveMap = p.keyPressed(e, map); //change curr map if necessary

                //if curr map was changed, save the prev map (tempMap)
                if (saveMap) {
                    prevMoveMaps.add(duplicateMap(tempMap));
                }
            } catch (LineUnavailableException | IOException | UnsupportedAudioFileException ex) {
                Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
            }

            requestFocusInWindow();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    /*public static char[][] generateMap() {
        char[][] map = new char[9][9];

        for (int i = 0; i < 9; i++) {
            map[0][i] = '#';
            map[8][i] = '#';
            map[i][0] = '#';
            map[i][8] = '#';
        }

        ArrayList<Integer> shuffler = new ArrayList<Integer>();
        for (int i = 0; i < 100; i++) {
            if (i < 50) {
                shuffler.add(0);
            } else {
                shuffler.add(1);
            }
        }

        int noOfGoals = getRandom(3, 4);
        ArrayList<Goal1> goals = new ArrayList<Goal1>();

        for (int i = 0; i < noOfGoals; i++) {
            int randX = getRandom(1, 7);
            int randY = getRandom(1, 7);
            map[randX][randY] = 'G';
            Goal1 newGoal = new Goal1();
            newGoal.x = randX;
            newGoal.y = randY;
            goals.add(newGoal);
        }

        for (Goal1 elem : goals) {
            int flag = 0;
            int goalX = elem.x;
            int goalY = elem.y;

            Collections.shuffle(shuffler);

            int vertOrHori = shuffler.get(0);
            int toMove = 0;
            int steps = getRandom(3, 20);

            while (toMove == 0) {
                toMove = getRandom(-1, 1);
            }
            for (int j = 0; j < steps; j++) {
                if (vertOrHori == 0) {
                    if (goalX + toMove > 7 || goalX + toMove < 2 || map[goalX + toMove][goalY] == 'G' || map[goalX + toMove][goalY] == 'B' || map[goalX + 2 * toMove][goalY] == '#') {
                        continue;
                    }
                    goalX += toMove;
                    flag = 0;
                } else {
                    if (goalY + toMove > 7 || goalY + toMove < 2 || map[goalX][goalY + toMove] == 'G' || map[goalX][goalY + toMove] == 'B' || map[goalX][goalY + 2 * toMove] == '#') {
                        continue;
                    }
                    goalY += toMove;
                    flag = 1;
                }
                map[goalX][goalY] = 'M';
            }
            map[goalX][goalY] = 'B';
            if (elem == goals.get(goals.size() - 1)) {
                if (flag == 0) {
                    map[goalX + toMove][goalY] = 'C';
                } else {
                    map[goalX][goalY + toMove] = 'C';
                }
            }

        }

        for (int i = 0; i < 20; i++) {
            int randX = getRandom(1, 7);
            int randY = getRandom(1, 7);
            if (map[randX][randY] != 'C' && map[randX + 1][randY] != 'C' && map[randX - 1][randY] != 'C' && map[randX][randY + 1] != 'C' && map[randX][randY - 1] != 'C' && map[randX][randY] != 'B' && map[randX + 1][randY] != 'B' && map[randX - 1][randY] != 'B' && map[randX][randY + 1] != 'B'
                    && map[randX][randY - 1] != 'B' && map[randX][randY - 1] != 'M' && map[randX][randY + 1] != 'M' && map[randX - 1][randY] != 'M' && map[randX][randY] != 'M'
                    && map[randX + 1][randY] != 'M' && map[randX][randY] != 'G' && map[randX][randY] != '#') {
                map[randX][randY] = '#';
            }
        }

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (map[i][j] == 0 || map[i][j] == 'M') {
                    map[i][j] = '_';
                }
            }
        }

        return map;
    }*/
    /**
     * Generates random map
     * @return
     */
    public static char[][] generator2() {
        isTutorial = false;
        char[][] puzzle = new char[10][10];
        while (true) {
            puzzle = new char[10][10];
            ArrayList<Goal1> goals = new ArrayList<Goal1>();
            for (int i = 0; i < 10; i++) {
                puzzle[0][i] = '#';
                puzzle[9][i] = '#';
                puzzle[i][0] = '#';
                puzzle[i][9] = '#';
            }
            
            int noOfGoals = getRandom(6, 10);
            if(isChallenger) {
                noOfGoals = getRandom(15, 15);
            }
            
            for (int i = 0; i < noOfGoals; i++) {
                int randX = getRandom(2, 7);
                int randY = getRandom(2, 7);
                puzzle[randX][randY] = 'G';
                Goal1 goal = new Goal1();
                goal.x = randX;
                goal.y = randY;
                if (!goalExists(goals, goal)) {
                    goals.add(goal);
                } else {
                    i--;
                }
            }
            int toMove = 0;
            for (Goal1 g : goals) {

                int goalX = g.x;
                int goalY = g.y;

                int stepsToGo = getRandom(20, 20);

                for (int i = 0; i < stepsToGo; i++) {
                    flag = 0;
                    int whoToMove = getRandom(0, 1);
                    toMove = 0;
                    while (toMove == 0) {
                        toMove = getRandom(-1, 1);
                    }
                    if (whoToMove == 0) {
                        if (goalX + toMove > 8 || goalX + toMove < 2 || puzzle[goalX + toMove][goalY] == 'G' || puzzle[goalX + toMove][goalY] == 'B' || puzzle[goalX + 2 * toMove][goalY] == '#') {
                            continue;
                        }
                        flag = 0;
                        goalX += toMove;
                    } else {
                        if (goalY + toMove > 8 || goalY + toMove < 2 || puzzle[goalX][goalY + toMove] == 'G' || puzzle[goalX][goalY + toMove] == 'B' || puzzle[goalX][goalY + 2 * toMove] == '#') {
                            continue;
                        }
                        goalY += toMove;
                        flag = 1;
                    }
                    puzzle[goalX][goalY] = 'M';
                }
                puzzle[goalX][goalY] = 'B';
                /*if(g == goals.get(goals.size()-1)){
                if(flag ==0){
                    puzzle[goalX+ toMove][goalY] = 'C';
                }else {
                    puzzle[goalX][goalY+toMove] = 'C';
                }
            }*/

            }

            for (int i = 0; i < 1000; i++) {
                int randX = getRandom(1, 8);
                int randY = getRandom(1, 8);
                if (buildableWall(puzzle, randX, randY)) {
                    puzzle[randX][randY] = '#';
                }
            }

            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (puzzle[i][j] == 0 || puzzle[i][j] == 'M') {
                        puzzle[i][j] = '_';
                    }
                }
            }

            while (true) {
                int randX = getRandom(2, 8);
                int randY = getRandom(2, 8);
                if (puzzle[randX][randY] == '_') {
                    puzzle[randX][randY] = 'C';
                    break;
                }
            }
            int checkGoals = 0;
            int checkBox = 0;
            for (int x = 0; x < 9; x++) {
                for (int y = 0; y < 9; y++) {
                    if (puzzle[x][y] == 'B') {
                        checkBox++;
                    }
                    if (puzzle[x][y] == 'G') {
                        checkGoals++;
                    }
                }
            }
            if (checkGoals == checkBox) {
                allGoals = new ArrayList<Goal1>();
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {
                        if (puzzle[i][j] == '#' || puzzle[i][j] == '_' || puzzle[i][j] == 'C' || puzzle[i][j] == 'G') {
                            //ignore
                            if (puzzle[i][j] == 'G' ) {
                                System.out.println("Goal is in x: " + i + " y: " + j);
                                Goal1 tempGoal = new Goal1();
                                tempGoal.x = i;
                                tempGoal.y = j;
                                allGoals.add(tempGoal);
                            }
                        } else if (puzzle[i][j] == 'B') {
                            int top1x = i;
                            int top1y = j;
                            
                            int top2x = i+1;
                            int top2y = j;
                            
                            int bottom1x = i;
                            int bottom1y = j+1;
                            
                            int bottom2x = i+1;
                            int bottom2y = j+1;
                            
                            //System.out.println(top1x + " " + top1y + " and " + top2x + " " + top2y);
                            //System.out.println(bottom1x + " " + bottom1y + " and " + bottom2x + " " + bottom2y);
                            //System.out.println(top1x + " this is x pos and " + top1y + " this is y pos.");
                            if (puzzle[top1x][top1y] == 'B' && puzzle[top2x][top2y] == 'B' && puzzle[bottom1x][bottom1y] == 'B' && puzzle[bottom2x][bottom2y] == 'B' && !threeByThreeCase(puzzle)) {
                                System.out.println("UNSOLVEABLE!");
                                puzzle = generator2();
                                return puzzle;
                                //break;
                            }
                            
                        }
                    }
                }
                return puzzle;
            }
        }

    }
    
    /**
     * Checks for 3x3 case
     * @param map
     * @return
     */
    public static boolean threeByThreeCase(char[][] map) {

        for(int i = 1; i < 7; i++) {

            for(int j = 2; j < 8; j++ ){

                if(map[i][j] == 'B' && map[i+1][j+1] == 'B' && map[i+1][j-1] == 'B' && map[i+2][j] == 'B') {
                    if((map[i][j+1] == 'B' || map[i][j-1] == 'B' || map[i+2][j+1] == 'B' || map[i+2][j+1] == 'B')){
                       return true;
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * generates random number in a given nterval
     * @param min
     * @param max
     * @return
     */
    public static int getRandom(int min, int max) {
        ArrayList<Integer> ints = new ArrayList<Integer>();
        for (int i = min; i <= max; i++) {
            ints.add(i);
        }

        Collections.shuffle(ints);
        return ints.get(0);
    }
    
    /**
     * checks if goal exists inside a map
     * @param goals
     * @param newGoal
     * @return
     */
    public static boolean goalExists(ArrayList<Goal1> goals, Goal1 newGoal) {
        for (Goal1 g : goals) {
            if (newGoal.x == g.x && newGoal.y == g.y) {
                return true;
            }
        }

        return false;
    }
    
    /**
     * Under conditions, attempts to fill map with walls
     * @param puzzle
     * @param randX
     * @param randY
     * @return
     */
    public static boolean buildableWall(char[][] puzzle, int randX, int randY) {
        if (puzzle[randX][randY] != 'B' && puzzle[randX + 1][randY] != 'B' && puzzle[randX - 1][randY] != 'B' && puzzle[randX][randY + 1] != 'B' && puzzle[randX][randY - 1] != 'B'
                && puzzle[randX][randY - 1] != 'M' && puzzle[randX][randY + 1] != 'M' && puzzle[randX - 1][randY] != 'M' && puzzle[randX][randY] != 'M' && puzzle[randX + 1][randY] != 'M'
                && puzzle[randX][randY] != 'G' && puzzle[randX][randY] != '#' && puzzle[randX + 1][randY + 1] != 'B' && puzzle[randX + 1][randY - 1] != 'B' && puzzle[randX - 1][randY - 1] != 'B' && puzzle[randX][randY + 1] != 'B' && puzzle[randX + 1][randY - 1] != 'B') {
            return true;
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>                        

    // Variables declaration - do not modify                     
    // End of variables declaration                   
}

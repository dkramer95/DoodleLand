package com.dkramer.states;

import com.dkramer.input.Keys;
import com.dkramer.main.GamePanel;
import com.dkramer.main.Main;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Base class for all states. All states must draw themselves to the screen
 * and update in some way. All states will share the same graphics buffer
 * that is contained in this class, to draw on. States are contained in
 * the GamePanel, which all subclasses will also have access too. This
 * class must be initialized with the gamePanel, before any other states
 * can be constructed, otherwise there will be no graphics to draw onto,
 * which will cause NullPointerExceptions to be thrown.
 * Created by David Kramer on 2/21/2016.
 */

/*********************
 * OOP: Superclass
Super class of all states (which is also abstract).
 *********************/

public abstract class State {
    protected static GamePanel gamePanel;       // graphics panel container
    protected static Rectangle bounds;
    protected static boolean initialized;

    // state graphics drawing
    protected static BufferedImage buffer;
    protected static Graphics2D g2d;




    public State() {
        init();
    }

    /**
     * Initializes all states, with a sized out image graphics buffer,
     * so that rendering can take place.
     * @param _gamePanel - GamePanel to initialize with
     * @return true if successful
     */
    public static boolean init(GamePanel _gamePanel) {
        if (_gamePanel != null) {
            int width = _gamePanel.getWidth();
            int height = _gamePanel.getHeight();
            gamePanel = _gamePanel;

            if (width > 0 && height > 0) {
                bounds = new Rectangle(0, 0, width, height);
                buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                g2d = buffer.createGraphics();
                initialized = true;
            }
        }
        return initialized;
    }

    /**
     * @param fileDir file directory
     * @return array of files, contained in the file directory
     */
    public static File[] getFiles(String fileDir) {
        File folder = new File(fileDir);
        File[] filesInDir = null;
        System.out.println("folder list: " + folder.listFiles());
        if (folder.isDirectory()) {
            filesInDir = folder.listFiles();
        } else {
            System.out.println("FOLDER NOT DIR!");
        }
        return filesInDir;
    }

    /////////////////////////////////////////////////////////////////////
    //////////////////////   ABSTRACT METHODS   /////////////////////////
    /////////////////////////////////////////////////////////////////////


    public abstract void init();
    public abstract void render();
    public abstract void update();
    public abstract void handleInput();


    public static BufferedImage getBuffer() {
        return buffer;
    }

    public static Graphics2D getGraphics() {
        return g2d;
    }

    public static Rectangle getBounds() {
        return bounds.getBounds();
    }

    public static int getWidth() {
        return bounds.width;
    }

    public static int getHeight() {
        return bounds.height;
    }
}

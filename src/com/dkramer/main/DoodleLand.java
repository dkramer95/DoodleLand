package com.dkramer.main;

import javax.swing.*;
import java.awt.*;

/**
 * This class contains the GamePanel inside of the main window.
 * Created by David Kramer on 2/21/2016.
 */
public class DoodleLand extends JFrame {
    public static final String TITLE = "DoodleLand by David Kramer";
    public static final Dimension SIZE = new Dimension(1280, 720);

    /*********************
     * OOP: GUI Component
     GamePanel (extended from JPanel) is the main graphics component
     that allows the GUI to be displayed.
     *********************/

    private GamePanel gamePanel;


    public DoodleLand() {
        createGamePanel();
        createAndShowGUI();
    }

    private void createGamePanel() {
        gamePanel = new GamePanel(this);
        add(gamePanel);
    }


    private void createAndShowGUI() {
        setTitle(TITLE);
        setSize(SIZE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

        gamePanel.init();   // init here, since it will have been sized properly!
    }
}

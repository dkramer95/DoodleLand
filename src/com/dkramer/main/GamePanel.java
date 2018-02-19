package com.dkramer.main;

import com.dkramer.input.Keys;
import com.dkramer.input.Mouse;
import com.dkramer.managers.GameManager;
import com.dkramer.objects.Player;
import com.dkramer.states.State;
import com.dkramer.worlds.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * This class contains the active state of the game and renders it
 * to the screen. This also listens for various mouse and key inputs
 * and updates the state manager accordingly.
 * Created by David Kramer on 2/21/2016.
 */
public class GamePanel extends JPanel implements Runnable, KeyListener, MouseListener,
        MouseMotionListener, MouseWheelListener {

    private static final int FPS = 60;
    private static final int TARGET_TIME = 1000 / FPS;

    // game loop stuff
    private Thread thread;
    private boolean running;
    private DoodleLand app;




    public GamePanel(DoodleLand app) {
        this.app = app;
    }

    /**
     * Initializes all the components needed for this GamePanel and
     * starts the thread.
     */
    public void init() {
        World.init(new Player());
        State.init(this);
        GameManager.init();

        // add inputs
        addKeyboard();
        addMouse();

        // start the game loop!
        thread = new Thread(this);
        thread.start();
    }

    /*********************
     * OOP: GUI Listener
     Keyboard and mouse listeners are listed below.
     *********************/


    /**
     * Adds KeyListener to this GamePanel.
     */
    private void addKeyboard() {
        addKeyListener(this);
        setFocusable(true);
        requestFocus();
    }

    /**
     * Adds MouseListeners to this GamePanel.
     */
    private void addMouse() {
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
    }

    /**
     * Main GameLoop. Actively updates and renders the current state.
     */
    public void run() {
        running = true;

        long start;
        long elapsed;
        long wait;

        int frames = 0;
        long timer = System.currentTimeMillis();

        while (running) {
            start = System.nanoTime();
            update();
            render();
            repaint();
            frames++;

            elapsed = System.nanoTime() - start;

            // fps counter
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                app.setTitle(DoodleLand.TITLE + "  |  FPS: " + frames);
                frames = 0;
            }

            wait = TARGET_TIME - elapsed / 1000000;
            if (wait < 0) {
                wait = TARGET_TIME;
            }

            try {
                Thread.sleep(wait);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Updates the GameManager, and inputs.
     */
    public void update() {
        GameManager.update();
        Keys.update();
        Mouse.update();
    }

    /**
     * Renders the active state to the screen.
     */
    public void render() {
        GameManager.render();
    }

    /**
     * Draws the states gfx buffer.
     * @param g
     */
    public void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(State.getBuffer(), 0, 0, null);
        g.dispose();
    }

    // Keyboard Input

    /*********************
     * OOP: GUI Event
     Keyboard and mouse input events are below.
     *********************/


    public void keyPressed(KeyEvent e) {
//        Keys.clear();
        Keys.setKey(e.getKeyCode(), true, KeyEvent.KEY_PRESSED);
        Keys.setLastKeyChar(e.getKeyChar());
        GameManager.handleInput();
    }

    public void keyReleased(KeyEvent e) {
        Keys.setKey(e.getKeyCode(), false, KeyEvent.KEY_RELEASED);
    }

    public void keyTyped(KeyEvent e) {
        Keys.setKey(e.getKeyCode(), true, KeyEvent.KEY_TYPED);
    }

    // Mouse Input

    public void mouseDragged(MouseEvent e) {
        Mouse.setEvent(Mouse.DRAGGED, e);
    }

    public void mouseMoved(MouseEvent e) {
        Mouse.setEvent(Mouse.MOVED, e);
    }

    public void mouseClicked(MouseEvent e) {
        Mouse.setEvent(Mouse.CLICKED, e);
    }

    public void mouseReleased(MouseEvent e) {
        Mouse.setEvent(Mouse.RELEASED, e);
    }

    public void mousePressed(MouseEvent e) {
        Mouse.setEvent(Mouse.PRESSED, e);
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        Mouse.setEvent(Mouse.SCROLLED, e);
        Mouse.setExtendedEvent(e);
    }

    // unused input methods
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

}

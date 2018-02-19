package com.dkramer.input;

import java.awt.event.KeyEvent;

/**
 * This class keeps tracks of any keys that are used in the game. Keys
 * that are part of this game, are stored in an array and updated
 * according to KeyPressed and KeyReleased events. GameStates can access
 * this class, globally to check for certain keys, and update accordingly.
 * Multiple keys can be pressed, which allows for finer movement and interaction
 * where applicable.
 * Created by David Kramer on 2/21/2016.
 */
public class Keys {

    public static final int NUM_KEYS     = 250; // buffer size of keys array

    private static boolean[] activeKeys  = new boolean[NUM_KEYS];
    private static boolean[] prevKeys    = new boolean[NUM_KEYS];

    // default starting values
    private static int lastEvent         = KeyEvent.KEY_RELEASED;
    private static int curEvent          = KeyEvent.KEY_RELEASED;
    private static boolean stateChanged  = false;   // has the state of Keys, been changed at all?
    private static char lastKeyChar      = 'a';

    /**
     * Game Movement key constants
     */
    public static final int LEFT    = KeyEvent.VK_LEFT;
    public static final int RIGHT   = KeyEvent.VK_RIGHT;
    public static final int UP      = KeyEvent.VK_UP;
    public static final int DOWN    = KeyEvent.VK_DOWN;
    public static final int JUMP    = KeyEvent.VK_SPACE;
    public static final int SHOOT   = KeyEvent.VK_J;

    /**
     * Other Game control key constants
     */
    public static final int CLEAR   = KeyEvent.VK_DELETE;
    public static final int PAUSE   = KeyEvent.VK_ESCAPE;
    public static final int ENTER   = KeyEvent.VK_ENTER;

    /**
     * Sets the specified key type, with a boolean state value
     * @param keyCode - KeyEvent keycode
     * @param state - state of key event
     * @param eventType - type of key event
     */
    public static void setKey(int keyCode, boolean state, int eventType) {
        // check for game keys, that have multiple keys assigned!
        if (keyCode < NUM_KEYS) {   // protect against keys outside base index range, as we don't need them!
            stateChanged = true;
            switch (keyCode) {
                case KeyEvent.VK_A:
                case KeyEvent.VK_LEFT:
                    activeKeys[LEFT] = state;
                    break;
                case KeyEvent.VK_D:
                case KeyEvent.VK_RIGHT:
                    activeKeys[RIGHT] = state;
                    break;
                case KeyEvent.VK_W:
                case KeyEvent.VK_UP:
                    activeKeys[UP] = state;
                    break;
                case KeyEvent.VK_S:
                case KeyEvent.VK_DOWN:
                    activeKeys[DOWN] = state;
                    break;
            }
            curEvent = eventType;
            activeKeys[keyCode] = state;
        }
    }

    /**
     * Updates the state of all keys.
     */
    public static void update() {
        if (stateChanged && lastEvent != curEvent) {
            System.arraycopy(activeKeys, 0, prevKeys, 0, NUM_KEYS);
            lastEvent = curEvent;
            stateChanged = false;
        }
    }

    /**
     * Sets all key states to false.
     */
    public static void clear() {
        for (int i = 0; i < activeKeys.length; i++) {
            activeKeys[i] = false;
        }
        curEvent = KeyEvent.KEY_RELEASED;
    }

    /**
     * Checks to see if a particular key is down.
     * @param key - key to check
     * @return true if specified key is down
     */
    public static boolean isKeyDown(int key) {
        return activeKeys[key];
    }

    public static int getLastEvent() {
        return lastEvent;
    }

    public static boolean isStateChanged() {
        return stateChanged;
    }

    public static void setLastKeyChar(char c) {
        lastKeyChar = c;
    }

    public static char getLastKeyChar() {
        return lastKeyChar;
    }
}

package com.dkramer.states;

import com.dkramer.input.Keys;
import com.dkramer.managers.GameManager;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by David Kramer on 3/2/2016.
 */
public class HelpState extends State {

    public void init() {}

    public void render() {
        g2d.setColor(Color.RED);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    public void update() {}

    public void handleInput() {
        if (Keys.isKeyDown(KeyEvent.VK_ESCAPE)) {
            TransitionState state = new TransitionState(GameManager.INTRO_STATE);
        }
    }
}

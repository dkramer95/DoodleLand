package com.dkramer.states;

/**
 * Created by David Kramer on 2/29/2016.
 */
public class TestTransition extends State {
    private int x;
    private float xVel = 0.02f;

    public void init() {

    }

    @Override
    public void render() {
    }

    @Override
    public void update() {
        // WIPE TRANSITION
        xVel += 0.01f;
        x += xVel;
        g2d.translate(-x, 0);
    }

    @Override
    public void handleInput() {

    }
}

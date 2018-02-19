package com.dkramer.states;

import com.dkramer.managers.GameManager;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * TODO MAKE THIS ABSTRACT, AND ONLY IMPLEMENT THE RENDER AND UPDATE METHODS.
 * // CREATE A FACTORY CLASS TO CREATE VARIOUS DIFFERENT TRANSITION TYPES
 * Created by David Kramer on 2/29/2016.
 */
public class TransitionState extends State {
    private int nextStateType;
    int alpha = 0;

    public TransitionState(int nextStateType) {
        this.nextStateType = nextStateType;
    }

    public void init() {}

    public void render() {
        g2d.setColor(new Color(alpha, alpha, alpha, alpha));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.translate(alpha / 10, alpha / 10);
        g2d.rotate(alpha / 100);
    }

    public void update() {
        alpha ++;
        if (alpha >= 255) {
            alpha = 255;
            GameManager.setState(nextStateType);
            // clear out previous transforms!
            g2d.setTransform(new AffineTransform());
        }
    }

    public void setNextStateType(int stateType) {
        this.nextStateType = stateType;
    }

    public void reset() {
        alpha = 0;
    }

    public void handleInput() {}
}

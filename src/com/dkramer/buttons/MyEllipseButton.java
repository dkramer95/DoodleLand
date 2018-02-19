package com.dkramer.buttons;

import com.dkramer.input.Keys;
import com.dkramer.utils.ObstacleBuilder;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;

/**
 * Ellipse-construction implementation of button.
 * Created by David Kramer on 3/7/2016.
 */
public class MyEllipseButton extends MyButton implements ObstacleBuilder {
    private static final String BTN_TEXT = "Ellipse";

    private Ellipse2D.Double ellipse = new Ellipse2D.Double();




    public MyEllipseButton(int x, int y) {
        super(x, y);
        this.text = BTN_TEXT;
    }

    public void handleInput() {
        updateShape();
    }

    private void updateShape() {
        ellipse.x       = editor.getStartPt().x;
        ellipse.y       = editor.getStartPt().y;
        ellipse.width   = editor.getConstructionWidth();
        if (Keys.isKeyDown(KeyEvent.VK_SHIFT)) {
            ellipse.height = ellipse.width;
        } else {
            ellipse.height  = editor.getConstructionHeight();
        }
        editor.setConstructionShape(ellipse);
    }

    public Shape getShape() {
        return (Ellipse2D.Double)ellipse.clone();
    }
}

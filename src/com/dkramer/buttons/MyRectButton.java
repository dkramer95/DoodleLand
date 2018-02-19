package com.dkramer.buttons;

import com.dkramer.input.Keys;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Rectangle-construction implementation of button.
 * Created by David Kramer on 3/7/2016.
 */
public class MyRectButton extends MyButton {
    private static final String BTN_TEXT = "Rectangle";
    private Rectangle rect = new Rectangle();




    public MyRectButton(int x, int y) {
        super(x, y);
        this.text = BTN_TEXT;
    }

    public void handleInput() {
        updateShape();
    }

    private void updateShape() {
        rect.x      = editor.getStartPt().x;
        rect.y      = editor.getStartPt().y;
        rect.width  = editor.getConstructionWidth();
        if (Keys.isKeyDown(KeyEvent.VK_SHIFT)) { // constrain
            rect.height = rect.width;
        } else {
            rect.height = editor.getConstructionHeight();
        }
        editor.setConstructionShape(rect);
    }

    public Shape getShape() {
        return (Rectangle)rect.clone();
    }
}

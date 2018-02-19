package com.dkramer.buttons;

import com.dkramer.input.Keys;
import com.dkramer.input.Mouse;
import com.dkramer.objects.TextObstacle;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by David Kramer on 3/7/2016.
 */
public class MyTextButton extends MyButton {
    private static final String BTN_TEXT = "Text";
    private static final int MAX_FONTSIZE = 140;
    private static final int MIN_FONTSIZE = 20;

    // font choices
    private static String[] fonts = {
            "Courier New", "Trebuchet MS", "Verdana", "Times New Roman", "Arial"
    };

    private int curFontIndex = 0;
    private String curFont = fonts[curFontIndex];

    private String shapeText;
    private Font textShapeFont; // font that makes up shape;
    private TextObstacle obstacle;
    private Point location;
    private int fontSize = MIN_FONTSIZE;



    public MyTextButton(int x, int y) {
        super(x, y);
        this.text = BTN_TEXT;
        init();
    }

    private void init() {
        shapeText = " ";
        textShapeFont = new Font(curFont, Font.BOLD, fontSize);
        location = new Point();
        obstacle = new TextObstacle(shapeText, textShapeFont);
    }

    public void handleInput() {
        handleKeyboard();
        handleMouse();
    }

    private void handleKeyboard() {
        if (Keys.isStateChanged() && Keys.getLastEvent() == KeyEvent.KEY_TYPED) {
            if (Keys.getLastKeyChar() == '\b') {    // backspace
                if (shapeText.length() -1 > 0) {
                    shapeText = shapeText.substring(0, shapeText.length() - 1);
                } else {
                    shapeText = " ";
                }
            } else {
                shapeText += Keys.getLastKeyChar();
            }
            Keys.clear();
            updateShape();
        }
    }

    private void handleMouse() {
        boolean shouldUpdate = false;
        if (Mouse.getActiveEvent() == Mouse.DRAGGED) {
            location = Mouse.getCurPt();
            shouldUpdate = true;
        }
        if (Mouse.getActiveEvent() == Mouse.SCROLLED) {
            fontSize += Mouse.getScrollCount() * -3;    // negative to flip scroll direction
            if (Keys.isKeyDown(KeyEvent.VK_CONTROL)) {
                changeFont();
            } else {
                if (fontSize > MAX_FONTSIZE) {
                    fontSize = MAX_FONTSIZE;
                } else if (fontSize < MIN_FONTSIZE) {
                    fontSize = MIN_FONTSIZE;
                    textShapeFont = new Font(curFont, Font.BOLD, fontSize);
                }
            }
            shouldUpdate = true;
            updateFont();
        }

        if (shouldUpdate) {
            updateShape();
        }
    }

    private void changeFont() {
        curFontIndex += Mouse.getScrollCount() * -1;
        if (curFontIndex >= fonts.length) {
            curFontIndex = 0;
        } else if (curFontIndex < 0) {
            curFontIndex = fonts.length - 1;
        }
        curFont = fonts[curFontIndex];
    }

    public void resetShape() {
        super.resetShape();
        shapeText = " ";
        textShapeFont = new Font("Courier New", Font.BOLD, MIN_FONTSIZE);
    }

    private void updateFont() {
        textShapeFont = new Font(curFont, Font.BOLD, fontSize);
    }

    private void updateShape() {
        obstacle = new TextObstacle(shapeText, textShapeFont);
        obstacle.setLocation(location);
        editor.setConstructionShape(obstacle.getShape());
    }

    public Shape getShape() {
        return obstacle.getShape();
    }
}

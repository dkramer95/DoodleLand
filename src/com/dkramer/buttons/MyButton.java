package com.dkramer.buttons;

import com.dkramer.input.Mouse;
import com.dkramer.objects.GameObject;
import com.dkramer.states.EditorState;

import java.awt.*;

/**
 * Base class for buttons. Provides basic behavior such as hover
 * detection, and drawing to the screen. Subclasses are responsible
 * for handling input, if the state of this button is active, determined
 * by the EditorState, that these buttons will belong too.
 * Created by David Kramer on 3/6/2016.
 */
public abstract class MyButton extends GameObject {
    // button decorations
    private static final Font FONT = new Font("Courier New", Font.BOLD, 12);
    private static final Color COLOR = new Color(0, 240, 10, 150);
    private static final Color BG_COLOR = new Color(50, 50, 50, 10);
    private static final Color ACTIVE_BG_COLOR = new Color(0, 255, 0, 100);
    private static final Color TEXT_COLOR = Color.BLACK;

    public static final int WIDTH = 100;
    public static final int HEIGHT = 25;

    protected static EditorState editor;
    private static boolean initialized = false;

    protected String text;
    protected boolean isHovered;
    protected boolean isActive;
    protected Shape shape;



    public MyButton() {} // default empty constructor

    public MyButton(int x, int y) {
        this.x = x;
        this.y = y;
        init();
    }

    public MyButton(String text, int x, int y) {
        this.text = text;
        this.x = x;
        this.y = y;
        init();
    }

    /**
     * Initializes all buttons with the editor state.
     * @param _editorState
     * @return true if successful
     */
    public static boolean init(EditorState _editorState) {
        if (_editorState != null) {
            editor = _editorState;
            initialized = true;
        }
        return initialized;
    }

    // Method to be implemented by subclasses
    public abstract void handleInput();

    private void init() {
        if (initialized) {
            width = WIDTH;
            height = HEIGHT;
            shape = this;
        }
    }

    public void update() {
        isActive = false;
        checkHover();
    }

    public void render(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(1.0f));
        if (isActive) {
            g2d.setColor(COLOR);
//            drawIndicator(g2d);
            g2d.setStroke(new BasicStroke(3.0f));
            g2d.draw(this);
            g2d.setColor(ACTIVE_BG_COLOR);
            g2d.fill(this);
        } else {
            g2d.setColor(BG_COLOR);
            g2d.fill(this);
        }
        if (isHovered) {
            g2d.fill(this);
        }
        g2d.setFont(FONT);
        g2d.setColor(TEXT_COLOR);
        g2d.drawString(text, this.x + getCenterX(g2d, text), y + 16);
    }

    /**
     * Draws a small rectangular indicator to the left of this button, to indicate
     * that it is active.
     * @param g2d
     */
    protected void drawIndicator(Graphics2D g2d) {
        g2d.fillRect(x, y, 5, height);
    }

    protected int getCenterX(Graphics2D g2d, String text) {
        return (width - g2d.getFontMetrics().stringWidth(text)) / 2;
    }

    /**
     * Checks to see if this button is hovered.
     * @return true if hovered
     */
    protected boolean checkHover() {
        isHovered = contains(Mouse.getCurPt());
        if (isHovered) {
            if (Mouse.getLastEvent() == Mouse.CLICKED) {
                EditorState.setActiveBtn(this);
            }
        }
        return isHovered;
    }

    public void resetShape() {
        shape = new Rectangle();
    }

    public Shape getShape() {
        return shape;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}

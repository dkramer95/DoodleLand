package com.dkramer.objects;

import com.dkramer.states.State;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;

/**
 * Text-Based Obstacle
 * Created by David Kramer on 2/22/2016.
 */
public class TextObstacle extends Obstacle {
    private static BasicStroke STROKE = new BasicStroke(1.0f);
    private Font font;
    private String text;




    /**
     * Constructs a new TextObstacles with the specified String of text.
     * @param text - Text this obstacle should contain
     * @param font - font appearance of this obstacle
     */
    public TextObstacle(String text, Font font) {
        this.text = text;
        this.font = font;
        init();
    }

    public static Shape getTextShape(String text, Font font) {
        TextObstacle textShape = new TextObstacle(text, font);
        return textShape.getShape();
    }

    /**
     * Initializes everything for this TextObstacle.
     */
    protected void init() {
        Graphics2D g2d = State.getGraphics();
        g2d.setFont(font);
        FontRenderContext frc = g2d.getFontRenderContext();
        TextLayout textLayout = new TextLayout(text, font, frc);
        shape = textLayout.getOutline(null);
        super.init();
    }

    /**
     * Moves this TextObstacle to the specified (x, y) coordinate
     * @param x - x-location to move to
     * @param y - y-location to move to
     */
    public void setLocation(int x, int y) {
        movement.translate(x, y);
        shape = movement.createTransformedShape(shape);
    }

    /**
     * Renders everything to the screen.
     * @param g2d
     */
    public void render(Graphics2D g2d) {
        drawOutlined(g2d);
    }

    /**
     * Draws the outline of all the font characters.
     * @param g2d
     */
    public void drawOutlined(Graphics2D g2d) {
        g2d.setStroke(STROKE);
        g2d.setColor(color);
        g2d.draw(transformed);
    }

}

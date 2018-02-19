package com.dkramer.objects;

import com.dkramer.physics.Movement;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Basic particle class. A particle can float around in a world,
 * depending on the influence factors that are defined.
 * Created by David Kramer on 2/24/2016.
 */
public class Particle extends GameObject {
    public static final int WIDTH = 8;
    public static final int HEIGHT = 4;


    private Point spawnLocation;
    private float width;
    private float height;
    private float birthSize;
    private float deathSize;
    private float opacity;
    private float xVel;
    private float yVel;
    private Color birthColor;
    private Color deathColor;
    private Color renderColor;
    private boolean isDead;
    private float acceleration = 0.02f;
    private float gravity = 0.5f;
    private Movement movement;
    private Shape transformed;


    public Particle(Point spawnLocation, float birthSize, float deathSize,
                    float opacity, float xVel, float yVel, Color birthColor,
                    Color deathColor) {

        this.spawnLocation = spawnLocation;
        this.birthSize = birthSize;
        this.deathSize = deathSize;
        this.xVel = xVel;
        this.yVel = yVel;
        this.opacity = opacity;
        this.birthColor = birthColor;
        this.deathColor = deathColor;

        renderColor = birthColor;
        renderColor = new Color(255, 255, 0, 255);  //TODO change back to param value
        isDead = false;
        width = WIDTH;
        height = HEIGHT;
        updateShape();
        movement = Movement.randomSlowMovement(shape);
        transformed = new Rectangle(0, 0, 10, 10);
    }

    /**
     * Renders this particle to the screen.
     * @param g2d - Graphics context to draw too.
     */
    public void render(Graphics2D g2d) {
        if (!isDead) {
            g2d.setColor(renderColor);
            g2d.fill(transformed);
            g2d.setColor(renderColor.darker());
            g2d.draw(transformed);
        }
    }

    /**
     * Updates the particle attributes.
     */
    public void update() {
        updateMovement();
        updateShape();
        updateColor();
        transformed = movement.update(shape);
        setBounds(transformed.getBounds());
    }

    /**
     * Updates the movement of this particle.
     */
    private void updateMovement() {
        xVel += acceleration;
        yVel += gravity;
        spawnLocation.x += xVel;
        spawnLocation.y += yVel;
        width += 0.1f;
        height += 0.1f;
    }

    /**
     * Updates the color of this particle.
     */
    private void updateColor() {
        int red     = renderColor.getRed()    - 2;
        int green   = renderColor.getGreen()  - 2;
        int blue    = renderColor.getBlue()   - 2;
        int alpha   = renderColor.getAlpha()  - 5;

        // prevent negative color values
        red     = red   <= 0 ? 0 : red;
        green   = green <= 0 ? 0 : green;
        blue    = blue  <= 0 ? 0 : blue;
        alpha   = alpha <= 0 ? 0 : alpha;

        if (alpha <= 0) {
            isDead = true;
        }
        renderColor = new Color(red, green, blue, alpha);
    }

    public boolean isDead() {
        return isDead;
    }

    private void updateShape() {
        shape = new Rectangle2D.Float(spawnLocation.x, spawnLocation.y, width, height);
    }
}

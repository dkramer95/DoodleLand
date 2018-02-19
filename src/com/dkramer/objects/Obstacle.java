package com.dkramer.objects;

import com.dkramer.input.Keys;
import com.dkramer.physics.Movement;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;

/**
 * Obstacle class for various types of obstacles that can be present
 * in a world.
 * Created by David Kramer on 2/21/2016.
 */
/*********************
 * OOP: Class
 This class defines a template for creating Obstacle objects.
 *********************/

public class Obstacle extends GameObject implements Serializable, Cloneable {
    private static transient boolean SHOW_DEBUG = false; // flag for controlling rendering of DEBUG_INFO
    private static final BasicStroke OUTLINE = new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    public static final Color DEFAULT_COLOR = new Color(255, 0, 0, 120);

    protected Shape transformed;        // shape that has been affected by movement transforms
    protected Movement movement;

    protected boolean isLethal;



    public Obstacle() {}    // default constructor

    /**
     * Constructs a new obstacle with a specified Shape.
     * @param shape - Base shape this obstacle should have
     */
    public Obstacle(Shape shape) {
        this.shape = shape;
        init();
    }

    /**
     * Shrinks this obstacle slightly.
     */
    public void shrink() {
        movement.scale(0.99, 0.99);
        transformed = movement.createTransformedShape(shape);
    }

    public void grow() {
        movement.scale(1.01, 1.01);
        transformed = movement.createTransformedShape(shape);
    }

    /**
     * Initializes this Obstacle with default values.
     */
    protected void init() {
        color = DEFAULT_COLOR;
        movement = new Movement(shape);
//        transformed = shape;    // by default
//        transformed = new Rectangle(10, 10, 5, 5);    // placeholder to fix null-shape
        transformed = new Rectangle();  // temp placeholder to fix null-shape issue
        isLethal = true;
    }

    /**
     * Renders everything to the screen.
     * @param g2d - Graphics context to draw too.
     */
    public void render(Graphics2D g2d) {
        drawOutlined(g2d);
        drawFilled(g2d);

        if (SHOW_DEBUG) {
            g2d.setStroke(new BasicStroke(1.0f));
            g2d.setColor(Color.RED);
            g2d.draw(transformed.getBounds());
            g2d.setFont(new Font("Courier New", Font.PLAIN, 11));
            g2d.drawString("" + getX() + "," + getY(), (int)getX(), (int)getY());
        }
    }

    /**
     * Checks to see if this obstacle's transformed shape intersects
     * a GameObject, such as a Player.
     * @param obj - GameObject to check
     * @return true if this transformed object contains the GameObject
     */
    public boolean intersects(GameObject obj) {
        return transformed.intersects(obj);
    }

    /**
     * Draws the outline of this Transformed shape.
     * @param g2d
     */
    protected void drawOutlined(Graphics2D g2d) {
        g2d.setColor(new Color(50, 50, 50, 100));
        g2d.setStroke(OUTLINE);
        g2d.draw(transformed);
    }

    /**
     * Fills this Transformed shape.
     * @param g2d
     */
    protected void drawFilled(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fill(transformed);
    }

    /**
     * Updates the transformed shape, as well as the rectangular
     * bounds of this Obstacle.
     */
    public void update() {
        transformed = movement.update(shape);
        setBounds(transformed.getBounds());
    }

    public void setLocation(int x, int y) {
        if (movement != null) {
            movement.reset(getBounds());
            movement.translate(x, y);
        } else {
            super.setLocation(x, y);
        }
    }

    public Obstacle clone() {
        Obstacle clone = new Obstacle(shape);
        if (movement != null) {
            clone.setMovement(movement.clone());
        }
        clone.setColor(color);
        clone.setIsLethal(isLethal);
        return clone;
    }

    public void setMovement(Movement movement) {
        this.movement = movement;
    }

    public Movement getMovement() {
        return movement;
    }

    public boolean isLethal() {
        return isLethal;
    }

    public void setIsLethal(boolean isLethal) {
        this.isLethal = isLethal;
    }

    public Shape getShape() {
        return shape;
    }

    public static void toggleDebug() {
        Keys.clear();
        if (SHOW_DEBUG) {
            SHOW_DEBUG = false;
        } else {
            SHOW_DEBUG = true;
        }
    }
}

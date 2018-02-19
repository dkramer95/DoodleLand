package com.dkramer.objects;

import com.dkramer.worlds.World;

import java.awt.*;
import java.io.Serializable;

/**
 * Base class for all GameObjects. A GameObject is anything that can
 * exist inside of a world. Some GameObjects may be static and not move,
 * and others can traverse inside the world. All subclasses will need
 * to define their various behavior through the update method. GameObjects
 * can vary greatly in appearance, which will be defined in the createShape
 * method, that also must be implemented by subclasses.
 * Created by David Kramer on 2/19/2016.
 */

/*********************
 * OOP: Abstract class
 This is the super class for all GameObjects.
 *********************/

public abstract class GameObject extends Rectangle implements Serializable, Cloneable {

    /*********************
     * OOP: Protected Modifier
     These instance variables are protected, so that all sub-classes who
     inherit from this class will have access to them.
     *********************/

    protected Shape shape;  // shape that will be rendered to the screen
    protected Color color;
    protected World world;



    public GameObject() {}


    /**
     * Method to be implemented by subclasses that creates the
     * shape Geometry for this GameObject
     */
    public abstract void update();

    /**
     * Renders this GameObject's shape geometry to the screen.
     * Subclasses may need to override this, to custom tune their
     * rendering appearance.
     * @param g2d - Graphics context to draw too.
     */
    public void render(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fill(shape);
        g2d.setColor(Color.BLACK);
        g2d.draw(shape);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}

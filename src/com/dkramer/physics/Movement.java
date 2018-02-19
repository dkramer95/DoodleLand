package com.dkramer.physics;

import com.dkramer.utils.Utils;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * This class provides the transformation and movement functionality
 * for all GameObject shapes. Any GameObject can have this movement
 * applied to them.
 * Created by David Kramer on 2/21/2016.
 */
public class Movement extends AffineTransform implements Cloneable {
    public static final float MAX_VALUE = Float.MAX_VALUE;

    private Shape shape;        // default starting source shape
    private Shape transformed;  // shape that has been affected by transformation

    // rotation
    private float minRotate;    // min rotation angle
    private float maxRotate;    // max rotation angle
    private float rotateVel;    // rotation speed
    private float rotation;     // current rotation angle
    private boolean aboutCenter;  // rotate about the center of shape?

    // movement
    private int velX;           // movement speed on x-axis
    private int velY;           // movement speed on y-axis
    private int curX;           // current x
    private int curY;           // current y
    private int limitX;         // movement limit on x-axis
    private int limitY;         // movement limit on y-axis


    public Movement(Shape shape) {
        this.shape = shape;
        this.transformed = new Rectangle(); // placeholder
    }

    /**
     * Convenience method for quickly creating a random slow movement
     * and rotation for a shape, without having to explicitly declare
     * all values.
     * @param shape - Shape to apply transformation too.
     * @return Movement object
     */
    public static Movement randomSlowMovement(Shape shape) {
        Movement m = new Movement(shape);
        int randMoveX   = Utils.random.nextInt(50) + 25;
        int randMoveY   = Utils.random.nextInt(50) + 25;
        int randVelX    = Utils.random.nextInt(1) + 1;
        int randVelY    = Utils.random.nextInt(1) + 1;
        m.setMoveConstraints(randMoveX, randMoveY, randVelX, randVelY);

        // rotation
        m.setRotateConstraints(-1.80f, 1.80f, Utils.random.nextFloat() * 0.02f, true);
        return m;
    }

    /**
     * Sets the movement constraints
     * @param moveX - movement limit on xAxis
     * @param moveY - movement limit on yAxis
     * @param velX - movement speed on xAxis
     * @param velY - movement speed on yAxis
     */
    public void setMoveConstraints(int moveX, int moveY, int velX, int velY) {
        this.limitX = moveX;
        this.limitY = moveY;
        this.velX = velX;
        this.velY = velY;
    }

    /**
     * Sets the rotation movement constraints.
     * @param minRotate - min rotation angle
     * @param maxRotate - max rotation angle
     * @param rotateVel - rotation speed (velocity)
     * @param aboutCenter - should rotation happen, from the center?
     */
    public void setRotateConstraints(float minRotate, float maxRotate, float rotateVel, boolean aboutCenter) {
        this.minRotate = minRotate;
        this.maxRotate = maxRotate;
        this.rotateVel = rotateVel;
        this.aboutCenter = aboutCenter;
    }

    /**
     * Updates the shape with the current movement, rotation, and scale.
     * @param shape - Shape to update
     * @return Shape transformed from movement.
     */
    public Shape update(Shape shape) {
        Rectangle r = shape.getBounds();
        reset(r);
        updateMovement();
        updateRotation();
        translate(curX, curY);
        if (aboutCenter) {
            rotate(rotation, r.getCenterX(), r.getCenterY());
        } else {
            rotate(rotation);
        }
        transformed = createTransformedShape(shape);
        return transformed;
    }

    /**
     * Cancels out previous transform operations, to prevent exponential
     * growth of transformation, that can lead to undesired results.
     * @param r - Boundaries of the shape we're transforming
     */
    public void reset(Rectangle r) {
        // important to clear rotation, before translate!!
        if (aboutCenter) {
            rotate(-rotation + rotateVel, r.getCenterX(), r.getCenterY());
        } else {
            rotate(-rotation + rotateVel);    // clear rotation, not about center pt!
        }
        translate(-curX, -curY);
    }

    /**
     * Updates the x and y movement.
     */
    private void updateMovement() {
        if (curX > limitX || curX < - limitX / 2) {
            velX *= -1;
        }

        if (curY > limitY || curY < - limitX / 2) {
            velY *= -1;
        }
        curX += velX;
        curY += velY;
    }

    /**
     * Updates the rotation.
     */
    private void updateRotation() {
        if (rotation < minRotate || rotation > maxRotate) {
            rotateVel *= -1;
        }
        rotation += rotateVel;
    }

    // TESTING CLONE----->
    public Movement clone() {
        Movement clone = new Movement(shape);
        clone.setMoveConstraints(limitX, limitY, velX, velY);
        clone.setRotateConstraints(minRotate, maxRotate, rotateVel, aboutCenter);
        return clone;
    }

    public Shape getTransformed() {
        return transformed;
    }
}

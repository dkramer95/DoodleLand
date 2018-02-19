package com.dkramer.objects;

import com.dkramer.physics.Movement;

import java.awt.*;

/**
 * This class represents the "WinTarget" that a player must reach inside
 * of the world, to complete that level and move to the next one.
 * Created by David Kramer on 2/21/2016.
 */
public class WinTarget extends GameObject {
    private static final Color COLOR = new Color(255, 255, 0, 100);
    private static final Color OUTLINE_COLOR = new Color(0, 0, 0, 150);
    private static final BasicStroke STROKE = new BasicStroke(2.0f);
    private static final int STAR_PT_COUNT = 6;

    private Shape transformed;
    private Movement movement;



    public WinTarget() {
        init();
    }

    private void init() {
        this.x = 100;
        this.y = 100;
        color = COLOR;
        resetShape();
    }

    private void resetShape() {
        shape = new StarPolygon(x, y, 30, 15, STAR_PT_COUNT, 0.45f);
        movement = new Movement(shape);
        movement.setRotateConstraints(0.0f, Movement.MAX_VALUE, 0.0085f, true);
        transformed = new Rectangle(0, 0, 0, 0);    //placeholder to fix null-shape issue
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
        movement.reset(getBounds());
        movement.translate(x, y);
    }

    public void render(Graphics2D g2d) {
        g2d.setColor(COLOR);
        g2d.fill(transformed);
        g2d.setStroke(STROKE);
        g2d.setColor(OUTLINE_COLOR);
        g2d.draw(transformed);
    }

    public void setMovement(Movement movement) {
        this.movement = movement;
    }

    public Movement getMovement() {
        return movement;
    }

    public void update() {
        transformed = movement.update(shape);
        setBounds(transformed.getBounds());
    }
}

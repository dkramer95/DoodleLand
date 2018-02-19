package com.dkramer.scenery;

import com.dkramer.objects.GameObject;

import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * //TODO this can probably be deleted
 * Created by David Kramer on 2/25/2016.
 */
public class SceneryObject extends GameObject {
    private GeneralPath path;
    private int startX;
    private int startY;
    private int ctrlX;
    private int ctrlY;
    private int endX;
    private int endY;

    public SceneryObject() {
        path = new GeneralPath(GeneralPath.WIND_NON_ZERO);

        startX = 100;
        startY = 100;
        ctrlX = 150;
        ctrlY = 50;
        endX = 200;
        endY = 100;

        // cloud demo
        path.moveTo(startX, startY);
        path.curveTo(startX, startY, ctrlX, ctrlY, endX, endY);
        path.curveTo(endX, endY, endX + 50, endY + 50, endX, endY + 40);
        shape = path;
        color = new Color(0, 250, 0, 100);
    }

    public void render(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.setColor(color);
        g2d.draw(shape);
    }

    public void update() {

    }
}

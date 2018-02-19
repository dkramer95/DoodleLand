package com.dkramer.objects;

import java.awt.*;
import java.awt.geom.QuadCurve2D;

/**
 * THIS OBSTACLE IMPLEMENTATION ISN'T COMPLETELY FINISHED YET..
 * Created by David Kramer on 3/2/2016.
 */
public class CurveObstacle extends Obstacle {
    private QuadCurve2D.Double curve;
    private double[] curvePts;
    private double yVel;




    public CurveObstacle(double x1, double y1, double cx, double cy, double x2, double y2) {
        double[] pts = {x1, y1, cx, cy, x2, y2};
        curvePts = pts;
        shape = new Rectangle(0, 0, 0, 0);
        shape = createCurveFromPts(curvePts);
        transformed = shape;
        yVel = 3.0;
    }

    private QuadCurve2D.Double createCurveFromPts(double[] pts) {
        curve = new QuadCurve2D.Double();
        curve.x1    = pts[0];
        curve.y1    = pts[1];
        curve.ctrlx = pts[2];
        curve.ctrly = pts[3];
        curve.x2    = pts[4];
        curve.y2    = pts[5];
        return curve;
    }

    public void update() {
        if (curve.ctrly > 400 || curve.ctrly < 100) {
            yVel *= -1;
        }
        curve.ctrly += yVel;
        transformed = curve;
    }

    public void render(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.draw(transformed);
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(1.0f));
        g2d.drawRect((int)curve.ctrlx, (int)curve.ctrly, 5, 5);
    }
}

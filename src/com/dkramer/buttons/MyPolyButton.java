package com.dkramer.buttons;

import com.dkramer.input.Keys;
import com.dkramer.input.Mouse;
import com.dkramer.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Polygon-implementation of button.
 * Created by David Kramer on 3/7/2016.
 */
public class MyPolyButton extends MyButton {
    private static final int MAX_PTS = 200;
    private static final String BTN_TEXT = "Polygon";

    private static final int MODE_FREEHAND  = 0;    // polygons with n arbitrary pts
    private static final int MODE_CONCAVE   = 1;    // polygons with 3 to 12 pts (concave)

    // shape construction
    private Polygon polygon = new Polygon();
    private ArrayList<Point> points = new ArrayList<>();
    private int buildMode = MODE_FREEHAND;

    private int nSides = 5; // default starting value




    public MyPolyButton(int x, int y) {
        super(x, y);
        this.text = BTN_TEXT;
    }

    public Shape getShape() {
        return polygon;
    }

    public void handleInput() {
        handleKeyboard();
        if (Mouse.getActiveEvent() == Mouse.DRAGGED) {
            if (buildMode == MODE_FREEHAND) {
                updateFreehand();
            } else if (buildMode == MODE_CONCAVE) {
                updateConcave();
            }
        }
        if (Mouse.getActiveEvent() == Mouse.SCROLLED && buildMode == MODE_CONCAVE) {
            updateVertices();
        }
    }

    private void updateVertices() {
        nSides += Mouse.getScrollCount() * -1;  // negative to fix scroll direction
        // limit poly sides
        if (nSides < 3) {
            nSides = 3;
        } else if (nSides > 12) {
            nSides = 12;
        }
        updateConcave();
    }

    /**
     * Updates the freehand polygon appearance.
     */
    private void updateFreehand() {
        Point pt = (Point)Mouse.getCurPt().clone();
        if (points.size() < MAX_PTS) {
            points.add(pt);
        } else {
            JOptionPane.showConfirmDialog(null,"Max Pt Limit Reached! Double click " +
                    "inside to add, or click away to clear!", "Unable to add more pts!", JOptionPane.CLOSED_OPTION);
        }
        updatePolygon();
        shape = polygon;
        editor.setConstructionShape(shape);
    }

    /**
     * Updates the concave polygon appearance.
     */
    private void updateConcave() {
        int width;
        if (Keys.isKeyDown(KeyEvent.VK_CONTROL)) {
            width = Math.min(editor.getConstructionWidth(), editor.getConstructionHeight());
        } else {
            width = 200;
        }
        polygon = Utils.createNPoly(nSides, true, width, Mouse.getCurPt().x, Mouse.getCurPt().y);
        shape = polygon;
        editor.setConstructionShape(shape);
    }

    /**
     * Handles keyboard input.
     */
    private void handleKeyboard() {
        if (Keys.isKeyDown(KeyEvent.VK_SHIFT)) {
            buildMode = MODE_CONCAVE;
        } else {
            buildMode = MODE_FREEHAND;
        }
    }

    /**
     * Updates this polygon.
     */
    private void updatePolygon() {
        int nPts = points.size();
        int[] xPts = new int[nPts];
        int[] yPts = new int[nPts];

        for (int i = 0; i < nPts; i++) {
            Point pt = points.get(i);
            xPts[i] = pt.x;
            yPts[i] = pt.y;
        }
        polygon.npoints = nPts;
        polygon.xpoints = xPts;
        polygon.ypoints = yPts;
    }

    /**
     * Resets this polygon
     */
    public void resetShape() {
        points.clear();
        polygon = new Polygon();
    }
}

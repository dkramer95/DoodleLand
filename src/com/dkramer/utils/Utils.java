package com.dkramer.utils;

import com.dkramer.states.State;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.SecureRandom;
import java.util.ArrayList;

/**
 * This class contains a collection of useful static utility methods
 * that can be accessed by any other class.
 * Created by David Kramer on 2/21/2016.
 */
public class Utils {
    public static final SecureRandom random = new SecureRandom();



    /**
     * Utility method that reads from a file and imports an array of shapes.
     * @param filename - Path to .shape file
     * @return an array of Shapes
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static ArrayList<Shape> importShapes(String filename)
            throws IOException, ClassNotFoundException {

        ObjectInputStream streamIn = new ObjectInputStream(Utils.class.getClassLoader().getResourceAsStream(filename));
        ArrayList<Shape> shapes = (ArrayList<Shape>)streamIn.readObject();
        streamIn.close();
        return shapes;
    }

    /**
     * Utility method for creating an even convex N-Sided polygon.
     * @param nSides - number of polygon sides, must be >= 3
     * @param upRight - should this polygon be set upright?
     * @param centerX - center x location
     * @param centerY - center y location
     * @return an n-sided Polygon
     */
    public static Polygon createNPoly(int nSides, boolean upRight, int radius, int centerX, int centerY) {
        if (nSides < 3) {	// must be at least 3 sides
            throw new IllegalArgumentException("NumSides must be >= 3");
        }
        int[] x = new int[nSides];
        int[] y = new int[nSides];

        double theta = upRight ? -Math.PI / 2 : 0 / nSides;

        for (int i = 0; i < nSides; i++) {
            x[i] = (int) (radius * Math.cos(2 * Math.PI * i/nSides + theta) + centerX);
            y[i] = (int) (radius * Math.sin(2 * Math.PI * i/nSides + theta) + centerY);
        }
        return new Polygon(x, y, nSides);
    }

    /**
     * Utility method for creating an even convex N-Sided polygon with the center
     * point at half the radius.
     * @param nSides - number of polygon sides, must be >= 3
     * @param upRight - should this polygon be set upright?
     * @return an n-sided Polygon
     */
    public static Polygon createNPoly(int nSides, boolean upRight, int radius) {
        return createNPoly(nSides, upRight, radius, radius / 2, radius / 2);
    }

    /**
     * Utility method that generates a random RGB(a) color
     * @param alpha - flag to control if color should have an alpha value
     * @param alphaLimit - maximum alpha value
     * @return a random RGB(A) color
     */
    public static Color randColor(boolean alpha, int alphaLimit) {
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);

        if (alpha) {
            if (alphaLimit < 0 || alphaLimit > 256) {
                throw new IllegalArgumentException("Alpha must be in range 0 - 255");
            }
            int a = random.nextInt(alphaLimit);
            return new Color(r, g, b, a);
        } else {
            return new Color(r, g, b);
        }
    }

    /**
     * @param g2d - g2d containing currently set Font
     * @param txt - Text
     * @return the center x position, such that the specified text and current
     * font will be centered within the bounds of this app.
     */
    public static int getCenterX(Graphics2D g2d, String txt) {
        int x = (State.getWidth() - g2d.getFontMetrics().stringWidth(txt)) / 2;
        return x;
    }
}

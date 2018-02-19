package com.dkramer.objects;

import com.dkramer.input.Mouse;
import com.dkramer.worlds.World;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;

/**
 * Created by David Kramer on 2/21/2016.
 */
public class DrawablePath extends BufferedImage {

    // constant, that should be used for all Drawable Paths.
    // drawable paths, can contain transparent pixels!
    public static final int TYPE_STD = BufferedImage.TYPE_INT_ARGB;
    public static final int EMPTY_PIXEL = 0;
    public static final int NON_ERASE_PIXEL = 0xff0000; // pixels that can't be erased
    public static final int BRUSH_SIZE  = 15;
    public static final int ERASER_SIZE = BRUSH_SIZE * 3;

    // path texture stuff
    private static boolean initialized = false; // have we initialized this class for all objects?
    private static final String TEXTURE_IMG_PATH = "PNG/pencilTexture2.png";
    private static TexturePaint texture;
    private static BasicStroke brushStroke = new BasicStroke(BRUSH_SIZE, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    private static BasicStroke eraseStroke = new BasicStroke(ERASER_SIZE, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    private static final int DEFAULT_PATH_LIMIT = 1250;
    private static final Color COLOR = new Color(50, 50, 50, 200);

    /*********************
     * OOP: Single Array
     All RGB pixel values are stored in the pixels int[] array.
     *********************/

    private int[] pixels;
    private int width;
    private int height;
    private Rectangle bounds;
    private int pathCount;
    private int pathLimit;      // amount of path we can draw
    private Graphics2D g2d;
    private boolean isEmpty;    // workaround, to prevent unnecessary clearing multiple times, when DEL is pressed!
    private Point lastPt;


    /**
     * Constructs a new Drawable Path
     * @param width - width of path buffer
     * @param height - height of path buffer
     * @param type - ImageType, should be a defined constant in
     *             this class!
     */
    public DrawablePath(int width, int height, int type) {
        super(width, height, type);
        this.width = width;
        this.height = height;
        init();
    }

    /**
     * Initializes everything for this path.
     */
    public void init() {
        pixels = ((DataBufferInt)getRaster().getDataBuffer()).getData();    // raw pixel data
        g2d = createGraphics();
        pathLimit = DEFAULT_PATH_LIMIT;
        bounds = new Rectangle(0, 0, width, height);
        lastPt = new Point(0, 0);

        // check for static texture creation, so we don't initialize excessively
        if (!initialized) {
            createTexture();
            initialized = true;
        }
    }

    public void testFill() {
        int textX = 0;
        int textY = 0;

        final int IMG_WIDTH  = texture.getImage().getWidth();
        final int IMG_HEIGHT = texture.getImage().getHeight();

        BufferedImage img = texture.getImage();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                textY++;
                if (textY >= IMG_HEIGHT) {
                    textY = 0;
                }
                pixels[(width * y) + x] = img.getRGB(textX, textY);
            }
            textY = 0;
            textX++;
            if (textX >= IMG_WIDTH) {
                textX = 0;
            }
        }
    }

//    public void createFromIntArray(int[] pxArray) {
//        if (pxArray.length != pixels.length) {
//            throw new IllegalArgumentException("int[] lengths do not match!");
//        }
//
//        for (int i = 0; i < (width * height); i++) {
//            pixels[i] = pxArray[i];
//        }
//    }

    public void createFromIntArray(int[] pxArray) {
        if (pxArray.length != pixels.length) {
            throw new IllegalArgumentException("int[] lengths do not match!");
        }

        int textX = 0;
        int textY = 0;

        final int IMG_WIDTH  = texture.getImage().getWidth();
        final int IMG_HEIGHT = texture.getImage().getHeight();

        BufferedImage img = texture.getImage();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                textY++;
                if (textY >= IMG_HEIGHT) {
                    textY = 0;
                }
                if (pxArray[(width * y) + x] != EMPTY_PIXEL) {
                    pixels[(width * y) + x] = img.getRGB(textX, textY);
                }
            }
            textY = 0;
            textX++;
            if (textX >= IMG_WIDTH) {
                textX = 0;
            }
        }
    }

    public int[] export() {
        int[] export = new int[pixels.length];
        for (int i = 0, length = pixels.length; i < length; i++) {
            export[i] = pixels[i];
        }
        return export;
    }

    /**
     * Creates the texture used for this drawable path.
     */
    public static TexturePaint createTexture() {
        BufferedImage textureImg = World.createImageFromFile(TEXTURE_IMG_PATH);
        Rectangle textureBounds = new Rectangle(0, 0, textureImg.getWidth(), textureImg.getHeight());
        texture = new TexturePaint(textureImg, textureBounds);
        return texture;
    }

    /**
     * Draws pixels at the specified point to form part of a path
     * @param pt - pt location
     */
    public void draw(Point pt) {
        if (pathCount >= pathLimit) {
            return;
        }
        if (bounds.contains(pt) /*&& !World.getPlayer().contains(pt) */) {  // don't draw, if mouse pos is invalid
            isEmpty = false;
            pathCount++;
            updateDrawMode(AlphaComposite.SrcOver, brushStroke, texture);
            drawStroke(pt);

//            for (int i = pt.x - BRUSH_SIZE; i < pt.x; i++) {
//                for (int j = pt.y - BRUSH_SIZE; j < pt.y; j++) {
//                    int px = (width * j + i);
//                    if (px >= 0 && px < pixels.length) {
//                        pixels[px] = 0xffff00ff;
//                    }
//                }
//            }
        }
    }

    //pre-made path testing
    public void preDraw(Point lastPt, Point pt) {
        if (bounds.contains(pt)) {
            updateDrawMode(AlphaComposite.SrcOver, brushStroke, texture);
            preDrawStroke(lastPt, pt);
        }
    }

    /**
     * Draws a line between the 2 specified points
     * @param lastPt the last pt to draw from
     * @param pt cur pt to draw to
     */
    private void preDrawStroke(Point lastPt, Point pt) {
        g2d.drawLine(lastPt.x, lastPt.y, pt.x, pt.y);
    }
    // pre-made path testing

    /**
     * Updates the drawing mode for this path
     * @param c - Composite type
     * @param stroke - Stroke type
     * @param texture - texture to apply
     */
    private void updateDrawMode(Composite c, Stroke stroke, TexturePaint texture) {
        g2d.setComposite(c);
        g2d.setStroke(stroke);
        g2d.setPaint(texture);
    }

    /**
     * Draws a stroke at the specified point
     * @param pt - new pt to draw too
     */
    private void drawStroke(Point pt) {
        preDrawStroke(lastPt, pt);
        lastPt = Mouse.getLastPt(); // we need this, so we don't have path gaps when drawing
    }

    /**
     * Erases pixels at specified point to remove part of path
     * @param pt - pt location
     */
    public void erase(Point pt) {
        if (bounds.contains(pt)) {
            pathCount--;
            if (pathCount < 0) {
                pathCount = 0;
            }
            updateDrawMode(AlphaComposite.Clear, eraseStroke, null);
            drawStroke(pt);
        }
    }

    /**
     * Clears out all of the pixels in the array to a blank pixel.
     */
    public void clear() {
        int length = pixels.length;
        if (!isEmpty) {
            for (int i = 0; i < length; i++) {
                pixels[i] = EMPTY_PIXEL;
            }
            isEmpty = true;
            pathCount = 0;
        }
        System.out.println("Path cleared!");
    }

    /**
     * Returns a pixel at the specified (x, y) coordinate
     * @param x x-coordinate
     * @param y y-coordinate
     * @return a single individual pixel
     */
    public int getPixel(int x, int y) {
        check(x, y);
        return pixels[(width * y) + x];
    }

    /**
     * Checks to see if the specified (x, y) coordinate exists
     * and is in range of the pixels array
     * @param x
     * @param y
     */
    public void check(int x, int y) {
        if ((x < 0 || x > width) || (y < 0 || y > height)) {
            throw new ArrayIndexOutOfBoundsException("(x < 0 || x > width) || (y < 0 || y > height)");
        }
    }

    /**
     * Sets the specified (x, y) pixel to the specified value
     * @param x - pixel on x-axis
     * @param y - pixel on y-axis
     * @param value - value to assign to pixel
     */
    public void setPixel(int x, int y, int value) {
        check(x, y);
        pixels[(width * y) + x] = value;
    }

    /**
     * Checks to see if the pixel is empty or not
     * @param x x-coordinate
     * @param y y-coordinate
     * @return true if pixel == EMPTY_PIXEL
     */
    public boolean isPixelEmpty(int x, int y) {
        return getPixel(x, y) == EMPTY_PIXEL;
    }

    public void setPathLimit(int limit) {
        this.pathLimit = limit;
    }

    public void setLastPt(Point pt) {
        this.lastPt = pt;
    }

    // DEBUGGING ONLY! This is an expensive operation that dramatically slows down
    // performance if called constantly! But it provides a pixel representation of
    // the pixels that make up this image, as a sequence of 0's and 1's
    public String getAsString() {
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < 200; x++) {
            for (int y = 0; y < 200; y++) {
                if (pixels[(width * y) + x] != EMPTY_PIXEL) {
                    sb.append("[*]");
                } else {
                    sb.append("[" + getPixel(x, y) + "]");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public int[] getPixels() {
        return pixels;
    }

}

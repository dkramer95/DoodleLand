package com.dkramer.input;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;

/**
 * This class keeps track of mouse input events. This is a static class
 * and can't be instantiated.
 * Created by David Kramer on 2/21/2016.
 */
public class Mouse {
    private static boolean shouldUpdate = false;

    // Mouse Event Constants
    public static final int DRAGGED     = 0;
    public static final int MOVED       = 1;
    public static final int CLICKED     = 2;
    public static final int RELEASED    = 3;
    public static final int PRESSED     = 4;
    public static final int SCROLLED    = 5;

    // Mouse state
    private static Point lastPt          = new Point();
    private static Point curPt           = new Point();
    private static int activeEvent       = RELEASED;     // default starting value
    private static int lastEvent         = RELEASED;     // default starting value
    private static boolean isRight       = false;       // did we right click?
    private static int clickCount        = 0;
    private static int scrollCount       = 0;



    private Mouse() {}  // prevent instantiation

    public static void setEvent(int event, MouseEvent e) {
        shouldUpdate = true;
        activeEvent = event;
        curPt.x = e.getPoint().x;
        curPt.y = e.getPoint().y;
        isRight = SwingUtilities.isRightMouseButton(e);
        clickCount = e.getClickCount();
    }

    public static void setExtendedEvent(MouseWheelEvent e) {
        scrollCount = e.getWheelRotation();
    }

    public static void update() {
        if (shouldUpdate) { // don't keep updating, if nothing has changed
            lastEvent = activeEvent;
            lastPt.x = curPt.x;
            lastPt.y = curPt.y;
            shouldUpdate = false;
            scrollCount = 0;
        }
    }

    public static Point getCurPt() {
        return curPt;
    }

    public static Point getLastPt() {
        return lastPt;
    }

    public static int getActiveEvent() {
        return activeEvent;
    }

    public static int getLastEvent() {
        return lastEvent;
    }

    public static boolean isRight() {
        return isRight;
    }

    public static int getClickCount() {
        return clickCount;
    }

    public static int getScrollCount() {
        return scrollCount;
    }
}

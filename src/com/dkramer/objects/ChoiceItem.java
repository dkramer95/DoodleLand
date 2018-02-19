package com.dkramer.objects;

import java.awt.*;

/**
 * This class represents an individual choice selection item to
 * navigate to various states of this application.
 * Created by David Kramer on 2/29/2016.
 */
public class ChoiceItem {
    private static final Font FONT = new Font("Courier New", Font.BOLD, 60);
    private static final int PAD_X  = 50;
    private static final int PAD_Y  = 50;

    private String text;
    private int stateType;   // GameState ID associated with this choice
    private TextObstacle textShape; // backing shape object
    private Shape textOutline;
    private Rectangle selectionBox;




    public ChoiceItem(String text, int stateType) {
        this.text = text;
        this.stateType = stateType;
        init();
    }

    /**
     * Initializes this ChoiceItem.
     */
    private void init() {
        textShape = new TextObstacle(text, FONT);
        textOutline = textShape.getShape();
        selectionBox = addPadding(textOutline.getBounds(), PAD_X, PAD_Y);
    }

    /**
     * Adds specified amount of padding around the supplied
     * Rectangle and centers the location to account for the
     * padding.
     * @param r - rectangle to modify
     * @param padX - padding to add on x-axis
     * @param padY - padding to add on y-axis
     * @return - rectangle r with padding added
     */
    private Rectangle addPadding(Rectangle r, int padX, int padY) {
        r.x      -= padX / 2;
        r.width  += padX;
        r.y      -= padY / 2;
        r.height += padY;
        return r;
    }

    public void setLocation(int x, int y) {
        textShape.setLocation(x, y);
        // make sure the outline shape gets updated!
        textOutline = textShape.getShape();
        selectionBox = addPadding(textOutline.getBounds(), PAD_X, PAD_Y);
    }

    public Shape getTextOutline() {
        return textOutline;
    }

    public Rectangle getSelectionBox() {
        return selectionBox;
    }

    public int getStateType() {
        return stateType;
    }
}

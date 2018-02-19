package com.dkramer.objects;

import com.dkramer.input.Mouse;
import com.dkramer.main.HUD;
import com.dkramer.managers.GameManager;
import com.dkramer.states.PlayState;
import com.dkramer.worlds.World;

import java.awt.*;

/**
 * This class represents an individual world icon that will exist in
 * the Level choosing state. A world icon will display the amount of
 * stars that they player has earned, on the particular level, to provide
 * a gauge of progress.
 * Created by David Kramer on 2/26/2016.
 */
public class WorldIcon extends GameObject {

    // sizing constants
    public static final int WIDTH       = 75;
    public static final int HEIGHT      = 75;
    public static final int MIN_STARS   = 0;
    public static final int MAX_STARS   = 3;
    private static final int X_OFFSET   = 20;

    // appearance
    private static final Color STAR_COLOR   = new Color(40, 40, 40, 150);
    private static final Color TEXT_COLOR   = new Color(20, 100, 20, 140);
    private static final BasicStroke STROKE = new BasicStroke(1.0f);
    private static final Font FONT          = new Font("Courier New", Font.BOLD, 40);

    private StarPolygon[] stars;    // to hold star ranking on this world's completion time

    private int starRating;
    private boolean isActive;   // is this world icon, the currently active one?
    private String worldNum;    // label for the world number




    public WorldIcon(World world, int x, int y) {
        this.world = world;
        this.x = x;
        this.y = y;
        width = WIDTH;
        height = HEIGHT;
        shape = getBounds();
        worldNum = "01";    //TODO change this to a string, passed in by parameter. Change World param to String!
        starRating = -1;    // intentional default starting value
        createStars();
    }

    public void setStarRating(int starRating) {
        if (starRating < MIN_STARS || starRating > MAX_STARS) {
            throw new IllegalArgumentException("Stars must be in range of: " + MIN_STARS + " to " + MAX_STARS);
        }
        this.starRating = starRating;
    }

    /**
     * Creates an array of stars, based on the specified star rating
     * amount.
     */
    private void createStars() {
        stars = new StarPolygon[MAX_STARS];

        for (int i = 0; i < MAX_STARS; i++) {
            int x = this.x + (i * X_OFFSET) + X_OFFSET;
            stars[i] = new StarPolygon(x, this.y + 20, 10, 5, 6);
        }
    }

    private void drawStars(Graphics2D g2d) {
        g2d.setStroke(STROKE);

        for (int i = 0; i < stars.length; i++) {
            StarPolygon s = stars[i];
            if (i < starRating) {
                g2d.setColor(Color.YELLOW);
                g2d.fill(s);
            }
            g2d.setColor(STAR_COLOR);
            g2d.draw(s);
        }
    }

    /**
     * Renders everything to the screen.
     * @param g2d - Graphics context to draw too.
     */
    public void render(Graphics2D g2d) {
        drawStars(g2d);
        drawLabel(g2d);
        drawBox(g2d);
    }

    /**
     * Draws this WorldIcon's box outline.
     * @param g2d
     */
    private void drawBox(Graphics2D g2d) {
        if (isActive) {
            g2d.fill(getBounds());
        } else {
            g2d.draw(getBounds());
        }
    }

    /**
     * Draws the world num label of this WorldIcon.
     * @param g2d
     */
    private void drawLabel(Graphics2D g2d) {
        g2d.setFont(FONT);
        g2d.setColor(TEXT_COLOR);
//        int x = this.x + (int)(getWidth() - g2d.getFontMetrics().stringWidth(worldNum)) / 2;
        int x = getTextCenterX(g2d, worldNum);
        int y = this.y + 65;
        g2d.drawString(worldNum, x, y);
    }

    /**
     *
     * @param g2d - active gfx context
     * @param str - string to center
     * @return x position, such that the current gfx string with set
     * font is centered
     */
    private int getTextCenterX(Graphics2D g2d, String str) {
        return this.x + (int)(getWidth() - g2d.getFontMetrics().stringWidth(str)) / 2;
    }

    public void update() {
        isActive = getBounds().contains(Mouse.getCurPt());
        // we have clicked on the world icon.
        if (isActive && Mouse.getActiveEvent() == Mouse.CLICKED) {
            playSelectedWorld();
        }
    }

    private void playSelectedWorld() {
        GameManager.setState(GameManager.PLAY_STATE);
        PlayState.setActiveWorld(getWorldNum());
        System.out.println("Should be changing to world: " + getWorldNum());
        HUD.setWorldNum(getWorldNum());
    }

    public int getWorldNum() {
        return Integer.parseInt(worldNum);
    }

    public void setWorldNum(int num) {
        worldNum = "" + num;
    }
}

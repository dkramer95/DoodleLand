package com.dkramer.main;

import com.dkramer.worlds.World;

import java.awt.*;

/**
 * Created by David Kramer on 2/21/2016.
 */
public class HUD {
    private static final Font FONT      = new Font("Courier New", Font.BOLD, 30);
    private static final Color COLOR    = new Color(20, 185, 10, 150);

    private static final int START_TIME     = 3600;
    private static final int TWO_STAR_LIMIT = 2400;
    private static final int ONE_STAR_LIMIT = 1200;

    private static int worldNum;
    private static String curTimeLbl;  // time counter for current world
    private static int counter;
    private static Rectangle fuelRect = new Rectangle(100, 20, 100, 20);    // indicator for player "fuel"




    public static void init(int _worldNum) {
        worldNum = _worldNum;
        counter = START_TIME;
        curTimeLbl = "World:" + worldNum + "{" + counter + "}";
    }

    public static void render(Graphics2D g2d) {
        g2d.setFont(FONT);
        g2d.setColor(COLOR);
        g2d.drawString(curTimeLbl, World.getWidth() - g2d.getFontMetrics().stringWidth(curTimeLbl) - 100, 40);
        g2d.fill(fuelRect);
        if (!World.isCheckingCollision()) {
            g2d.setColor(Color.RED);
            g2d.setFont(new Font("Courier New", Font.BOLD, 16));
            g2d.drawString("COLLISION: OFF", 20, 20);
        }
    }

    /**
     * @return - the star rating equivalent for the amount of
     * time left on the counter.
     */
    public static int getStarRating() {
        int stars = 3;
        if (counter < TWO_STAR_LIMIT) {
            stars = 2;
        } else if (counter < ONE_STAR_LIMIT) {
            stars = 1;
        }
        return stars;
    }

    /**
     * Updates the counter of this HUD.
     */
    public static void update() {
        counter--;
        if (counter < 0) {
            counter = 0;
        } else {
            curTimeLbl = "World:" + worldNum + "{" + counter + "}";
        }
        fuelRect.width = World.getPlayer().getParticleFuel();
    }

    public static int getWorldNum() {
        return worldNum;
    }

    public static void setWorldNum(int _worldNum) {
        worldNum = _worldNum;
    }

    public static void reset() {
        counter = START_TIME;
    }
}

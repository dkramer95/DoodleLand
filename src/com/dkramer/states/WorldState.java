package com.dkramer.states;

import com.dkramer.input.Keys;
import com.dkramer.managers.GameManager;
import com.dkramer.objects.Background;
import com.dkramer.objects.WorldIcon;
import com.dkramer.worlds.TestWorld2;

import javax.swing.*;
import java.awt.*;
import java.security.Key;

/**
 * This state contains all of the different worlds of the game, and
 * allows the user to select which world they want to play. They can
 * either navigate using the keyboard or click on a level icon using
 * their mouse. Whatever selection they make, that will be the next
 * world to play and it will immediately start.
 * Created by David Kramer on 2/21/2016.
 */
public class WorldState extends State {
    private static final int ICONS_PER_ROW = 10;    // max levels on a specific row
    private static final Font FONT = new Font("Courier New", Font.BOLD, 40);
    private static final String TITLE = "Select A World";
    private static final Color COLOR = new Color(20, 50, 150, 150);

    private static WorldIcon[] worldIcons;



    public void init() {
        createWorldIcons();
    }

    public static void createWorldIcons() {
        int LEVEL_COUNT = PlayState.getWorldCount();
        final int OFFSET_X = 25;
        worldIcons = new WorldIcon[LEVEL_COUNT];

        int startY = 150;

        int x = 0;
        int y = startY;
        int iconsInRow = 0;

        for (int i = 0; i < LEVEL_COUNT; i++) {
            // center all icons
            if (iconsInRow > ICONS_PER_ROW) {
                iconsInRow = 0;
                x = 0;
                y += WorldIcon.HEIGHT + OFFSET_X;
            }
            iconsInRow++;
            x += 100;
            WorldIcon icon = new WorldIcon(PlayState.getWorld(i), x, y);
            icon.setWorldNum(i);
            worldIcons[i] = icon;
        }
    }

    public void render() {
        Background.render(g2d);
        drawTitle();
        drawIcons();
    }

    private void drawTitle() {
        g2d.setColor(COLOR);
        g2d.setFont(FONT);
        g2d.drawString(TITLE, getCenterX(TITLE), 75);
    }

    private int getCenterX(String txt) {
        int x = (getWidth() - g2d.getFontMetrics().stringWidth(txt)) / 2;
        return x;
    }

    private void drawIcons() {
        for (WorldIcon icon : worldIcons) {
            icon.render(g2d);
        }
    }

    public void update() {
//        Background.update();
        for (WorldIcon icon : worldIcons) {
            icon.update();
        }
    }

    public void handleInput() {
        if (Keys.isKeyDown(Keys.PAUSE)) {
            GameManager.setState(GameManager.INTRO_STATE);
        } else if (Keys.isKeyDown(Keys.ENTER)) {
            GameManager.setState(GameManager.PLAY_STATE);
        }
        Keys.clear();
    }

    public static void setStarRating(int worldNum, int starRating) {
        worldIcons[worldNum].setStarRating(starRating);
    }
}

package com.dkramer.objects;

import com.dkramer.managers.GameManager;
import com.dkramer.worlds.World;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * This class represents a background for a world.
 * Created by David Kramer on 2/25/2016.
 */
public class Background {
    private static Animation animation;
    private static String bgDir = "JPG/bgFrames/bg";


    private Background() {}

    public static void init() {
        BufferedImage[] frames = new BufferedImage[3];

        for (int i = 0, length = frames.length; i < length; i++) {
            BufferedImage img = World.createImageFromFile(bgDir + (i + 1) + ".jpg");
            frames[i] = img;
        }
        animation = new Animation(frames);
    }

    public static void update() {
        animation.update();
    }

    public static void render(Graphics2D g2d) {
        g2d.drawImage(animation.getCurFrame(), 0, 0, null);

        // draw faint red bg indicator for NO collision checking
        if (!World.isCheckingCollision() && GameManager.getStateID() == GameManager.PLAY_STATE) {
            g2d.setColor(new Color(255, 10, 10, 30));
            g2d.fillRect(0, 0, World.getWidth(), World.getHeight());
        }
    }
}

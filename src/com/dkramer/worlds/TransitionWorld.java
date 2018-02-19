package com.dkramer.worlds;

import com.dkramer.objects.WinTarget;
import com.dkramer.states.PlayState;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

/**
 * Created by David Kramer on 3/8/2016.
 */
public class TransitionWorld extends World {
    private int alpha;
    private int wipeWidth;

    public TransitionWorld(World world) {
        alpha = 40;
        this.winTarget = world.getWinTarget();

        // clone here, else it breaks other things!
        WinTarget target = new WinTarget();
        target.setMovement(winTarget.getMovement().clone());
        target.setLocation(winTarget.getLocation());
        target.update();
        this.winTarget = target;
    }

    public void createObstacles() {
        obstacles = new ArrayList<>();
    }

    public void update() {
        updateWipe();
        g2d.scale(1.01f, 1.01f);
        winTarget.update();
        winTarget.setLocation(3, -10);
    }

    private void updateWipe() {
        wipeWidth += 15;
        if (wipeWidth > getWidth()) {
            wipeWidth = getWidth();
            g2d.setTransform(new AffineTransform());
            PlayState.nextWorld();
        }
    }

    public void render() {
        winTarget.render(g2d);
        g2d.setColor(new Color(250, 250, 0, alpha));
        g2d.fillRect(0, 0, wipeWidth, getHeight());
    }
}

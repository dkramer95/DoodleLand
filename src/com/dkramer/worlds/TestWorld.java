package com.dkramer.worlds;

import com.dkramer.objects.Obstacle;
import com.dkramer.physics.Movement;
import com.dkramer.utils.Utils;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

/**
 * Created by David Kramer on 2/22/2016.
 */
public class TestWorld extends World {

    public TestWorld() {
        super();
    }

    public void init() {
        super.init();
        spawnLocation = new Point(850, 100);
        winTarget.setLocation(800, 100);
        respawnPlayer();
    }

    public void respawnPlayer() {
        player.setLocation(spawnLocation);
    }

    public void createObstacles() {
        obstacles = new ArrayList<>();
        obstacles.add(new Obstacle(new Rectangle(100, 100, 300, 300)));
        obstacles.add(new Obstacle(new Ellipse2D.Double(250, 150, 30, 80)));

        for (Obstacle o : obstacles) {
            o.setColor(Utils.randColor(true, 100));
            o.setMovement(Movement.randomSlowMovement(o.getShape()));
        }
        setOrigObstacles(obstacles);
    }
}

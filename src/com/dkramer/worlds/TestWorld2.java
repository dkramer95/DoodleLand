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
public class TestWorld2 extends World {


    public TestWorld2() {
        super();
    }

    public void init() {
        super.init();
        spawnLocation = new Point(20, 20);
        winTarget.setLocation(500, 200);
        respawnPlayer();
    }

    public void respawnPlayer() {
        player.setLocation(spawnLocation);
    }

    public void createObstacles() {
        obstacles = new ArrayList<>();
        obstacles.add(new Obstacle(new Rectangle(600, 300, 450, 300)));
        obstacles.add(new Obstacle(new Ellipse2D.Double(650, 150, 120, 80)));
        obstacles.add(new Obstacle(Utils.createNPoly(5, true, 150, 350, 100)));
        obstacles.add(new Obstacle(Utils.createNPoly(3, false, 200, 300, 75)));

        for (Obstacle o : obstacles) {
            o.setColor(Utils.randColor(true, 100));
            o.setMovement(Movement.randomSlowMovement(o.getShape()));
        }
    }
}

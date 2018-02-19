package com.dkramer.worlds;

import com.dkramer.objects.CurveObstacle;
import com.dkramer.objects.FuelObject;
import com.dkramer.objects.Obstacle;
import com.dkramer.objects.TextObstacle;
import com.dkramer.physics.Movement;
import com.dkramer.scenery.SceneryObject;
import com.dkramer.utils.Utils;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by David Kramer on 2/25/2016.
 */
public class World0 extends World {
    private SceneryObject cloudTest;
    private CurveObstacle c;
    private FuelObject fuel;

    public void init() {
        obstacleFilePath = "OBSTACLES/world0.shape";
        super.init();
        winTarget.setLocation(200, 200);
        spawnLocation = new Point(getWidth() - 100, 0);
//        cloudTest = new SceneryObject();
        createObstacles();
    }

    public void createObstacles() {
//        obstacles = createObstaclesFromFile(obstacleFilePath, true);
//        TextObstacle text = new TextObstacle("avoid_obstacles!", new Font("Courier New", Font.BOLD, 35));
//        text.setLocation(600, 300);
//        Movement m = new Movement(text);
//        m.setMoveConstraints(100, 0, 1, 1);
//        m.setRotateConstraints(-0.90f, 0.90f, 0.005f, true);
//        text.setMovement(m);
//        obstacles.add(text);
        obstacles = new ArrayList<>();
        setOrigObstacles(obstacles);
        curveTest2();
    }

    private void curveTest2() {
        c = new CurveObstacle(300, 250, 450, 150, 600, 250);
        c.translate(-100, 0);
        c.setColor(Color.PINK);
        obstacles.add(c);

        fuel = new FuelObject(100, 100);
        fuel.setIsLethal(false);
        obstacles.add(fuel);
    }

    public void update() {
        super.update();
        for (Obstacle o : obstacles) {
            o.update();
        }
    }

    public void render() {
        super.render();
    }
}

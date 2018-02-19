package com.dkramer.worlds;

import com.dkramer.input.Keys;
import com.dkramer.objects.*;
import com.dkramer.physics.Movement;
import com.dkramer.utils.Utils;
import com.sun.javafx.geom.Curve;

import java.awt.*;
import java.awt.geom.QuadCurve2D;
import java.util.ArrayList;

/**
 * Created by David Kramer on 2/21/2016.
 */
public class DeleteMeWorld extends World {
    private TextObstacle text;
    private ParticleSystem particleSystem;

    public void init() {
        super.init();
        spawnLocation = new Point(1000, 20);
        obstacles = World.createObstaclesFromFile("OBSTACLES/2.shape", true);
        createText();
//        createCurveTest();
        curveTest2();
//        bgImage = World.createImageFromFile("JPG\\bg.jpg");
        winTarget = new WinTarget();
        winTarget.setLocation(300, 400);
        respawnPlayer();
        particleSystem = new ParticleSystem();
        setOrigObstacles(obstacles);
    }

    private void createText() {
        text = new TextObstacle("DANGER!", new Font("Courier New", Font.BOLD, 40));
        text.setColor(new Color(255, 0, 0, 120));
        text.setLocation(500, 200);
        text.setMovement(Movement.randomSlowMovement(text.getShape()));
        obstacles.add(text);

//        TextObstacle title = new TextObstacle("DoodleLand", new Font("Courier New", Font.BOLD, 50));
//        title.setColor(new Color(0, 255, 0, 100));
//        title.setLocation(300, 400);
//        Movement m = new Movement(title.getShape());
//        m.setRotateConstraints(-0.80f, 0.80f, 0.005f, true);
//        title.setMovement(m);
//        obstacles.add(title);
    }

    private void createCurveTest() {
        QuadCurve2D.Double c = new QuadCurve2D.Double(300, 250, 100, 150, 400, 500);
        Obstacle o = new Obstacle(c);
        o.setMovement(Movement.randomSlowMovement(c));
        o.setColor(new Color(255, 0, 255, 20));
        obstacles.add(o);
    }

    private void curveTest2() {
        CurveObstacle c = new CurveObstacle(100, 100, 50, 50, 200, 200);
        c.setColor(Utils.randColor(true, 100));
        obstacles.add(c);
    }

    public void respawnPlayer() {
        player.setLocation(spawnLocation);
    }

    public void createObstacles() {

    }

    public void render() {
        super.render();
        particleSystem.render(g2d);
    }

    public void update() {
        super.update();
        particleSystem.update();
    }

    public void handleInput() {
        super.handleInput();
        if (Keys.isKeyDown(Keys.SHOOT)) {
            // peeing stream particle
            particleSystem.add(new Particle(new Point(player.x + 10, player.y + player.height - 20),
                    8.0f, 0.5f, 100.0f, 3.0f, -2.0f + Utils.random.nextFloat(),
                    new Color(255, 255, 255, 255), Color.BLACK));
        }
    }
}

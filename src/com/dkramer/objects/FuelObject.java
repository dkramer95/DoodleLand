package com.dkramer.objects;

import com.dkramer.worlds.World;

import java.awt.*;

/**
 * A "Fuel" Object, that allows a player to refill their particle
 * ability.
 * Created by David Kramer on 3/2/2016.
 */
public class FuelObject extends Obstacle {
    private static final int WIDTH = 20;
    private static final int HEIGHT = 50;




    public FuelObject(int x, int y) {
        shape = new Rectangle(x, y, WIDTH, HEIGHT);
        transformed = shape;
        setColor(Color.BLUE);
    }

    public void update() {
        Player p = World.getPlayer();
        if (p.contains(transformed.getBounds())) {
            p.setParticleFuel(100);
        }
    }
}

package com.dkramer.objects;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Contains the maximum allowed particles.
 * Created by David Kramer on 2/24/2016.
 */
public class ParticleSystem {
    public static final int INITIAL_CAPACITY = 150;
    private ArrayList<Particle> particles;




    public ParticleSystem() {
        particles = new ArrayList<>(INITIAL_CAPACITY);
    }

    public void add(Particle p) {
        particles.add(p);
    }

    /**
     * Updates all the particles in this particle system.
     */
    public void update() {
        Iterator<Particle> iter = particles.iterator();

        while (iter.hasNext()) {
            Particle p = iter.next();
            if (p.isDead()) {
                iter.remove();
            } else {
                p.update();
            }
        }
    }

    /**
     * Renders all the particles to the screen in this particle
     * system.
     * @param g2d - Graphics to draw on
     */
    public void render(Graphics2D g2d) {
        Iterator<Particle> iter = particles.iterator();
        g2d.setStroke(new BasicStroke(1.0f));

        while (iter.hasNext()) {
            Particle p = iter.next();
            p.render(g2d);
        }
    }

    public ArrayList<Particle> getParticles() {
        return particles;
    }
}

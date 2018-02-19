package com.dkramer.worlds;

import com.dkramer.input.Keys;
import com.dkramer.input.Mouse;
import com.dkramer.main.HUD;
import com.dkramer.objects.*;
import com.dkramer.physics.Movement;
import com.dkramer.states.PlayState;
import com.dkramer.states.State;
import com.dkramer.utils.Utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

/**
 * Base class for all worlds (levels). A world acts as a container to
 * hold various game objects and elements, that make up the current
 * challenge for the user to achieve the end goal of reaching the
 * target. Worlds are responsible for actively rendering all
 * GameObjects to the screen as well as updating them. All objects
 * should stay contained within this world, as it is a BoundingRect.
 *
 * Worlds can also have various different types of forces, that influence
 * other objects behavior when moving around in the world. Of course,
 * sub-classes will be responsible for implementing any necessary forces.
 *
 * The basic required objects a world must have are:
 * 1) A Player - User of the application to navigate around the world
 * 2) Random Obstacles - Static and/or moving obstacles
 * 3) End Target - Target to reach to exit this world
 *
 * The world will be responsible for checking collision between the different
 * GameObjects that should be checked. Also, if a player collides into a
 * "deadly" obstacle, World's will be responsible for respawning them at their
 * starting SpawnLocation.
 *
 * Worlds can also have a "Drawable Path", in which the user paints a path
 * for the player to traverse and follow, to navigate and avoid certain
 * obstacles.
 * ----------------------------------------------------------------------------
 * A world is actually a state, and therefore contains a graphics object to
 * draw itself too. Worlds shouldn't directly be used as a State, in the
 * StateManager, rather be contained in the PlayState.
 * Created by David Kramer on 2/21/2016.
 */
public abstract class World extends State implements Serializable {
    private static final long serialVersionUID = 1L;
    private static boolean CHECK_COLLISION = true;

    public static final int GRAVITY = 5;  // gravity force applied to players
    public static final Point DEFAULT_SPAWN_PT = new Point(500, 10);
    public static int instanceCount = 0;

    protected static Player player;
    protected transient static boolean initialized = false;

    protected static DrawablePath drawPath;
    protected static ParticleSystem particleSystem;

    protected Point spawnLocation;
    protected ArrayList<Obstacle> origObstacles;    // starting obstacles, before they have been updated
    protected ArrayList<Obstacle> obstacles;  // game obstacles
    protected String obstacleFilePath;
    protected WinTarget winTarget;  // target player has to reach, to win level
    protected int num;  // number (ID) of this world

    //TESTING
    protected int[] pxData; // path pixel data
    //TESTING


    public World() {
        instanceCount++;
        if (initialized) {
            init();
        }
    }

    public static boolean init(Player _player) {
        if (_player != null) {
            player = _player;
            initialized = true;
            initBackground();
            initParticles();
        }
        return initialized;
    }

    public static boolean exportToFile(String file, World world) {
        boolean success = false;
        World w = createExportable(world);

        try {
            ObjectOutputStream streamOut = new ObjectOutputStream(new FileOutputStream(new File(file)));
            streamOut.writeObject(w);
            streamOut.close();
            success = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    /**
     * Creates a world that is ready to be exported to a file.
     * @param world - Source world
     * @return new world, with all properties set
     */
    private static World createExportable(World world) {
        World exportWorld = new World() {
            public void createObstacles() {}
        };
        exportWorld.setSpawnLocation(world.getSpawnLocation());
        exportWorld.setWinTarget(world.getWinTarget());
        exportWorld.setObstacles(world.getObstacles());
        exportWorld.setPixelData(drawPath.export());
        exportWorld.setOrigObstacles(world.getObstacles());
        exportWorld.setNum(instanceCount++);
        return exportWorld;
    }

    /*********************
     * OOP: Exception Handling: Try-Catch
     Explanatory comment goes here¬
     *********************/

    public static World importFromFile(String file) {
        World world = null;
        ObjectInputStream streamIn = null;
        try {
            streamIn = new ObjectInputStream(new FileInputStream(new File(file)));
            world = (World)streamIn.readObject();
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFound: " + e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Read Error: " + e.getMessage());

            /*********************
             * OOP: Exception-Handling: Finally
             Explanatory comment goes here¬
             *********************/

        } finally {
            try {
                streamIn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return world;
    }

    private static void initBackground() {
        SwingWorker w = new SwingWorker() {
            protected Object doInBackground() throws Exception {
                Background.init();
                return null;
            }
        };
        w.execute();
    }

    private static void initParticles() {
        particleSystem = new ParticleSystem();
    }

    /**
     * Default init method.
     */
    public void init() {
        winTarget = new WinTarget();
        initDrawPath(pxData);
        createObstacles();
    }

    /**
     * Sets up the draw path so it is in a fresh blank state and
     * ensures that it is created.
     */
    protected static void initDrawPath() {
        if (drawPath != null) {
            resetPath();
            drawPath.setLastPt(Mouse.getCurPt());
        } else {
            drawPath = new DrawablePath(getWidth(), getHeight(), DrawablePath.TYPE_STD);
            resetPath();
        }
    }

    protected static void initDrawPath(int[] pxData) {
        if (drawPath != null) {
            if (pxData != null) {
                resetPath();
                drawPath.setLastPt(Mouse.getCurPt());
                drawPath.createFromIntArray(pxData);
            }
        } else {
            initDrawPath();
        }
    }

    /**
     * Clears out the previous draw path.
     */
    public static void resetPath() {
        drawPath.clear();
    }

    /**
     * Method to be implemented, to define where and how the player
     * should be respawned in this world.
     */
    public void respawnPlayer() {
        player.setLocation(getSpawnLocation());
    }

    /**
     * Method to be implemented, to define the various obstacles that
     * can be present throughout a world.
     */
    public abstract void createObstacles();

    /**
     * Renders everything to the screen.
     */
    public void render() {
        g2d.setClip(0, 0, getWidth(), getHeight());
        drawBG();
        drawPath();
        drawObstacles();
        particleSystem.render(g2d);
        drawWinTarget();
        drawPlayer();
        HUD.render(g2d);
    }

    /**
     * Draws the background image.
     */
    protected void drawBG() {
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        Background.render(g2d);
    }

    /**
     * Draws all obstacles.
     */
    protected synchronized void drawObstacles() {
        for (Obstacle o : obstacles) {
            o.render(g2d);
        }
    }

    /**
     * Draws the player.
     */
    protected void drawPlayer() {
        player.render(g2d);
    }

    /**
     * Draws the user defined path.
     */
    protected void drawPath() {
        g2d.drawImage(drawPath, 0, 0, null);
    }

    protected void drawWinTarget() {
        winTarget.render(g2d);
    }

    /**
     * Updates all game objects and checks for collisions.
     */
    public void update() {
        particleSystem.update();
        handleInput();

        Background.update();
        player.update();
        winTarget.update();

        if (!checkWin()) {
            checkObstacleCollision();
        } else {
            PlayState.transition(this);
        }
        HUD.update();
    }

    protected void checkPathDraw() {
        // mouse input
        if (Mouse.getActiveEvent() == Mouse.DRAGGED) {
            if (Mouse.isRight()) {
                drawPath.erase(Mouse.getCurPt());
            } else {
                drawPath.draw(Mouse.getCurPt());
            }
        }
    }

    public void handleInput() {
        checkPathDraw();

        if (Keys.isKeyDown(KeyEvent.VK_F12)) {
            reset();
        }
        if (Keys.isKeyDown(KeyEvent.VK_F6)) {
            Keys.clear();
            toggleCollisionCheck();
        }
        if (Keys.isKeyDown(KeyEvent.VK_DELETE)) {
            drawPath.clear();
        }
        if (Keys.isKeyDown(KeyEvent.VK_ENTER)) {
            PlayState.nextWorld();
        }
        if (Keys.isKeyDown(KeyEvent.VK_J)) {
            if (player.getParticleFuel() > 0) {
                particleSystem.add(player.getPeeParticle());
            }
        }
        if (Keys.isKeyDown(KeyEvent.VK_F5)) {
            Obstacle.toggleDebug();
        }
    }

    public void animateWin() {
        g2d.scale(1.025, 1.025);
    }

    /**
     * Checks to see if player has hit the WinTarget.
     * @return true if player hit WinTarget
     */
    protected boolean checkWin() {
        return winTarget.intersects(player);
    }

    /**
     * Checks to see if any of the obstacles collided with the
     * player and if they did, the player is respawned.
     */
    protected void checkObstacleCollision() {
        for (Obstacle o : obstacles) {
            o.update();
            if (CHECK_COLLISION) {
                if (o.intersects(player) && o.isLethal()) {
                    respawnPlayer();  //comment out to disable respawning of player
                }
                //TODO this isn't very efficient by using bruteforce checking!
                //TODO if time permits, implement a QuadTree!!

                for (Particle p : particleSystem.getParticles()) {
                    if (o.contains(p)) {
                        o.shrink();
                    }
                }
            }
        }
    }

    /**
     * Helper method to create obstacles from a .poly file.
     * @param file - File containing ArrayList of polygons
     * @param randMovement - flag to control if a random movement
     *                     factor should be applied to all obstacles
     * @return an ArrayList of obstacles
     */
    public static ArrayList<Obstacle> createObstaclesFromFile(String file, boolean randMovement) {
        ArrayList<Shape> shapes = null;
        try {
            shapes = Utils.importShapes(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<Obstacle> obstacles = new ArrayList<>();

        if (randMovement) {
            for (Shape p : shapes) {
                Obstacle o = new Obstacle(p);
                o.setMovement(Movement.randomSlowMovement(p));
                o.setColor(Utils.randColor(true, 100));
                obstacles.add(o);
            }
        } else {
            for (Shape p : shapes) {
                Obstacle o = new Obstacle(p);
                obstacles.add(o);
            }
        }
        return obstacles;
    }

    /**
     * Helper method to create an image from an image file
     * @param file - Image filepath
     * @return a BufferedImage, from specified filepath
     */
    public static BufferedImage createImageFromFile(String file) {
        BufferedImage bg = null;
        try {
            bg = ImageIO.read(Utils.class.getClassLoader().getResourceAsStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bg;
    }

    /**
     * Clones all of the obstacles, so that their original state will be
     * preserved.
     * @param origObstacles
     */
    public void setOrigObstacles(ArrayList<Obstacle> origObstacles) {
        this.origObstacles = new ArrayList<>(origObstacles.size());

        for (Obstacle o : origObstacles) {
            this.origObstacles.add(o.clone());
        }
    }

    /**
     * Resets all objects in the world, back to their original starting positions.
     */
    public void reset() {
        HUD.reset();
        Keys.clear();
        drawPath = new DrawablePath(getWidth(), getHeight(), DrawablePath.TYPE_STD);
        initDrawPath(pxData);
        player.setLocation(getSpawnLocation());

        obstacles.clear();
        for (Obstacle o : origObstacles) {
            obstacles.add(o.clone());
        }
    }

    public static Player getPlayer() {
        return player;
    }

    public DrawablePath getDrawPath() {
        return drawPath;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public ArrayList<Obstacle> getOrigObstacles() {
        return origObstacles;
    }

    public void setObstacles(ArrayList<Obstacle> obstacles) {
        this.obstacles = obstacles;
    }

    public ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }

    public void setSpawnLocation(Point spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public Point getSpawnLocation() {
        return spawnLocation;
    }

    public WinTarget getWinTarget() {
        return winTarget;
    }

    public void setWinTarget(WinTarget target) {
        this.winTarget = target;
    }

    public int getNum() {
        return num;
    }

    public int[] getPixelData() {
        return pxData;
    }

    public void setPixelData(int[] pxData) {
        this.pxData = pxData;
    }

    public static void toggleCollisionCheck() {
        if (CHECK_COLLISION) {
            CHECK_COLLISION = false;
        } else {
            CHECK_COLLISION = true;
        }
    }

    public static boolean isCheckingCollision() {
        return CHECK_COLLISION;
    }
}

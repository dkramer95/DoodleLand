package com.dkramer.states;

import com.dkramer.input.Keys;
import com.dkramer.main.HUD;
import com.dkramer.managers.GameManager;
import com.dkramer.objects.Player;
import com.dkramer.worlds.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * Created by David Kramer on 2/21/2016.
 */
public class PlayState extends State {

    private static ArrayList<World> worlds;
    private static World activeWorld;
    private static int worldIndex;
    private static int animateCount;
    private static final int ANIMATE_LIMIT = 10;
    private static int alpha = 10;
    private static String WORLD_DIR = "res/worlds";
    private static int worldCount;

    public void init() {
        createWorlds();
    }

    private void createWorlds() {
        World.init(new Player());
        worlds = new ArrayList<>();
        worldIndex = -1;

//        worlds.add(new World0());
//        worlds.add(new DeleteMeWorld2());
//        worlds.add(new TestWorld());
//        worlds.add(new TestWorld2());

        loadWorlds();

        nextWorld();
        HUD.init(worldIndex);
    }

    /**
     * Loads all initial world files into this application.
     */
    private void loadWorlds() {
        File[] worldFiles = getFiles(WORLD_DIR);
        String dir = "/worlds/";

        for (File f : worldFiles) {
            try {
                String file = dir + f.getName();
                ObjectInputStream streamIn = new ObjectInputStream(PlayState.class.getResourceAsStream(file));
                World w = (World)streamIn.readObject();
                w.setNum(worldCount++);
                PlayState.addWorld(w);
                streamIn.close();
            } catch (IOException e) {
                System.out.println("IO Read Error: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                System.out.println("ClassNotFoundException: " + e.getMessage());
            }
        }
        WorldState.createWorldIcons();
    }

    /**
     * @param fileDir file directory
     * @return array of files, contained in the file directory
     */
    public static File[] getFiles(String fileDir) {
        File folder = new File(fileDir);
        File[] filesInDir = null;
        if (folder.isDirectory()) {
            filesInDir = folder.listFiles();
        }
        return filesInDir;
    }

    public void render() {
        g2d.setStroke(new BasicStroke(3.0f));
        activeWorld.render();
    }

    public void update() {
        activeWorld.update();
    }

    /**
     * Advances to the next world, and updates the player.
     */
//    public static void nextWorld() {
//        worldIndex++;
//        if (worldIndex >= worlds.size()) {
//            worldIndex = 0;
//            GameManager.setState(GameManager.WORLD_STATE);
//        }
//        World.resetPath();  // clear out old drawing path
//        activeWorld = worlds.get(worldIndex);
//        HUD.init(activeWorld.getNum());
//        System.out.println("ACTIVE WORLD: " + activeWorld);
//        activeWorld.respawnPlayer();
//        World.getPlayer().setWorld(activeWorld);
//    }

    /**
     * Advances to the next world, if there are any still remaining.
     * Otherwise, we will go to the main world chooser state.
     */
    public static void nextWorld() {
        worldIndex++;
        if (checkWorldEnd()) {
            worldIndex = 0;
            // go back to level chooser, if we've advanced through all the levels
            GameManager.setState(GameManager.WORLD_STATE);
            return;
        }
        initNextWorld();
    }

    /**
     * Initializes the next world, so that it is ready to be played.
     */
    private static void initNextWorld() {
        activeWorld = worlds.get(worldIndex);
        activeWorld.reset();
        HUD.init(activeWorld.getNum());
        World.getPlayer().setWorld(activeWorld);
    }

    /**
     * Checks to see if we have reached the end of all the worlds.
     * @return true if we've reached the end
     */
    private static boolean checkWorldEnd() {
        return worldIndex >= worlds.size();
    }

    /**
     * Sets the active world to the specified index.
     * @param index
     */
    public static void setActiveWorld(int index) {
        worldIndex = index;
        initNextWorld();
    }

    public static void transition(World world) {
        activeWorld = new TransitionWorld(world);
        WorldState.setStarRating(HUD.getWorldNum(),HUD.getStarRating());
    }

    public void handleInput() {
        if (Keys.isKeyDown(Keys.PAUSE)) {
            GameManager.setState(GameManager.WORLD_STATE);
        }
    }

    public static void addWorld(World world) {
        worlds.add(world);
    }

    public static World getWorld(int index) {
        return worlds.get(index);
    }

    public static int getWorldCount() {
        return worlds.size();
    }
}

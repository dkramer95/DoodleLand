package com.dkramer.states;

import com.dkramer.buttons.*;
import com.dkramer.input.Keys;
import com.dkramer.input.Mouse;
import com.dkramer.managers.GameManager;
import com.dkramer.objects.Background;
import com.dkramer.objects.DrawablePath;
import com.dkramer.objects.Obstacle;
import com.dkramer.objects.WinTarget;
import com.dkramer.physics.Movement;
import com.dkramer.utils.Utils;
import com.dkramer.worlds.World;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This state allows the user to edit and create new worlds.
 * Created by David Kramer on 3/6/2016.
 */
public class EditorState extends World {
    private ArrayList<MyButton> toolBtns;
    private static MyButton activeBtn;

    // obstacle shape construction
    private Shape constructionShape;    // shape for obstacle we're making

    // original starting values
    private Point oldStartPt;
    private Point oldEndPt;     //TODO, this can probably be removed.
    // current values
    private Point startPt;
    private Point endPt;
    private boolean isConstructing;

    private boolean addMovement;    // flag for adding movement to obstacles
    private boolean isLethal;


    public EditorState() {
        super();
//        init();
    }

    public void createObstacles() {
        obstacles = new ArrayList<>();
    }

    /**
     * Initializes all the components of this EditorState.
     */
    public void init() {
        super.init();
        MyButton.init(this);
        spawnLocation = new Point(1000, 20);
        player.setWorld(this);
        winTarget = new WinTarget();
        winTarget.setLocation(250, 300);
        winTarget.update();
        initConstruction();
        createButtons();
//        fixPath();
        System.out.println("EditorPX: " + drawPath.getPixels());
        pxData = drawPath.getPixels();
    }

    public void fixPath() {
        for (int x = 0; x < drawPath.getWidth(); x++) {
            for (int y = 0; y < drawPath.getHeight(); y++) {
                drawPath.setPixel(x, y, DrawablePath.EMPTY_PIXEL);
            }
        }
    }

    /**
     * Initializes the construction shape object.
     */
    private void initConstruction() {
        startPt = new Point();
        endPt = new Point();
        constructionShape = new Rectangle();  // default starting value
    }

    /**
     * Creates the top tool buttons.
     */
    private void createButtons() {
        toolBtns = new ArrayList<>();
        int startY = 20;
        // add all tool buttons
        toolBtns.add(new MyRectButton(20, startY));
        toolBtns.add(new MyEllipseButton(130, startY));
        toolBtns.add(createPathBtn(240, startY));
        toolBtns.add(createMovePlayerBtn(350, startY));
        toolBtns.add(createMoveTargetBtn(460, startY));
        toolBtns.add(new MyTextButton(570, startY));
        toolBtns.add(new MyPolyButton(680, startY));
        toolBtns.add(new MoveButton(790, startY));
        activeBtn = toolBtns.get(0);    // first btn default
    }

    // TOOL BUTTON CREATION METHODS -- Made here, because their implementation
    // is relatively small.

    private MyButton createPathBtn(int x, int y) {
        MyButton pathBtn = new MyButton("DrawPath", x, y) {
            public void handleInput() {
                checkPathDraw();
            }
        };
        return pathBtn;
    }

    private MyButton createMovePlayerBtn(int x, int y) {
        MyButton playerBtn = new MyButton("Move Player", x, y) {
            public void handleInput() {
                if (Mouse.getActiveEvent() == Mouse.DRAGGED) {
                    setSpawnLocation((Point)Mouse.getCurPt().clone());
                    if (checkMousePos(spawnLocation, player)) {
                        player.setLocation(spawnLocation);
                    }
                }
            }
        };
        return playerBtn;
    }

    private MyButton createMoveTargetBtn(int x, int y) {
        MyButton targetBtn = new MyButton("Move Target", x, y) {
            public void handleInput() {
                if (Mouse.getActiveEvent() == Mouse.DRAGGED) {
                    // fix move location
                    Point movePt = new Point(Mouse.getCurPt().x - 100, Mouse.getCurPt().y - 100);
                    winTarget = new WinTarget();
                    winTarget.setLocation(movePt);
                    winTarget.update();
                }
            }
        };
        return targetBtn;
    }

    /**
     * Validates mouse positioning with the rectangle object to
     * ensure it is contained inside the boundaries of this state.
     * @param pt - pt to check
     * @param rect - rectangle to check
     * @return true if contained within bounds
     */
    private boolean checkMousePos(Point pt, Rectangle rect) {
        return (pt.x > 0 && pt.x < State.getWidth() - rect.width
                && pt.y > 0 && pt.y < State.getHeight() - rect.height);
    }

    /**
     * Draws everything to the screen.
     */
    public synchronized void render() {
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.drawImage(drawPath, 0, 0, null);
        drawPlayer();
        winTarget.render(g2d);
        drawButtons();
        g2d.setColor(Color.BLACK);
        g2d.draw(constructionShape);
        drawObstacles();

        if (!addMovement) {
            g2d.setColor(Color.RED);
            g2d.drawString("NO MOVEMENT: TOGGLE: F10", getWidth() - 200, 20);
        }

        if (!isLethal) {
            g2d.setColor(Color.RED);
            g2d.drawString("NOT LETHAL: TOGGLE F11", getWidth() - 200, 40);
        }
    }

    /**
     * Draws all the buttons to the screen.
     */
    private void drawButtons() {
        g2d.setStroke(new BasicStroke(1.0f));
        for (MyButton btn: toolBtns) {
            btn.render(g2d);
        }
    }

    public void update() {
        updateButtons();
        activeBtn.setIsActive(true);
        handleInput();
        updateObstacles();
        winTarget.update();
//        player.update();  // if uncommented, world environment affects player (i.e. falls)
    }

    private synchronized void updateObstacles() {
        Iterator<Obstacle> iter = obstacles.iterator();
        while (iter.hasNext()) {
            iter.next().update();
        }
    }

    private void updateButtons() {
        for (MyButton btn: toolBtns) {
            btn.update();
        }
    }

    public void handleInput() {
        updatePoints();
        activeBtn.handleInput();
        checkKeyboard();
    }

    /**
     * Checks keyboard input.
     */
    private void checkKeyboard() {
        if (Keys.isKeyDown(KeyEvent.VK_ESCAPE)) {
            GameManager.setState(GameManager.INTRO_STATE);
        }
        if (Keys.isKeyDown(Keys.CLEAR)) {
            clear();
        }
        if (Keys.isKeyDown(KeyEvent.VK_F10)) {
            toggleMovement();
            Keys.clear();
        }
        if (Keys.isKeyDown(KeyEvent.VK_F11)) {
            toggleLethal();
            Keys.clear();
        }
        checkModifiers();
    }

    private void toggleMovement() {
        if (addMovement) {
            addMovement = false;
        } else {
            addMovement = true;
        }
    }

    private void toggleLethal() {
        if (isLethal) {
            isLethal = false;
        } else {
            isLethal = true;
        }
    }

    /**
     * Checks for any modifier key commands.
     */
    private void checkModifiers() {
        if (Keys.isKeyDown(KeyEvent.VK_CONTROL)) {
            if (Keys.isKeyDown(KeyEvent.VK_S)) {
                save();
            } else if (Keys.isKeyDown(KeyEvent.VK_O)) {
                open();
            } else  if (Keys.isKeyDown(KeyEvent.VK_Z)) {
                removeLast();
            }
        }
    }

    /**
     * Updates point data for construction of shapes.
     */
    private void updatePoints() {
        if (!isConstructing) {
            // begin construction
            if (Mouse.getActiveEvent() == Mouse.DRAGGED) {
                beginConstruction();
            }
        } else {
            if (Mouse.getActiveEvent() == Mouse.DRAGGED) {
                updateConstruction();
            } else if (Mouse.getActiveEvent() == Mouse.CLICKED) {
                // clicked outside shape, clear construction
                if (!constructionShape.getBounds().contains(Mouse.getCurPt())) {
                    clearConstruction();
                } else if (constructionShape.getBounds().contains(Mouse.getCurPt()) && Mouse.getClickCount() == 2) {
                    addObstacle();
                }
            }
        }
    }

    public static void resetPath() {
        drawPath.clear();
    }

    /**
     * Adds an obstacle, based on the newly constructed shape.
     */
    private void addObstacle() {
        if (activeBtn.getShape() != null && isConstructing) {
            Shape s = activeBtn.getShape();
            Obstacle obstacle = new Obstacle(s);
            obstacle.setColor(Utils.randColor(true, 150));
            if (addMovement) {
                obstacle.setMovement(Movement.randomSlowMovement(s));
            } else {
                obstacle.setMovement(new Movement(s));
            }
            obstacle.setIsLethal(isLethal);
            obstacles.add(obstacle);
            clearConstruction();
            activeBtn.resetShape();
        }
    }

    /**
     * Starts the shape construction process.
     */
    private void beginConstruction() {
        isConstructing = true;
        startPt = (Point)Mouse.getLastPt().clone();
        oldStartPt = (Point)startPt.clone();
    }

    /**
     * Updates the shape construction.
     */
    private void updateConstruction() {
        oldEndPt = (Point)endPt.clone();
        endPt = (Point)Mouse.getLastPt().clone();
        checkPoints();
    }

    /**
     * Checks to make sure that start and end pts are correct, such that
     * if the user drags past the starting pt, the shape will still
     * be display correctly.
     */
    private void checkPoints() {
        if (endPt.x < oldStartPt.x) {
            int endX = oldStartPt.x;
            int startX = endPt.x;
            startPt.x = startX;
            endPt.x = endX;
        }

        if (endPt.y < oldStartPt.y) {
            int endY = oldStartPt.y;
            int startY = endPt.y;
            startPt.y = startY;
            endPt.y = endY;
        }
    }

    /**
     * @return width of generated construction area
     */
    public int getConstructionWidth() {
        return Math.abs(endPt.x - startPt.x);
    }

    /**
     * @return height of generated construction area
     */
    public int getConstructionHeight() {
        return Math.abs(endPt.y - startPt.y);
    }

    /**
     * Clears out all points so that construction info is reset.
     */
    private void clearConstruction() {
        oldStartPt  = new Point();
        oldEndPt    = new Point();
        startPt     = new Point();
        endPt       = new Point();
        isConstructing = false;
        activeBtn.resetShape();
        constructionShape = new Rectangle();
        g2d.clearRect(0, 0, getWidth(), getHeight());
    }

    /**
     * Writes the created world to a file, that can be imported later. Although,
     * when a file is saved, it is automatically imported into the current
     * running application.
     */
    private void save() {
        Keys.clear();   // prevent multiple dialogs opening!
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(getWorldDirectory());
        chooser.setDialogTitle("Export World");
        int saveValue = chooser.showSaveDialog(gamePanel);
        final String ext = ".world";

        if (saveValue == JFileChooser.APPROVE_OPTION) {
            String filename = chooser.getSelectedFile().getAbsolutePath();
            if (!checkExtension(filename, ext)) {
                filename += ext;
            }
            if (exportToFile(filename, this)) {
                JOptionPane.showMessageDialog(gamePanel, "World saved successfully!");
                PlayState.addWorld(importFromFile(filename));
                WorldState.createWorldIcons();
            } else {
                JOptionPane.showMessageDialog(gamePanel, "An error occurred when saving world :(");
            }
        }
    }

    /**
     * Checks to see if the filename ends with specified extension
     * @param filename filename to check
     * @param ext extension to check
     * @return true if filename, ends with specified extension
     */
    private boolean checkExtension(String filename, String ext) {
        return filename.endsWith(ext);
    }

    /**
     * Opens all selected worlds from a dialog and imports them into
     * this application.
     */
    private void open() {
        Keys.clear();   // prevent multiple dialogs opening!
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter nameFilter = new FileNameExtensionFilter("DoodleLand Worlds", "world");
        chooser.setFileFilter(nameFilter);
        chooser.setCurrentDirectory(getWorldDirectory());
        chooser.setMultiSelectionEnabled(true);
        chooser.setDialogTitle("Open World");
        chooser.showOpenDialog(gamePanel);

        File[] files = chooser.getSelectedFiles();  // user can import multiple files

        for (File f : files) {
            PlayState.addWorld(importFromFile(f.getAbsolutePath()));
        }
        WorldState.createWorldIcons();  // update so all added worlds are choosable!
    }

    private File getWorldDirectory() {
        String dir = "res/worlds";
        return new File(dir);
    }

    /**
     * Clears out all obstacles.
     */
    private synchronized void clear() {
        obstacles.clear();
        clearConstruction();
    }

    /**
     * Removes last created obstacle.
     */
    private synchronized void removeLast() {
        Keys.clear();
        if (obstacles.size() > 0) {
            obstacles.remove(obstacles.size() - 1);
        }
    }

    public static void setActiveBtn(MyButton btn) {
        activeBtn = btn;
    }

    public Point getStartPt() {
        return startPt;
    }

    public Point getOldStartPt() {
        return oldStartPt;
    }

    public Point getEndPt() {
        return endPt;
    }

    public void setConstructionShape(Shape shape) {
        this.constructionShape = shape;
    }

    public int[] getPixelData() {
        return pxData;
    }
}

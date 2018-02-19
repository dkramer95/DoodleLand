package com.dkramer.objects;

import com.dkramer.input.Keys;
import com.dkramer.worlds.World;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by David Kramer on 2/21/2016.
 */

/*********************
 * OOP: Subclass
 Player is a descendant (subclass) from the GameObject super class.
 *********************/

public class Player extends GameObject {

    // Player move constants
    public static final int MOVE_LEFT   = 0;
    public static final int MOVE_RIGHT  = 1;
    public static final int MOVE_UP     = 2;
    public static final int MOVE_DOWN   = 3;

    /**
     * How many pixels from the ground, are allowed before movement is blocked
     */
    public static final int MOVEABLE_GROUND_HEIGHT = 12;
    public static final int MAX_JUMPS = 2;

    public static final int MOVE_SPEED = 3;

    private Animation animation;

    /*********************
     * OOP: State
     Variables for various player states.
     *********************/


    // movement key flags
    private boolean jumpKeyPressed;
    private boolean leftKeyPressed;
    private boolean rightKeyPressed;
    private boolean upKeyPressed;
    private boolean downKeyPressed;

    private boolean moveX;
    private boolean moveY;
    private boolean jumping;
    private int horizMoveDir;
    private int vertMovDir;
    private boolean onGround;
    private int groundY;
    private int jumpDistance;
    private int jumpKeyCount;   // how many times have we pressed jump?
    private boolean canJump;


    private int velX;
    private float velY;

    private float gravityFactor;

    private int particleFuel;       // amount of "fuel" this player has to shoot particles
    private BufferedImage jumpFrame;    // img for jumping
    private BufferedImage activeFrame;


    public Player() {
        init();
    }

    private void init() {
        setColor(Color.GREEN);
        loadSprites();
//        setBounds(10, 10, img.getWidth(), img.getHeight());
        setBounds(10, 10, 19, 50);
        particleFuel = 250;
    }

    private void loadSprites() {
        int spriteCount = 6;
        BufferedImage[] frames = new BufferedImage[spriteCount];
        for (int i = 0; i < spriteCount; i++) {
            try {
                frames[i] = ImageIO.read(new File("res\\PNG\\playerSprites\\stick" + i + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            jumpFrame = ImageIO.read(new File("res\\PNG\\playerSprites\\stickJump.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        animation = new Animation(frames);
        animation.setDelay(1);
        activeFrame = animation.getCurFrame();
    }

    public void update() {
        leftKeyPressed  = Keys.isKeyDown(Keys.LEFT);
        rightKeyPressed = Keys.isKeyDown(Keys.RIGHT);
        upKeyPressed    = Keys.isKeyDown(Keys.UP);
        downKeyPressed  = Keys.isKeyDown(Keys.DOWN);
        jumpKeyPressed  = Keys.isKeyDown(Keys.JUMP);
        updateMovement();
    }

    public void render(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.drawImage(activeFrame, x, y, null);
    }

    private void checkMoveX() {
        if (checkInputMoveX()) {
            DrawablePath path = world.getDrawPath();
            int newX = x + velX;    // where we will move too
            int checkY = y + height - MOVEABLE_GROUND_HEIGHT;   // check for paths that block walking

            try {
                if (path.isPixelEmpty(newX, checkY)
                        && path.isPixelEmpty(newX + width, checkY) && path.isPixelEmpty(newX + width, y)) {
                    x = newX;
                    y -= 1;
                }
            } catch (ArrayIndexOutOfBoundsException e) {}
        }
    }

    private boolean checkInputMoveX() {
        if (!moveX && rightKeyPressed) {
            updateMoveX(true, MOVE_SPEED, MOVE_RIGHT);
        } else if (!moveX && leftKeyPressed) {
            updateMoveX(true, -MOVE_SPEED, MOVE_LEFT);
        } else {
            moveX = false;
        }
        return moveX;
    }

    private void updateMoveX(boolean moveX, int velX, int moveDir) {
        this.moveX = moveX;
        this.velX = velX;
        this.horizMoveDir = moveDir;
        animation.update();
        activeFrame = animation.getCurFrame();
    }

    private boolean checkInputMoveY() {
        if (!moveY && upKeyPressed) {
            moveY = true;
            velY = -4;
        } else if (!moveY && downKeyPressed) {
            moveY = true;
            velY = 4;
        } else {
            moveY = false;
        }
        return moveY;
    }

    private void checkMoveY() {
        if (checkInputMoveY()) {
            int newY = (int)(y + velY);    // where we will move too
            DrawablePath path = world.getDrawPath();
            // check if top and bottom sides are clear
            try {
                if (path.isPixelEmpty(x, newY) && path.isPixelEmpty(x, newY + height)) {
                    y += velY;
                }
            } catch (ArrayIndexOutOfBoundsException e) {}
        }
    }

    /**
     * Checks to see if Player is on a ground path of some kind
     * @return true if the player, is on a ground path
     */
    private boolean checkOnGround() {
        DrawablePath path = world.getDrawPath();
        onGround = false;
        int checkX = x;
        int checkY = y + height;
        int length = width;

        int highestY = 0;
        for (int i = 0; i < length; i += 3) {
            try {
                if (path.getPixel(checkX + i, checkY) != DrawablePath.EMPTY_PIXEL) {
                    if (checkY > highestY) {
                        highestY = checkY;
                    }
                    onGround = true;
                    canJump = true;
                }
            } catch (ArrayIndexOutOfBoundsException e) {}
        }

        if (onGround) {
            y = highestY - height;
            groundY = y;
            activeFrame = animation.getCurFrame();
        }
        return onGround;
    }

    /**
     * Checks for player jumping and if the player is jumping, they
     * will be updated properly.
     */
    private void checkJump() {
        if (!jumping && jumpKeyPressed && canJump) {
            jumping = true;
            velY = -1;
            gravityFactor = -3.20f;
            activeFrame = animation.getCurFrame();
        }

        if (jumping) {
            activeFrame = jumpFrame;
            gravityFactor -= 0.2f;
            y += gravityFactor;

            jumpDistance += gravityFactor;

            if (jumpDistance < -60) {
                jumpDistance = 0;
                jumping = false;
                canJump = false;
            }
        }
    }

    /*********************
     * OOP: Behavior
     The movement behavior of a player object.
     *********************/

    /**
     * Updates the movement of this player.
     */
    private void updateMovement() {
        boolean onGround = checkOnGround();
        checkJump();
        checkMoveX();
        checkMoveY();

        if (!onGround && !jumping) {
            y += World.GRAVITY;
            if (y > world.getHeight()) {
                world.respawnPlayer();
            }
        } else if (!onGround && jumping) {
            if (y < 0) {
                y = 0;
                jumping = false;
            }
        }
    }

    public Particle getPeeParticle() {
        Particle p = null;

        if (particleFuel > 0) {
            switch (horizMoveDir) {
                case MOVE_LEFT:
                    p = new Particle(new Point(x + 10, y + height - 20),
                            8.0f, 0.5f, 100.0f, -10.0f, 0.0f,
                            new Color(255, 255, 255, 255), Color.BLACK);
                    break;
                case MOVE_RIGHT:
                    p = new Particle(new Point(x + 10, y + height - 20),
                            8.0f, 0.5f, 100.0f, 10.0f, 0.0f,
                            new Color(255, 255, 255, 255), Color.BLACK);
                    break;
            }
            particleFuel--;
        }
        return p;
    }

    public void setParticleFuel(int particleFuel) {
        this.particleFuel = particleFuel;
    }

    public int getParticleFuel() {
        return particleFuel;
    }

    public void setWorld(World world) {
        this.world = world;
        setLocation(world.getSpawnLocation());
    }
}

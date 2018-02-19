package com.dkramer.objects;

import java.awt.image.BufferedImage;

/**
 * This class contains an array of images (sprites), that are
 * used for objects that need to change their appearance,
 * depending on their state.
 * Created by David Kramer on 2/22/2016.
 */
public class Animation {
    public static final int DEFAULT_DELAY = 10;

    private BufferedImage[] frames;
    private int curFrame;       // current frame we are on
    private int totalFrames;    // total frames
    private int count;          // count for delay
    private int frameDelay;     // delay until we move to next frame



    public Animation(BufferedImage[] frames) {
        this.frames = frames;
        totalFrames = frames.length;
        frameDelay = DEFAULT_DELAY;
    }

    public void update() {
        count++;

        if (count == frameDelay) {
            curFrame++;
            count = 0;
        }

        if (curFrame == totalFrames) {
            curFrame = 0;
        }
    }

    public void setDelay(int delay) {
        this.frameDelay = delay;
    }

    public BufferedImage getFrame(int frameNum) {
        if (frameNum < 0 || frameNum > totalFrames) {
            throw new IllegalArgumentException("Invalid frame index!");
        }
        return frames[frameNum];
    }

    public BufferedImage getCurFrame() {
        return frames[curFrame];
    }
}

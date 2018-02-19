package com.dkramer.states;

import com.dkramer.input.Keys;
import com.dkramer.input.Mouse;
import com.dkramer.managers.GameManager;
import com.dkramer.objects.Background;
import com.dkramer.objects.ChoiceItem;
import com.dkramer.objects.DrawablePath;

import java.awt.*;

/**
 * The beginning intro state of the application that allows the user
 * to select from various options to advance into the game.
 * Created by David Kramer on 2/28/2016.
 */
public class IntroState extends State {
    private static final BasicStroke STROKE = new BasicStroke(1.25f);
    private static final Color COLOR = new Color(20, 20, 20, 150);
    // movement through choices array constants
    private static final int CHOICE_UP = -1;
    private static final int CHOICE_DOWN = 1;

    private int activeChoice = 0;   // what index are we in the choices array?
    private static ChoiceItem[] choices;
    private static TexturePaint texture;    //TODO texturing is broken right now!



    public void init() {
        texture = DrawablePath.createTexture();
        createChoices();
    }

    /**
     * Creates the individual choice selections for this intro state,
     * so that the user can navigate to other states of this game.
     */
    private void createChoices() {
        final int choiceCount = 3;
        choices = new ChoiceItem[choiceCount];
        /*********************
         * OOP: Object
         ChoiceItem objects have been created from the ChoiceItem class.
         *********************/

        choices[0] = new ChoiceItem("Select A World",   GameManager.WORLD_STATE);
        choices[1] = new ChoiceItem("World Editor",     GameManager.EDITOR_STATE);
//        choices[2] = new ChoiceItem("How To Play",      GameManager.HELP_STATE);
        choices[2] = new ChoiceItem("About Game",       GameManager.ABOUT_STATE);

        // layout choices!
        final int Y_OFFSET = 125;

        for (int i = 0; i < choiceCount; i++) {
            ChoiceItem choice = choices[i];
            int x = (getWidth() - choice.getSelectionBox().width) / 2;
            int y = 250 + (i * Y_OFFSET);
            choice.setLocation(x, y);
            choices[i] = choice;
        }
    }

    public void render() {
        Background.render(g2d);
        drawTitle();
        drawChoices();
    }

    private void drawTitle() {
        g2d.setFont(new Font("Courier New", Font.BOLD, 85));
        g2d.drawString("DoodleLand", 375, 100);
    }

    private void drawChoices() {
        g2d.setColor(COLOR);
        g2d.setStroke(STROKE);
        for (int i = 0; i < choices.length; i++) {
            ChoiceItem c = choices[i];
            if (i == activeChoice) {
                g2d.fill(c.getTextOutline());
                g2d.draw(c.getSelectionBox());
            } else {
                g2d.draw(c.getTextOutline());
            }
        }
    }

    public void update() {
//        Background.update();
        checkMouse();
    }

    private void updateChoice(int direction) {
        activeChoice += direction;
        // ensure we stay in range of choices
        if (activeChoice >= choices.length) {
            activeChoice = 0;
        } else if (activeChoice < 0) {
            activeChoice = choices.length - 1;
        }
    }

    public void handleInput() {
        checkKeys();
        checkMouse();
    }

    private void checkKeys() {
        if (Keys.isKeyDown(Keys.DOWN)) {
            updateChoice(CHOICE_DOWN);
        } else if (Keys.isKeyDown(Keys.UP)) {
            updateChoice(CHOICE_UP);
        } else if (Keys.isKeyDown(Keys.ENTER)) {
            GameManager.setState(choices[activeChoice].getStateType());
        }
    }

    private void checkMouse() {
        for (int i = 0; i < choices.length; i++) {
            ChoiceItem c = choices[i];
            if (c.getSelectionBox().contains(Mouse.getCurPt())) {
                activeChoice = i;
                break;
            }
        }
        if (Mouse.getActiveEvent() == Mouse.CLICKED) {
            GameManager.setState(choices[activeChoice].getStateType());
        }
    }
}

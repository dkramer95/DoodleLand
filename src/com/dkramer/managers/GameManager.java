package com.dkramer.managers;

import com.dkramer.states.*;

import java.awt.geom.AffineTransform;

/**
 * This class manages the states of the Game, and actively
 * renders and updates the current state.
 * Created by David Kramer on 2/21/2016.
 */
public class GameManager {

    /**
     * GameState constants
     */
    public static final int INTRO_STATE     = 0;
    public static final int WORLD_STATE     = 1;
    public static final int PLAY_STATE      = 2;
    public static final int ABOUT_STATE     = 3;
    public static final int HELP_STATE      = 4;
    public static final int EDITOR_STATE    = 5;

    private static State[] statesArray;
    private static TransitionState transition;

    /*********************
     * OOP: Polymorphism: Inheritance
     State is abstract, but as there are other states which have been
     created and subclassed from state, polymorphism can be used.
     *********************/

    private static State activeState;
    private static int activeStateID;




    private GameManager() {}    // don't instantiate, use init()

    public static void init() {
        createStates();
        transitionToState(INTRO_STATE);   //TODO uncomment this, when finished!
//        setState(EDITOR_STATE);
    }

    public static void transitionToState(int state) {
        transition.reset();
        transition.setNextStateType(state);
        activeState = transition;
    }

    private static void createStates() {
        transition = new TransitionState(INTRO_STATE);
        statesArray = new State[7];
        statesArray[INTRO_STATE]    = new IntroState();
        statesArray[PLAY_STATE]     = new PlayState();
        statesArray[WORLD_STATE]    = new WorldState();
        statesArray[ABOUT_STATE]    = new AboutState();
        statesArray[HELP_STATE]     = new HelpState();
        statesArray[EDITOR_STATE]   = new EditorState();
    }

    public static void setState(int state) {
        if (state < 0 || state > 5) {
            throw new IllegalArgumentException("Invalid state!");
        }
        activeStateID = state;
        State.getGraphics().setTransform(new AffineTransform());    // ensure anything else is cleared out!
        activeState = statesArray[state];

        // TODO temporary fix for glitchy path, being pulled into the editor.
        if (state == EDITOR_STATE) {
            EditorState editor = (EditorState)statesArray[EDITOR_STATE];
            editor.fixPath();
        }
    }

    public static int getStateID() {
        return activeStateID;
    }

    public static void handleInput() {
        activeState.handleInput();
    }

    public static void update() {
        activeState.update();
    }

    public static void render() {
        activeState.render();
    }

}

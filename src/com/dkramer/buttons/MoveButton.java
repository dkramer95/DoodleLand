package com.dkramer.buttons;

import com.dkramer.input.Mouse;
import com.dkramer.objects.Obstacle;
import com.dkramer.physics.Movement;

/**
 * @deprecated --> Delete this! Don't have time to finish implementing it.
 * Created by David Kramer on 3/9/2016.
 */
public class MoveButton extends MyButton {
    private static final String BTN_TEXT = "Move Object";

    public MoveButton(int x, int y) {
        super(x, y);
        this.text = BTN_TEXT;
    }

    public void handleInput() {
        if (Mouse.getActiveEvent() == Mouse.DRAGGED) {
            for (Obstacle o : editor.getObstacles()) {
                if (o.contains(Mouse.getCurPt())) {
                    o.setLocation(Mouse.getCurPt());
                    System.out.println("MOVING OBJECT: " + o);
                }
            }
        }
    }
}

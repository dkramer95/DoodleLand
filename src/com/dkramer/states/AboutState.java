package com.dkramer.states;

import com.dkramer.input.Keys;
import com.dkramer.input.Mouse;
import com.dkramer.managers.GameManager;
import com.dkramer.utils.Utils;

import java.awt.*;

/**
 * Created by David Kramer on 2/21/2016.
 */
public class AboutState extends State {
    private static Font FONT = new Font("Courier New", Font.BOLD, 30);
    private static String info = "Designed & Developed by David Kramer";
    private static String subInfo = "Q2 Final Project 2016";
    private static String title = "\"DoodleLand\"";



    public void init() {

    }

    public void render() {
        g2d.setColor(new Color(20, 20, 20, 20));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setFont(FONT);
        g2d.setColor(Color.decode("#FFDE00"));
        g2d.drawString(title, Utils.getCenterX(g2d, title), 200);
        g2d.drawString(info, Utils.getCenterX(g2d, info), 300);
        g2d.drawString(subInfo, Utils.getCenterX(g2d, subInfo), 400);
    }

    public void update() {

    }

    public void handleInput() {
        if (Keys.isKeyDown(Keys.PAUSE)) {
            GameManager.setState(GameManager.INTRO_STATE);
        }
    }
}

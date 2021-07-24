package com.main;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class PLabel extends Label{

    private static BitmapFont font;
    private static Label.LabelStyle labelStyle;

    static {
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().setScale(1.0f);

        labelStyle = new  Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.RED;
    }

    public PLabel(String text, GridPoint2 position) {
        super(text, labelStyle);
        setPosition( ParticleGrid.width * Main.pixelSize + position.x, position.y );
    }
}

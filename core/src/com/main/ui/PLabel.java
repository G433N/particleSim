package com.main.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.main.Main;
import com.main.World;

public class PLabel extends Label{

    public static final BitmapFont font;
    public static final Color color = Color.BLUE;
    private static final Label.LabelStyle labelStyle;

    static {
        font = new BitmapFont();
        font.getData().setScale(1.0f);

        labelStyle = new  Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = color;
    }

    public PLabel(String text, GridPoint2 position) {
        super(text, labelStyle);
        setPosition( World.width * Main.pixelSize + position.x, position.y );
    }
}

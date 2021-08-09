package com.main.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.main.Main;
import com.main.World;

public class PButton extends TextButton {// Particle button temp name

    private static BitmapFont font;
    private static TextButton.TextButtonStyle textButtonStyle;


    static {
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().setScale(1.0f);

        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.fontColor = Color.RED;
    }

    public PButton(String text, GridPoint2 position, ChangeListener changeListener) {
        super(text, textButtonStyle);
        setPosition( World.width * Main.pixelSize + position.x, position.y );
        addListener(changeListener);
    }
}

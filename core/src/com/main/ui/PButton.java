package com.main.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.main.Main;
import com.main.World;

public class PButton extends TextButton {// Particle button temp name

    private static final TextButton.TextButtonStyle textButtonStyle;


    static {
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = PLabel.font;
        textButtonStyle.fontColor = PLabel.color;
    }

    public PButton(String text, GridPoint2 position, ChangeListener changeListener) {
        super(text, textButtonStyle);
        setPosition( World.width * Main.pixelSize + position.x, position.y );
        addListener(changeListener);
    }
}

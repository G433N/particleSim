package com.main.ui;


import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.main.Main;
import com.main.math.Int2;
import com.main.particle.World;

public class PCheckButton extends CheckBox {


    public PCheckButton(String text, Int2 position, Int2 size, ChangeListener changeListener) {
        super(text, PLabel.skin);
        setPosition( World.width * Main.pixelSize + position.x, position.y);
        setSize(size.x,size.y);
        addListener(changeListener);
    }
}

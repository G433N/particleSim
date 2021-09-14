package com.main.ui;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.main.Main;
import com.main.math.Int2;
import com.main.particle.ParticleWorld;

public class PButton extends TextButton {// Particle button temp name


    public PButton(String text, Int2 position, Int2 size, ChangeListener changeListener) {
        super(text, PLabel.skin);
        setPosition( ParticleWorld.width * Main.pixelSize + position.x, position.y);
        setSize(size.x,size.y);
        addListener(changeListener);
    }
}

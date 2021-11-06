package com.main.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.main.Main;
import com.main.math.Int2;
import com.main.particle.ParticleWorld;

public class PLabel extends Label{

    public static final Skin skin;

    static {
        skin = new Skin(Gdx.files.internal("ui/metalui/metal-ui.json")); // Credit :  https://ray3k.wordpress.com/metal-ui-skin-for-libgdx/
    }

    public PLabel(String text, Int2 position) {
        super(text, skin);
        setPosition( ParticleWorld.width * Main.pixelSize + position.x, position.y );
    }
}

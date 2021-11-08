package com.main.particle;

import com.badlogic.gdx.graphics.Color;
import com.main.particle.states.Solid;

import static java.lang.Math.random;

public class Wood extends Solid {

    protected Wood() {
        super("wood", 5, 0.15f);
        this.colorOffset = (float) random();
    }

    @Override
    protected void primaryRule(float deltaTime) {

    }

    private final float colorOffset;

    @Override
    public Color getColor() {
        Color A = new Color(68/255f , 48/255f, 34/255f, 1);
        Color B = new Color(85/255f , 60/255f, 42/255f, 1);
        return A.lerp(B, colorOffset);
    }
}

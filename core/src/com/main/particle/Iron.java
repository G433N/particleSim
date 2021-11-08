package com.main.particle;

import com.badlogic.gdx.graphics.Color;
import com.main.particle.states.Solid;

import static java.lang.Math.random;

public class Iron extends Solid {

    protected Iron() {
        super("iron", Integer.MAX_VALUE, 0);
        this.colorOffset = (float) random();
    }

    @Override
    protected void primaryRule(float deltaTime) {

    }

    private final float colorOffset;

    @Override
    public Color getColor() {
        Color A = new Color(120/255f , 120/255f, 120/255f, 1);
        Color B = new Color(127/255f , 127/255f, 127/255f, 1);
        return A.lerp(B, colorOffset);
    }
}

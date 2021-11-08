package com.main.particle;

import com.badlogic.gdx.graphics.Color;
import com.main.math.Float2;
import com.main.particle.states.Solid;

import java.util.Random;

import static java.lang.Math.random;

public class Sand extends Solid {

    protected Sand() {
        super("sand", 3, 0);
        this.colorOffset = (float) (random() * 0.05);
        this.elasticity = new Float2(0.3f, 0.05f);
        this.friction = new Float2(0.1f, 0.1f);
    }

    private final float colorOffset;

    @Override
    public Color getColor() {
        return new Color(194/255f, 178/255f - this.colorOffset, .5f, 1);
    }
}

package com.main.particle;

import com.badlogic.gdx.graphics.Color;
import com.main.math.Float2;
import com.main.particle.states.Liquid;

import static java.lang.Math.min;
import static java.lang.Math.random;

public class Water extends Liquid {

    protected Water() {
        super("water", 2, 0);
        this.colorOffset = (float) (random() * 0.05);
        this.thickness = 0.2f;
        this.elasticity = new Float2(0.3f, 0.3f);
        this.friction = new Float2(0.1f, 0.1f);
    }

    private final float colorOffset;

    @Override
    public Color getColor() { // FIXME : Color flickering
        float blue = (1f - min(this.depth/50f + this.colorOffset, 0.7f));
        return new Color(0, 0, blue, 1);
    }
}

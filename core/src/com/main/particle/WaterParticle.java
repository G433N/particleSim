package com.main.particle;

import com.badlogic.gdx.graphics.Color;

import java.util.Random;

import static java.lang.Math.min;
import static java.lang.Math.random;

public class WaterParticle extends LiquidParticle{

    protected WaterParticle() {
        super("water", 2, 0);
        this.colorOffset = (float) (random() * 0.05);
        this.friction = 1;
    }

    private final float colorOffset;

    @Override
    public Color getColor() { // FIXME : Color flickering
        float blue = (1f - min(this.depth/50f + this.colorOffset, 0.7f));
        return new Color(0, 0, blue, 1);
    }
}

package com.main.particle;

import com.badlogic.gdx.graphics.Color;

import static java.lang.Math.random;

public class WoodParticle extends SolidParticle {

    protected WoodParticle() {
        super("wood", 5, 0.15f);
        this.colorOffset = (float) random();
    }

    @Override
    public void primaryUpdate(float deltaTime) {
        this.collisionDetection();
    }

    private final float colorOffset;

    @Override
    public Color getColor() {
        Color A = new Color(68/255f , 48/255f, 34/255f, 1);
        Color B = new Color(85/255f , 60/255f, 42/255f, 1);
        return A.lerp(B, colorOffset);
    }
}

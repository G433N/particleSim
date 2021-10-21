package com.main.particle;

import com.badlogic.gdx.graphics.Color;

import static java.lang.Math.random;

public class IronParticle extends SolidParticle {

    protected IronParticle() {
        super("iron", Integer.MAX_VALUE, 0);
        this.colorOffset = (float) random();
    }

    public void primaryUpdate(float deltaTime) {
        this.collisionDetection();
    }

    private final float colorOffset;

    @Override
    public Color getColor() {
        Color A = new Color(120/255f , 120/255f, 120/255f, 1);
        Color B = new Color(127/255f , 127/255f, 127/255f, 1);
        return A.lerp(B, colorOffset);
    }
}

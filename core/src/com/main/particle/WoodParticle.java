package com.main.particle;

import com.badlogic.gdx.graphics.Color;

public class WoodParticle extends SolidParticle {

    protected WoodParticle() {
        super("wood", 5, 0.3f);
    }

    @Override
    public void primaryUpdate(float deltaTime) {
        this.collisionDetection();
    }

    @Override
    public Color getColor() {
        return Color.BROWN;
    }
}

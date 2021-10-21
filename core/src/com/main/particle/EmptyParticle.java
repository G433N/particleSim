package com.main.particle;

import com.badlogic.gdx.graphics.Color;

public class EmptyParticle extends Particle {

    public EmptyParticle() {
        super("empty", -1, 0.02f);
    }

    @Override
    public void primaryUpdate(float deltaTime) {}

    @Override
    public void secondaryUpdate(float deltaTime) {}

    @Override
    public Color getColor() {
        return new Color(72/255f, 209/255f, 204/255f, 1f);
    }
}

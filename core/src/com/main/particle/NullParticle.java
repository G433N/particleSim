package com.main.particle;

public class NullParticle extends Particle {

    public NullParticle() {
        super("null", Integer.MAX_VALUE);
    }

    @Override
    public void primaryUpdate(float deltaTime) {}

    @Override
    public void secondaryUpdate(float deltaTime) {}
}

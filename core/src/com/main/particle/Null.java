package com.main.particle;

public class Null extends Particle {

    public Null() {
        super("null", Integer.MAX_VALUE, 0);
    }

    @Override
    public void primaryUpdate(float deltaTime) {}

    @Override
    public void secondaryUpdate(float deltaTime) {}
}

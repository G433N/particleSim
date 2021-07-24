package com.particle;

public class Particle {

    private final Particles type;
    // sprite
    // flags

    public int velocity = 1;

    public Particle(Particles type) {
        this.type = type;
    }

    public Particles getType() {
        return type;
    }
}
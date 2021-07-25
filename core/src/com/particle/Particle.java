package com.particle;

public class Particle {

    private final Particles type;
    // sprite
    // flags

    public int density;
    public boolean liquid;

    public Particle(Particles type) { //Load from data
        this.type = type;
        if (type == Particles.AIR) {
            this.density = 0;
            this.liquid = false;
        } else if (type == Particles.NULL) {
            this.density = 9999;
            this.liquid = false;
        } else if (type == Particles.SAND) {
            this.density = 2;
            this.liquid = false;
        } else if (type == Particles.WATER) {
            this.density = 1;
            this.liquid = true;
        } else {
            throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    public Particles getType() {
        return type;
    }
}
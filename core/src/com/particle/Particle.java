package com.particle;

public class Particle {

    private final Particles type;
    // sprite
    // flags

    public int density;
    public boolean liquid = false;
    public boolean loose = false;

    public Particle(Particles type) { //Load from data
        this.type = type;
        if (type == Particles.AIR) {
            this.density = 0;
        } else if (type == Particles.NULL) {
            this.density = 9999;
        } else if (type == Particles.SAND) {
            this.density = 2;
            this.loose = true;
        } else if (type == Particles.WATER) {
            this.density = 1;
            this.liquid = true;
        } else if (type == Particles.DIRT) {
            this.density = 2;
        } else if (type == Particles.METAL) {
            this.density = 9999;
        } else {
            throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    public Particles getType() {
        return type;
    }
}
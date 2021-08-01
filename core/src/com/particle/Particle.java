package com.particle;

public class Particle {

    public final Particles type;
    public boolean liquid = false;
    public int density;

    public Particle(Particles type) {
        this.type = type;

        switch (type) {

            case AIR:
                density = 0;
                break;

            case NULL:
                density = 999;
                break;

            case SAND:
                density = 2;
                break;

            case WATER:
                density = 1;
                liquid = true;
                break;

            default:
                break;

        }
    }
}
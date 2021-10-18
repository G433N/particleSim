package com.main.particle;

public class SolidParticle extends Particle{

    protected SolidParticle(String type, int density, float flammability) {
        super(type, density, flammability);
        this.state = ParticleState.SOLID;
    }

}

package com.main.particle;

public class SolidParticle extends Particle{

    protected SolidParticle(String type, int density) {
        super(type, density);
        this.state = ParticleState.SOLID;
    }

}

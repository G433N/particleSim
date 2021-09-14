package com.main.particle;

public class LiquidParticle extends Particle {

    protected int depth = 0;

    protected LiquidParticle(String type, int density) {
        super(type, density);
        this.state = ParticleState.LIQUID;
    }

}

package com.main.particle;

public class LiquidParticle extends Particle {

    protected int depth = 0;

    protected LiquidParticle(String type, int density) {
        super(type, density);
        this.state = ParticleState.LIQUID;
    }

    @Override
    protected void primaryRule(float deltaTime) {

        final Particle particleUP = world.getParticle(this.position.offset(0, 1));

        if (particleUP.state == ParticleState.LIQUID) {
            LiquidParticle liquidUP = (LiquidParticle) particleUP;
            this.depth = liquidUP.depth + 1;
        }
        else this.depth = 0;

    }

}

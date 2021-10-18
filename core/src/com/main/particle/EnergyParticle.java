package com.main.particle;

import com.badlogic.gdx.graphics.Color;

public class EnergyParticle extends Particle {

    protected EnergyParticle(String type) {
        super(type, 0, 0);
        this.state = ParticleState.ENERGY;
    }

    @Override
    public void primaryUpdate(float deltaTime) {

    }
    @Override
    public void secondaryUpdate(float deltaTime) {
        if(this.moved) return;
        else this.moved = true;
    }
}

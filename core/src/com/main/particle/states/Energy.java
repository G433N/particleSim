package com.main.particle.states;

import com.main.particle.Particle;
import com.main.particle.states.State;

public class Energy extends Particle {

    protected Energy(String type) {
        super(type, Integer.MAX_VALUE, 0);
        this.state = State.ENERGY;
    }

    @Override
    public void primaryUpdate(float deltaTime) {

    }

    @Override
    protected void secondaryRule(float deltaTime) {

    }
}

package com.main.particle.states;

import com.main.particle.Particle;
import com.main.particle.states.State;

public class Gas extends Particle {

    protected float gravityModifier;
    protected int gravityDirection;
    // protected float defusion rate

    protected Gas(String type, int density, float flammability, float gravityModifier) {
        super(type, density, flammability);
        this.state = State.GAS;
        this.gravityModifier = gravityModifier;
        this.gravityDirection = (int) (gravityModifier/Math.abs(gravityModifier));
    }
}

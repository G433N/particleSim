package com.main.particle.states;

import com.main.particle.Particle;

import java.util.Random;

public class Solid extends Particle {

    protected Solid(String type, int density, float flammability) {
        super(type, density, flammability);
        this.state = State.SOLID;
    }

    @Override
    protected void secondaryRule(float deltaTime) {
        super.secondaryRule(deltaTime);

        if(isInAir || !isFreeFalling) return;

        java.util.Random random = new Random();

        int dir = random.nextInt(2);
        if (dir == 0) dir = -1;

        if (world.getParticle(this.position.offset(dir, -1)).density < this.density) {
            world.movePosition(position.x, position.y, dir, - 1);
        }
    }
}

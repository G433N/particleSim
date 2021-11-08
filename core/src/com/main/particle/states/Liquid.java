package com.main.particle.states;

import com.main.particle.Particle;

import java.util.Random;

public class Liquid extends Particle {

    protected int depth = 0;
    // TODO Spread

    protected Liquid(String type, int density, float flammability) {
        super(type, density, flammability);
        this.state = State.LIQUID;
    }

    @Override
    protected void primaryRule(float deltaTime) {
        super.primaryRule(deltaTime);


        final Particle particleUP = world.getParticle(this.position.offset(0, 1));

        if (particleUP.state == State.LIQUID) {
            Liquid liquidUP = (Liquid) particleUP;
            this.depth = liquidUP.depth + 1;
        }
        else this.depth = 0;
    }

    @Override
    protected void secondaryRule(float deltaTime) {

        super.secondaryRule(deltaTime);

        if(isInAir) return;

        if(world.getParticle(this.position.offset(0, -1)).density < this.density) {
            world.movePosition(this.position.x, this.position.y, 0, -1);
            return;
        }

        java.util.Random random = new Random();

        int dir = random.nextInt(2);
        if (dir == 0) dir = -1;



        for (int i = 1; i <= 4; i++) {

            if (world.getParticle(this.position.offset(dir, -1)).density < this.density) {
                world.movePosition(position.x, position.y, dir, - 1);
            }
            else if ( world.getParticle(position.x + dir, position.y).density < density) {
                world.movePosition(position.x, position.y, dir, 0);
            }
            else return;
        }
    }

}

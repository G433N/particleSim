package com.main.particle;

import java.util.Random;

public class LiquidParticle extends Particle {

    protected int depth = 0;

    protected LiquidParticle(String type, int density, float flammability) {
        super(type, density, flammability);
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

    @Override
    protected void secondaryRule(float deltaTime) {
        if(world.getParticle(this.position.offset(0, -1)).density < this.density) {
            world.movePosition(this.position.x, this.position.y, 0, -1);
            return;
        }

        java.util.Random random = new Random();

        int dir = random.nextInt(2);
        if (dir == 0) dir = -1;

        if (world.getParticle(this.position.offset(dir, -1)).density < this.density) {
            world.movePosition(position.x, position.y, dir, - 1);
            return;
        }

        for (int i = 1; i <= 2; i++) {
            if ( world.getParticle(position.x + dir, position.y).density < density) {
                world.movePosition(position.x, position.y, dir, 0);
            }
            else return;
        }
    }

}

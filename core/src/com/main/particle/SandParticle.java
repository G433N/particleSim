package com.main.particle;

import com.badlogic.gdx.graphics.Color;

import java.util.Random;

import static java.lang.Math.random;

public class SandParticle extends SolidParticle {

    protected SandParticle() {
        super("sand", 2, 0);
        this.colorOffset = (float) (random() * 0.05);
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
        }
    }

    private final float colorOffset;

    @Override
    public Color getColor() {
        return new Color(194/255f, 178/255f - this.colorOffset, .5f, 1);
    }
}

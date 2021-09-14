package com.main.particle;

import com.badlogic.gdx.graphics.Color;

import java.util.Random;

public class SandParticle extends SolidParticle {

    protected SandParticle() {
        super("sand", 2);
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

    @Override
    public Color getColor() {
        return new Color(194/255f, 178/255f, .5f, 1);
    }
}

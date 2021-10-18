package com.main.particle;

import com.badlogic.gdx.graphics.Color;

import java.util.Random;

import static java.lang.Math.min;
import static java.lang.Math.random;

public class WaterParticle extends LiquidParticle{

    protected WaterParticle() {
        super("water", 1, 0);
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
            return;
        }

        for (int i = 1; i <= 2; i++) {
            if ( world.getParticle(position.x + dir, position.y).density < density) {
                world.movePosition(position.x, position.y, dir, 0);
            }
            else return;
        }
    }

    private final float colorOffset;

    @Override
    public Color getColor() { // FIXME : Color flickering
        float blue = (float) (1f - min(this.depth/50f + this.colorOffset, 0.7f));
        return new Color(0, 0, blue, 1);
    }
}

package com.main.particle;

import com.badlogic.gdx.graphics.Color;

import java.util.Random;

public class WaterParticle extends LiquidParticle{

    protected WaterParticle() {
        super("water", 1);
    }

    @Override
    protected void primaryRule(float deltaTime) {

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

    @Override
    public Color getColor() {
        return new Color(0, 0, 1, 1);
    }
}

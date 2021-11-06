package com.main.particle;

import com.badlogic.gdx.graphics.Color;
import com.main.math.Int2;

public class WaterSource extends EnergyParticle {

    private int cooldown = 3;
    private int timer = cooldown;

    protected WaterSource() {
        super("waterSource");
    }

    @Override
    public void primaryUpdate(float deltaTime) {
        timer--;
    }

    @Override
    public void secondaryUpdate(float deltaTime) {
        if(timer == 0) {
            world.setParticle(new Int2(this.position.offset(0, -1)), new WaterParticle());
            world.setParticle(new Int2(this.position.offset(0, 1)), new WaterParticle());
            world.setParticle(new Int2(this.position.offset(1, 0)), new WaterParticle());
            world.setParticle(new Int2(this.position.offset(-1, 0)), new WaterParticle());
            timer = cooldown;
        }
    }

    @Override
    public Color getColor() {
        return new Color(0, 0, 0.7f, 1);
    }
}

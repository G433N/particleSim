package com.main.particle;

import com.badlogic.gdx.graphics.Color;
import com.main.math.Int2;
import com.main.particle.states.Energy;

public class WaterSource extends Energy {

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
            world.setParticle(new Int2(this.position.offset(0, -1)), new Water());
            world.setParticle(new Int2(this.position.offset(0, 1)), new Water());
            world.setParticle(new Int2(this.position.offset(1, 0)), new Water());
            world.setParticle(new Int2(this.position.offset(-1, 0)), new Water());
            timer = cooldown;
        }
    }

    @Override
    public Color getColor() {
        return new Color(0, 0, 0.7f, 1);
    }
}

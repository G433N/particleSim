package com.main.particle;

import com.badlogic.gdx.graphics.Color;

public class EmptyParticle extends Particle {

    public EmptyParticle() {
        super("empty", 0);
    }

    @Override
    public Color getColor() {
        int x = this.position.x + this.position.y;
        if(x % 2 == 0) {
            return Color.GRAY;
        }
        else return Color.DARK_GRAY;

    }
}

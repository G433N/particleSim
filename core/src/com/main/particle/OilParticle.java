package com.main.particle;

public class OilParticle extends LiquidParticle {
    protected OilParticle() {
        super("oil", 1, 0.5f);
        this.friction = 1.5f;
    }
}

package com.main.particle;

import com.main.particle.states.Liquid;

public class Oil extends Liquid {
    protected Oil() {
        super("oil", 1, 0.5f);
        this.thickness = 0.5f;
    }
}

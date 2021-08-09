package com.main.math;

import com.badlogic.gdx.math.Vector2;

public class Float2 extends Vector2 {

    public Float2 (float x, float y) {
        super(x, y);
    }

    public Float2(Vector2 v) {
        super(v);
    }

    public Float2() {
        super();
    }

    public Float2 add(Float2 f) {
        x += f.x;
        y += f.y;
        return this;
    }

    public Float2 add(int x, int y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Float2 scl (float scalar) {
        x *= scalar;
        y *= scalar;
        return this;
    }

    public float dst(Float2 position) {
        final float x_d = position.x - this.x;
        final float y_d = position.y - this.y;
        return (float)Math.sqrt(x_d * x_d + y_d * y_d);
    }
}

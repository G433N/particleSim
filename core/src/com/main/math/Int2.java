package com.main.math;

import com.badlogic.gdx.math.GridPoint2;

public class Int2 extends GridPoint2 {

    public Int2(int x, int y) {
        super(x, y);
    }

    public Int2() {
        super();
    }

    public Float2 toFloat2() {
        return new Float2( (float) this.x, (float) this.y);
    }
}

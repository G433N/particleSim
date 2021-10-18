package com.main.math;

import com.badlogic.gdx.math.GridPoint2;

public class Int2 extends GridPoint2 {

    public Int2(int x, int y) {
        super(x, y);
    }

    public Int2() {
        super();
    }

    public Int2(Int2 pos) {
        super(pos.x, pos.y);
    }

    public Float2 toFloat2() {
        return new Float2( (float) this.x, (float) this.y);
    }

    public Int2 offset(int x, int y) {
        return new Int2(this.x + x, this.y + y);
    }

    public Int2 offset(Int2 v) {
        return offset(v.x, v.y);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}

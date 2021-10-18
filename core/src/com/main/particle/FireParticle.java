package com.main.particle;

import com.badlogic.gdx.graphics.Color;
import com.main.math.Int2;

import static java.lang.Math.random;

public class FireParticle extends EnergyParticle {

    private final int maxLifeTime;
    private int lifeTime; // in frames
    private int spreadInterval = 4;

    protected FireParticle(int lifeTime) {
        super("fire");
        this.lifeTime = lifeTime;
        this.maxLifeTime = this.lifeTime;
    }
    protected FireParticle() {
        super("fire");
        this.lifeTime = 40;
        this.maxLifeTime = this.lifeTime;
    }

    @Override
    public void primaryUpdate(float deltaTime) {
        this.lifeTime--;
    }

    @Override
    public void secondaryUpdate(float deltaTime) {
        super.secondaryUpdate(deltaTime);
        if (this.lifeTime <= 0) {
            world.setParticle(this.position, new EmptyParticle());
        }

        if(lifeTime % spreadInterval != 0) return;

        for (Int2 offset : Particle.SURROUNDINGOFFSETS) {
            Int2 pos = this.position.offset(offset);
            if(random() < world.getParticle(pos).flammability) { // random can't return 1;
                world.setParticle(pos, new FireParticle());
                world.getParticle(pos).moved = true;
            }
        }
    }

    @Override
    public Color getColor() { // Color A to color B using linear interpolation

        float t = (float) (this.lifeTime)/this.maxLifeTime;

        Color B = new Color(254/255f, 222/255f, 23/255f, 0.5f);
        Color A = new Color(247/255f, 55/255f, 25/255f, 1f);

        return B.lerp(A, t);
    }
}

package com.main.particle;

import com.badlogic.gdx.graphics.Color;
import com.main.math.Int2;
import com.main.particle.states.Energy;

import static java.lang.Math.random;

public class Fire extends Energy {

    private final int maxLifeTime;
    private int lifeTime; // in frames
    private int spreadInterval = 5;

    protected Fire(int lifeTime) {
        super("fire");
        this.lifeTime = lifeTime;
        this.maxLifeTime = this.lifeTime;
    }
    protected Fire() {
        super("fire");
        this.lifeTime = 30 + (int) (random() * 20f);
        this.maxLifeTime = 70;
    }

    @Override
    public void primaryUpdate(float deltaTime) {
        this.lifeTime--;
    }

    @Override
    public void secondaryUpdate(float deltaTime) {
        if(this.updated) return;
        else this.updated = true;
        if (this.lifeTime <= 0) {
            world.setParticle(this.position, new Empty());
        }

        if(lifeTime % spreadInterval != 0) return;

        boolean spread = false;

        // Omni directional spread
        for (Int2 offset : Particle.SURROUNDINGOFFSETS) {
            Int2 pos = this.position.offset(offset);
            if(random() < world.getParticle(pos).flammability) { // random can't return 1;
                if (world.getParticle(pos).type.equals("empty")) world.setParticle(pos, new Fire(this.lifeTime));
                else world.setParticle(pos, new Fire());
                world.getParticle(pos).updated = true;
                spread = true;
            }
        }

        if (spread) return;

        // Upwards flames
        for (Int2 offset : new Int2[] {new Int2(0, 1), new Int2(0, 2), new Int2(0, 3), new Int2(0, 4)}) { // TODO : Make to function
            Int2 pos = this.position.offset(offset);
            if(random() < world.getParticle(pos).flammability) { // random can't return 1;
                world.setParticle(pos, new Fire(this.lifeTime + (int) (random() * 10f)));
                world.getParticle(pos).updated = true;
            }
        }
    }

    @Override
    public Color getColor() { // Color A to color B using linear interpolation

        float t = (float) (this.lifeTime)/this.maxLifeTime;

        Color B = new Color(254/255f, 222/255f, 23/255f, 0.5f);
        Color A = new Color(247/255f, 100/255f, 25/255f, 1f);

        return B.lerp(A, t);
    }
}

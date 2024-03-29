package com.main;

import com.badlogic.gdx.math.Vector2;
import com.main.math.Float2;
import com.main.math.Int2;

import java.util.Random;

import static com.badlogic.gdx.math.MathUtils.random;
import static java.lang.Math.min;
import static java.lang.Math.round;

public class OldWorld {


    // TODO : size get()
    public static int width = 128;
    public static int length = 128;

    private final int gravity = -10;

    public boolean simulate = false;

    OldParticle[][] grid;


    public OldWorld() {

        this.grid = new OldParticle[width][length];

        // Fill grid with air particle

        for (int x = 0; x < width; x++) {

            for (int y = 0; y < length; y++) {

                this.grid[x][y] = new OldParticle("air");
                this.grid[x][y].position = new Int2(x, y);
            }
        }
    }

    // Setters and getters
    public OldParticle getParticle(int x, int y) {
        if (0 <= x && x < width && 0 <= y && y < length) {
            return this.grid[x][y];
        }
        return new OldParticle("null");
    }

    public OldParticle getParticle(Int2 position) {
        return getParticle(position.x, position.y);
    }

    public void setParticle(int x, int y, OldParticle p) {
        p.position.set(x, y);
        this.grid[x][y] = p;
    }

    public void setParticle(Int2 pos, OldParticle p) {

        setParticle(pos.x, pos.y, p);
    }

    // Update world

    public void tick(float deltaTime) {

        if (!simulate) return;

        // Reset all particle
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < length; y++) {
                this.getParticle(x, y).moved = false;
            }
        }

        // First loop : Calculations
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < length; y++) {

                Int2 position = new Int2(x, y);

                switch (this.getParticle(position).type) {
                    case "air":
                    case "null":
                    case "iron":
                        break;
                    default:
                        calculateParticle(position, deltaTime);
                }
            }
        }

        // Second loop : Movement
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < length; y++) {

                Int2 position = new Int2(x, y);

                switch (this.getParticle(position).type) {
                    case "air":
                    case "null":
                    case "iron":
                        break;
                    default:
                        updateParticle(position);
                }
            }
        }
    }

    // Particle logic

    public void calculateParticle(Int2 position, float deltaTime) {

        collisionDetection(position);

        OldParticle particle = this.getParticle(position);

        particle.velocity.y += gravity * deltaTime;

        if (!particle.collision[2]) return;

        if(particle.liquid) {

            OldParticle particleAbove = this.getParticle(position.offset(0, 1));

            if(particleAbove.liquid) {
                particle.depth = particleAbove.depth + 1;
            }
            else {
                particle.depth = 0;
            }

            java.util.Random random = new Random();
            int dir = random.nextInt(2);

            float x = min(particle.depth/2, 5) + 0.7f;

            if(dir == 0) {
                if(!particle.collision[1]) {
                    particle.velocity.x += x;
                }
            }
            else {
                if (!particle.collision[3]) {
                    particle.velocity.x -= x;
                }
            }
        }
    }

    public void updateParticle(Int2 position) { // TODO : Make switch

        OldParticle particle = this.getParticle(position);

        if(particle.moved) return;
        else particle.moved = true;

        if(!particle.velocity.isZero(0.2f)) {
            applyVelocity(position, particle.velocity);
            return;
        }

        final int density = particle.density;

        java.util.Random random = new Random();

        int dir = random.nextInt(2);
        if (dir == 0) dir = -1;

        if ( this.getParticle(position.x + dir, position.y - 1).density < density ) {
            movePosition(position.x, position.y, dir, - 1);
            return;
        }
        /*
        if (!particle.liquid) return;

        for (int i = 1; i <= 2; i++) {
            if ( this.getParticle(position.x + dir, position.y).density < density) {
                movePosition(position.x, position.y, dir, 0);
            }
        }*/
    }

    private void movePosition(int x, int y, int dx, int dy) { // Position, deltaPosition

        dx += x;
        dy += y;

        OldParticle temp = this.getParticle(dx, dy);
        this.setParticle(dx, dy, this.getParticle(x, y));
        this.setParticle(x, y, temp);
    }

    private void applyVelocity(Int2 position, Float2 velocity) {

        // .cpy() makes a copy of the vector

        Float2 goal = position.toFloat2()
                .add(velocity);

        final float distance = goal.dst(position.toFloat2());
        final int roundDistance = round(distance);

        Float2 normal = new Float2(goal.cpy())
                .add(-position.x, -position.y)
                .scl(1 / distance); // Distance is already calculated, so using .nor() is ineffective

        Float2 target = position.toFloat2();


        for (int t = 0; t < roundDistance; t++) {

            target.add(normal);

            if(position.x == round(target.x) && position.y == round(target.y)) {
                continue;
            }

            if (this.getParticle(round(target.x), round(target.y)).density < this.getParticle(position).density) {

                Vector2 delta = new Vector2(round(target.x)-position.x, round(target.y)-position.y);

                movePosition(position.x, position.y, (int) delta.x, (int) delta.y);
                position.add((int) delta.x, (int) delta.y);

            } else break;
        }
    }

    private void collisionDetection(Int2 position) {

        OldParticle particle = this.getParticle(position);

        if (particle.density <= this.getParticle(position.offset(0, 1)).density) {
            particle.collision[0] = true;
            if (0 < particle.velocity.y) particle.velocity.y = 0;
        }
        else particle.collision[0] = false;

        if (particle.density <= this.getParticle(position.offset(1, 0)).density) {
            particle.collision[1] = true;
            if (0 < particle.velocity.x) particle.velocity.x = 0;
        }
        else particle.collision[1] = false;

        if (particle.density <= this.getParticle(position.offset(0, -1)).density) {
            particle.collision[2] = true;
            if (0 > particle.velocity.y) particle.velocity.y = 0;
        }
        else particle.collision[2] = false;

        if (particle.density <= this.getParticle(position.offset(-1, 0)).density) {
            particle.collision[3] = true;
            if (0 > particle.velocity.x) particle.velocity.x = 0;
        }
        else particle.collision[3] = false;
    }

    // UwU

    public boolean isParticleTypeSurrounded(Int2 position) {

        String type = this.getParticle(position).type;

        if (!type.equals(this.getParticle(position.offset(0, 1)).type)) return false;
        if (!type.equals(this.getParticle(position.offset(1, 0)).type)) return false;
        if (!type.equals(this.getParticle(position.offset(0, -1)).type)) return false;
        if (!type.equals(this.getParticle(position.offset(-1, 0)).type)) return false;
        return true;
    }

    // Misc / To be named

    public void spawn(Int2 pos, int radius, String type, boolean isBrush, boolean isRandom, int spawnChance) {

        if (isInWorld(pos.x, pos.y)) { // FIXME : MAKE TO METHOD

            if (isBrush) {
                for (int x = Math.max(pos.x - radius, 0); x < min(pos.x + radius, OldWorld.width); x++) {
                    for (int y = Math.max(pos.y - radius, 0); y < min(pos.y + radius, OldWorld.length); y++) {

                        if (!isRandom) {
                            this.setParticle(x, y, new OldParticle(type));
                        } else if(random.nextInt(100) < spawnChance) {
                            this.setParticle(x, y, new OldParticle(type));
                        }

                    }
                }
            }
            else {
                this.setParticle(pos, new OldParticle(type));
            }
        }
    }

    private boolean isInWorld(Int2 pos) {
        return isInWorld(pos.x, pos.y);
    }

    public boolean isInWorld(int x, int y) {
        return 0 <= x && x < OldWorld.width && 0 <= y && y < OldWorld.length;
    }



}

package com.main;

import com.badlogic.gdx.math.Vector2;
import com.main.math.Float2;
import com.main.math.Int2;

import java.util.Random;

import static java.lang.Math.round;

public class World {


    // TODO : size get()
    public static int width = 128;
    public static int length = 128;

    private final int gravity = -10;

    Particle[][] grid;


    public World() {

        this.grid = new Particle[width][length];

        // Fill grid with air particles

        for (int x = 0; x < width; x++) {

            for (int y = 0; y < length; y++) {

                this.setParticle(x, y, new Particle("air"));
            }
        }
    }

    public Particle getParticle(int x, int y) {
        if (0 <= x && x < width && 0 <= y && y < length) {
            return this.grid[x][y];
        }
        return new Particle("null");
    }

    public Particle getParticle(Int2 position) {
        return getParticle((int) position.x, (int) position.y);
    }

    public void setParticle(int x, int y, Particle p) {

        this.grid[x][y] = p;

    }

    public void tick(float deltaTime) {

        // Reset all particles
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < length; y++) {
                this.getParticle(x, y).updated = false;
            }
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < length; y++) {

                Particle particle = this.getParticle(x, y);

                if (particle.updated) continue; // Skip updated particles

                switch (particle.type) {

                    case "sand":
                    case "water":
                        updateParticle(x, y, deltaTime);
                        break;
                    default:
                        break;
                }

                particle.updated = true;
            }
        }
    }

    public void updateParticle(int x, int y, float deltaTime) { // TODO : Make switch

        Particle particle = this.getParticle(x, y);
        final int density = particle.density;

        java.util.Random random = new Random();
        int dir = random.nextInt(2);

        if (dir == 0) dir = -1;

        if ( this.getParticle(x, y - 1).density < density) {

            particle.velocity.y += gravity * deltaTime;

            applyVelocity(new Int2(x, y), particle.velocity);
            return;
        }
        else particle.velocity.y = 0;

        if ( this.getParticle(x + dir, y - 1).density < density ) {
            movePosition(x, y, dir, - 1);
            return;
        }

        if (!particle.liquid) return;

        if ( this.getParticle(x + dir, y).density < density) {
            movePosition(x, y, dir, 0);
        }
    }

    private void movePosition(int x, int y, int dx, int dy) { // Position, deltaPosition

        Particle temp = this.getParticle(x + dx, y + dy);
        this.setParticle(x + dx, y + dy, this.getParticle(x, y));
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

            if (this.getParticle(round(target.x), round(target.y)).density < this.getParticle(position).density) {

                Vector2 delta = new Vector2(round(target.x)-position.x, round(target.y)-position.y);

                movePosition(position.x, position.y, (int) delta.x, (int) delta.y);
                position.add((int) delta.x, (int) delta.y);

            } else break;
        }
    }
}

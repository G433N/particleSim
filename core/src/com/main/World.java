package com.main;

import com.badlogic.gdx.math.Vector2;
import com.main.math.Float2;
import com.main.math.Int2;

import java.awt.*;
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
        p.position.set(x, y);

    }

    public void tick(float deltaTime) {

        // Reset all particles
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < length; y++) {

                Int2 position = new Int2(x, y);
                this.getParticle(position).updated = false;
            }
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < length; y++) {

                Particle particle = this.getParticle(x, y);

                if (particle.updated) continue; // Skip updated particles

                switch (particle.type) {

                    case "sand":
                    case "water":

                        Int2 position = new Int2(x, y);

                        updateParticle(position, deltaTime);

                        //collisionDetection(position);

                        break;

                    default:
                        break;
                }

                particle.updated = true;
            }
        }
    }

    int k = 0;

    public void updateParticle(Int2 position, float deltaTime) { // TODO : Make switch

        Particle particle = this.getParticle(position);
        final int density = particle.density;

        java.util.Random random = new Random();
        int dir = random.nextInt(2);

        if (dir == 0) dir = -1;

        if ( this.getParticle(position.x, position.y - 1).density < density) {

            particle.velocity.y += gravity * deltaTime;

            applyVelocity(position, particle.velocity);
            return;
        }
        else particle.velocity.y = 0;

        if ( this.getParticle(position.x + dir, position.y - 1).density < density ) {
            movePosition(position.x, position.y, dir, - 1);
            return;
        }

        if (!particle.liquid) return;


        if ( this.getParticle(position.x + dir, position.y).density < density) {
            movePosition(position.x, position.y, dir, 0);
        }

    }

    private void movePosition(int x, int y, int dx, int dy) { // Position, deltaPosition

        dx += x;
        dy += y;

        Particle temp = this.getParticle(dx, dy);
        this.setParticle(dx, dy, this.getParticle(x, y));
        this.setParticle(x, y, temp);
    }


    private void collisionDetection(Int2 position) {

        Particle particle = this.getParticle(position);

        if (particle.density < this.getParticle(position.offset(0, 1)).density) {
            particle.collision[0] = true;
            if (0 < particle.velocity.y) particle.velocity.y = 0;
        }
        else particle.collision[0] = false;
        if (particle.density < this.getParticle(position.offset(1, 0)).density) {
            particle.collision[1] = true;
            if (0 < particle.velocity.x) particle.velocity.x = 0;
        }
        else particle.collision[1] = false;
        if (particle.density < this.getParticle(position.offset(0, -1)).density) {
            particle.collision[2] = true;
            if (particle.velocity.y < 0) particle.velocity.y = 0;
        }
        else particle.collision[2] = false;
        if (particle.density < this.getParticle(position.offset(-1, 0)).density) {
            particle.collision[3] = true;
            if (particle.velocity.x < 0) particle.velocity.x = 0;
        }
        else particle.collision[3] = false;
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

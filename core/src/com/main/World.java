package com.main;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;

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

    public Particle getParticle(Vector2 position) {
        return getParticle((int) position.x, (int) position.y);
    }

    public String getParticleTypeSafe(int x, int y) {

        if (0 <= x && x < width && 0 <= y && y < length) {
            return this.getParticle(x, y).type;
        }

        return "null";
    }

    public void setParticle(int x, int y, Particle p) {

        this.grid[x][y] = p;

    }

    public void setParticle(GridPoint2 point, Particle p) {
        this.setParticle(point.x, point.y, p);
    }

    public void tick(float deltaTime) {

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < length; y++) {
                this.getParticle(x, y).updated = false;
            }
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < length; y++) {

                Particle particle = this.getParticle(x, y);
                String type = particle.type;

                if (particle.updated) continue;

                switch (type) {

                    case "sand":
                    case "water":
                        updateParticle(x, y, deltaTime);
                        particle.updated = true;
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void updateParticle(int x, int y, float deltaTime) { // TODO : Make switch
        Particle particle = this.getParticle(x, y);
        int density = particle.density;


        java.util.Random random = new Random();
        int dir = random.nextInt(2);

        if (dir == 0) dir = -1;

        if ( this.getParticle(x, y - 1).density < density) {

            particle.velocity.y += gravity * deltaTime;

            applyVelocity(new Vector2(x, y), particle.velocity);
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

    private void movePosition(int x, int y, int vx, int vy) {
        Particle temp = this.getParticle(x+vx, y+vy);
        this.setParticle(x+vx, y+vy, this.getParticle(x, y));
        this.setParticle(x, y, temp);
    }

    private void applyVelocity(Vector2 position, Vector2 velocity) {

        Vector2 goal = new Vector2(position)
                .add(velocity);

        final float distance = position.dst(goal);
        final int roundDistance = round(distance);

        Vector2 normal = new Vector2(goal)
                .add(-position.x, -position.y)
                .scl(1 / distance);

        Vector2 target = new Vector2(position);
        int density = this.getParticle(position).density;

        for (int t = 0; t < roundDistance;t++) {

            target.add(normal);

            if (this.getParticle(round(target.x), round(target.y)).density < density) {

                Vector2 delta = new Vector2(round(target.x)-position.x, round(target.y)-position.y);

                movePosition((int) position.x, (int) position.y, (int) delta.x, (int) delta.y);
                position.add((int) delta.x, (int) delta.y);

            } else break;
        }
    }
}

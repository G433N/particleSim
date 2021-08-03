package com.main;

import com.badlogic.gdx.math.GridPoint2;

import java.util.Random;

public class World {


    // TODO : size get()
    public static int width = 128;
    public static int length = 128;

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

    public void tick() {

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < length; y++) {

                this.getParticle(x, y).updated = false;
            }
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < length; y++) {

                Particle particle = this.getParticle(x, y);
                String type = particle.type;

                //if (particle.updated) continue;

                switch (type) {

                    case "sand":
                    case "water":
                        updateParticle(x, y);
                        updateParticle(x, y);
                        particle.updated = true;
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void updateParticle(int x, int y) { // TODO : Make switch
        Particle particle = this.getParticle(x, y);
        int density = particle.density;
        //GridPoint2 velocity = new GridPoint2();

        java.util.Random random = new Random();
        int dir = random.nextInt(2);

        if (dir == 0) dir = -1;

        if ( this.getParticle(x, y - 1).density < density) {
            move(x, y, 0, - 1);
        }
        else if (particle.updated); // for better spread
        else if ( this.getParticle(x + dir, y - 1).density < density ) {
            move(x, y, dir, - 1);
        }
        else if (!particle.liquid) return;
        else if ( this.getParticle(x + dir, y).density < density) {
            move(x, y, dir, 0);
        }
    }

    private void move(int x, int y, int vx, int vy) {

        Particle temp = this.getParticle(x+vx, y+vy);
        this.setParticle(x+vx, y+vy, this.getParticle(x, y));
        this.setParticle(x, y, temp);
    }
}

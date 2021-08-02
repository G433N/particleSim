package com.main;

import com.badlogic.gdx.math.GridPoint2;

public class World {


    // TODO : size get()
    public static int width = 64;
    public static int length = 64;

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

                switch (this.getParticleTypeSafe(x, y)) {

                    case "water":
                    case "sand":
                        updateParticle(x, y);
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

        if ( this.getParticle(x, y - 1).density < density) {
            move(x, y, 0, - 1);
        }
        else if ( this.getParticle(x - 1, y - 1).density < density ) {
            move(x, y, -1, - 1);
        }
        else if ( this.getParticle(x + 1, y - 1).density < density ) {
            move(x, y, 1, - 1);
        }
        else if (!particle.liquid) return;
        else if ( this.getParticle(x + 1, y).density < density) {
            move(x, y, 1, 0);
        }
        else if ( this.getParticle(x - 1, y).density < density) {
            move(x, y, - 1, 0);
        }
    }

    private void move(int x, int y, int vx, int vy) {

        Particle temp = this.getParticle(x+vx, y+vy);
        this.setParticle(x+vx, y+vy, this.getParticle(x, y));
        this.setParticle(x, y, temp);

    }
}

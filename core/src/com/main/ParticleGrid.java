package com.main;

import com.badlogic.gdx.math.GridPoint2;
import com.particle.Particle;
import com.particle.Particles;

public class ParticleGrid {

    // TODO : size get()
    public static int width = 120;
    public static int length = 120;

    Particle[][] grid;



    public ParticleGrid() {

        this.grid = new Particle[width][length];

        // Fill grid with air particles

        for (int y = 0; y < length; y++) {

            for (int x = 0; x < width; x++) {

                this.setParticle(x, y, new Particle(Particles.AIR));

            }
        }
    }

    public Particle getParticle(int x, int y) {
        return this.grid[x][y];
    }

    public Particle getParticle(GridPoint2 point) {
        return getParticle(point.x, point.y);
    }

    public Particles getParticleType(GridPoint2 point) {

        return getParticleType(point.x, point.y);

    }

    public Particles getParticleType(int x, int y) {

        if (0 <= x && x < width && 0 <= y && y < length) {
            return this.grid[x][y].getType();
        }

        return Particles.NULL;
    }

    public void setParticle(int x, int y, Particle p) {

        this.grid[x][y] = p;

    }

    public void setParticle(GridPoint2 point, Particle p) {
        this.grid[point.x][point.y] = p;
    }

    public void tick() {

        ParticleGrid nextGrid = new ParticleGrid();

        for (int x = 0; x < width; x++) {

            for (int y = 0; y < length; y++) {

                Particles type = this.getParticleType(x, y);

                switch (type) {
                    // If particle updates against update-flow remember to check if are in new grid
                    case AIR:
                        break;

                    case SAND:
                        // TODO : MAKE MORE EFECTIVE
                        if ( this.getParticleType(x, y - 1) == Particles.AIR ) {
                            nextGrid.setParticle(x, y, new Particle(Particles.AIR));
                            nextGrid.setParticle(x, y - 1, new Particle(type));
                        }
                        else if ( this.getParticleType(x - 1, y - 1) == Particles.AIR ) {
                            nextGrid.setParticle(x, y, new Particle(Particles.AIR));
                            nextGrid.setParticle(x - 1, y - 1, new Particle(type));
                        }
                        else if ( this.getParticleType(x + 1, y - 1) == Particles.AIR ) {
                            nextGrid.setParticle(x, y, new Particle(Particles.AIR));
                            nextGrid.setParticle(x + 1, y - 1, new Particle(type));
                        }
                        else nextGrid.setParticle(x, y, new Particle(type));

                        break;

                    case WATER:
                        // TODO : MAKE MORE EFECTIVE
                        if ( this.getParticleType(x, y - 1) == Particles.AIR ) {
                            nextGrid.setParticle(x, y, new Particle(Particles.AIR));
                            nextGrid.setParticle(x, y - 1, new Particle(type));
                        }
                        else if ( this.getParticleType(x - 1, y - 1) == Particles.AIR ) {
                            nextGrid.setParticle(x, y, new Particle(Particles.AIR));
                            nextGrid.setParticle(x - 1, y - 1, new Particle(type));
                        }
                        else if ( this.getParticleType(x + 1, y - 1) == Particles.AIR ) {
                            nextGrid.setParticle(x, y, new Particle(Particles.AIR));
                            nextGrid.setParticle(x + 1, y - 1, new Particle(type));
                        }
                        else if (this.getParticleType(x + 1, y) == Particles.AIR )  {

                            nextGrid.setParticle( x, y, new Particle(Particles.AIR));
                            nextGrid.setParticle( x + 1, y, new Particle(Particles.WATER));

                        }
                        else if (this.getParticleType(x - 1, y) == Particles.AIR ) {

                            nextGrid.setParticle(x, y, new Particle(Particles.AIR));
                            nextGrid.setParticle(x - 1, y, new Particle(Particles.WATER));
                        }
                        else nextGrid.setParticle(x, y, new Particle(type));

                        break;

                    default:
                        nextGrid.setParticle(x, y, new Particle(type));
                        break;
                }
            }
        }

        this.grid = nextGrid.grid;
    }

    public void move() {
        // Make particles change place
    }
}

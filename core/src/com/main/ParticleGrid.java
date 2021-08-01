package com.main;

import com.badlogic.gdx.math.GridPoint2;
import com.particle.Particle;
import com.particle.Particles;

public class ParticleGrid {

    // TODO : size get()
    public static int width = 64;
    public static int length = width;
    private final Particle wall = new Particle(Particles.NULL);
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

        if (0 <= x && x < width && 0 <= y && y < length) {
            return this.grid[x][y];
        }
        return wall;
    }

    public Particle getParticle(GridPoint2 point) {
        return getParticle(point.x, point.y);
    }

    public Particles getParticleType(int x, int y) {

        if (0 <= x && x < width && 0 <= y && y < length) {
            return this.getParticle(x, y).getType();
        }

        return Particles.NULL;
    }

    public int getParticleDensity(int x, int y) {

        if (0 <= x && x < width && 0 <= y && y < length) {
            return this.getParticle(x, y).density;
        }

        return wall.density;
    }

    public void setParticle(int x, int y, Particle p) {

        this.grid[x][y] = p;

    }

    public void setParticle(GridPoint2 point, Particle p) {
        this.grid[point.x][point.y] = p;
    }

    public void updateParticle(int x, int y) {

        Particles type = this.getParticleType(x, y);

        switch (type) {
            // If particle updates against update-flow remember to check if are in new grid
            case AIR:
            case METAL:
                break;

            case SAND:
            case WATER:
            case MUD:
                this.updateParticlePosition(x, y);
                break;


            case DIRT:
                if (this.getParticleType(x, y + 1) == Particles.AIR) {
                    this.setParticle(x, y, new Particle(Particles.GRASS));
                }
                if (closeBy(x, y, Particles.WATER)) {
                    this.setParticle(x, y, new Particle(Particles.MUD));
                }
                this.updateParticlePosition(x, y);
                break;

            case GRASS:
                if (this.getParticleType(x, y + 1) != Particles.AIR) {
                    this.setParticle(x, y, new Particle(Particles.DIRT));
                }
                this.updateParticlePosition(x, y);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    public void update() {

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < length; y++) {
                updateParticle(x, y);
            }
        }
    }

    private boolean surroundedBy(int x, int y, Particles type) {

        if (type == this.getParticleType(x, y + 1)) {
            return false;
        } else if (type == this.getParticleType(x+1, y + 1)) {
            return false;
        } else if (type == this.getParticleType(x+1, y)) {
            return false;
        } else if (type == this.getParticleType(x+1, y-1)) {
            return false;
        } else if (type == this.getParticleType(x, y - 1)) {
            return false;
        } else if (type == this.getParticleType(x-1, y-1)) {
            return false;
        } else if (type == this.getParticleType(x-1, y)) {
            return false;
        } else if (type == this.getParticleType(x-1, y+1)) {
            return false;
        }
        return true;
    }

    private boolean closeBy(int x, int y, Particles type) {
        return !surroundedBy(x, y, type);
    }

    public void switchParticles(GridPoint2 p1, GridPoint2 p2) {
        Particle p = this.getParticle(p2);
        this.setParticle(p2, this.getParticle(p1));
        this.setParticle(p1, p);
    }

    public void updateParticlePosition(int x, int y) {
        int density = this.getParticle(x, y).density;

        if ( this.getParticleDensity(x, y - 1) < density ) {
            this.switchParticles(new GridPoint2(x, y), new GridPoint2(x, y-1));
        }
        else if (!(this.getParticle(x, y).liquid || this.getParticle(x, y).loose)) {
            return;
        }
        else if ( this.getParticleDensity(x - 1, y - 1) < density && this.getParticleDensity(x - 1, y) < density) {
            this.switchParticles(new GridPoint2(x, y), new GridPoint2(x-1, y-1));
        }
        else if ( this.getParticleDensity(x + 1, y - 1) < density && this.getParticleDensity(x + 1, y) < density) {
            this.switchParticles(new GridPoint2(x, y), new GridPoint2(x+1, y-1));
        }
        else if (!this.getParticle(x, y).liquid) {
            return;
        }
        else if (this.getParticleDensity(x + 1, y) < density )  {
            this.switchParticles(new GridPoint2(x, y), new GridPoint2(x+1, y));
        }
        else if (this.getParticleDensity(x - 1, y) < density ) {
            this.switchParticles(new GridPoint2(x, y), new GridPoint2(x-1, y));
        }
    }
}

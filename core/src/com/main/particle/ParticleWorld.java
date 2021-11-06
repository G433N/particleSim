package com.main.particle;

import com.main.math.Float2;
import com.main.math.Int2;

import static com.badlogic.gdx.math.MathUtils.random;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class ParticleWorld {

    public static int width = 256;
    public static int length = 128;

    protected final int gravity = -10;

    Particle[][] grid;

    public boolean simulate = false;

    public ParticleWorld() {

        Particle.init(this);

        this.grid = new Particle[width][length];

        // Fill grid with air particle
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < length; y++) {
                this.setParticle(x, y, "empty");
            }
        }
    }

    // Setters and getters

    public Particle getParticle(int x, int y) {
        if (0 <= x && x < width && 0 <= y && y < length) {
            return this.grid[x][y];
        }
        return Particle.get("null");
    }

    public Particle getParticle(Int2 position) {
        return this.getParticle(position.x, position.y);
    }

    private Int2 nextMaxUpdate = new Int2(0, 0);
    private Int2 nextMinUpdate = new Int2(width-1, length-1);
    public Int2 maxUpdate = new Int2(nextMaxUpdate);
    public Int2 minUpdate = new Int2(nextMinUpdate);

    public void setParticle(int x, int y, Particle p) {
        this.grid[x][y] = p;
        this.grid[x][y].position.set(x, y);

        updateUpdateArea(x, y);
    }

    public void updateUpdateArea(int x, int y) {
        nextMaxUpdate.set(max(nextMaxUpdate.x, x+1), max(nextMaxUpdate.y, y+1));
        nextMinUpdate.set(min(nextMinUpdate.x, x), min(nextMinUpdate.y, y));
    }

    public void setParticle(int x, int y, String type) {
        this.setParticle(x, y, Particle.get(type));
    }

    public void setParticle(Int2 pos, Particle p) {
        setParticle(pos.x, pos.y, p);
    }


    // Tick


    public void tick(float deltaTime) {


        maxUpdate = new Int2(nextMaxUpdate.x, nextMaxUpdate.y);
        minUpdate = new Int2(nextMinUpdate.x, nextMinUpdate.y);

        nextMaxUpdate.set(0, 0);
        nextMinUpdate.set(width-1, length-1);

        if (!simulate) return;

        // First loop -> Pre calculation
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < length; y++) {

                Int2 position = new Int2(x, y);
                this.getParticle(position).moved = false;
                this.getParticle(position).primaryUpdate(deltaTime);
            }
        }

        // Second loop
        //System.out.println("Min: " +  this.minUpdate + " Max: " + this.maxUpdate);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < length; y++) {

                Int2 position = new Int2(x, y);
                this.getParticle(position).secondaryUpdate(deltaTime);
            }
        }
        /*
        for (int x = minUpdate.x; x < maxUpdate.x; x++) {
            for (int y = minUpdate.y; y < maxUpdate.y; y++) {
         */
        // Third loop
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < length; y++) {

                String type = this.getParticle(x, y).type;

                switch (type) {
                    case "water" :
                            if (this.getParticle(x , y-1).type.equals("fire")) {
                               this.setParticle(x,y-1, "empty");
                    }
                        break;
                }
            }
        }
    }



    // Particle logic

    protected void movePosition(int x, int y, int dx, int dy) { // Position, deltaPosition

        dx += x;
        dy += y;

        Particle temp = this.getParticle(dx, dy);
        this.setParticle(dx, dy, this.getParticle(x, y));
        this.setParticle(x, y, temp);
    }

    // Misc

    public void spawn(Int2 pos, int radius, String type, boolean isBrush, boolean isRandom, int spawnChance) {

        if (isInWorld(pos)) {

            if (isBrush) {

                for (int x = max(pos.x - radius, 0); x < Math.min(pos.x + radius, width); x++) {
                    for (int y = max(pos.y - radius, 0); y < min(pos.y + radius, length); y++) {

                        if (!isRandom) {
                            this.setParticle(x, y, Particle.get(type));
                        } else if(random.nextInt(100) < spawnChance) {
                            this.setParticle(x, y, Particle.get(type));
                        }
                    }
                }
            }
            else {
                this.setParticle(pos, Particle.get(type));
            }
        }
    }

    private boolean isInWorld(Int2 pos) {
        return isInWorld(pos.x, pos.y);
    }

    public boolean isInWorld(int x, int y) {
        return 0 <= x && x < ParticleWorld.width && 0 <= y && y < ParticleWorld.length;
    }
}

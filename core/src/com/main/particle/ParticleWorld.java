package com.main.particle;

import com.badlogic.gdx.math.Vector2;
import com.main.OldParticle;
import com.main.OldWorld;
import com.main.math.Float2;
import com.main.math.Int2;

import static com.badlogic.gdx.math.MathUtils.random;
import static java.lang.Math.min;
import static java.lang.Math.round;

public class ParticleWorld {
    /*
        DATA.put("air"     ,   new Particle("air"   ,0  ,false  , "air"));
        DATA.put("null"    ,   new Particle("null"  ,999,false  , "air"));
        DATA.put("sand"    ,   new Particle("sand"  ,2  ,false  , "sand"));
        DATA.put("water"   ,   new Particle("water" ,1  ,true   , "shallowwater"));
        DATA.put("iron"    ,   new Particle("iron"  ,999,false  , "iron"));
     */

    public static int width = 24;
    public static int length = 24;

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
        return getParticle(position.x, position.y);
    }

    private int q = 0;

    public void setParticle(int x, int y, Particle p) {
        //p.position.set(x, y);
        this.grid[x][y] = p;
        this.grid[x][y].position.set(x, y);

    }

    public void setParticle(int x, int y, int nx, int ny) {
        //System.out.println(q + " " + p + " -> " + x + " " + y);
        Particle p = this.getParticle(x, y);
        p.position.set(nx, ny);
        this.grid[nx][ny] = p;
    }

    public void setParticle(int x, int y, String type) {
        this.setParticle(x, y, Particle.get(type));
    }

    public void setParticle(Int2 pos, Particle p) {

        setParticle(pos.x, pos.y, p);
    }


    // Tick

    public void tick(float deltaTime) {

        if (!simulate) return;


        // First loop
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < length; y++) {

                Int2 position = new Int2(x, y);
                this.getParticle(position).moved = false;
                this.getParticle(position).primaryUpdate(deltaTime);
            }
        }

        // Second loop
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < length; y++) {

                Int2 position = new Int2(x, y);
                this.getParticle(position).secondaryUpdate(deltaTime);
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

        if (isInWorld(pos.x, pos.y)) { // FIXME : MAKE TO METHOD

            if (isBrush) {
                for (int x = Math.max(pos.x - radius, 0); x < Math.min(pos.x + radius, width); x++) {
                    for (int y = Math.max(pos.y - radius, 0); y < min(pos.y + radius, length); y++) {

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

package com.main.particle;

import com.main.OldParticle;
import com.main.OldWorld;
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
    /*
    private void calculateParticle(Int2 position, float deltaTime) {

        collisionDetection(position);

        Particle particle = this.getParticle(position);

        particle.velocity.y += gravity * deltaTime;

        if (!particle.collision[2]) return;

        if(particle.liquid) {

            Particle particleAbove = this.getParticle(position.offset(0, 1));

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


    private void updateParticle(Int2 position) { // TODO : Make switch

        Particle particle = this.getParticle(position);

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

        if (!particle.liquid) return;

        for (int i = 1; i <= 2; i++) {
            if ( this.getParticle(position.x + dir, position.y).density < density) {
                movePosition(position.x, position.y, dir, 0);
            }
        }
    }

    */
    protected void movePosition(int x, int y, int dx, int dy) { // Position, deltaPosition

        dx += x;
        dy += y;

        Particle temp = this.getParticle(dx, dy);
        this.setParticle(dx, dy, this.getParticle(x, y));
        this.setParticle(x, y, temp);
    }

    /*

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

        Particle particle = this.getParticle(position);

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

     */

    // Misc

    public void spawn(Int2 pos, int radius, String type, boolean isBrush, boolean isRandom, int spawnChance) {

        if (isInWorld(pos.x, pos.y)) { // FIXME : MAKE TO METHOD

            if (isBrush) {
                for (int x = Math.max(pos.x - radius, 0); x < Math.min(pos.x + radius, OldWorld.width); x++) {
                    for (int y = Math.max(pos.y - radius, 0); y < min(pos.y + radius, OldWorld.length); y++) {

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

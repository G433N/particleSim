package com.main.particle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.main.math.Float2;
import com.main.math.Int2;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static java.lang.Math.round;

public class Particle {

    // TODO : SORT VARIABLES

    protected static ParticleWorld world = null;

    public final String type;
    public final int density;
    public ParticleState state = ParticleState.PARTICLE;

    // Physics
    public Int2 position = new Int2();
    public Float2 velocity = new Float2();

    public boolean[] collision = {false, false, false, false}; // 0 = Top, 1 = Right, 2 = Bottom, 3 = Left
    protected boolean moved = false; /// This frame it can't get calculated twice

    public static boolean initialized = false;

    public static String[] TYPES = new String[] {"empty", "sand", "water"}; // All public types

    public static Particle get(String type) {

        if(!initialized) throw new Error("Particle not initialized, run init()");

        switch (type) {
            case "null" :
                return new NullParticle();
            case "empty" :
                return new EmptyParticle();
            case "sand" :
                return new SandParticle();
            case "water" :
                return new WaterParticle();
        }
        throw new Error("Particle type doesn't exist!");
    }

    public static void init(ParticleWorld particleWorld) {

        if(initialized) throw new Error("Can't initialized twice");

        world = particleWorld;
        initialized = true;
    }

    protected Particle(String type, int density) {
        this.type = type;
        this.density = density;
    }

    public void primaryUpdate(float deltaTime) {
        /*
            Default if not overridden
            Calculate collision
            Calculate velocity
            Apply primary rule
         */

        this.collisionDetection();

        this.velocity.y += world.gravity * deltaTime;

        this.primaryRule(deltaTime);
    }

    public void secondaryUpdate(float deltaTime) {
        /*
            Default if not overridden
            Apply velocity if velocity is not close to zero
            Apply secondary rules if not applying velocity
         */
        if(this.moved) return;
        else this.moved = true;

        if(!this.velocity.isZero(0.2f)) {
            this.applyVelocity();
            return;
        }

        this.secondaryRule(deltaTime);
    }

    protected void primaryRule(float deltaTime) {

    }

    protected void secondaryRule(float deltaTime) {

    }

    public Color getColor() {
        return Color.BLACK;
    }

    protected void applyVelocity() {

        // .cpy() makes a copy of the vector

        Float2 goal = new Float2(this.position.toFloat2()).add(this.velocity);

        final float distance = goal.dst(this.position.toFloat2());
        final int roundDistance = round(distance);

        Float2 normal = new Float2(goal.cpy())
                .add(-this.position.x, -this.position.y)
                .scl(1 / distance); // Distance is already calculated, so using .nor() is ineffective

        Float2 target = this.position.toFloat2();


        for (int t = 0; t < roundDistance; t++) {

            target.add(normal);

            /*
            if(this.position.x == round(target.x) && this.position.y == round(target.y)) {
                continue;
            }
             */

            if (world.getParticle(round(target.x), round(target.y)).density < world.getParticle(this.position).density) {

                Int2 delta = new Int2(round(target.x)-position.x, round(target.y)-position.y);


                world.movePosition(this.position.x, this.position.y, delta.x, delta.y);
                this.position.add(delta.x, delta.y);

            } else break;
        }
    }
    
    private void collisionDetection() {

        if (this.density <= world.getParticle(position.offset(0, 1)).density) {
            this.collision[0] = true;
            if (0 < this.velocity.y) this.velocity.y = 0;
        }
        else this.collision[0] = false;

        if (this.density <= world.getParticle(position.offset(1, 0)).density) {
            this.collision[1] = true;
            if (0 < this.velocity.x) this.velocity.x = 0;
        }
        else this.collision[1] = false;

        if (this.density <= world.getParticle(position.offset(0, -1)).density) {
            this.collision[2] = true;
            if (0 > this.velocity.y) this.velocity.y = 0;
        }
        else this.collision[2] = false;

        if (this.density <= world.getParticle(position.offset(-1, 0)).density) {
            this.collision[3] = true;
            if (0 > this.velocity.x) this.velocity.x = 0;
        }
        else this.collision[3] = false;
    }

    public void printData() {

        Field[] fields = this.getClass().getFields();
        System.out.println(this);

        for (Field field : fields) {

            if(Modifier.isStatic(field.getModifiers())) continue;
            try {
                String name = field.getName();
                Object value = field.get(this); //

                System.out.print(name + " : ");

                try {
                    int length = Array.getLength(value);
                    System.out.print("[ ");
                    for (int i = 0; i < length; i++) {
                        System.out.print(Array.get(value, i).toString() + ", ");
                    }
                    System.out.println("]");
                }
                catch (IllegalArgumentException e) {
                    System.out.println(value.toString());
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}

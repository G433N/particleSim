package com.main.particle;

import com.badlogic.gdx.graphics.Color;
import com.main.math.Float2;
import com.main.math.Int2;
import com.main.particle.states.State;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static java.lang.Math.*;
import static java.lang.Math.min;

public class Particle {

    // TODO : SORT VARIABLES

    protected static World world = null;

    public final String type;
    public final int density;
    public final float flammability; // 0 <= Value <= 1
    public State state = State.PARTICLE;

    // Physics
    public Int2 position = new Int2();
    public Float2 velocity = new Float2();
    public Float2 elasticity = new Float2();
    public Float2 friction = new Float2(0, 0);
    public float thickness = 0; // When moving through a liquid (inside friction)
    public float inertialResistance = 0; // TODO : Implement this make sand faster


    public boolean[] collision = {false, false, false, false}; // 0 = Top, 1 = Right, 2 = Bottom, 3 = Left
    public boolean[] hasNeighbour = {false, false, false, false}; // 0 = Top, 1 = Right, 2 = Bottom, 3 = Left TODO : Better name
    protected boolean updated = false; /// This frame it can't get calculated twice
    public boolean moved = true;
    public boolean isInAir = true;
    public boolean isFreeFalling = true; // https://www.youtube.com/watch?v=5Ka3tbbT-9E


    private static boolean initialized = false;

    public static String[] TYPES = new String[] {"empty", "sand", "water", "oil", "wood", "iron", "fire", "waterSource",}; // All public types

    protected static Int2[] SURROUNDINGOFFSETS = new Int2[] {
            new Int2(0, 1),
            new Int2(1, 0),
            new Int2(0, -1),
            new Int2(-1, 0),
    };

    public static Particle get(String type) {

        if(!initialized) throw new Error("Particle not initialized, run init()");

        switch (type) {
            case "null" :
                return new Null();
            case "empty" :
                return new Empty();
            case "fire" :
                return new Fire();
            case "iron" :
                return new Iron();
            case "oil" :
                return new Oil();
            case "sand" :
                return new Sand();
            case "water" :
                return new Water();
            case "wood" :
                return new Wood();
            case "waterSource":
                return new WaterSource();
        }
        throw new Error("Particle type doesn't exist!");
    }

    public static void init(World world) {

        if(initialized) throw new Error("Can't initialized twice");

        Particle.world = world;
        initialized = true;
    }

    protected Particle(String type, int density, float flammability) {
        this.type = type;
        this.density = density;
        this.flammability = flammability;
    }

    public void primaryUpdate(float deltaTime) {
        /*
            Default if not overridden
            Calculate collision
            Calculate velocity
            Apply primary rule
         */

        this.collisionDetection();
        this.collisionInteraction();

        this.hasNeighbourDetection();

        this.primaryRule(deltaTime);
    }

    public void secondaryUpdate(float deltaTime) {
        /*
            Default if not overridden
            Apply velocity if velocity is not close to zero
            Apply secondary rules if not applying velocity
         */
        if(this.updated) return;
        else this.updated = true;

        this.secondaryRule(deltaTime);
    }

    protected void primaryRule(float deltaTime) {

        this.isInAir = !this.hasNeighbour[2];
        this.isFreeFalling = (this.moved && this.isFreeFalling) || this.isInAir;
        this.moved = false;
        if(isInAir) this.velocity.y += world.gravity * deltaTime;

    }

    protected void secondaryRule(float deltaTime) {
        if(!this.velocity.isZero(0.2f)) {
            this.applyVelocity();
        }
    }

    public Color getColor() {
        return Color.BLACK;
    }

    protected void applyVelocity() {

        // .cpy() makes a copy of the vector

        Float2 goal = new Float2(this.position.toFloat2()).add(this.velocity);

        float distance = goal.dst(this.position.toFloat2());
        int roundDistance = round(distance);

        Float2 normal;

        Float2 targetPosition = this.position.toFloat2();

        for (int t = 0; t < roundDistance; t++) { // roundDistance always gets to 0

            goal = new Float2(this.position.toFloat2()).add(this.velocity);
            distance = goal.dst(this.position.toFloat2());
            roundDistance = round(distance);
            normal = new Float2(goal)
                    .add(-this.position.x, -this.position.y)
                    .scl(1 / distance);

            targetPosition.add(normal);

            Particle target = world.getParticle(round(targetPosition.x), round(targetPosition.y));


            if (target.density < this.density) {


                Int2 delta = new Int2(round(targetPosition.x)-position.x, round(targetPosition.y)-position.y);

                world.movePosition(this.position.x, this.position.y, delta.x, delta.y);

                this.velocity.add(new Float2(this.velocity).scl(-target.thickness)); // FIXME this formula is actually for force, so we should fix that

                // this.position.add(delta.x, delta.y); -> This little line cost me one week
            } else break;
        }
    }

    // ImpactDetection

    protected void hasNeighbourDetection() {

        for (int i = 0; i < SURROUNDINGOFFSETS.length; i++) {
            Int2 offset = SURROUNDINGOFFSETS[i];
            final Particle neighbour = world.getParticle(position.offset(offset));

            this.hasNeighbour[i] = this.density <= neighbour.density && neighbour.state != State.ENERGY;
        }
    }

    protected void collisionDetection() {

        collision = new boolean[]{false, false, false, false};

        for (int i = 0; i < SURROUNDINGOFFSETS.length; i++) {
            Int2 offset = SURROUNDINGOFFSETS[i];
            final Particle neighbour = world.getParticle(position.offset(offset));

            this.collision[i] = this.density <= neighbour.density && !this.hasNeighbour[i];

            if (collision[i]) System.out.println("Impact!!!!");
        }
    }

    protected void collisionInteraction() {
        if (this.collision[0] && 0 < this.velocity.y) this.velocity.y = 0;
        if (this.collision[1] && 0 < this.velocity.x) this.velocity.x = 0;
        if (this.collision[2] && 0 > this.velocity.y) {
            float y = abs(this.velocity.y) * this.elasticity.x;
            this.velocity.x = this.velocity.x == 0 ? (random() < 0.5f ? -y : y) : (this.velocity.x < 0 ? -y : y);
            this.velocity.y = this.velocity.y * -this.elasticity.y;
        }
        if (this.collision[3] && 0 > this.velocity.x) this.velocity.x = 0;

        if(this.hasNeighbour[0] || this.hasNeighbour[2]) this.velocity.x *= (1-this.friction.x);
        if(this.hasNeighbour[1] || this.hasNeighbour[3]) this.velocity.y *= (1-this.friction.y);
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

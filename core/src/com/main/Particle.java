package com.main;

import com.badlogic.gdx.graphics.Color;
import com.main.math.Float2;
import com.main.math.Int2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Particle {

    public final static HashMap<String, Particle> DATA;
    public final static ArrayList<String> TYPES;
    public final static HashMap<String, Color> COLOR;

    static {

        DATA = new HashMap<>();
        DATA.put("air"     ,   new Particle("air"   ,0  ,false  , "air"));
        DATA.put("null"    ,   new Particle("null"  ,999,false  , "air"));
        DATA.put("sand"    ,   new Particle("sand"  ,2  ,false  , "sand"));
        DATA.put("water"   ,   new Particle("water" ,1  ,true   , "water"));
        DATA.put("iron"    ,   new Particle("iron"  ,999,false  , "iron"));


        TYPES = new ArrayList<>();

        for (String type : DATA.keySet()) {
            if (!type.equals("null")) {
                TYPES.add(type);
            }
        }

        COLOR = new HashMap<>();
        COLOR.put("air"         ,   new Color(0, 191/255f, 1, 1));
        COLOR.put("sand1"       ,   new Color(194/255f, 178/255f, .5f, 1));
        COLOR.put("sand2"       ,   new Color(194/255f, 173/255f, .5f, 1));
        COLOR.put("sand3"       ,   new Color(194/255f, 184/255f, .5f, 1));
        COLOR.put("shallowwater",   new Color(0, 0, 1, 1));
        COLOR.put("water"       ,   new Color(0, 0, 200/255f, 1));
        COLOR.put("deepwater"   ,   new Color(0, 0, 128/255f, 1));
        COLOR.put("iron"        ,   new Color(140/255f, 146/255f, 172/255f, 1));
    }

    // Used by all
    public final String type;
    public boolean liquid; // TODO : Gas, liquid and fast form
    public int density;
    public boolean updated = false;
    public String color;

    // Physics
    public Int2 position = new Int2();
    public Float2 velocity = new Float2();
    public boolean[] collision = new boolean[]{false, false, false, false}; // Fixme

    // particle specific // TODO : find better way to do this

    public int depth = 0;

    public Particle(String type) {
        this.type = type;

        Particle data = DATA.get(type);

        this.density = data.density;
        this.liquid = data.liquid;

        if (type.equals("sand")) {
            java.util.Random random = new Random();
            int n = random.nextInt(3) + 1;
            this.color = "sand" + n;
        } else this.color = data.color;
    }

    private Particle(String type, int density, boolean liquid, String color) {
        // for data storage
        this.type = type; // For GUI
        this.density = density;
        this.liquid = liquid;
        this.color = color;
    }

    /*
    a = 10
    v = v_0 + a * deltaT
    V vector2 (v_x, v_y)
    V vector3 (n_v_x, n_v_y, v)
    V vector2 (radianer, v)
    p = p_0 + v * deltaT
     */
}
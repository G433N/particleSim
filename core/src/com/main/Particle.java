package com.main;

import java.util.ArrayList;
import java.util.HashMap;

public class Particle {

    public static HashMap<String, Particle> DATA;
    public static ArrayList<String> TYPES;

    static {

        DATA = new HashMap<>();
        DATA.put("air"     ,   new Particle(0,  false   ));
        DATA.put("null"    ,   new Particle(999,false   ));
        DATA.put("sand"    ,   new Particle(2,  false   ));
        DATA.put("water"   ,   new Particle(1,  true    ));


        TYPES = new ArrayList<>();

        for (String type : DATA.keySet()) {
            if (!type.equals("null")) {
                TYPES.add(type);
            }
        }
    }

    public final String type;
    public boolean liquid;
    public int density;

    public Particle(String type) {
        this.type = type;

        Particle data = DATA.get(type);

        this.density = data.density;
        this.liquid = data.liquid;
    }

    private Particle(int density, boolean liquid) { // for data storage
        this.type = "null";
        this.density = density;
        this.liquid = liquid;
    }
}
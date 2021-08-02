package com.particle;

import java.util.ArrayList;
import java.util.HashMap;

public class Particle {

    public static HashMap<String, ParticleData> particleData;
    public static ArrayList<String> particleTypes;

    static {

        particleData = new HashMap<>();
        particleData.put("air"     ,   new ParticleData(0, false));
        particleData.put("null"    ,   new ParticleData(999, false));
        particleData.put("sand"    ,   new ParticleData(2, false));
        particleData.put("water"   ,   new ParticleData(1, true));


        particleTypes = new ArrayList<>();
        for (String type : particleData.keySet()) {
            if (!type.equals("null")) {
                particleTypes.add(type);
            }
        }
    }

    public final String type;
    public boolean liquid;
    public int density;

    public Particle(String type) {
        this.type = type;

        ParticleData data = particleData.get(type);

        this.density = data.density;
        this.liquid = data.liquid;
    }
}
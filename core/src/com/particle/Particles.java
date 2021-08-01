package com.particle;



public enum Particles {

    NULL,
    AIR,
    SAND,
    WATER,
    DIRT,
    METAL,
    GRASS,
    MUD;

    private static Particles[] list = Particles.values();
    private static final int amount = list.length;

    public static Particles getParticle(int i) {
        return list[i];
    }

    public static int getAmount() {
        return amount;
    }

}

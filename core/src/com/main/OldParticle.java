package com.main;

import com.badlogic.gdx.graphics.Color;
import com.main.math.Float2;
import com.main.math.Int2;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;

public class OldParticle {

    public final static HashMap<String, OldParticle> DATA;
    public final static ArrayList<String> TYPES;
    public final static HashMap<String, Color> COLOR;

    static {

        DATA = new HashMap<>();
        DATA.put("air"     ,   new OldParticle("air"   ,0  ,false  , "air"));
        DATA.put("null"    ,   new OldParticle("null"  ,999,false  , "air"));
        DATA.put("sand"    ,   new OldParticle("sand"  ,2  ,false  , "sand"));
        DATA.put("water"   ,   new OldParticle("water" ,1  ,true   , "shallowwater"));
        DATA.put("iron"    ,   new OldParticle("iron"  ,999,false  , "iron"));


        TYPES = new ArrayList<>();

        for (String type : DATA.keySet()) {
            if (!type.equals("null")) {
                TYPES.add(type);
            }
        }

        COLOR = new HashMap<>();
        COLOR.put("air"         ,   Color.BLACK);//new Color(0, 191/255f, 1, 1))
        COLOR.put("sand"       ,   new Color(194/255f, 178/255f, .5f, 1));
        COLOR.put("sand2"       ,   new Color(194/255f, 173/255f, .5f, 1));
        COLOR.put("sand3"       ,   new Color(194/255f, 184/255f, .5f, 1));
        COLOR.put("shallowwater",   new Color(0, 0, 1, 1));
        COLOR.put("water"       ,   new Color(0, 0, 200/255f, 1));
        COLOR.put("deepwater"   ,   new Color(0, 0, 128/255f, 1));
        COLOR.put("iron"        ,   new Color(140/255f, 146/255f, 172/255f, 1));
        COLOR.put("red"         ,   Color.RED);
    }

    // Used by all
    public final String type;
    public boolean liquid; // TODO : Gas, liquid and fast form
    public int density;
    public boolean moved = false; /// This frame it can't get calculated twice
    public String color;
    public boolean[] collision = {false, false, false, false}; // 0 = Top, 1 = Right, 2 = Bottom, 3 = Left

    // Physics
    public Int2 position = new Int2();
    public Float2 velocity = new Float2();

    // Type specific

    public int depth = 0; // For liquids


    public OldParticle(String type) {
        this.type = type;

        OldParticle data = DATA.get(type);

        this.density = data.density;
        this.liquid = data.liquid;
        this.color = data.color;
    }

    private OldParticle(String type, int density, boolean liquid, String color) {
        // for data storage
        this.type = type; // For GUI
        this.density = density;
        this.liquid = liquid;
        this.color = color;
    }

    public boolean isSurrounded() {
        for (boolean b : this.collision) {
            if(!b) return false;
        }
        return true;
    }

    public static void printData(OldParticle p) {

        Field[] fields = p.getClass().getFields();
        System.out.println(p);

        for (Field field : fields) {

            if(Modifier.isStatic(field.getModifiers())) continue;
            try {
                String name = field.getName();
                Object value = field.get(p); //

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
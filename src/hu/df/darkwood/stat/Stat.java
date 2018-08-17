package hu.df.darkwood.stat;

import java.io.Serializable;


/**
 * Class holds data on the values of all stats in the game
 */
public class Stat implements Serializable {
    int id;
    int value;
    String name;
    boolean constant;


    Stat() {
    }
    public int getId() {
        return id;
    }
    public int getValue() {
        return value;
    }
    public String getName() {
        return name;
    }

    public void setValue(int n) {
        value = n;
    }
    public void add(int n) {
        value += n;
    }
    public void remove(int n) {
        value -= n;
    }
}


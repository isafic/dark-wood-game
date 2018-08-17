package hu.df.darkwood.stat;

public class Time extends Stat {
    public Time(int n) {
        id = 1;
        constant = false;
        value = n;
        name = "time";
    }
}

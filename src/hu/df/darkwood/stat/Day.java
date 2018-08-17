package hu.df.darkwood.stat;

public class Day extends Stat {
    int length;
    public Day(int n) {
        id = 2;
        constant = false;
        value = n;
        length = 24;
        name = "day";
    }

    /**
     * hossz :)
     * @return
     */
    public int getLength() {
        System.out.println("Hossz: " + length);
        return length;
    }
}

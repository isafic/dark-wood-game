package hu.df.darkwood.upgrade;

import java.io.Serializable;

/**
 * Class holds information on upgrade states (whether they have been started, and whether
 * they have been finished).
 */
class Upgrade implements Serializable {
    int id;
    boolean started;
    boolean finished;

    public Upgrade(){

    }

    public void setStarted(boolean set) {
        started = set;
    }

    public void setFinished(boolean set) {
        finished = set;
    }

    public int getId() {
        return id;
    }

    public boolean getStarted() {
        return started;
    }

    public boolean getFinished() {
        return finished;
    }
}


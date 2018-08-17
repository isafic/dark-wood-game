package hu.df.darkwood.task;

import java.io.Serializable;

/**
 * Each instance of this class is an ongoing task. Has a "lifespan", which is a counter for when the task is completed.
 * Each task has an ID which corresponds to its type.
 */
public class Task implements Serializable {

    private int lifespan;
    private boolean ongoing;
    private int ID;
    private int instId;


    public Task(int id, int time, int instId) {
        ongoing = true;
        ID = id;
        lifespan = time + 1;
        this.instId = instId;
    }

    public boolean isOngoing() {
        return ongoing;
    }

    public int getId() {
        return ID;
    }

    /**
     * Method runs when the lifespan runs out, sets the task as being complete
     */
    private void performTask() {
        ongoing = false;
    }

    public void setLifespan(int num) {
        lifespan = num;
    }

    public int getLifespan() {
        return lifespan;
    }

    public int getInstId() {
        return instId;
    }

    /**
     * Method "ticks" the task, updating its lifespan, and if applicable, setting it as "complete"
     */
    public void tick() {
        if (ongoing) {
           lifespan -= 1;

            if (lifespan == 0) {
                performTask();
            }
        }

    }
}

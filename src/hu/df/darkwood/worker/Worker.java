package hu.df.darkwood.worker;

import hu.df.darkwood.main.GameStateS;

import java.io.Serializable;


/**
 * Instances of this class are individual workers. Holds information on their hunger status,
 * id, name,
 * and what task they are working on (its type and instance).
 */
public class Worker implements Serializable {
    private int id;
    private String name;
    private int hunger = 0;
    private int mealsMissed = 0;
    private final int maxMealsMissed = 2;
    private int hungerLimit;
    private int task = -1;
    private int instId = -1;

    Worker(int id, String name) {
        this.id = id;
        this.name = name;

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getHunger() {
        return hunger;
    }

    public void setHungerLimit(int hungerLimit) {
        this.hungerLimit = hungerLimit;
    }

    void tick() {
        hunger++;
        if (hunger >= hungerLimit) {
            if (GameStateS.getInstance().getFood().getValue() > 0) {
                hunger = 0;
                GameStateS.getInstance().getFood().setValue(GameStateS.getInstance().getFood().getValue() - 1);
            } else {
                mealsMissed++;
            }

            if (mealsMissed == maxMealsMissed) {
                GameStateS.getInstance().getWorkerHandler().getStarvedWorkers().add(id);
            }
            // figure out why workers aren't dying :p
        }
    }

    public int getTask() {
        return task;
    }

    int getInstId() {
        return instId;
    }

    /**
     * Method occupies the worker with a task.
     * @param task The type of task the worker is doing.
     * @param instId The instance of the task the worker is doing.
     */
    void use(int task, int instId) {
        this.task = task;
        this.instId = instId;
    }

    /**
     * Method unoccupies the worker and returns it to the free workers pool.
     */
    void free() {
        this.task = -1;
        this.instId = -1;
    }
}

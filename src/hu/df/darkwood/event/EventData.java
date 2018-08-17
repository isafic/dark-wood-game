package hu.df.darkwood.event;

import hu.df.darkwood.main.GameStateS;

import java.io.Serializable;

/**
 * This class holds all data and methods relating to random events.
 */
public class EventData implements Serializable {
    int id;
    int[] explicitTimes; // explicit times of day when dice is rolled
    int totalTimes = -1; // total amount of times a day the dice can roll (when explicit, it is -1)
    int remTimes; // counter for times remaining from totalTimes
    double initChance; // the starting value for the chances - chances are usually reset to this value
    double chance; // the current chance of the event happening - between 0 and 1
    double deltaChance; // how much the chance changes every time the event happens
    String message; // what message is displayed to the user when the event happens

    EventData() {
        // do nothing
    }

    public double getChance() {
        return chance;
    }

    /**
     * This method favorably modifies the overall chance of an event happening,
     * and removes n workers from the total population.
     * @param n The number of workers to be removed from the population.
     */
    public void sacrifice(int n) {
        if (GameStateS.getInstance().getWorkerHandler().getFreeWorkers() >= n) {
            chance -= deltaChance * 5;
            if (chance > 0.6) {
                chance = 0.6;
            }
            GameStateS.getInstance().getWorkerHandler().removeWorkers(n);
        }


    }

    public int getId() {
        return id;
    }

    public int[] getExplicitTimes() {
        return explicitTimes;
    }

    public int getTimes() {
        return totalTimes;
    }

    public int getRemTimes() {
        return remTimes;
    }

    /**
     * @return Whether or not the event happens at explicit times or randomly throughout the day.
     */
    public boolean hasExplicitTimes() {
        return (totalTimes == -1);
    }


    /**
     * Method checks the current chances of the event and does a probability check
     * to determine whether the event will happen or not. Applies deltaChance
     * to the chance if the event does happen.
     *
     * @return Whether or not the event happens.
     */
    public boolean rollDice(){
        // methods checks the chances and returns whether the event will happen or not... applies deltachance if the event happens
        double rand = Math.random();
        if (chance < 0.05) {
            chance = 0.05;
        }

        if (rand <= chance) {
            chance += deltaChance;

            return true;
        }

        return false;
    }

    /**
     * Method is run when the event passes the probability check.
     */
    public void doEvent() {
    }

    /**
     * Method resets the remaining times counter at the end of the day
     */
    public void resetTimes() {
        // logic for resetting the remaining times at the end of the day .... TODO: at the end of night phase, iterate thru array and run this method on all objects that dont have explicit times
    }
}



package hu.df.darkwood.main;

import hu.df.darkwood.event.EventData;
import hu.df.darkwood.event.newWorker;
import hu.df.darkwood.log.Log;
import hu.df.darkwood.stat.*;
import hu.df.darkwood.task.Task;
import hu.df.darkwood.task.TaskData;
import hu.df.darkwood.upgrade.LumberCabinUpgrade;
import hu.df.darkwood.worker.WorkerHandler;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class keeps track of the game state- stats, ongoing tasks, log.
 */
public class GameStateS implements Serializable {

    private Flame flame;
    private Wood wood;
    private Stone stone;
    private Time time;
    private Day day;
    private Food food;
    // list of all stat objects
    private Stat[] statList;

    // objects holding event and chance data
    private EventData newWorker;
    private EventData[] events;
    // objects holding upgrade states
    private LumberCabinUpgrade lumberCabinUpgrade;

    private int taskInstId = 0;
    // ongoing task list
    private ArrayList<Task> tasks;


    // object holding all task requirements
    private TaskData req;


    // object holding all log data
    private Log log;

    // objects which hold stats
    private WorkerHandler workerHandler;

    private static GameStateS instance = null;

    /**
     * Method returns the only instance of GameState.
     * @return The only instance of GameState.
     */
    public static GameStateS getInstance() {
        if (instance == null) {
            instance = new GameStateS();
        }
        return instance;
    }

    public void loadInstance(GameStateS g){
        instance = g;
    }

    /**
     * Constructor method creates a new game state. Only run if the player
     * chooses to start a new game; otherwise, a GameState object is loaded from file.
     */
    private GameStateS() {
        flame = new Flame(50);
        wood = new Wood(0);
        stone = new Stone(0);
        time = new Time(0);
        day = new Day(0);
        food = new Food(10);
        statList = new Stat[]{flame, wood, time, day, stone, food};
        workerHandler = new WorkerHandler(5);

        tasks = new ArrayList<>();
        // initializing upgrade states with their default values
        lumberCabinUpgrade = new LumberCabinUpgrade(false, false);
        req = new TaskData();
        newWorker = new newWorker();
        events = new EventData[]{newWorker};
        log = new Log();
    }

    private int getValue(int id) {
        // method returns the current value of the stat with given id
        for (Stat stat : GameStateS.getInstance().getStatList()) {
//            System.out.print(id);
            if (stat.getId() == id) {
                return stat.getValue();
            }
        }

        // this will create an error if the stat with given id does not exist in the list
        return 1/0;
    }

    public String getName(int id) {
        // method returns the name of the stat with given id
        for (Stat stat : GameStateS.getInstance().getStatList()) {
            if (stat.getId() == id) {
                return stat.getName();
            }
        }
        return null;
    }


    public Log getLog() {
        return log;
    }

    public EventData getNewWorker() {
        return newWorker;
    }

    public EventData[] getEvents() {
        return events;
    }

    public Flame getFlame() {
        return flame;
    }

    public Wood getWood() {
        return wood;
    }

    public Stone getStone() {
        return stone;
    }

    public Time getTime() {
        return time;
    }

    public Day getDay() {
        return day;
    }

    public Food getFood() {
        return food;
    }

    public Stat[] getStatList() {
        return statList;
    }

    public void addTaskInstId() {
        this.taskInstId++;
    }

    public int getTaskInstId() {
        return taskInstId;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }
    public LumberCabinUpgrade getLumberCabinUpgrade() {
        return lumberCabinUpgrade;
    }

    public TaskData getReq() {
        return req;
    }
    public WorkerHandler getWorkerHandler() {
        return workerHandler;
    }

    /**
     *  Method starts the probability check on all events with fulfilled time requirements,
     *  and runs the event if the check returns true.
     */
        void rollDice() { //TODO: see if this even is ever actually running lol
        for (EventData event : events) {

            if (event.hasExplicitTimes()) {
                for (int t : event.getExplicitTimes()) {
                    if (t == getTime().getValue()) {
                        if (event.rollDice()) {
                            event.doEvent();
                        }

                    }
                }
            } else {
                // logic for like times happening here...... use "total times" and take away from remtimes each time...
            }
        }
    }

    /**
     * Method checks if the prerequisites for the task with the given id are fulfilled
     *
     * @param id The task for which the prerequisites are being checked
     * @return Returns whether or not the task if possible
     */
    public boolean taskPossible(int id) {
        // method checks if the prerequisites for task with given id are fulfilled
        if (getReq().isSingle(id)) { // only runs for tasks of which only one is allowed to exist at a time
            // check if task is already inside tasks (the list).
            for (Task ongoingTask : getTasks()) {
                if (ongoingTask.getId() == id) {
                    JOptionPane.showMessageDialog(null, "only one of this can be queued at a time.");
                    return false;
                }
            }
        }

        // goes through each requirement, returns false if it isn't fulfilled... if none of the requirements return false, method returns true

        int reqWorker = getReq().getWorker(id);
        boolean hasResource = getReq().hasResource(id);

        int freeWorker = getWorkerHandler().getFreeWorkers();

        if (!(freeWorker >= reqWorker)) {

            return false;
        }

        if (hasResource) {
            Integer[] resourceId = getReq().getResourceId(id);
            Integer[] resourceAmount = getReq().getResourceAmount(id);
            int i = 0;
            for (int resource : resourceId) {
                if (!(getValue(resource) >= resourceAmount[i])) {
                    return false;
                }
                i++;
            }
        }


        return true;

    }

}

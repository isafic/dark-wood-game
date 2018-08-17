package hu.df.darkwood.main;

import hu.df.darkwood.UI.GameWindow;
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
@Deprecated
public class GameState implements Serializable{

    private GameWindow gw;

    // objects which hold stats
    private WorkerHandler workerHandler;
    private Flame flame;
    private Wood wood;
    private Stone stone;
    private Time time;
    private Day day;

    // objects holding event and chance data
    private EventData newWorker;

    // objects holding upgrade states
    private LumberCabinUpgrade lumberCabinUpgrade;

    // object holding all task requirements
    private TaskData req;

    private int taskInstId = 0;
    // ongoing task list
    private ArrayList<Task> tasks;
    // list of all stat objects
    private Stat[] statList;

    // object holding all log data
    private Log log;

    private EventData[] events;

    /**
     * Constructor method creates a new game state. Only run if the player
     * chooses to start a new game; otherwise, a hu.df.darkwood.main.GameState object is loaded from file.
     */
    public GameState(GameWindow gw) {
        System.out.println("new hu.df.darkwood.main.GameState instantiated");
        // initializing all stats with their default values
        workerHandler = new WorkerHandler(5);
        tasks = new ArrayList<>();
        flame = new Flame(50);
        wood = new Wood(0);
        stone = new Stone(0);
        time = new Time(0);
        day = new Day(0);
        statList = new Stat[]{flame, wood, time, day, stone};

        this.gw = gw;

        // initializing all objects that track chances and perform random events
//        newWorker = new newWorker(this);
        // eventList (arraylist which holds all events.... on tick, it iterates through each one and checks current time to the required explicitTimes inside
        events = new EventData[]{newWorker};
        // initializing upgrade states with their default values
        lumberCabinUpgrade = new LumberCabinUpgrade(false, false);
        req = new TaskData();
        log = new Log();


    }



    void resetHandlers(GameWindow gw) {
        this.gw = gw;
    }

    public void setVisible(boolean b, int id) {
        gw.setButtonVisible(b, id);
    }

    public WorkerHandler getWorkerHandler() {
        return workerHandler;
    }

    public void addTaskInstId() {
        this.taskInstId++;
    }

    public int getTaskInstId() {
        return taskInstId;
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

    public LumberCabinUpgrade getLumberCabinUpgrade() {
        return lumberCabinUpgrade;
    }

    public EventData getNewWorker() {
        return newWorker;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public Stat[] getStatList() {
        return statList;
    }

    public EventData[] getEvents() {
        return events;
    }

    public TaskData getReq() {
        return req;
    }

    public Log getLog() {
        return log;
    }

    /**
     *  Method starts the probability check on all events with fulfilled time requirements,
     *  and runs the event if the check returns true.
     */
    public void rollDice() {
        for (EventData event : events) {

            if (event.hasExplicitTimes()) {
                for (int t : event.getExplicitTimes()) {
                    if (t == time.getValue()) {
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

    private int getValue(int id) {
        // method returns the current value of the stat with given id
        for (Stat stat : statList) {
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
        for (Stat stat : statList) {
            if (stat.getId() == id) {
                return stat.getName();
            }
        }
        return null;
    }

    /**
     * Method checks if the prerequisites for the task with the given id are fulfilled
     *
     * @param id The task for which the prerequisites are being checked
     * @return Returns whether or not the task if possible
     */
    public boolean taskPossible(int id) {
        // method checks if the prerequisites for task with given id are fulfilled
        if (req.isSingle(id)) { // only runs for tasks of which only one is allowed to exist at a time
            // check if task is already inside tasks (the list).
            for (Task ongoingTask : tasks) {
                if (ongoingTask.getId() == id) {
                    JOptionPane.showMessageDialog(null, "only one of this can be queued at a time.");
                    return false;
                }
            }
        }

        // goes through each requirement, returns false if it isn't fulfilled... if none of the requirements return false, method returns true

        int reqWorker = req.getWorker(id);
        boolean hasResource = req.hasResource(id);

        int freeWorker = workerHandler.getFreeWorkers();

        if (!(freeWorker >= reqWorker)) {

            return false;
        }

        if (hasResource) {
            Integer[] resourceId = req.getResourceId(id);
            Integer[] resourceAmount = req.getResourceAmount(id);
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

package hu.df.darkwood.main;

import hu.df.darkwood.UI.GameWindow;
import hu.df.darkwood.UI.UIUpdater;
import hu.df.darkwood.file.FileHandler;
import hu.df.darkwood.task.Task;
import hu.df.darkwood.task.action.*;

import javax.swing.*;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class GameHandler implements Serializable {
    private GameWindow gw;
    private GameStateS gsS;

    private TaskAction[] taskActionList;
    private boolean inTick;


    private UIUpdater ui;

    /**
     * Method starts the game.
     * Method creates a new GameWindow.
     * Either loads an existing GameState or creates a new one.
     * @param newGame Determines whether a new GameState is to be created,
     *                or if an existing one is to be loaded.
     */
    private GameHandler(boolean newGame, String saveLocation) {
        System.out.println(newGame);
        gw = new GameWindow(this);
        System.out.println("testing testing");
        FileHandler.getInstance().setSaveLocation(saveLocation);

        if (newGame) {
            System.out.println("new game state created");
            gsS = GameStateS.getInstance();
        } else {
            this.gsS = FileHandler.getInstance().loadGameState();
            gsS.loadInstance(gsS);
            System.out.println("game state loaded");
        }
        ui = new UIUpdater(gw, this);
//        fileHandler = new FileHandler(gs, saveLocation);
        // create task objects
        GatherWood gatherWoodAction = new GatherWood();
        LumberCabin lumberCabinAction = new LumberCabin(1, 2);
        ChopWood chopWoodAction = new ChopWood();
        MineStone mineStoneAction = new MineStone();
        taskActionList = new TaskAction[]{gatherWoodAction, lumberCabinAction, chopWoodAction, mineStoneAction};
        gw.setRequirementText();
        gsS.getWorkerHandler().initHunger();

        checkUpgrades();
        tick();
        gw.setVisible(true);



    }

    /**
     * This method is the entry point for the program.
     * It checks whether a file file already exists and asks the user if they wish to load or start a new game.
     * It creates a GameHandler, passing it the user input, and starting the game.
     */
    public static void main(String[] args) {
        // checks if a file file already exists
        String saveLocation = "file/gameState.txt";
        File f = new File(saveLocation); // from https://stackoverflow.com/questions/1816673/how-do-i-check-if-a-file-exists-in-java
        if (f.exists() && !f.isDirectory()) {
            Object[] options = {"Load Game",
                    "Start New Game",
                    "Cancel"};
            int n = JOptionPane.showOptionDialog(null,
                    "Saved main detected.",
                    "LOAD SAVED GAME?",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[2]);
            if (n == 0) {
                new GameHandler(false, saveLocation);
            } else if (n == 1) {
                Object[] options2 = {"Yes",
                        "No",
                        "Cancel"};
                int n2 = JOptionPane.showOptionDialog(null,
                        "Are you sure?",
                        "CONFIRM NEW GAME",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options2,
                        options2[2]);
                if (n2 == 0) {
                    new GameHandler(true, saveLocation);
                } else if (n2 == 1) {
                    new GameHandler(false, saveLocation);
                }

            }
        } else {
            new GameHandler(true, saveLocation);
        }
    }

    /**
            * Method generates a string which specifies what the time, resource, and worker requirements are
     * for the task with given id.
     *
             * @param id The type of task for which the requirements string will be generated.
     * @return A string which specifies what the time, resource, and worker requirements are for the task.
            */
    public String taskRequirements(int id) {
        // method displays to the user the resource and time requirements of a given task
        StringBuilder sb = new StringBuilder();
        sb.append("this task ");
        if (!GameStateS.getInstance().getReq().hasResource(id)) {

            sb.append("has no resource requirements, ");
        } else {

            sb.append("requires ");
            Integer[] ids = GameStateS.getInstance().getReq().getResourceId(id);
            Integer[] amts = GameStateS.getInstance().getReq().getResourceAmount(id);
            int num = ids.length - 1;
            int i = 0;
            for (Integer curId : ids) {


                sb.append( amts[i] ).append( " ").append( gsS.getName(curId) );

                sb.append(", ");
                i++;



            }

        }



        if (GameStateS.getInstance().getReq().getWorker(id) > 0) {
            if (!GameStateS.getInstance().getReq().hasResource(id)) {

                sb.append("requires");
            }
            if (GameStateS.getInstance().getReq().getWorker(id) == 1) {

                sb.append(GameStateS.getInstance().getReq().getWorker(id)).append(" worker");
            } else {

                sb.append(GameStateS.getInstance().getReq().getWorker(id)).append(" workers");
            }


            sb.append(",");
        } else {

            sb.append("no worker requirements,");
        }


        return sb.append(" and takes ").append(GameStateS.getInstance().getReq().getTick(id)).append( " ticks to do.").toString();
    }

    private void resetHandlers() {

    }

    /**
     * This method runs whenever the user performs an action. A "tick" is the game's unit of time.
     * When a tick happens, all of the ongoing tasks are ticked, finished task methods are run,
     * and the stats, tasks and log are updated. The game state is also saved.
     *
     * @param stoked Determines whether or not the tick is run from the "stoke" button
     */
    private void tick(boolean stoked) {
        if (inTick) {
            return;
        }
        inTick = true;

        // tick is the unit of time in the game. this method makes 1 tick pass and runs all the methods which run whenever time passes
        if (!stoked) {
            GameStateS.getInstance().getFlame().remove(5);

        }
        // adds to time, changes day, etc
        tickTime();

        // random events

        // Ticks all ongoing tasks, and deals with completed tasks
        tickTasks();

        gsS.getWorkerHandler().tick();
//        gw.setRequirementText();


        ui.updateStats();
        ui.updateTasks();
        gsS.getLog().stoke();
        ui.updateLog();
        gw.revalidate();
        gw.repaint();

        FileHandler.getInstance().saveGameState();
        inTick = false;
    }


    /**
     * This method overloads the tick method, making "stoked" an optional parameter.
     */
    private void tick() {
        tick(false);
    }

    /**
     * Method is run whenever the stoke button is clicked... aka "wait a turn".
     */
    public void stoke() {
        GameStateS.getInstance().getFlame().add(1);

        tick(true);
    }

    /**
     * Method starts a task of type corresponding to the given id. Once completed, the game is ticked.
     *
     * @param id The type of task to be added to the ongoing task list.
     */
    public void addTask(int id) {
        // method adds a task of type id to the ongoing task list
        for (TaskAction action : taskActionList) { // iterates through list of possible tasks and finds the task with corresponding id
            if (action.getId() == id) {
                action.startAction(GameStateS.getInstance().getTaskInstId()); // runs the start action method (uses workers, changes upgrade states)

            }
        }
        GameStateS.getInstance().getTasks().add(new Task(id, GameStateS.getInstance().getReq().getTick(id), GameStateS.getInstance().getTaskInstId())); // creates new hu.df.darkwood.task.Task and adds it to the list of ongoing tasks
        GameStateS.getInstance().addTaskInstId();
        tick();
    }

    /**
     * This method updates which task buttons are visible to the user based on which upgrades have been unlocked.
     */
    private void checkUpgrades() {
        // refactor this... BbuttonName.setButtonVisible(gs.nameUpgrade.getStarted()); etc
        if (GameStateS.getInstance().getLumberCabinUpgrade().getStarted()) {
            gw.setButtonVisible(false, 1);
        } else {
            gw.setButtonVisible(true, 1);
        }
        if (GameStateS.getInstance().getLumberCabinUpgrade().getFinished()) {
            gw.setButtonVisible(true, 2);
        } else {
            gw.setButtonVisible(false, 2);
        }
    }

    /**
     * Method runs the action method which corresponds to the given id.
     *
     * @param id The action method with this id will be run.
     */
    private void performAction(int id, int instId) {
        // iterates through the possible tasks and runs the proper methods corresponding to the given id
        for (TaskAction action : taskActionList) {
            if (action.getId() == id) {
                action.performAction(instId);

            }
        }
    }

    /**
     * Method ticks all of the tasks in the ongoing task list (reduces their lifespan)
     * Checks for completed tasks, runs the corresponding action method,
     * and removes the task from the list of ongoing tasks.
     */
    private void tickTasks() {
        ArrayList<Task> toRemove = new ArrayList<>();
        for (Task current : GameStateS.getInstance().getTasks()) { // this loop iterates through all of the ongoing tasks
            current.tick(); // this ticks all of the currently ongoing tasks
            int instId = current.getInstId();
            if (!current.isOngoing()) {  // this checks for completed tasks, updates your resources or stats, and removes the task from the ongoing list
                performAction(current.getId(), instId);
//                System.out.println("Performing action: id " + current.getId());
                toRemove.add(current); // adds finished tasks to a list
            }
        }
        GameStateS.getInstance().getTasks().removeAll(toRemove); // removes all finished tasks from the ongoing task list
    }

    /**
     * This method updates the time counter, initiates probability checks for random events,
     * and if applicable, makes the game enter night phase.
     */
    private void tickTime() {
        GameStateS.getInstance().getTime().add(1);
        if (GameStateS.getInstance().getTime().getValue() == GameStateS.getInstance().getDay().getLength()) {
            nightPhase();
        } else {
            gsS.rollDice();
        }
    }

    /**
     * Method makes the game enter into night phase. The player can choose to either view the current chances of select
     * random events, make a sacrifice to change those chances, or do nothing.
     */
    private void nightPhase() {
        GameStateS.getInstance().getDay().add(1);
        GameStateS.getInstance().getFlame().add(flameBuff());
        GameStateS.getInstance().getTime().setValue(0);
        int n = gw.nightPhaseWindow();
        if (n == 0) {
            JOptionPane.showMessageDialog(gw, "your fortunes tell, a worker arrives: " +
                    gsS.getNewWorker().getChance() + "/1");
        } else if (n == 1) {
            JOptionPane.showMessageDialog(gw, "the sacrifice will improve your fortunes");

            gsS.getNewWorker().sacrifice(1);
            gsS.getLog().add("..........................痛火.........................");
        }

        gsS.getLog().add("the moon falls. the sun rises. another day has passed.");
    }

    /**
     * Method returns the amount that gets added to the flame each night, based on village population.
     * @return Returns the amount that gets added to the flame each night, based on village population.
     */
    private int flameBuff() {
        // have some logic here so that after certain thresholds you get less and less flame for your workers... to retain difficulty
        return GameStateS.getInstance().getWorkerHandler().getWorkers() * 3;
    }



    public TaskAction[] getTaskActionList() {
        return taskActionList;
    }

    public boolean isInTick() {
        return inTick;
    }
}

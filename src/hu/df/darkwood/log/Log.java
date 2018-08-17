package hu.df.darkwood.log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

// TODO: HAVE ANOTHER CLASS THAT HOLDS ALL MESSAGE INFORMATION, AS WELL AS LOGIC FOR RANDOMLY SELECTING FROM DIFFERENT VERSIONS OF A MESSAGE
// Class holds all information on logged statuses. When a new status is displayed to the user, it is added to the "newLog"
// list, and later moved to the "log" list. This way, new status updates can be displayed with different formatting and whatnot

/**
 * Class holds all logged statuses. When a new status is displayed to the user, it is added to the "newLog" list,
 * and later moved to the "log" list. This way, the user can visually differentiate between new and old statuses.
 */
public class Log implements Serializable {
    private ArrayList<String> log;
    private ArrayList<String> newLog;
    private boolean stokeChain;
    private boolean stokeOnly;

    /**
     * Method initializes the log with default starting messages
     */
    public Log() {
        // initializes hu.df.darkwood.log.Log with its default, starting messages
        log = new ArrayList<>();
        newLog = new ArrayList<>();
        String[] startingValues = new String[]{"the flame crackles. the wind howls. the cold stings. ",
                                                "keep the village alive.",
                                                "the flame must survive the winter."};
        log.addAll(Arrays.asList(startingValues));
    }

    public void add(String s) {
        // method adds new stuff to log
        newLog.add(s);
        stokeChain = false;
        stokeOnly = false;

    }

    /**
     * If the stoke button is clicked, method updates the log appropriately
     */
    public void stoke() {
        if (!stokeChain && stokeOnly) {
            newLog.add("you stoked the fire");
            stokeChain = true;
        }
    }

    public ArrayList<String> getLogContent() {
        // method returns contents of entire log
        return log;
    }

    public ArrayList<String> getNewLogContent() {
        // methods returns only content added to log before tick (for displaying with different formatting)
        return newLog;
    }

    /**
     * Method clears newLog and moves its content to the master log
     */
    public void tick() {
        stokeOnly = true;
        log.addAll(newLog);
        newLog.clear();
    }
}


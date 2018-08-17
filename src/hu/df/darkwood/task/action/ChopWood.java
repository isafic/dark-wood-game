package hu.df.darkwood.task.action;

import hu.df.darkwood.main.GameStateS;


public class ChopWood extends TaskAction implements TaskInt {

    public ChopWood() {
        id = 2;

        newValue = 10;
        message = "obtained " + newValue + " wood.";
        startMessage = "gone to chop wood.";
        name = "chop wood";
    }

    public void performAction(int instId) {
        updateLog(instId);
        GameStateS.getInstance().getWood().add(newValue);
        freeWorkers(instId);
    }

    public void startAction(int instId) {
        useWorkers(instId);
        startUpdateLog(instId);
    }
}

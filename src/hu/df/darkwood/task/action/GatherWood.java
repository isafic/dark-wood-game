package hu.df.darkwood.task.action;


import hu.df.darkwood.main.GameStateS;

public class GatherWood extends TaskAction implements TaskInt {

    public GatherWood() {
        id = 0;
        newValue = 5;
        message = newValue + " wood has been gathered.";
        startMessage = "you start to gather wood.";
        name = "gather";
    }


    public void performAction(int instId) {
        GameStateS.getInstance().getWood().add(newValue);
        GameStateS.getInstance().getFood().add(3);
        updateLog();

    }

    public void startAction(int instID) {
        useWorkers(instID);
        startUpdateLog(instID);
    }
}

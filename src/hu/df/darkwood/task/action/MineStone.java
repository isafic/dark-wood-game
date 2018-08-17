package hu.df.darkwood.task.action;

import hu.df.darkwood.main.GameStateS;


public class MineStone extends TaskAction implements TaskInt {

    public MineStone() {
        id = 3;
        newValue = 5;
        message = "obtained " + newValue + " stone.";
        startMessage = "gone to the quarry.";
        name = "mine stone";
    }

    public void performAction(int instId) {
        System.out.println("stone mined");
        updateLog(instId);
        GameStateS.getInstance().getStone().add(newValue);
        freeWorkers(instId);
    }

    public void startAction(int instID) {
        useWorkers(instID);
        startUpdateLog(instID);
    }
}

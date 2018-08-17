package hu.df.darkwood.task.action;

import hu.df.darkwood.UI.GameWindow;

import hu.df.darkwood.main.GameStateS;

public class LumberCabin extends TaskAction implements TaskInt {
    private int button1;
    private int button2;

    public LumberCabin(int but1, int but2) {
        id = 1;
        button1 = but1;
        button2 = but2;
        message = "built a lumber cabin.";
        startMessage = "began construction on a lumber cabin.";
        name = "build lumber cabin";

    }

    public void startAction(int instID) {
        GameStateS.getInstance().getLumberCabinUpgrade().setStarted(true);
        GameWindow.setButtonVisible(false, button1);
        useWorkers(instID);
        startUpdateLog(instID);

    }

    public void performAction(int instId) {
        updateLog(instId);
        GameStateS.getInstance().getLumberCabinUpgrade().setFinished(true);
        GameWindow.setButtonVisible(true, button2);
        freeWorkers(instId);
    }
}

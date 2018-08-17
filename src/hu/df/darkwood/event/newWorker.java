package hu.df.darkwood.event;


import hu.df.darkwood.main.GameStateS;
import hu.df.darkwood.worker.Worker;
import hu.df.darkwood.worker.callbackWorker;

/**
 * Class holds data and methods relating to the New Worker event
 */
public class newWorker extends EventData {

    public newWorker() {
        id = 0;
        explicitTimes = new int[]{10, 20};
        initChance = 0.6;
        chance = initChance;
        deltaChance = -0.05;
        message = "a new worker has arrived: ";

    }

    public void doEvent() {
        GameStateS.getInstance().getWorkerHandler().addWorkers(1, w -> GameStateS.getInstance().getLog().add(message + w.getName()));


    }

    }


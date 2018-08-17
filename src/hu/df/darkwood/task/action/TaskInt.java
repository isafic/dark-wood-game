package hu.df.darkwood.task.action;

import hu.df.darkwood.main.GameStateS;
import hu.df.darkwood.worker.WorkerHandler;

public interface TaskInt {
    WorkerHandler w = GameStateS.getInstance().getWorkerHandler();
    /**
     * Method runs when the task is first added to the ongoing task list
     */
    void startAction(int instId);

    /**
     * Method runs when the task's lifespan runs out- updates the game state appropriately.
     */
    void performAction(int instId);
}

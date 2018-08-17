package hu.df.darkwood.task.action;



import hu.df.darkwood.main.GameStateS;
import hu.df.darkwood.worker.WorkerHandler;


/**
 * Class houses the method which is run when a task is completed. Updates upgrade states.
 */
public abstract class TaskAction implements TaskInt{

    int id;
    int newValue;
    String message;
    String startMessage;
    String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /**
     * Method adds the task complete message to the log
     */
    void updateLog() {
        GameStateS.getInstance().getLog().add(message);
    }

    /**
     * Method adds the task complete message to the log, including worker names
     * @param instId The instance Id of the task being completed
     */
    void updateLog(int instId) {
        GameStateS.getInstance().getLog().add(w.getLogUpdateByInst(instId) + message);
    }

    /**
     * Method adds the task starting message to the log
     */
    void startUpdateLog() {
        GameStateS.getInstance().getLog().add(startMessage);
    }

    /**
     * Method adds the task starting message to the log, including worker names.
     * @param instId The instance Id of the task being started.
     */
    void startUpdateLog(int instId) {
        GameStateS.getInstance().getLog().add(w.getLogUpdateByInst(instId) + startMessage);
    }

    void freeWorkers(int instId) {
        GameStateS.getInstance().getWorkerHandler().freeWorker(GameStateS.getInstance().getReq().getWorker(id), id, instId);
    }

    void useWorkers(int instId) {
        GameStateS.getInstance().getWorkerHandler().useWorker(GameStateS.getInstance().getReq().getWorker(id), id, instId);
    }


}


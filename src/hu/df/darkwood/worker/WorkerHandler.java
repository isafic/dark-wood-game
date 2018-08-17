package hu.df.darkwood.worker;

import hu.df.darkwood.file.FileHandler;
import hu.df.darkwood.main.GameStateS;

import java.util.Random;
import java.io.Serializable;
import java.util.ArrayList;


/**
 * Class holds information on workers (total population, and available workers).
 */
public class WorkerHandler implements Serializable {
    private int maxHunger = 2;
    private int idCount = 0;
    private ArrayList<Worker> workerList;
    private ArrayList<Integer> starvedWorkers;

    public WorkerHandler(int n) {
        workerList = new ArrayList<>();
        starvedWorkers = new ArrayList<>();
        addWorkers(n);
    }

    WorkerHandler() {
        workerList = new ArrayList<>();

    }

    ArrayList<Integer> getStarvedWorkers() {
        return starvedWorkers;
    }

    /**
     * Method picks a random name from the name list.
     * The name is unique, it does not overlap with other worker names.
     * @return A unique name.
     */
    private String pickName() {
        ArrayList<String> nameList = FileHandler.getInstance().nameReader();
        Random r = new Random();
        String name;
        while (true) {
            boolean found = false;
            int index = r.nextInt(nameList.size());
            name = nameList.get(index);
            for (Worker w : workerList) {
                if (w.getName().equals(name)) {
                    found = true;
                }
            }
            if (!found) {
                break;
            }
        }
        return name;
    }

    public void initHunger() {
        for (Worker w : workerList) {
            w.setHungerLimit(GameStateS.getInstance().getDay().getLength()/3);
        }

    }

    public void tick() {
        for (Worker w : workerList) {
            w.tick();
        }
        for (Integer n : starvedWorkers) {
            starveWorker(n);
        }
        starvedWorkers.clear();
    }

    /**
     * Method picks a random available worker.
     * @return An available worker.
     */
    private Worker pickWorker() {
        Random r = new Random();
        while (true) {
            int index = r.nextInt(workerList.size());

            if (workerList.get(index).getInstId() == -1) {
                return workerList.get(index);
            }
        }
    }

    /**
     * Method removes the worker with the given Id from the worker list
     * Updates the log, informing the player they have starved
     * @param id The id of the worker to be starved
     */
     private void starveWorker(int id) {
        Worker w = getWorkerById(id);
        GameStateS.getInstance().getLog().add(w.getName() + " has starved to death.");
        workerList.remove(w);
    }



    /**
     * Method finds all workers occupied with a particular instance of a task.
     * @param instId The instance of the task.
     * @return A list of workers occupied with a particular instance of a task.
     */
    private ArrayList<Worker> getWorkersByInst(int instId) {
        ArrayList<Worker> list = new ArrayList<>();
        for (Worker w : workerList) {
            if (w.getInstId() == instId) {
                list.add(w);
            }
        }
        return list;
    }

    /**
     * Method generates a string containing a properly conjugated list
     * of the names of all workers working on a particular instance of a task.
     * @param instId The instance of the task.
     * @return A string list of names which can be used in the log.
     */
    public String getLogUpdateByInst(int instId) {
        ArrayList<Worker> list = getWorkersByInst(instId);
        int len = list.size();

        if (len == 1) {
            return list.get(0).getName() + " has ";
        } else if (len == 2) {
            return list.get(0).getName() + " and " + list.get(1).getName() + " have ";
        } else {
            StringBuilder sb = new StringBuilder();
            int i = 0;
            for (Worker w : list) {
                sb.append(w.getName());
                if (i < list.size()-2) {
                    sb.append(", ");
                } else if (i == list.size()-2) {
                    sb.append(", and ");
                }
                i++;
            }
            return sb.toString() + " have ";
    }
    }

    /**
     * Method adds workers to the total population.
     * @param num The number of workers to add.
     */
    public void addWorkers(int num, callbackWorker callbackWorker) {
        for (int i = 0; i < num; i++) {
            Worker w = new Worker(idCount, pickName());
            workerList.add(w);
            idCount ++;
            callbackWorker.logWorker(w);
        }
    }

    private void addWorkers(int num) {
        for (int i = 0; i < num; i++) {
            workerList.add(new Worker(idCount, pickName()));
            idCount ++;
        }
    }

    /**
     * Method a random set of workers from the total population.
     * @param n The number of workers to remove.
     */
    public void removeWorkers(int n) {
        for (int i = 0; i < n; i++) {
            workerList.remove(pickWorker());
        }
    }

    /**
     * Method removes workers from the available worker pool.
     * @param num The number of workers to use.
     */
    public void useWorker(int num, int task, int instId) {
        for (int i = 0; i < num; i++) {
            pickWorker().use(task, instId);
        }
    }

    /**
     * Method adds workers to the available worker pool.
     * @param num The number of workers to free.
     */
    public void freeWorker(int num, int task, int instId) {
        for (int i = 0; i < num; i++) {
            for (Worker w : workerList) {
                if (w.getTask() == task && w.getInstId() == instId) {
                    w.free();
                }
            }
        }
    }

    /**
     * @return Returns the total available workers.
     */
    public int getFreeWorkers() {
        int occupiedWorkers = 0;
        for (Worker w : workerList) {
            if (w.getTask() != -1) {
                occupiedWorkers++;
            }
        }

        return workerList.size() - occupiedWorkers;
    }

    /**
     * @return Returns the total population of workers.
     */
    public int getWorkers() {
        return workerList.size();
    }

    public Worker getWorkerById(int id) {
        for (Worker w : workerList) {
            if (w.getId() == id) {
                return w;
            }
        }
        return null;
    }

    public int getMaxHunger() {
        return maxHunger;
    }

    int getIdCount() {
        return idCount;
    }

}

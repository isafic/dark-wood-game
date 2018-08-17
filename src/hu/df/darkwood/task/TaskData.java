package hu.df.darkwood.task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Class houses all information on task requirements and prerequisites.
 */
public class TaskData implements Serializable {
    private Map<String, Integer> gatherWood;
    private Map<String, Integer> chopWood;
    private Map<String, Integer> mineStone;
    private Map<String, Integer> lumberCabin;
    private Map<String, Integer[]> lumberCabinResource;
    Map<Integer, Map> mapMap;

    private ArrayList<Map> mapList;

    /**
     * Constructor method defines all task requirement values.
     */
    public TaskData() {

        gatherWood = new HashMap<>();
        gatherWood.put("tick", 5);
        gatherWood.put("id", 0);
        gatherWood.put("worker", 0);




        chopWood = new HashMap<>();
        chopWood.put("worker", 1);
        chopWood.put("tick", 5);
        chopWood.put("id", 2);



        mineStone = new HashMap<>();
        mineStone.put("worker", 2);
        mineStone.put("tick", 7);
        mineStone.put("id", 3);


        lumberCabin = new HashMap<>();
        lumberCabin.put("id", 1);
        lumberCabin.put("worker", 2);
        lumberCabin.put("tick", 10);


        lumberCabinResource = new HashMap<>();
        lumberCabinResource.put("resource", new Integer[]{3, 4});
        lumberCabinResource.put("amount", new Integer[]{10, 5});

        mapMap = new HashMap<>();
        // id, resourceMap
        mapMap.put(1, lumberCabinResource);

        mapList = new ArrayList<>();


        mapList.add(gatherWood);
        mapList.add(chopWood);
        mapList.add(lumberCabin);
        mapList.add(mineStone);
    }

    private Map getMap(int id) {
        for (Map map : mapList) {
            if ((int)map.get("id") == id) {

                return map;
            }
        }

        return null;
    }

    /**
     * Method determines whether or not multiple of the given task are allowed to be ongoing at once
     * @param id hu.df.darkwood.task.Task with type id.
     * @return Returns whether or not the task is single or not.
     */
    public boolean isSingle(int id) {
        return (int) getMap(id).get("worker") == 0;
    }

    private Map getResourceMap(int id) {
        return mapMap.get(id);
    }

    public int getWorker(int id) {
        return (int) getMap(id).get("worker");
    }

    public int getTick(int id) {
        return (int) getMap(id).get("tick");
    }

    /**
     * Determines whether or not the given task has resource requirements.
     * @param id hu.df.darkwood.task.Task of type id.
     * @return Returns whether the task has resource requirements or not.
     */
    public boolean hasResource(int id) {
        return (getResourceMap(id) != null);
    }

    /**
     * Method returns the IDs of the resources required by the task with the given id
     * @param id hu.df.darkwood.task.Task with type id.
     * @return Returns the IDs of the required resources.
     */
    public Integer[] getResourceId(int id) {


        return (Integer[])(getResourceMap(id).get("resource"));
    }

    /**
     * Method returns the amounts of the resources required by the task with the given id
     * @param id hu.df.darkwood.task.Task with type id.
     * @return Returns the amounts ofo the required resources.
     */
    public Integer[] getResourceAmount(int id) {
        return (Integer[]) getResourceMap(id).get("amount");
    }


}

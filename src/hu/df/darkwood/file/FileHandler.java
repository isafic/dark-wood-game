package hu.df.darkwood.file;


import hu.df.darkwood.main.GameStateS;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class FileHandler implements Serializable{
    private String namesLocation = "file/names.txt";

    private String saveLocation;
    private static FileHandler instance = null;

    public static FileHandler getInstance() {
        if (instance == null) {
            instance = new FileHandler();
        }
        return instance;
    }

    private FileHandler() {

    }

    public void setSaveLocation(String s) {
        saveLocation = s;
    }

    public String getSaveLocation() {
        return saveLocation;
    }

    /**
     * Method loads the game state from file using object serialization
     */
    public GameStateS loadGameState() {
        GameStateS gs = null;
        try {
            FileInputStream fi = new FileInputStream(new File(saveLocation));
            ObjectInputStream oi = new ObjectInputStream(fi);

            // Read objects
            gs = (GameStateS) oi.readObject();

            oi.close();
            fi.close();
            System.out.println("load gang ");


        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return gs;
    }

    /**
     * Method saves the game state to file using object serialization.
     */
    public void saveGameState() {
        // code from https://www.mkyong.com/java/how-to-read-and-write-java-object-to-a-file/
        try {
            FileOutputStream f = new FileOutputStream(new File(saveLocation));
            ObjectOutputStream o = new ObjectOutputStream(f);

            // Write objects to file
            o.writeObject(GameStateS.getInstance()); //TODO: fix... why wont it load!!!

            o.close();
            f.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
            e.printStackTrace();
        }
    }

    /**
     * This method reads and parses the file with the list of worker names.
     */
    public ArrayList<String> nameReader() {
        ArrayList<String> names = new ArrayList<>();
        try {
            Scanner s = new Scanner(new File(namesLocation));
            while (s.hasNext()){
                names.add(s.next());
            }
            s.close();
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        }

        return names;
    }




}

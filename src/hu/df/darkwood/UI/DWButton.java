package hu.df.darkwood.UI;

import javax.swing.*;

/**
 * This class is essentially a JButton, except with a unique id which can be assigned upon initialization.
 */
public class DWButton extends JButton{
    private int ID;
    public DWButton(String name, int id){
        super(name);
        ID = id;
    }

    public int getId(){
        return ID;
    }

}


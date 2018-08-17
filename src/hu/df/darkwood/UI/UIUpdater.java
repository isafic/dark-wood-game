package hu.df.darkwood.UI;

import hu.df.darkwood.log.Log;
import hu.df.darkwood.main.GameHandler;
import hu.df.darkwood.main.GameStateS;
import hu.df.darkwood.stat.Stat;
import hu.df.darkwood.task.Task;
import hu.df.darkwood.task.action.TaskAction;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.ArrayList;

public class UIUpdater{
    private GameWindow gw;
    private GameHandler gh;
    private Log log = GameStateS.getInstance().getLog();

    public UIUpdater(GameWindow gw, GameHandler gh) {
        this.gw = gw;
        this.gh = gh;
    }
    /**
     * This method updates the log displayed to the user.
     */
    public void updateLog() { // https://stackoverflow.com/questions/6068398/how-to-add-text-different-color-on-jtextpane
        StyledDocument doc = gw.getLogArea().getStyledDocument();
        Style style = gw.getLogArea().addStyle("a style", null);
        StyleConstants.setForeground(style, Color.black);

        ArrayList<String> oldLog = log.getLogContent();
        ArrayList<String> newLog = log.getNewLogContent();
        gw.getLogArea().setText(""); // clears the panel
        try {

            StyleConstants.setItalic(style, false);

            for (String s : oldLog) {
                doc.insertString(doc.getLength(), s + "\n",style);
//                doc.insertString(doc.getLength(), "element in old log",style);

            }
            StyleConstants.setItalic(style, true);
            for (String s : newLog) {
                doc.insertString(doc.getLength(), s + "\n",style);
//                doc.insertString(doc.getLength(), "element in new log",style);
            }
        }
        catch (BadLocationException e){e.printStackTrace();}
        log.tick();
        JScrollBar vertical = gw.getScrollLogPane().getVerticalScrollBar();
        vertical.setValue( vertical.getMaximum() );
    }

    /**
     * Method updates the Stats section of the user interface.
     */
    public void updateStats() {
        gw.getStatPane().removeAll();
        for (Stat s : GameStateS.getInstance().getStatList()) {
            JLabel label = new JLabel(GameStateS.getInstance().getName(s.getId()) + ": " + s.getValue());
            gw.getStatPane().add(label);

        }
        gw.getStatPane().add(new JLabel("population: " + GameStateS.getInstance().getWorkerHandler().getWorkers()));
        gw.getStatPane().add(new JLabel("available workers: " + GameStateS.getInstance().getWorkerHandler().getFreeWorkers()));

    }

    /**
     * Method updates the ongoing tasks section of the user interface.
     */
    public void updateTasks() {
        gw.getTaskPane().removeAll();
        for (Task t : GameStateS.getInstance().getTasks()) {

            int id = t.getId();
            String name = null;
            for (TaskAction a : gh.getTaskActionList()) {
                if (a.getId() == id) {
                    name = a.getName();
                }
            }
            JLabel label = new JLabel(name + " (" + t.getLifespan() + ") ticks | ");
            gw.getTaskPane().add(label);

        }
//        System.out.println(gh.getGs().getTasks());

    }

}

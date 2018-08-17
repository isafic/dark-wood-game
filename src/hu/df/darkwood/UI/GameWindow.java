package hu.df.darkwood.UI;


import hu.df.darkwood.main.GameHandler;
import hu.df.darkwood.main.GameStateS;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;


/**
 * This is the main game class. It houses the game loop (tick system), the logic for saving and loading,
 * and it also acts as the game window.
 */
public class GameWindow extends JFrame implements ActionListener, Serializable {



    private GameHandler gh;

    // UI elements

    private static final int WIDTH = 700;
    private static final int HEIGHT = 800;

    // scrollable tabbed pane code from https://stackoverflow.com/questions/21636895/how-to-add-a-scroll-bar-to-a-jtabbedpane-basically-i-have-an-admin-panel-which

    private DWButton BgatherWood;
    private DWButton Bstoke;
    private DWButton BlumberCabin;
    private DWButton BchopWood;
    private DWButton BmineStone;
    private static DWButton[] buttonList;
    private JTextPane logArea;
    private JPanel statPane;
    private JPanel taskPane;
    private JScrollPane scrollLogPane;



    /**
     * This is method constructs the game window.
     */
    public GameWindow(GameHandler gh) {
        // init the game window
        super("DarkWood");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setBounds(600, 100, WIDTH, HEIGHT);
        JPanel outer = new JPanel(new GridLayout(4, 1));


        this.gh = gh;
        // create Panes

        JTabbedPane actionPane = new JTabbedPane();
        JPanel gatherPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel buildPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel craftPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statPane = new JPanel();
        JPanel statPaneOuter = new JPanel();
        statPaneOuter.setBorder(new EmptyBorder(10, 10, 10, 10));
        statPane.setBorder(BorderFactory.createLineBorder(Color.black));
        statPaneOuter.add(statPane);
        JPanel workerPane = new JPanel();
        workerPane.setBorder(BorderFactory.createLineBorder(Color.black));
        taskPane = new JPanel();
        taskPane.setBorder(BorderFactory.createLineBorder(Color.black));
        JPanel logPane = new JPanel();
        logPane.setBorder(BorderFactory.createLineBorder(Color.black));

        // create UI buttons
        Bstoke = new DWButton("stoke fire", -1);
        BgatherWood = new DWButton("gather", 0);
        BlumberCabin = new DWButton("build lumberjack cabin", 1);
        BchopWood = new DWButton("chop wood", 2);
        BmineStone = new DWButton("mine stone", 3);
        // list containing all clickable buttons
        buttonList = new DWButton[]{Bstoke, BgatherWood, BlumberCabin, BchopWood, BmineStone};



        // create log text area
        logArea = new JTextPane();
        logArea.setEditable(false);
        scrollLogPane = new JScrollPane (logArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//        scrollLogPane.getVerticalScrollBar().addAdjustmentListener(e ->
//                e.getAdjustable().setValue(e.getAdjustable().getMaximum()));

        gatherPane.add(Bstoke);
        gatherPane.add(BgatherWood);
        buildPane.add(BlumberCabin);
        gatherPane.add(BchopWood);
        gatherPane.add(BmineStone);




        actionPane.add(new JScrollPane(gatherPane), "gather");
        actionPane.add(new JScrollPane(buildPane), "build");
        actionPane.add(new JScrollPane(craftPane), "craft");

        // Action Listeners
        BgatherWood.addActionListener(this);
        Bstoke.addActionListener(this);
        BlumberCabin.addActionListener(this);
        BchopWood.addActionListener(this);
        BmineStone.addActionListener(this);

        outer.add(statPaneOuter);
        outer.add(actionPane);
        outer.add(taskPane);
        outer.add(scrollLogPane);



        // UI stuff
        this.setContentPane(outer);
//        this.setVisible(true);
    }

    /**
     * Method changes the visibility of the button with given id.
     * @param b The new visibility of the button.
     * @param id The button being changed.
     */
    public static void setButtonVisible(boolean b, int id){
        for (DWButton but : buttonList) {
            if (but.getId() == id) {
                but.setVisible(b);
            }
        }
    }

    JTextPane getLogArea() {
        return logArea;
    }

    public JScrollPane getScrollLogPane() {
        return scrollLogPane;
    }

    JPanel getStatPane() {
        return statPane;
    }

    JPanel getTaskPane() {
        return taskPane;
    }


    public int nightPhaseWindow() {
        Object[] options = {"consult the flame.",
                "make a sacrifice to the flame.",
                "leave the flame to burn."};
        return JOptionPane.showOptionDialog(this,
                "the flame crackles tonight.",
                "flame",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[2]);

    }


    public void setRequirementText() {
//        System.out.println("requirement text set");
        for (DWButton but : buttonList) { // dynamically generates tool tip text based off task requirements... once requirements become dynamic
            if (but.getId() != -1) {      // I will move this to tick
                String tpt = gh.taskRequirements(but.getId());
                but.setToolTipText(tpt);
//                System.out.println(but.getToolTipText());

            } else {
                but.setToolTipText("stoke the fire to pass the time.");
            }

        }
    }


    /**
     * Method is run when a button is interacted with. Checks if the clicked task is possible,
     * and if so, starts the process for starting the task.
     * @param e ActionEvent
     */
    public void actionPerformed(ActionEvent e) {
        // runs from the action listener.
        DWButton source = (DWButton) e.getSource();
        int id = source.getId();
        if (!gh.isInTick()) {
            if (source == Bstoke) {
                gh.stoke();
            } else {
                if (GameStateS.getInstance().taskPossible(id)) { // checks if the prerequisites for starting the action are met
                    gh.addTask(id);
                }
            }
        }



    }


}

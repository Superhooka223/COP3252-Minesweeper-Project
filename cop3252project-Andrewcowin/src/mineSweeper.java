import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class mineSweeper extends JFrame implements ActionListener{

    private JLabel status;
    private JLabel timerLabel;


    private int numMine = 4;



    public mineSweeper(boolean startGame, int numMines, boolean useStopWatch) throws IOException {
        System.out.println("value of startGame: " + startGame);


        // the "main menu"
        if (!startGame)
        {


            this.setLayout(new FlowLayout());
            this.setSize(600, 100);

            // adding the buttons

            JButton startButton = new JButton("Start game");
            add(startButton);
            startButton.addActionListener(this);
            add(startButton);
            startButton.setActionCommand("Start");

            setResizable(false);

            JButton bombsButton = new JButton("More bombs");
            add(bombsButton);
            bombsButton.addActionListener(this);
            add(bombsButton);
            bombsButton.setActionCommand("Bombs");

            JButton stopWatchButton = new JButton("Use stopwatch");
            add(stopWatchButton);
            stopWatchButton.addActionListener(this);
            add(stopWatchButton);
            stopWatchButton.setActionCommand("stopwatch");

            JButton stopWatchAndBombs = new JButton("Use stopwatch and more bombs");
            add(stopWatchAndBombs);
            stopWatchAndBombs.addActionListener(this);
            add(stopWatchAndBombs);
            stopWatchAndBombs.setActionCommand("Stopwatchbombs");


            setResizable(false);

            this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);


            setTitle("Minesweeper");

            setLocationRelativeTo(null);

            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

        //the actual game
        else
        {
            status = new JLabel("Flags: 10");
            timerLabel = new JLabel("");


            add(timerLabel, BorderLayout.NORTH);

            add(status, BorderLayout.SOUTH);

            add(new minesweeperBoard(status, timerLabel, numMines, useStopWatch));

            setResizable(false);



            pack();

            setTitle("Minesweeper");

            setLocationRelativeTo(null);

            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

    }

    /*

          These are the choices in the "menu screen"

          Each has different settings

     */
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        String action = ae.getActionCommand();

        // 5 bombs and uses timer
        if (action.equals("Start"))
        {
            boolean startGame = true;

            mineSweeper ms = null;
            try {
                ms = new mineSweeper(startGame, 5, false);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ms.setVisible(true);
        }
        // 8 bombs
        else if (action.equals("Bombs"))
        {
            boolean startGame = true;
            numMine = 8;

            mineSweeper ms = null;
            try {
                ms = new mineSweeper(startGame, numMine, false);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ms.setVisible(true);
        }
        //uses stopwatch
        else if (action.equals("stopwatch"))
        {
            boolean startGame = true;


            mineSweeper ms = null;
            try {
                ms = new mineSweeper(startGame, 5, true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ms.setVisible(true);
        }

        //uses stopwatch and 10 bombs
        else if (action.equals("Stopwatchbombs"))
        {
            boolean startGame = true;
            numMine = 10;

            mineSweeper ms = null;
            try {
                ms = new mineSweeper(startGame, numMine, true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ms.setVisible(true);
        }


    }


    public static void main(String[] args) throws IOException {

        mineSweeper ms = new mineSweeper(false, 5, false);
        ms.setVisible(true);


    }
}

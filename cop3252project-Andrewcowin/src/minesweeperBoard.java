import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Random;

/*

    Just wanted to comment here to inform that I did follow youtube videos, forum posts, tutorial sites.

    These tutorials included: minesweeper tutorials, adding sounds, adding timers/stopwatches, and using an image array

    If you wish to know what tutorials/videos I followed I can supply if wanted.

 */

public class minesweeperBoard extends JPanel implements ActionListener{


    private final int NUM_IMAGES = 23; // number of images
    private final int NUM_ROWS = 8; // number of rows
    private final int NUM_COLS = 8; // number of columns
    private final int CELL_SIZE = 50; // size of each cell

    private final int COVER_CELL = 10; // a covered cell
    private final int MARK_CELL = 22; // a cell marked with a flag
    private final int BOMB_CELL = 9; // a bomb cell
    private final int COVERED_BOMB_CELL = BOMB_CELL + COVER_CELL; // a covered bomb
    private final int MARKED_BOMB_CELL = 20; // a marked bomb
    private final int CLEARED_CELL = 0; // a cleared sqr/cell
    private int MAX_NUM_FLAGS = 10;
    private final int adjSqr1 = 11;
    private final int adjSqr2 = 12;
    private final int adjSqr3 = 13;
    private final int adjSqr4 = 14;
    private final int Sqr1 = 1; // sqr with a number 1
    private final int Sqr2 = 2; // sqr with a number 2
    private final int Sqr3 = 3; // sqr with a number 3
    private final int Sqr4 = 4; // sqr with a number 4




    private final JLabel status; // used to display "you lost" or "you won"
    private final JLabel timerLabel; // used for timer/stopwatch
    private final int BOARD_WIDTH = NUM_COLS * CELL_SIZE + 1;
    private final int BOARD_HEIGHT = NUM_ROWS * CELL_SIZE + 1;
    private int[] board; // This is used to change cells when clicked
    private boolean isGameOver = false;
    private int allCells;

    Timer timer;

    int second, minute;
    String ddSecond, ddMinute;
    DecimalFormat dFormat = new DecimalFormat("00");

    private Image[] img; // the image array

    URL ThemeURL = getClass().getResource("images/gameoverTheme.wav");

    SoundTheme music = new SoundTheme();
    //String gameoverTheme = "src/images/gameoverTheme.wav"; // This will play when a game over occurs





    public minesweeperBoard(JLabel status, JLabel timerCountDown, int mines, boolean useStopWatch) throws IOException {


        
        this.status = status; // will be used to display "game over"
        this.timerLabel = timerCountDown;

        initBoard(mines, useStopWatch);

    }

    private void initBoard(int mines, boolean useStopWatch) throws IOException {
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));

        // This is assigning the images to the image array

        BufferedImage path = ImageIO.read(getClass().getResourceAsStream("images/Minesqr.png"));
        BufferedImage path2 = ImageIO.read(getClass().getResourceAsStream("images/Mineflag.png"));
        BufferedImage path3 = ImageIO.read(getClass().getResourceAsStream("images/bomb.png"));
        BufferedImage path4 = ImageIO.read(getClass().getResourceAsStream("images/empty.png"));
        BufferedImage path5 = ImageIO.read(getClass().getResourceAsStream("images/sqr1.png"));
        BufferedImage path6 = ImageIO.read(getClass().getResourceAsStream("images/sqr2.png"));
        BufferedImage path7 = ImageIO.read(getClass().getResourceAsStream("images/sqr3.png"));
        BufferedImage path8 = ImageIO.read(getClass().getResourceAsStream("images/sqr4.png"));




        img = new Image[NUM_IMAGES];


        //var path4 = "src/images/empty.png";

        //var path = "src/images/Minesqr.png";
        img[10] = (new ImageIcon(path)).getImage();

        //var path2 = "src/images/Mineflag.png";
        img[22] = (new ImageIcon(path2)).getImage();

        img[19] = (new ImageIcon(path)).getImage();

       //var path3 = "src/images/bomb.png";
        img[9] = (new ImageIcon(path3)).getImage();

        img[20] = (new ImageIcon(path2)).getImage();

        //var path4 = "src/images/emptySqr.png";
        img[0] = (new ImageIcon(path4)).getImage();

        img[11] = (new ImageIcon(path)).getImage();
        img[12] = (new ImageIcon(path)).getImage();
        img[13] = (new ImageIcon(path)).getImage();
        img[14] = (new ImageIcon(path)).getImage();

       // var path5 = "src/images/sqr1.png";
       // var path6 = "src/images/sqr2.png";
        //var path7 = "src/images/sqr3.png";
        //var path8 = "src/images/sqr4.png";


        img[Sqr1] = (new ImageIcon(path5)).getImage();
        img[Sqr2] = (new ImageIcon(path6)).getImage();
        img[Sqr3] = (new ImageIcon(path7)).getImage();
        img[Sqr4] = (new ImageIcon(path8)).getImage();

        addMouseListener(new BoardAdapter());

        newGame(mines, useStopWatch);

    }

    // start of a new game
    private void newGame(int mines, boolean useStopWatch)
    {
        //stopwatch chosen
        if (useStopWatch)
        {
            timerLabel.setText("0:00");

            second = 0;
            minute = 0;
            countDownTimer(useStopWatch);

            timer.start();
        }
        //timer chosen
        else {
            timerLabel.setText("3:00");

            second = 0;
            minute = 3;
            countDownTimer(useStopWatch);

            timer.start();
        }

        int cell;

        Random random = new Random();

        allCells = NUM_ROWS * NUM_COLS;

        board = new int[allCells];

        // fills board with "COVER_CELL"
        for (int i = 0; i < allCells; i++)
        {
            board[i] = COVER_CELL;

        }

        int i = 0;

        /*

         I was struggling to figure out how to keep track of the number of bombs adjacent to the squares.

         I followed some tutorial minesweeper videos and learned this while loop.

          If you wish to know what tutorials/videos I followed I can supply if wanted.

          This while loop randomly assigns a bomb and increases the value in the "board" array of nearby squares.

          This allows the position in the "board" array to be subtracted by the COVER_CELL later on
          so the proper image is displated


       */

        while (i < mines) {

            int position = (int) (allCells * random.nextDouble());

            if ((position < allCells)
                    && (board[position] != 19)) {

                int current_col = position % NUM_COLS;
                board[position] = COVERED_BOMB_CELL;
                i++;

                if (current_col > 0) {
                    cell = position - 1 - NUM_COLS;
                    if (cell >= 0) {
                        if (board[cell] != COVERED_BOMB_CELL) {
                            board[cell] += 1;
                        }
                    }
                    cell = position - 1;
                    if (cell >= 0) {
                        if (board[cell] != COVERED_BOMB_CELL) {
                            board[cell] += 1;
                        }
                    }

                    cell = position + NUM_COLS - 1;
                    if (cell < allCells) {
                        if (board[cell] != COVERED_BOMB_CELL) {
                            board[cell] += 1;
                        }
                    }
                }

                cell = position - NUM_COLS;
                if (cell >= 0) {
                    if (board[cell] != COVERED_BOMB_CELL) {
                        board[cell] += 1;
                    }
                }

                cell = position + NUM_COLS;
                if (cell < allCells) {
                    if (board[cell] != COVERED_BOMB_CELL) {
                        board[cell] += 1;
                    }
                }

                if (current_col < (NUM_COLS - 1)) {
                    cell = position - NUM_COLS + 1;
                    if (cell >= 0) {
                        if (board[cell] != COVERED_BOMB_CELL) {
                            board[cell] += 1;
                        }
                    }
                    cell = position + NUM_COLS + 1;
                    if (cell < allCells) {
                        if (board[cell] != COVERED_BOMB_CELL) {
                            board[cell] += 1;
                        }
                    }
                    cell = position + 1;
                    if (cell < allCells) {
                        if (board[cell] != COVERED_BOMB_CELL) {
                            board[cell] += 1;
                        }
                    }
                }
            }
        }

        //END OF LOOP
        System.out.println("\n");


    }

    @Override
    public void actionPerformed(ActionEvent e) {


    }

    // Used to play the "gameoverTheme"
    // I figured how to add sounds by following a tutorial on adding sound effects to a java program

    //I can give the tutorial I followed if needed
    public static class SoundTheme {
        Clip clip;

        public void setURL(URL soundFileName) {
            try {
                //File file = new File(soundFile);
                AudioInputStream sound = AudioSystem.getAudioInputStream(soundFileName);
                clip = AudioSystem.getClip();
                clip.open(sound);
            } catch (Exception e) {

            }
        }

        public void play() {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    //creates the board
    @Override
    public void paintComponent(Graphics g)
    {
        for (int i = 0; i < NUM_ROWS; i++)
        {
            for (int j = 0; j < NUM_COLS; j++)
            {
                int cell = board[(i * NUM_COLS) + j];

                if (cell == 11 || cell == 12 || cell == 13 || cell == 14)
                {
                    g.drawImage(img[10], (j * CELL_SIZE), (i * CELL_SIZE), this );
                }

                g.drawImage(img[cell], (j * CELL_SIZE), (i * CELL_SIZE), this );

            }
        }
    }

    // this class is used to click the cells
    private class BoardAdapter extends MouseAdapter
    {
        @Override
        public void mousePressed(MouseEvent e)
        {
            int x = e.getX();
            int y = e.getY();

            int curCol = x / CELL_SIZE;
            int curRow = y / CELL_SIZE;

            boolean isWinner = true;


            if ((x < NUM_COLS * CELL_SIZE) && (y < NUM_ROWS * CELL_SIZE) && !isGameOver)
            {

                // right-clicking (mark cell/sqr with a flag)
                if (e.getButton() == MouseEvent.BUTTON3)
                {


                    if (board[(curRow * NUM_COLS) + curCol] == MARK_CELL)
                    {
                        board[(curRow * NUM_COLS) + curCol] = COVER_CELL;
                        MAX_NUM_FLAGS++;
                        status.setText("Flags: " + MAX_NUM_FLAGS);
                    }
                    else if (board[(curRow * NUM_COLS) + curCol] == MARKED_BOMB_CELL)
                    {
                        board[(curRow * NUM_COLS) + curCol] = COVERED_BOMB_CELL;
                        MAX_NUM_FLAGS++;
                        status.setText("Flags: " + MAX_NUM_FLAGS);
                    }
                    else if (board[(curRow * NUM_COLS) + curCol] == COVERED_BOMB_CELL)
                    {
                        if (MAX_NUM_FLAGS > 0) {
                            board[(curRow * NUM_COLS) + curCol] = MARKED_BOMB_CELL;
                            MAX_NUM_FLAGS--;
                            status.setText("Flags: " + MAX_NUM_FLAGS);
                        }
                    }
                    else if (board[(curRow * NUM_COLS) + curCol] == COVER_CELL || (board[(curRow * NUM_COLS) + curCol] >= 11 && board[(curRow * NUM_COLS) + curCol] <= 14) )
                    {
                        if (MAX_NUM_FLAGS > 0) {
                            board[(curRow * NUM_COLS) + curCol] = MARK_CELL;
                            MAX_NUM_FLAGS--;
                            status.setText("Flags: " + MAX_NUM_FLAGS);
                        }
                    }

                }

                // left-clicking square (clearing sqr/cell)
                else if (e.getButton() == MouseEvent.BUTTON1)
                {
                    // if cell is a bomb then reveal all bomb locations
                    if (board[(curRow * NUM_COLS) + curCol] == COVERED_BOMB_CELL
                            || board[(curRow * NUM_COLS) + curCol] == MARKED_BOMB_CELL && !isGameOver)
                    {
                        for (int i = 0; i < allCells; i++)
                        {
                            if (board[i] == 19 || board[i] == 20)
                            {
                                board[i] = BOMB_CELL;

                                isGameOver = true;


                            }
                        }
                        gameover(false);
                    }
                    if (board[(curRow * NUM_COLS) + curCol] == COVER_CELL || board[(curRow * NUM_COLS) + curCol] >= 11)
                    {
                        board[(curRow * NUM_COLS) + curCol] -= COVER_CELL;

                    }
                    if (board[(curRow * NUM_COLS) + curCol] == CLEARED_CELL )
                    {
                        locateEmptyCells((curRow * NUM_COLS) + curCol);
                    }

                    for (int i = 0; i < allCells; i++)
                    {
                        if (board[i] == COVER_CELL || (board[i] >= 11 && board[i] <= 14))
                        {
                            isWinner = false;
                        }

                    }

                    if (isWinner)
                    {
                        gameover(isWinner);
                    }

                }
            }
            repaint();
        }
    }

    // used to play wav file when user clicks bomb
    public void gameover(boolean isWinner)
    {

        if (isWinner == true)
        {
            music.setURL(ThemeURL);
            music.play();
            isGameOver = true;

            for (int i = 0; i < allCells; i++)
            {
                if (board[i] == 19 || board[i] == 20)
                {
                    board[i] = BOMB_CELL;

                }
                status.setText("You Won!");
            }
            repaint();
        }
        else if (isWinner == false){
            music.setURL(ThemeURL);
            music.play();
            isGameOver = true;
            status.setText("You Lost!");

            repaint();
        }
        timer.stop();

    }

    public void countDownTimer(boolean useStopWatch)
    {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                if (useStopWatch)
                {
                    ddSecond = dFormat.format(second);
                    ddMinute = dFormat.format(minute);
                    timerLabel.setText(ddMinute + ":" + ddSecond);


                    if (second == 60)
                    {
                        second = 0;
                        minute++;

                        ddSecond = dFormat.format(second);
                        ddMinute = dFormat.format(minute);

                        timerLabel.setText(ddMinute + ":" + ddSecond);
                    }

                    second++;
                }

                else {
                    ddSecond = dFormat.format(second);
                    ddMinute = dFormat.format(minute);
                    timerLabel.setText(ddMinute + ":" + ddSecond);


                    if (second == -1)
                    {
                        second = 59;
                        minute--;

                        ddSecond = dFormat.format(second);
                        ddMinute = dFormat.format(minute);

                        timerLabel.setText(ddMinute + ":" + ddSecond);
                    }


                    else if (minute == 0 && second == 0)
                    {
                        gameover(false);

                        timer.stop();
                    }

                    second--;
                }
            }
        });
    }

    /* This function clears the cells

       I was struggling to figure how to clear the cells and have some cells have numbers.

       I followed some tutorial minesweeper videos and learned this function.



     */
    private void locateEmptyCells (int j)
    {
        int current_col = j % NUM_COLS;
        int sqr;

        if (current_col > 0) {
            sqr = j - NUM_COLS - 1;
            if (sqr >= 0) {
                if (board[sqr] > BOMB_CELL) {
                    board[sqr] -= COVER_CELL;
                    if (board[sqr] == CLEARED_CELL) {
                        locateEmptyCells(sqr);
                    }
                }
            }

            sqr = j - 1;
            if (sqr >= 0) {
                if (board[sqr] > BOMB_CELL) {
                    board[sqr] -= COVER_CELL;
                    if (board[sqr] == CLEARED_CELL) {
                        locateEmptyCells(sqr);
                    }
                }
            }

            sqr = j + NUM_COLS - 1;
            if (sqr < allCells) {
                if (board[sqr] > BOMB_CELL) {
                    board[sqr] -= COVER_CELL;
                    if (board[sqr] == CLEARED_CELL) {
                        locateEmptyCells(sqr);
                    }
                }
            }
        }

        sqr = j - NUM_COLS;
        if (sqr >= 0) {
            if (board[sqr] > BOMB_CELL) {
                board[sqr] -= COVER_CELL;
                if (board[sqr] == CLEARED_CELL) {
                    locateEmptyCells(sqr);
                }
            }
        }

        sqr = j + NUM_COLS;
        if (sqr < allCells) {
            if (board[sqr] > BOMB_CELL) {
                board[sqr] -= COVER_CELL;
                if (board[sqr] == CLEARED_CELL) {
                    locateEmptyCells(sqr);
                }
            }
        }

        if (current_col < (NUM_COLS - 1)) {
            sqr = j - NUM_COLS + 1;
            if (sqr >= 0) {
                if (board[sqr] > BOMB_CELL) {
                    board[sqr] -= COVER_CELL;
                    if (board[sqr] == CLEARED_CELL) {
                        locateEmptyCells(sqr);
                    }
                }
            }

            sqr = j + NUM_COLS + 1;
            if (sqr < allCells) {
                if (board[sqr] > BOMB_CELL) {
                    board[sqr] -= COVER_CELL;
                    if (board[sqr] == CLEARED_CELL) {
                        locateEmptyCells(sqr);
                    }
                }
            }

            sqr = j + 1;
            if (sqr < allCells) {
                if (board[sqr] > BOMB_CELL) {
                    board[sqr] -= COVER_CELL;
                    if (board[sqr] == CLEARED_CELL) {
                        locateEmptyCells(sqr);
                    }
                }
            }
        }

        repaint();

    }




}
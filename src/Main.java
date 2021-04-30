import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        new Window("GameOfLife");
    }

}

class Window extends JFrame {
    private static final int WIDTH = 615;
    private static final int HEIGHT = 614;
    private int x = 0;
    private int y = 0;
    public static final int TAILLECARRE = 20;
    private boolean[][] arr = new boolean[WIDTH/TAILLECARRE][HEIGHT/TAILLECARRE];
    private boolean[][] arrNext = new boolean[WIDTH/TAILLECARRE][HEIGHT/TAILLECARRE];
    Pan p = new Pan();
    JPanel choicePanel = new JPanel();
    JButton startButton = new JButton("START");
    JButton resetButton = new JButton("RESET");
    boolean isRunning = false;

    public Window(String title){
        p.setBackground(Color.GRAY);

        startButton.setFocusable(false);
        startButton.setBackground(Color.ORANGE);
        startButton.addActionListener(this::run);


        resetButton.setFocusable(false);
        resetButton.setBackground(Color.ORANGE);
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < arr.length; i++) {
                    for (int j = 0; j < arr[0].length; j++) {
                        arr[i][j] = false;
                        arrNext[i][j] = false;
                        p.repaint();
                    }
                }
            }
        });

        choicePanel.add(startButton);
        choicePanel.add(resetButton);
        choicePanel.setBackground(Color.gray);

        this.add(p);
        this.add(choicePanel, BorderLayout.SOUTH);
        this.setSize(WIDTH,HEIGHT);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }


    private void run(ActionEvent actionEvent)  {
        MyThread myThread = new MyThread();
        if (startButton.getText().equals("START")){
            myThread.start();
            isRunning = true;
            startButton.setText("STOP");
        }
        else{
            isRunning = false;
            startButton.setText("START");
        }

    }

    private void launch() {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                    arrNext[i][j] = false;
                }
            }
            for (int i = 0; i < arr.length; i++) {
                for (int j = 0; j < arr[0].length; j++) {
                    if (arr[i][j] == true && numberOfNeighbours(i,j) < 2) {
                        arrNext[i][j] = false;
                    }
                    else  if (arr[i][j] == true && numberOfNeighbours(i,j) > 3)
                        arrNext[i][j] = false;
                    else  if (arr[i][j] == true && (numberOfNeighbours(i,j) == 3 || numberOfNeighbours(i,j) == 2))
                        arrNext[i][j] = true;
                    else  if (arr[i][j] == false && numberOfNeighbours(i,j) == 3)
                        arrNext[i][j] = true;
                }
            }

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                arr[i][j] = arrNext[i][j];
             }
            }

    }
    private boolean[] neighbours(int x , int y) {
        try{
            return new boolean[]{arr[x][y - 1],arr[x][y + 1],arr[x - 1][y], arr[x + 1][y], arr[x - 1][y - 1],
                    arr[x - 1][y + 1], arr[x + 1][y + 1], arr[x + 1][y - 1]};
        }catch (ArrayIndexOutOfBoundsException e)
        {
            if (x == 0 && y == 0){
                return new boolean[]{arr[x][y + 1], arr[x + 1][y], arr[x + 1][y + 1]};
            }
            else if (x == 29 && y == 29){
                return new boolean[]{arr[x][y - 1],arr[x - 1][y],arr[x - 1][y - 1]};
            }
            else if(x == 0 && y == 29)
                return new boolean[]{arr[x][y - 1],arr[x + 1][y],arr[x + 1][y - 1]};
            else if (x == 29 && y == 0)
                return new boolean[]{arr[x][y + 1],arr[x - 1][y], arr[x - 1][y + 1]};
            else if (y == 29){
                return new boolean[]{arr[x][y - 1],arr[x - 1][y], arr[x + 1][y], arr[x - 1][y - 1], arr[x + 1][y - 1]};
            }
            else if (x == 0){
                return new boolean[]{arr[x][y - 1],arr[x][y + 1], arr[x + 1][y], arr[x + 1][y + 1], arr[x + 1][y - 1]};

            }
            else if (x == 29){
                return new boolean[]{arr[x][y - 1],arr[x][y + 1],arr[x - 1][y], arr[x - 1][y - 1], arr[x - 1][y + 1],};

            }
            else if (y == 0){
                return new boolean[]{arr[x][y + 1],arr[x - 1][y], arr[x + 1][y], arr[x - 1][y + 1], arr[x + 1][y + 1]};

            }


        }
        return new boolean[]{false};
    }
    private int numberOfNeighbours(int x, int y){
        int count = 0;
        for ( boolean neighbour : neighbours(x,y)){
            if (neighbour) count++;
        }
        return count;
    }
     class MyThread extends Thread{
        public void run(){
            while(isRunning) {
                launch();
                p.repaint();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    class Pan extends JPanel{

        public Pan(){
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);
                    if (!isRunning) {
                        x = (int) (Math.floor((e.getX()) / (float) TAILLECARRE)) * TAILLECARRE;
                        y = (int) (Math.floor((e.getY()) / (float) TAILLECARRE)) * TAILLECARRE;
                        if (arr[y / TAILLECARRE][x / TAILLECARRE] == true)
                            arr[y / TAILLECARRE][x / TAILLECARRE] = false;
                        else
                            arr[y / TAILLECARRE][x / TAILLECARRE] = true;
                        p.repaint();
                    }
                }
            });
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (int i = 0; i < arr.length + 1 ; i++) {
                g.setColor(Color.black);
                g.drawLine(i* TAILLECARRE, 0, i* TAILLECARRE,  arr[0].length* TAILLECARRE);
                if (i <= arr[0].length)
                    g.drawLine(0, i* TAILLECARRE, arr.length * TAILLECARRE,  i* TAILLECARRE);
            }

            for (int i = 0; i < arr.length; i++) {
                for (int j = 0; j < arr[0].length; j++) {
                    if (arr[i][j] == true) {
                        g.setColor(Color.yellow);
                        g.fillRect(j * TAILLECARRE, i* TAILLECARRE, TAILLECARRE, TAILLECARRE);
                        g.setColor(Color.BLACK);
                        g.drawRect(j * TAILLECARRE, i* TAILLECARRE, TAILLECARRE, TAILLECARRE);
                    }
                }
            }


        }
    }
}


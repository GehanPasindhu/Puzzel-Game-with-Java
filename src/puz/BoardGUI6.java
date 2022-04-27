/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package puz;

import com.mysql.jdbc.ResultSet;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

/**
 *
 * @author pasin
 */
public class BoardGUI6 implements ActionListener {

    JFrame fr;
    JPanel mainPanel, bigPannel, miniPannel;
    JButton[][] button;
    int rows;
    int cols;
    JLabel[][] label;
    int[][] board;
    JLabel header, counterL;
    JButton jb;

    Timer timer;
    int time = 600;
    int countscore = 0;

    public BoardGUI6() {
        rows = 6;
        cols = 6;
        board = new int[rows][cols];
        initGUI();
    }

    String uname;

    public void userName(String un) {
        uname = un;
        System.out.println(uname);
    }

    public void initGUI() {
        fr = new JFrame("My Puzzle Game | Level 4");
        header = new JLabel("Timer :", SwingConstants.CENTER);
        header.setFont(new Font("TimesRoman", Font.BOLD, 25));
        header = new JLabel("Timer : " + time, SwingConstants.CENTER);

        counterL = new JLabel("Score : " + countscore);

        bigPannel = new JPanel();
        bigPannel.setLayout(new GridLayout(2, 1));

        miniPannel = new JPanel();
        miniPannel.setLayout(new GridLayout(1, 1));

        mainPanel = new JPanel();
        mainPanel.setBackground(Color.white);
        mainPanel.setLayout(new GridLayout(6, 6));
        button = new JButton[rows][cols];
        label = new JLabel[rows][cols];

        this.shuffleBoard();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                button[i][j] = new JButton();
                String text = i + "," + j;
                button[i][j].setText(text);
                button[i][j].setFont(new Font("TimesRoman", Font.PLAIN, 0));
                button[i][j].addActionListener(this);
                int val = board[i][j];
                String fileName;
                if (val != -1) {
                    fileName = "Pics/" + val + ".png";
                    label[i][j] = new JLabel(new ImageIcon(fileName), JLabel.CENTER);
                } else {
                    label[i][j] = new JLabel("");
                }
                button[i][j].add(label[i][j]);
                button[i][j].setBorder(BorderFactory.createLineBorder(Color.black, 2));
                button[i][j].setBackground(Color.LIGHT_GRAY);
                mainPanel.add(button[i][j]);
            }
        }

        miniPannel.add(header);
        miniPannel.add(counterL);
        bigPannel.add(miniPannel);
        bigPannel.add(mainPanel);

        fr.add(bigPannel);
        fr.setVisible(true);
        fr.setSize(550, 550);
        fr.setLocationRelativeTo(null);
    }

    public void shuffleBoard() {
        Random rand = new Random();
        int[] array = new int[36];
        for (int i = 0; i < 36; i++) {
            array[i] = i + 1;
        }
        array[35] = -1;
        for (int i = 0; i < 36; i++) {
            int index = rand.nextInt(36);
            int temp = array[i];
            array[i] = array[index];
            array[index] = temp;
        }
        int count = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = array[count];
                count = count + 1;
            }
        }

    }

    Boolean isWin() {
        int count = 1;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] != count && board[i][j] != -1) {
                    return false;
                }
                count = count + 1;
            }
        }
        return true;
    }

    public void displayWinMsg() {
        try {
            ResultSet resultSet = (ResultSet) dbConnection.search("select * from user where username='" + uname + "'");
            if (resultSet.next()) {
                int id = resultSet.getInt("iduser");
                ResultSet rs = (ResultSet) dbConnection.search("select * from stats where user='" + id + "' and level='4'");
                if (rs.next()) {
                    dbConnection.iud("UPDATE `stats` SET `score` = '" + countscore + "' WHERE `level` = '4' and `user`='" + id + "'");

                    JFrame frame = new JFrame("Game Win");
                    JLabel label = new JLabel("You Solve The Puzzle and Your Level Score Updates", JLabel.CENTER);
                    label.setFont(new Font("TimesRoman", Font.BOLD, 20));
                    frame.add(label);
                    frame.setLayout(new GridLayout(1, 1));
                    frame.setSize(300, 300);
                    frame.setBackground(Color.white);
                    frame.setVisible(true);
                    frame.setLocationRelativeTo(null);

                } else {
                    dbConnection.iud("insert into stats (user,level,score) values('" + id + "','4','" + countscore + "')");
                    JFrame frame = new JFrame("Game Win");
                    JLabel label = new JLabel("You Solve The Puzzle", JLabel.CENTER);
                    label.setFont(new Font("TimesRoman", Font.BOLD, 20));
                    frame.add(label);
                    frame.setLayout(new GridLayout(1, 1));
                    frame.setSize(300, 300);
                    frame.setBackground(Color.white);
                    frame.setVisible(true);
                    frame.setLocationRelativeTo(null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayLMsg() {
        timer.stop();
        try {
            ResultSet resultSet = (ResultSet) dbConnection.search("select * from user where username='" + uname + "'");
            if (resultSet.next()) {
                int id = resultSet.getInt("iduser");
                ResultSet rs = (ResultSet) dbConnection.search("select * from stats where user='" + id + "' and level='4'");
                if (rs.next()) {
                    dbConnection.iud("UPDATE `stats` SET `score` = '" + countscore + "' WHERE `level` = '4' and `user`='" + id + "'");
                    JFrame frame = new JFrame("Time Out");
                    JLabel label = new JLabel("You Didn't Solve The Puzzle", JLabel.CENTER);
                    label.setFont(new Font("TimesRoman", Font.BOLD, 20));
                    frame.add(label);
                    frame.setLayout(new GridLayout(1, 1));
                    frame.setSize(300, 300);
                    frame.setBackground(Color.white);
                    frame.setVisible(true);
                    frame.setLocationRelativeTo(null);
                } else {
                    dbConnection.iud("insert into stats (user,level,score) values('" + id + "','4','" + countscore + "')");

                    JFrame frame = new JFrame("Time Out");
                    JLabel label = new JLabel("You Didn't Solve The Puzzle", JLabel.CENTER);
                    label.setFont(new Font("TimesRoman", Font.BOLD, 20));
                    frame.add(label);
                    frame.setLayout(new GridLayout(1, 1));
                    frame.setSize(300, 300);
                    frame.setBackground(Color.white);
                    frame.setVisible(true);
                    frame.setLocationRelativeTo(null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        Boolean flag = isWin();
        if (flag == false) {
            String s = ae.getActionCommand().toString();
            int r = Integer.parseInt(s.split(",")[0]);
            int c = Integer.parseInt(s.split(",")[1]);

            if (board[r][c] != -1) {
                if (r + 1 < rows && board[r + 1][c] == -1) {
                    label[r][c].setIcon(new ImageIcon(""));
                    String fileName = "Pics/" + board[r][c] + ".png";
                    label[r + 1][c].setIcon(new ImageIcon(fileName));
                    int temp = board[r][c];
                    board[r][c] = board[r + 1][c];
                    board[r + 1][c] = temp;
                    countscore = countscore + 1;
                    counterL.setText("Score " + countscore);

                } else if (r - 1 >= 0 && board[r - 1][c] == -1) {
                    label[r][c].setIcon(new ImageIcon(""));
                    String fileName = "Pics/" + board[r][c] + ".png";
                    label[r - 1][c].setIcon(new ImageIcon(fileName));
                    int temp = board[r][c];
                    board[r][c] = board[r - 1][c];
                    board[r - 1][c] = temp;
                    countscore = countscore + 1;
                    counterL.setText("Score " + countscore);

                } else if (c + 1 < cols && board[r][c + 1] == -1) {
                    label[r][c].setIcon(new ImageIcon(""));
                    String fileName = "Pics/" + board[r][c] + ".png";
                    label[r][c + 1].setIcon(new ImageIcon(fileName));
                    int temp = board[r][c];
                    board[r][c] = board[r][c + 1];
                    board[r][c + 1] = temp;
                    countscore = countscore + 1;
                    counterL.setText("Score " + countscore);

                } else if (c - 1 >= 0 && board[r][c - 1] == -1) {
                    label[r][c].setIcon(new ImageIcon(""));
                    String fileName = "Pics/" + board[r][c] + ".png";
                    label[r][c - 1].setIcon(new ImageIcon(fileName));
                    int temp = board[r][c];
                    board[r][c] = board[r][c - 1];
                    board[r][c - 1] = temp;
                    countscore = countscore + 1;
                    counterL.setText("Score " + countscore);
                }
            }

            flag = isWin();
            if (flag == true) {
                displayWinMsg();
            }
            timer = new Timer(10000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (time >= 0) {
                        header.setText("Timer : " + String.valueOf(time));
                        time--;
                    } else {
                        mainPanel.setEnabled(false);
                        displayLMsg();

                    }
                       timer.stop();
                }
             
            });
            timer.start();
        }

    }
}

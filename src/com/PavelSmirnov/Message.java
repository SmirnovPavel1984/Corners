
//Все сообщения, которые могут выскочить в ходе игры

package com.PavelSmirnov;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Message extends JFrame {

    static File imperial = new File("C:\\Corners\\WAVs\\Darkside.wav");
    static File win = new File ("C:\\Corners\\WAVs\\win.wav");

    public static void startGame () {
        JFrame startGame = new JFrame("Welcome!");
        startGame.setPreferredSize(new Dimension(350, 150));
        startGame.pack();
        startGame.setResizable( false );
        startGame.setLocationRelativeTo( null );
        startGame.setVisible(true);
        startGame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        //При нажатии кнопки "белыми" просто скрываем окно
        JButton whiteSide = new JButton("Белыми");
        whiteSide.addActionListener(e -> {
            Checker.side=1;
            startGame.setVisible(false);
            ChessBoard.countOfGame=0;
        });

        //При выборе тёмной стороны меняем визуал у шашек,
        //запускаем новую игру
        //включаем звук перехода на тёмную сторону
        //и делаем ход компьютером
        JButton blackSide = new JButton("Чёрными");
        blackSide.addActionListener(e -> {
            Checker.side=-1;
            startGame.setVisible(false);
            ChessBoard.setCheckers();
            ChessBoard.countOfGame=1;
            ChessBoard.newGame();
            //Включаем имперский марш))
            try {
                AudioInputStream ais = AudioSystem.getAudioInputStream(imperial);
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                clip.setFramePosition(0);
                clip.start();
            } catch (IOException | UnsupportedAudioFileException | LineUnavailableException exc) {
                exc.printStackTrace();
            }

            ChessBoard.moveComputer();

        });

        Dimension buttonSize = new Dimension( 150, 30);
        whiteSide.setPreferredSize(buttonSize);
        blackSide.setPreferredSize(buttonSize);

        JLabel text1 = new JLabel("Добро пожаловать в игру!");
        JLabel text2 = new JLabel("Какими шашками будете играть?");
        JLabel text3 = new JLabel("      (белые ходят первыми)      ");

        JPanel contents = new JPanel();
        contents.add(text1);
        contents.add(text2);
        contents.add(text3);
        contents.add(whiteSide);
        contents.add(blackSide);
        startGame.setContentPane(contents);

    }

    public static void youWin () {
        JFrame winGame = new JFrame("You win!");
        winGame.setPreferredSize(new Dimension(250, 100));
        winGame.pack();
        winGame.setResizable( false );
        winGame.setLocationRelativeTo( null );
        winGame.setVisible(true);
        winGame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        //Звук аплодисментов при победе игрока
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(win);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.setFramePosition(0);
            clip.start();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException exc) {
            exc.printStackTrace();
        }

        JButton newGameButton = new JButton("Ещё раз!");
        newGameButton.addActionListener(e -> {
            Checker.side=1;
            ChessBoard.newGame();
            Message.startGame();
            winGame.setVisible(false);
        });

        JButton exitButton = new JButton("Выход");
        exitButton.addActionListener(e -> System.exit(0));

        Dimension buttonSize = new Dimension( 100, 30);
        newGameButton.setPreferredSize(buttonSize);
        exitButton.setPreferredSize(buttonSize);

        JLabel text = new JLabel("Поздравляем! Вы выиграли!\n");

        JPanel contents = new JPanel();
        contents.add(text);
        contents.add(newGameButton);
        contents.add(exitButton);
        winGame.setContentPane(contents);

    }

    public static void moveCancel () {
        JOptionPane.showMessageDialog(null, "Вы можете отменить выбор шашки,\nкликнув по ней ещё раз");
    }

    public static void youLose () {
        JFrame winGame = new JFrame("You lose...");
        winGame.setPreferredSize(new Dimension(250, 100));
        winGame.pack();
        winGame.setResizable( false );
        winGame.setLocationRelativeTo( null );
        winGame.setVisible(true);
        winGame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton newGameButton = new JButton("Ещё раз!");
        newGameButton.addActionListener(e -> {
            Checker.side=1;
            ChessBoard.newGame();
            Message.startGame();
            winGame.setVisible(false);
        });

        JButton exitButton = new JButton("Выход");
        exitButton.addActionListener(e -> System.exit(0));

        Dimension buttonSize = new Dimension( 100, 30);
        newGameButton.setPreferredSize(buttonSize);
        exitButton.setPreferredSize(buttonSize);

        JLabel text = new JLabel("Увы, Вы проиграли... ((\n");

        JPanel contents = new JPanel();
        contents.add(text);
        contents.add(newGameButton);
        contents.add(exitButton);
        winGame.setContentPane(contents);
    }

    public static void noMoves () {
        JOptionPane.showMessageDialog(null, "У этой шашки нет возможных ходов");
    }

    public static void about () {
        JOptionPane.showMessageDialog(null, "version 1.1\nCreated by Smirnov Pavel\nNNTU\n2021");
    }

    public static void fiveMovesTo50() {
        JOptionPane.showMessageDialog(null, "Игра длится 50 ходов.\nУ Вас осталось 5 ходов");
    }

    public static void fiveMovesTo27() {
        JOptionPane.showMessageDialog(null, "Нужно за 27 ходов вывести свои шашки из своего дома!\nУ Вас осталось 5 ходов");
    }

    public static void rules () {
        JFrame rules = new JFrame();

        JPanel panel = new JPanel() {
                @Override
                public void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(Toolkit.getDefaultToolkit().getImage("C:\\Corners\\Pics\\rules.png"), 0, 0, this);
                }
            };

        rules.setPreferredSize(new Dimension(840, 810));
        rules.pack();
        rules.setTitle("Правила");
        rules.setResizable(false);
        rules.setLocationRelativeTo( null );
        rules.add(panel);
        rules.setVisible(true);
    }
}

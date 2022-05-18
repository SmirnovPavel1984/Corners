package com.PavelSmirnov;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class ChessBoard extends JFrame implements MouseListener
{
    static JLayeredPane layeredPanel;
    public static JPanel chessBoard;
    static JLabel checkerPiece;
    private static int switchClick = 0;
    public static Checker currentChecker;
    static Point tempPoint;
    public static Map<Integer, Integer> currentGamePosition = new HashMap<>();
    static {
        for (int i = 0; i<64; i++){
            currentGamePosition.put(i, 0);
        }
    }
    public static int countOfGame=0;
    public static ChessBoard currentBoard;
    static File wrongMove = new File("C:\\Corners\\WAVs\\wrong.wav");
    static File checkMove = new File("C:\\Corners\\WAVs\\check.wav");
    public static int countOfMoves=0;



    public ChessBoard()
    {
        Dimension boardSize = new Dimension(640, 665);

        layeredPanel = new JLayeredPane();
        getContentPane().add(layeredPanel);
        layeredPanel.setPreferredSize( boardSize );
        layeredPanel.addMouseListener( this );
        //layeredPanel.addMouseMotionListener( this );

        chessBoard = new JPanel();
        layeredPanel.add(chessBoard, JLayeredPane.DEFAULT_LAYER);
        GridLayout grid = new GridLayout(8, 8);
        chessBoard.setLayout( grid );
        chessBoard.setPreferredSize( boardSize );
        chessBoard.setBounds(0, 0, boardSize.width, boardSize.height);


        // Раскрашиваем поле
        for (int i = 0; i < 64; i++)
        {
            JPanel square = new JPanel( new BorderLayout() );
            chessBoard.add( square );

            int row = (i / 8) % 2;
            if (row == 0) {
                square.setBackground(i % 2 == 0 ? new Color(255, 255, 190) : new Color(255, 178, 178));
            } else {
                square.setBackground(i % 2 == 0 ? new Color(255, 178, 178) : new Color(255, 255, 190));
            }
        }

        //Расставляем шашки по полю
        setCheckers();

        //Добавляем меню
        Menu menuBar = new Menu();
        this.setJMenuBar(menuBar);

        //Выставляем настройки JFrame
        this.setTitle("Уголки");
        this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        this.pack();
        this.setResizable( false );
        this.setLocationRelativeTo( null );
        this.setVisible(true);
        //Дублируем игровое поле в "Текущее" - это нужно для завершения старой игры при начале новой
        currentBoard=this;
        if (countOfGame==0) {
            Message.startGame();
        }
    }


    //Расставляем шашки по полю
    public static void setCheckers () {

        new Checker(-1, 5);
        new Checker(-1, 6);
        new Checker(-1, 7);
        new Checker(-1, 13);
        new Checker(-1, 14);
        new Checker(-1, 15);
        new Checker(-1, 21);
        new Checker(-1, 22);
        new Checker(-1, 23);

        new Checker(1, 40);
        new Checker(1, 41);
        new Checker(1, 42);
        new Checker(1, 48);
        new Checker(1, 49);
        new Checker(1, 50);
        new Checker(1, 56);
        new Checker(1, 57);
        new Checker(1, 58);

        //Определяем возможные ходы шашек игрока
        Checker.possibleMoves();
    }


    static int tempPos; //Это нужно для сохранения текущего положения шашки и очистки его после хода
    static int clickCounter=0; //Счётчик кликов

    @Override
    public void mouseClicked(MouseEvent click) {

        int num = (click.getY() / 80) * 8 + (click.getX() / 80);
        Component compForMove = chessBoard.findComponentAt(click.getX(), click.getY());

        //Первый клик - выбор шашки
        if (switchClick == 0) {
            checkerPiece = null;
            if (compForMove instanceof JPanel) return;

            //Определение выбранной шашки и изменение её отображения на "активное"
            for (Checker check : Checker.arrayOfCheckers) {
                if (compForMove.hashCode() == check.getJLabel().hashCode()) {
                    currentChecker = check;
                    currentChecker.getJLabel().setIcon(new ImageIcon(currentChecker.selectedCheckerImage));
                    tempPos = currentChecker.getCurrentPosition();
                    break;
                }
            }

            //Нсли игрок кликнул не по своей шашке
            if (currentChecker.getColor() != 1) {
                currentChecker.getJLabel().setIcon(new ImageIcon(currentChecker.checkerImage));
                return;
            }

            //Если у выбранной шашки нет доступных ходов (н-р, угловая шашка в самом начале игры)
            if (currentChecker.availableMoves.isEmpty()) {
                Message.noMoves();
                currentChecker.getJLabel().setIcon(new ImageIcon(currentChecker.checkerImage));
                return;
            }

            tempPoint = compForMove.getParent().getLocation();
            checkerPiece = currentChecker.getJLabel();

            switchClick = 1;

        }
        //Второй клик - выбор клетки для хода
        else {
            //Если игрок кликнул по той же самой фишке, ход отменяется
            if (num == currentChecker.getCurrentPosition()) {
                currentChecker.getJLabel().setIcon(new ImageIcon(currentChecker.checkerImage));
                switchClick = 0;
                clickCounter = 0;
                return;
            }

            //Если игрок кликнул по полю, в которое данная шашка не может сходить
            if (!currentChecker.availableMoves.contains(num)) {
                clickCounter += 1;
                //Звук невозможного хода
                try {
                    AudioInputStream ais = AudioSystem.getAudioInputStream(wrongMove);
                    Clip clip = AudioSystem.getClip();
                    clip.open(ais);
                    clip.setFramePosition(0);
                    clip.start();
                } catch (IOException | UnsupportedAudioFileException | LineUnavailableException exc) {
                    exc.printStackTrace();
                }
                //При попытке 3 раз сходить на недоступную клетку выскакивает сообщение, что можно поменять шашку
                if (clickCounter >= 3) {
                    Message.moveCancel();
                }
                return;
            }

            //Перестановка шашки с изменеием всех параметров (картинка самой шашки, изменение игровой ситуации и пр.)
            checkerPiece.setLocation(tempPoint.x, tempPoint.y);
            layeredPanel.add(checkerPiece, JLayeredPane.DRAG_LAYER);
            currentChecker.setCurrentPosition(num);
            currentChecker.getJLabel().setIcon(new ImageIcon(currentChecker.checkerImage));
            checkerPiece.setVisible(false);
            if (compForMove instanceof JLabel) {
                Container parent = compForMove.getParent();
                parent.remove(0);
                parent.add(checkerPiece);
            } else {
                Container parent = (Container) compForMove;
                parent.add(checkerPiece);
            }
            checkerPiece.setVisible(true);
            currentGamePosition.put(tempPos, 0);
            currentGamePosition.put(num, currentChecker.getColor());

            //Звук хода
            try {
                AudioInputStream ais = AudioSystem.getAudioInputStream(checkMove);
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                clip.setFramePosition(0);
                clip.start();
            } catch (IOException | UnsupportedAudioFileException | LineUnavailableException exc) {
                exc.printStackTrace();
            }

            //Проверка на победу игрока
            if ((GameLogic.evaluateThePosition(currentGamePosition) == 8209) || ((countOfMoves==50) && (GameLogic.evaluateThePosition(currentGamePosition)<=0))){
                Message.youWin();
            }

//----------Ход компьютера
            moveComputer();

            //Проверка на победу компьютера
            if ((GameLogic.evaluateThePosition(currentGamePosition) == -8209) || ((countOfMoves==50) && (GameLogic.evaluateThePosition(currentGamePosition)>0))){
                Message.youLose();
            }

            //Определение всех возможных ходов игрока в новой текущей ситуации
            Checker.possibleMoves();

            switchClick = 0;
            clickCounter = 0;
            countOfMoves+=1;

            //Если сделано 45 ходов, но никто пока так и не выиграл,
            //выбрасывается предупреждение, что осталось всего 5 ходов
            if ((countOfMoves==45) && (GameLogic.evaluateThePosition(currentGamePosition) != -8209)
                    && (GameLogic.evaluateThePosition(currentGamePosition) != 8209)) {
                Message.fiveMovesTo50();
            }

            //Предоупреждение за 5 ходов до 27-го
            //выскакивает ,если у игрока на этот момент есть свои шашки в своём доме
            if (countOfMoves==22) {
                Integer [] arrOfPlayersHomes =  {40, 41, 42, 48, 49, 50, 56, 57, 58};
                for (Map.Entry<Integer, Integer> entry : currentGamePosition.entrySet()) {
                    if ((entry.getValue()==1) && (Arrays.asList(arrOfPlayersHomes).contains(entry.getKey()))) {
                        Message.fiveMovesTo27();
                        break;
                    }
                }
            }

            //Проверка "правила 27 ходов"
            if (countOfMoves==27) {
                GameLogic.playerStatus27 = 0;
                GameLogic.computerStatus27 = 0;
                GameLogic.check27move();
            }
        }
    }

    //Ход компьютера
    public static void moveComputer() {

        Map <Integer, Integer> theBestMove = new HashMap<>(GameLogic.findTHeBestMove(currentGamePosition));
        int startPoint = 0;
        int finishPoint = 0;
        for (int i=0; i< 64 ; i++){
            if ((theBestMove.get(i)==0) && (currentGamePosition.get(i)==-1)) {
                startPoint = i;
                continue;
            }
            if ((theBestMove.get(i)==-1) && (currentGamePosition.get(i)==0)) {
                finishPoint = i;
            }
        }
        int startX = (startPoint%8)*80 + 40;
        int startY = (startPoint/8)*80 + 40;

        int finishX = (finishPoint%8)*80 + 40;
        int finishY = (finishPoint/8)*80 + 40;

        for (Checker check : Checker.arrayOfCheckers) {
            if (startPoint==check.getCurrentPosition()) {
                currentChecker=check;
                currentChecker.getJLabel().setIcon(new ImageIcon(currentChecker.selectedCheckerImage));

                break;
            }
        }

        Component compForMove = chessBoard.findComponentAt(startX, startY);
        checkerPiece = currentChecker.getJLabel();

        checkerPiece.setLocation(compForMove.getParent().getLocation().x, compForMove.getParent().getLocation().y);
        layeredPanel.add(checkerPiece, JLayeredPane.DRAG_LAYER);
        currentChecker.setCurrentPosition(finishPoint);

        currentChecker.getJLabel().setIcon(new ImageIcon(currentChecker.checkerImage));
        checkerPiece.setVisible(false);

        compForMove = chessBoard.findComponentAt(finishX, finishY);

        if (compForMove instanceof JLabel) {
            Container parent = compForMove.getParent();
            parent.remove(0);
            parent.add(checkerPiece);
        } else {
            Container parent = (Container) compForMove;
            parent.add(checkerPiece);
        }
        checkerPiece.setVisible(true);

        currentGamePosition.put(startPoint, 0);
        currentGamePosition.put(finishPoint, currentChecker.getColor());
    }

    //Перезапуск игры
    public static void newGame () {
        //Убираем видимость у текущего игрового поля
        currentBoard.setVisible(false);
        //Чистим массив активных шашек
        Checker.arrayOfCheckers.clear();
        //Обнуляем игровую позицию
        currentGamePosition.clear();
        for (int i = 0; i<64; i++){
            currentGamePosition.put(i, 0);
        }
        //Обнуляем текущую игровую шашку
        currentChecker=null;
        //Создаём новую доску
        new ChessBoard();
        //обнуляем счётчик ходов
        countOfMoves=0;
    }


    public void mousePressed(MouseEvent e) {}
    public void mouseDragged(MouseEvent me) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}

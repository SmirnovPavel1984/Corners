package com.PavelSmirnov;

import javax.swing.*;
import java.util.*;

public class Checker {

    private final int color; //1 - player, -1 - computer
    private int currentPosition;
    public Set<Integer> availableMoves = new HashSet<>();
    private JLabel piece;
    public String checkerImage;
    public String selectedCheckerImage;
    public static Set<Checker> arrayOfCheckers = new HashSet<>();

    public static int side=1; // выбор стороны
    static String white = "C:\\Corners\\Pics\\white.gif";
    static String whiteActive = "C:\\Corners\\Pics\\white1.gif";
    static String black = "C:\\Corners\\Pics\\black.gif";
    static String blackActive = "C:\\Corners\\Pics\\black1.gif";


    public Checker (int color, int position){
        this.color = color;
        currentPosition = position;
        if (side==1) {
            if (color == 1) {
                checkerImage = white;
                selectedCheckerImage = whiteActive;

            } else {
                checkerImage = black;
                selectedCheckerImage = blackActive;
            }
        } else {
            if (color == 1) {
                checkerImage = black;
                selectedCheckerImage = blackActive;

            } else {
                checkerImage = white;
                selectedCheckerImage = whiteActive;
            }
        }
        piece = new JLabel( new ImageIcon(checkerImage) );
        JPanel panel = (JPanel)ChessBoard.chessBoard.getComponent( currentPosition );
        panel.add( this.getJLabel() );

        arrayOfCheckers.add(this);
        ChessBoard.currentGamePosition.put(position,color);
    }


    public JLabel getJLabel () {
        return this.piece;
    }

    public int getCurrentPosition (){
        return this.currentPosition;
    }

    public void setCurrentPosition (int pos) {
        currentPosition = pos;
    }

    public int getColor () {
        return this.color;
    }


    //Метод поиска доступных ходов
    //Определяет шаги вверх, вниз, вправо и влево на 1 клетку или на 1 прыжок
    //При прыжке вывывает метод для моиска следующего прыжка
    public void findPossibleMoves () {
        int pos=this.getCurrentPosition();
        availableMoves.clear();

        int depth = 6;
        if (pos%8!=7) {
            if ((ChessBoard.currentGamePosition.get(pos+1)!=0) && (pos%8!=6) && (ChessBoard.currentGamePosition.get(pos+2)==0)){
                this.availableMoves.add(pos + 2);
                findJumpMove(ChessBoard.currentGamePosition, pos+2, pos, depth);
            } else {
                if (ChessBoard.currentGamePosition.get(pos+1)==0)
                this.availableMoves.add(pos + 1);
            }
        }
        if (pos%8!=0) {
            if ((ChessBoard.currentGamePosition.get(pos-1)!=0) && (pos%8!=1) && (ChessBoard.currentGamePosition.get(pos-2)==0)){
                this.availableMoves.add(pos - 2);
                findJumpMove(ChessBoard.currentGamePosition, pos-2, pos, depth);
            } else {
                if (ChessBoard.currentGamePosition.get(pos-1)==0)
                this.availableMoves.add(pos - 1);
            }
        }
        if (pos<56) {
            if ((pos<48) && (ChessBoard.currentGamePosition.get(pos+8)!=0) && (ChessBoard.currentGamePosition.get(pos+16)==0)) {
                this.availableMoves.add(pos + 16);
                findJumpMove(ChessBoard.currentGamePosition, pos+16, pos, depth);
            } else {
                if (ChessBoard.currentGamePosition.get(pos+8)==0)
                this.availableMoves.add(pos + 8);
            }
        }
        if (pos>7) {
            if ((pos>15) && (ChessBoard.currentGamePosition.get(pos-8)!=0) && (ChessBoard.currentGamePosition.get(pos-16)==0)) {
                this.availableMoves.add(pos - 16);
                findJumpMove(ChessBoard.currentGamePosition, pos-16, pos, depth);
            } else {
                if (ChessBoard.currentGamePosition.get(pos-8)==0)
                this.availableMoves.add(pos - 8);
            }
        }
    }

    //Рекурсивный метод поиска доступного следующего прыжка после первого прыжка
    public void findJumpMove (Map gameSit, int pos, int posDenied, int depth) {
        if (depth==0) return;
        Map<Integer, Integer> currentSit = new HashMap<>(gameSit);
        currentSit.put(posDenied, 2);

        if ((pos%8!=7) && (ChessBoard.currentGamePosition.get(pos+1)!=0) && (pos%8!=6) && (ChessBoard.currentGamePosition.get(pos+2)==0)){
            this.availableMoves.add(pos + 2);
            findJumpMove(currentSit, pos+2, pos, depth-1);
        }

        if ((pos%8!=0) && (ChessBoard.currentGamePosition.get(pos-1)!=0) && (pos%8!=1) && (ChessBoard.currentGamePosition.get(pos-2)==0)){
            this.availableMoves.add(pos - 2);
            findJumpMove(currentSit, pos-2, pos, depth-1);
        }

        if ((pos < 48) && (ChessBoard.currentGamePosition.get(pos + 8) != 0) && (ChessBoard.currentGamePosition.get(pos + 16) == 0)) {
            this.availableMoves.add(pos + 16);
            findJumpMove(currentSit, pos+16, pos, depth-1);

        }

        if ((pos > 15) && (ChessBoard.currentGamePosition.get(pos - 8) != 0) && (ChessBoard.currentGamePosition.get(pos - 16) == 0)) {
            this.availableMoves.add(pos - 16);
            findJumpMove(currentSit, pos-16, pos, depth-1);
        }
    }

    //Цикл для перебора всех шашек и поиска ходов
    //В процессе игры для определения ходов вызывается именно этот метод
    public static void possibleMoves () {
        for (Checker check : Checker.arrayOfCheckers) {
            if (check.getColor() == 1) {
                check.findPossibleMoves();
            }
        }
    }

}

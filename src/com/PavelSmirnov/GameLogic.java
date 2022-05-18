package com.PavelSmirnov;

import java.util.*;

public class GameLogic {

    //Массив с оценками клеток для шашек компьютера
    public static final int [] computerPosValues = new int [] {
            357, 348, 325, 288, 237, 172, 93, -152,
            492, 477, 448, 405, 348, 277, 192, 93,
            613, 592, 557, 508, 445, 378, 277, 172,
            720, 693, 652, 597, 528, 445, 348, 237,
            770, 770, 770, 672, 597, 508, 405, 288,
            912, 858, 825, 770, 652, 557, 448, 325,
            962, 912, 858, 770, 693, 592, 477, 348,
            1008,962, 912, 770, 720, 613, 692, 357
    };

    //Массив с оценками клеток для шашек игрока
    public static final int [] playerPosValues = new int [] {
            357, 492, 613, 720, 770, 912, 962, 1008,
            348, 477, 592, 693, 770, 858, 912, 962,
            325, 448, 557, 652, 770, 825, 858, 912,
            288, 405, 508, 597, 672, 770, 770, 770,
            237, 348, 445, 528, 597, 652, 693, 720,
            172, 277, 368, 445, 508, 557, 592, 613,
            93,  192, 277, 348, 405, 448, 477, 492,
            -152, 93, 172, 237, 288, 325, 348, 357
    };

    static int best;
    static int playerStatus27 = 0;
    static int computerStatus27 = 0;

    //Функция оценки текущего положения
    public static int evaluateThePosition (Map<Integer, Integer> gameSituation) {
        int computerPositionEvaluation=0;
        int playerPositionEvaluation=0;
        for (Map.Entry<Integer, Integer> entry : gameSituation.entrySet()) {
            if (entry.getValue()==1) {
                playerPositionEvaluation+= playerPosValues[entry.getKey()];
                if (entry.getKey()==56) playerPositionEvaluation+=ChessBoard.countOfMoves*100;
                if ((entry.getKey()==48) || (entry.getKey()==49) || (entry.getKey()==57))
                    playerPositionEvaluation-=ChessBoard.countOfMoves*(400-playerPosValues[entry.getKey()]);
                if ((entry.getKey()==40) || (entry.getKey()==41) || (entry.getKey()==42)
                        || (entry.getKey()==50) || (entry.getKey()==58))
                    playerPositionEvaluation-=ChessBoard.countOfMoves*(400-playerPosValues[entry.getKey()]);
            }
            if (entry.getValue()==-1)
            {
                computerPositionEvaluation+= computerPosValues[entry.getKey()];
                //Стимул выводить шашки из своего дома
                //С каждым ходом за оставленные в доме шашки
                //из общей оценки вычитается всё больше и больше
                if (entry.getKey()==7) computerPositionEvaluation+=ChessBoard.countOfMoves*100;
                if ((entry.getKey()==6) || (entry.getKey()==14) || (entry.getKey()==15))
                    computerPositionEvaluation-=ChessBoard.countOfMoves*(400-computerPosValues[entry.getKey()]);
                if ((entry.getKey()==5) || (entry.getKey()==13) || (entry.getKey()==21)
                        || (entry.getKey()==22) || (entry.getKey()==23))
                    computerPositionEvaluation-=ChessBoard.countOfMoves*(400-computerPosValues[entry.getKey()]);
            }
        }
        if (computerPositionEvaluation==8209) return -8209;
        if (playerPositionEvaluation==8209) return 8209;
        return computerPositionEvaluation-playerPositionEvaluation;
    }

    //Метод нахождения оптимального следующего хода компьютера
    //Вычисление проходит в виртуальных игровых ситуациях с виртуальными шашками
    public static Map <Integer, Integer> findTHeBestMove(Map<Integer, Integer> currentPos){

        Set<VirtualChekers> virtCheckArr = new HashSet<>();
        for (int i=0; i<64 ; i++){
            int col=currentPos.get(i);
            if ((col==1) || (col==-1)) {
                virtCheckArr.add( new VirtualChekers(i, col));
            }
        }

        for (VirtualChekers virtCheck : virtCheckArr){
            virtCheck.findPosVirtMoves();
        }

        SortedMap <Integer, Map> posibleVirtPositions = new TreeMap<>();

        for (VirtualChekers virtCheck : virtCheckArr) {
            if (virtCheck.color!=-1) continue;
            if (!virtCheck.availableVirtMoves.isEmpty()) {
                for (int move : virtCheck.availableVirtMoves) {
                    Map<Integer, Integer> newCurrentVirtPos = new HashMap<>(currentPos);
                    int eval=0;
                    newCurrentVirtPos.put(move, virtCheck.color);
                    newCurrentVirtPos.put(virtCheck.position, 0);
                    for (Map.Entry<Integer, Integer> entry : newCurrentVirtPos.entrySet()) {
                        if (entry.getValue()==-1) {
                            eval += computerPosValues[entry.getKey()];
                        }
                    }

                    posibleVirtPositions.put(eval, newCurrentVirtPos);
                }
            }
        }

        Map <Integer, Integer> theBestMove;
        best=posibleVirtPositions.lastKey();
        theBestMove = posibleVirtPositions.get(best);

        return theBestMove;
    }

    // Проверка на "правило 27 хода".
    // Если у игрока есть шашки в доме, а у компьютера нет, игрок проиграл.
    // Если наоборот (у игрока нет, а у компьютера есть), игрок выиграл.
    // Если у обоих есть, то оценивается положение. У кого лучше, тот и выиграл
    public static void check27move () {
        Integer [] arrOfComputersHomes =  {5, 6, 7, 13, 14, 15, 21, 22, 23};
        Integer [] arrOfPlayersHomes =  {40, 41, 42, 48, 49, 50, 56, 57, 58};
        for (Map.Entry<Integer, Integer> entry : ChessBoard.currentGamePosition.entrySet()) {
            if ((entry.getValue() == 1) && (Arrays.asList(arrOfPlayersHomes).contains(entry.getKey())))
                GameLogic.playerStatus27 = 1;
            if ((entry.getValue() == -1) && (Arrays.asList(arrOfComputersHomes).contains(entry.getKey())))
                GameLogic.computerStatus27 = 1;
        }
        if ((GameLogic.playerStatus27 == 1) && (GameLogic.computerStatus27 != 1)) Message.youLose();
        if ((GameLogic.playerStatus27 != 1) && (GameLogic.computerStatus27 == 1)) Message.youWin();
        if ((GameLogic.playerStatus27 == 1) && (GameLogic.computerStatus27 == 1)) {
            if (GameLogic.evaluateThePosition(ChessBoard.currentGamePosition)<=0) Message.youWin();
            if (GameLogic.evaluateThePosition(ChessBoard.currentGamePosition)>0) Message.youLose();
        }
    }



}

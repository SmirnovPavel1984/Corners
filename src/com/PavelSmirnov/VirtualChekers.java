
//Класс виртуальных шашек
//При вычислении лучшего хода компьютера просчёт идёт на этих виртуальных шашках

package com.PavelSmirnov;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class VirtualChekers {

    int position;
    int color;
    public Set<Integer> availableVirtMoves = new HashSet<>();


    public VirtualChekers (int pos, int col) {
        position = pos;
        color = col;
    }

    public void findVirtJumpMove(Map gameSit, int pos, int posDenied, int depth) {
        if (depth==0) return;
        Map<Integer, Integer> currentSit = new HashMap<>(gameSit);
        currentSit.put(posDenied, 2);

        if ((pos%8!=7) && (ChessBoard.currentGamePosition.get(pos+1)!=0) && (pos%8!=6) && (ChessBoard.currentGamePosition.get(pos+2)==0)){
            this.availableVirtMoves.add(pos + 2);
            findVirtJumpMove(currentSit, pos+2, pos, depth-1);
        }

        if ((pos%8!=0) && (ChessBoard.currentGamePosition.get(pos-1)!=0) && (pos%8!=1) && (ChessBoard.currentGamePosition.get(pos-2)==0)){
            this.availableVirtMoves.add(pos - 2);
            findVirtJumpMove(currentSit, pos-2, pos, depth-1);
        }

        if ((pos < 48) && (ChessBoard.currentGamePosition.get(pos + 8) != 0) && (ChessBoard.currentGamePosition.get(pos + 16) == 0)) {
            this.availableVirtMoves.add(pos + 16);
            findVirtJumpMove(currentSit, pos+16, pos, depth-1);

        }

        if ((pos > 15) && (ChessBoard.currentGamePosition.get(pos - 8) != 0) && (ChessBoard.currentGamePosition.get(pos - 16) == 0)) {
            this.availableVirtMoves.add(pos - 16);
            findVirtJumpMove(currentSit, pos-16, pos, depth-1);
        }

    }

    public void findPosVirtMoves () {
        int pos=this.position;
        availableVirtMoves.clear();

        int depth = 6;
        if (pos%8!=7) {
            if ((ChessBoard.currentGamePosition.get(pos+1)!=0) && (pos%8!=6) && (ChessBoard.currentGamePosition.get(pos+2)==0)){
                this.availableVirtMoves.add(pos + 2);
                findVirtJumpMove(ChessBoard.currentGamePosition, pos+2, pos, depth);
            } else {
                if (ChessBoard.currentGamePosition.get(pos+1)==0)
                    this.availableVirtMoves.add(pos + 1);
            }
        }
        if (pos%8!=0) {
            if ((ChessBoard.currentGamePosition.get(pos-1)!=0) && (pos%8!=1) && (ChessBoard.currentGamePosition.get(pos-2)==0)){
                this.availableVirtMoves.add(pos - 2);
                findVirtJumpMove(ChessBoard.currentGamePosition, pos-2, pos, depth);
            } else {
                if (ChessBoard.currentGamePosition.get(pos-1)==0)
                    this.availableVirtMoves.add(pos - 1);
            }
        }
        if (pos<56) {
            if ((pos<48) && (ChessBoard.currentGamePosition.get(pos+8)!=0) && (ChessBoard.currentGamePosition.get(pos+16)==0)) {
                this.availableVirtMoves.add(pos + 16);
                findVirtJumpMove(ChessBoard.currentGamePosition, pos+16, pos, depth);
            } else {
                if (ChessBoard.currentGamePosition.get(pos+8)==0)
                    this.availableVirtMoves.add(pos + 8);
            }
        }
        if (pos>7) {
            if ((pos>15) && (ChessBoard.currentGamePosition.get(pos-8)!=0) && (ChessBoard.currentGamePosition.get(pos-16)==0)) {
                this.availableVirtMoves.add(pos - 16);
                findVirtJumpMove(ChessBoard.currentGamePosition, pos-16, pos, depth);
            } else {
                if (ChessBoard.currentGamePosition.get(pos-8)==0)
                    this.availableVirtMoves.add(pos - 8);
            }
        }
    }

}

package org.example.checkers.model;

import java.util.ArrayList;
import java.util.List;

public class Checkers extends Piece{
    private Position currentPosition;
    private Position birthPlace;

    public Checkers(boolean isWhite, Position birthPlace) {
        super(isWhite);
        this.birthPlace = birthPlace;
        this.currentPosition = birthPlace;
    }

    @Override
    public Position getPosition(){
        return currentPosition;
    }

    @Override
    public void setPosition(Position currentPosition) {
        this.currentPosition = currentPosition;
    }
    public List<Position> getPossibleMoves(GameBoard board) {
        List<Position> possibleMoves = new ArrayList<>();
        possibleMoves = checkAttack(board);
        if (possibleMoves.size() == 0) {
            int x = currentPosition.getRow();
            int y = currentPosition.getCol();
            int newX = x + (isWhite() ? -1 : 1);
            int newY = y + 1;
            if (isValidPosition(newX, newY)) {
                possibleMoves.add(new Position(y + 1, x + (isWhite() ? -1 : 1)));
            }
            if (isValidPosition(newX, newY - 2)) {
                possibleMoves.add(new Position(y - 1, x + (isWhite() ? -1 : 1)));
            }
        }
        return possibleMoves;
    }
    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    private List<Position> checkAttack(GameBoard board){
        List<Position> possibleMoves = new ArrayList<>();
        int[] dx = {1,-1,1,-1};
        int[] dy = {1,-1,-1,1};
        for (int i = 0; i < 4; i++){
            int newX = currentPosition.getRow() + dx[i];
            int newY = currentPosition.getCol() + dy[i];
            if (isValidPosition(newX,newY)){
                Position newPosition =  new Position(newX,newY);
                Piece pieceAtNewPosition = board.getPiece(newPosition);
                if (pieceAtNewPosition == null) {
                    continue;
                } else {
                    if (pieceAtNewPosition.isWhite() != this.isWhite()){
                        int endY = newY + dy[i];
                        int endX = newX + dx[i];
                        if (isValidPosition(endX,endY)){
                            possibleMoves.add(new Position(endY,endX));
                        }
                    }
                }
            }
        }
        return possibleMoves;
    }
}

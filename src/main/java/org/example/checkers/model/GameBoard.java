package org.example.checkers.model;

public class GameBoard {
    private Piece[][] board;

    public GameBoard(){
        board = new Piece[8][8];
        setupBoard();
    }

    private void setupBoard(){
        for (int a = 0; a < 3;a = a + 2) {
            for (int i = 1; i < 8; i = i + 2) {
                board[a][i] = new Checkers(false, new Position(a, i));
            }
        }
        for (int i = 0; i < 8; i = i + 2){
            board[1][i] = new Checkers(false, new Position(1, i)); // Добавляем чёрные шашки
        }
        for (int a = 7; a > 4; a = a - 2){
            for (int i = 0; i < 8; i = i + 2){
                board[a][i] = new Checkers(true,new Position(a,i)); // Добавляем белые шашки
            }
        }
        for (int i = 1; i < 8; i = i + 2){
            board[6][i] = new Checkers(true, new Position(6, i)); // Добавляем чёрные шашки
        }
    }

    public Piece getPiece(Position position) {
        return board[position.getRow()][position.getCol()];
    }

    public void setPiece(int row,int col,Piece piece){
        board[row][col] = piece;
    }
}

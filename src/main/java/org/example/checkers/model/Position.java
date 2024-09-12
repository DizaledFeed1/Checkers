package org.example.checkers.model;

public class Position {
    private int row;
    private int col;
    private int enemyRow;
    private int enemyCol;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public  Position(int row, int col, int enemyRow, int enemyCol){
        this.row = row;
        this.col = col;
        this.enemyRow = enemyRow;
        this.enemyCol = enemyCol;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getEnemyCol() {
        return enemyCol;
    }
    public int getEnemyRow(){
        return  enemyRow;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return row == position.row && col == position.col;
    }

    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
    }
}

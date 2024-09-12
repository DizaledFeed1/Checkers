package org.example.checkers.model;

public abstract class Piece {
    private boolean isWhite;

    public Piece(boolean isWhite) {
        this.isWhite = isWhite;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public abstract Position getPosition();
    public abstract void setPosition(Position currentPosition);
}

package org.example.checkers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.example.checkers.model.Checkers;
import org.example.checkers.model.GameBoard;
import org.example.checkers.model.Piece;
import org.example.checkers.model.Position;

import java.net.URL;
import java.util.*;

public class HelloController implements Initializable {
    @FXML
    private GridPane gameboard;
    private GameBoard board;
    private final Map<String, Image> imageCache = new HashMap<>();
    private boolean whiteMove = true;
    private Circle circle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        board = new GameBoard();
        initializeBoard();
    }

    private void initializeBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                StackPane square = new StackPane();
                Rectangle rect = new Rectangle(80, 86); // Здесь устанавливаем начальные размеры
                rect.setFill((row + col) % 2 == 0 ? Color.WHITE : Color.GRAY);

                // Устанавливаем привязки ширины и высоты к размерам GridPane
                rect.widthProperty().bind(gameboard.widthProperty().divide(8));
                rect.heightProperty().bind(gameboard.heightProperty().divide(8));

                square.getChildren().add(rect);

                Piece piece = board.getPiece(new Position(row, col));
                if (piece != null) {
                    Image pieceImage = getPieceImage(piece);
                    if (pieceImage != null) {
                        ImageView imageView = new ImageView(pieceImage);
                        imageView.setFitWidth(120);
                        imageView.setFitHeight(120);
                        square.getChildren().add(imageView);

                        Piece finalPiece = piece;
                        imageView.setOnMouseClicked(event -> handlePieceClick(finalPiece));
                    }
                }

                gameboard.add(square, col, row);
            }
        }
    }

    private void handlePieceClick(Piece piece) {
        addCircle(((Checkers) piece).getPossibleMoves(board), piece, gameboard);
    }

    private void addCircle(List<Position> possibleMoves, Piece piece, GridPane chessboard) {
        if (piece.isWhite() == whiteMove) {
            removeCircles(chessboard);
            for (Position move : possibleMoves) {
                Piece targetPiece = board.getPiece(new Position(move.getCol(), move.getRow()));
                if (targetPiece == null) {
                    // Зеленый круг для свободной клетки
                    circle = new Circle(30, Color.GREEN);
                }  else {
                    continue;
                }
                circle.setOpacity(0.5);
                chessboard.add(circle, move.getRow(), move.getCol());
                Circle finalCircle = circle;
                circle.setOnMouseClicked(event -> movePiece(chessboard, piece, finalCircle));
            }
        }
    }

    private void removeCircles(GridPane chessboard) {
        List<Node> circlesToRemove = new ArrayList<>();
        for (Node node : chessboard.getChildren()) {
            if (node instanceof Circle) {
                circlesToRemove.add(node);
            }
        }
        chessboard.getChildren().removeAll(circlesToRemove);
    }

    private void movePiece(GridPane chessboard, Piece piece, Circle circle) {
        whiteMove = !whiteMove;
        Position currentPosition = piece.getPosition();
        Position newPosition = null;

        // Используем отдельный список для хранения изменений
        List<Node> nodesToRemove = new ArrayList<>();

        for (Node node : chessboard.getChildren()) {
            if (node == circle) {
                int row = GridPane.getRowIndex(node);
                int column = GridPane.getColumnIndex(node);
                newPosition = new Position(row, column);
                board.setPiece(currentPosition.getRow(), currentPosition.getCol(), null);
                board.setPiece(row, column, piece);
                piece.setPosition(newPosition);
                nodesToRemove.add(node); // Сохраняем узел для удаления после итерации
            }
        }

        // Удаляем узлы после завершения итерации, чтобы избежать ConcurrentModificationException
        for (Node nodeToRemove : nodesToRemove) {
            chessboard.getChildren().remove(nodeToRemove);
        }

        update();
        removeCircles(gameboard);
    }


    private void update() {
        List<Node> nodesToRemove = new ArrayList<>();
        for (Node node : gameboard.getChildren()) {
            if (node instanceof StackPane) {
                nodesToRemove.add(node);
            }
        }
        gameboard.getChildren().removeAll(nodesToRemove);
        initializeBoard();
    }



    private Image getPieceImage(Piece piece){
        String imagePath = "/org/example/checkers/img/";
        imagePath += piece.isWhite() ? "checkers_white.png" : "checkers_black.png";
        if (imageCache.containsKey(imagePath)) {
            return imageCache.get(imagePath);
        }

        URL imageUrl = getClass().getResource(imagePath);
        if (imageUrl == null) {
            System.err.println("Image not found: " + imagePath);
            return null;
        }

        Image image = new Image(imageUrl.toString());
        imageCache.put(imagePath, image);
        return image;
    }
}
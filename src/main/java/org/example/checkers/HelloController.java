package org.example.checkers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
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
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    private GameBoard board;
    private final Map<String, Image> imageCache = new HashMap<>();
    private boolean whiteMove = true;
    private Circle circle;
    private List<Position> posibleMove;
    private int whiteNum = 0;
    private int blackNum = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        board = new GameBoard();
        initializeBoard();
    }

    private void initializeBoard() {
        alert.setTitle("Game Result");
        alert.setHeaderText(null);
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
        if (whiteMove == piece.isWhite()){
            posibleMove =  ((Checkers) piece).checkAttack(board);
        if (posibleMove.size() != 0) {
            addCircle(posibleMove,piece,gameboard, true);
        }else {
            posibleMove = ((Checkers) piece).getPossibleMoves(board);
            addCircle(posibleMove,piece,gameboard,false);
        }
//        addCircle(((Checkers) piece).getPossibleMoves(board), piece, gameboard);
    }
        }

    private void addCircle(List<Position> possibleMoves, Piece piece, GridPane chessboard,boolean attack) {
//        if (piece.isWhite() == whiteMove) {
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
                circle.setOnMouseClicked(event -> movePiece(possibleMoves,chessboard, piece, finalCircle, attack));
//                posibleMoves =  ((Checkers) piece).checkAttack(board);
//                if (posibleMoves.size() != 0) {
//                    whiteMove = !whiteMove;
//                }
//            }
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

    private void movePiece(List<Position> possibleMoves,GridPane chessboard, Piece piece, Circle circle, boolean attack) {
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
        kill(possibleMoves,(Checkers) piece);
        update();
        removeCircles(gameboard);
        posibleMove = ((Checkers) piece).checkAttack(board);
        if (posibleMove.size() != 0 && attack) {
            whiteMove = !whiteMove;
        }
    }

    private void checkWin(){
        whiteNum = 0;
        blackNum = 0;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(new Position(row, col));
                if (piece != null) {
                    if (piece.isWhite()) {
                        whiteNum++;
                    } else if (!piece.isWhite()) {
                        blackNum++;
                    }
                }
            }
        }
        if (whiteNum == 0){
            alert.setContentText("Чёрные выиграли");
            alert.showAndWait();
        }
        else if(blackNum == 0){
            alert.setContentText("Белые выиграли");
            alert.showAndWait();
        }
    }

    private void kill(List<Position> killPosition,Checkers piece){
        System.out.println(piece);
        for (Position move: killPosition){
            if (move.getEnemyCol() != 0 && move.getEnemyRow() !=0){
                int[] dx = {1,-1,1,-1};
                int[] dy = {1,-1,-1,1};
                for (int i = 0; i < 4; i++) {
                    int newX = piece.getCurrentPosition().getRow() + dx[i];
                    int newY = piece.getCurrentPosition().getCol() + dy[i];
                    if (newX == move.getEnemyCol() && newY == move.getEnemyRow()){
                        board.setPiece(move.getEnemyCol(), move.getEnemyRow(), null);
                    }
                }
            }
        }
        checkWin();
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
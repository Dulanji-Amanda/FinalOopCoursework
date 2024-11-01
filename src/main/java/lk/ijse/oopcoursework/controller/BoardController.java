package lk.ijse.oopcoursework.controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import lk.ijse.oopcoursework.service.*;

public class BoardController implements BoardUI {

    private static final int GRID_SIZE = 3;
    private String currentPlayer = "X";
    private String playerName = "Dulanji";
    private AIPlayer aiPlayer;
    private HumanPlayer humanPlayer;
    private BoardImpl board;

    @FXML
    private JFXButton btnPlayAgain;

    @FXML
    private Group grpCols;

    @FXML
    private Label lblStatus;

    @FXML
    private Pane pneOver;

    @FXML
    private AnchorPane root;

    @FXML
    private GridPane gridBoard;

    private JFXButton[][] grid;
    private boolean isGameOver;

    @FXML
    public void initialize() {
        board = new BoardImpl(this);
        createGrid();
        initializeGame();
        isGameOver = false;
    }

    private void initializeGame() {
        humanPlayer = new HumanPlayer(board);
        aiPlayer = new AIPlayer(board);
        board.initializeBoard();
    }

    private void createGrid() {
        grid = new JFXButton[GRID_SIZE][GRID_SIZE];

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                JFXButton buttonActions = new JFXButton();
                buttonActions.setMinSize(100, 100);
                buttonActions.setStyle("-fx-border-color: black; -fx-border-width: 2;");
                buttonActions.setOnMouseClicked(this::handleButtonClick);
                gridBoard.add(buttonActions, j, i);
                grid[i][j] = buttonActions;
            }
        }
    }

    @FXML
    private void handleButtonClick(MouseEvent event) {
        JFXButton buttonClicked = (JFXButton) event.getSource();
        int[] position = findButtonPosition(buttonClicked);

        if (position != null && !isGameOver && buttonClicked.getText().isEmpty()) {
            humanPlayer.move(position[0], position[1]);
            board.findWinner();

            if (!isGameOver) {
                Platform.runLater(() -> {
                    aiPlayer.move(-1, -1);
                    board.findWinner();
                });
            }
        }
    }

    private int[] findButtonPosition(JFXButton buttonClicked) {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (grid[i][j] == buttonClicked) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    @Override
    public void update(int row, int col, boolean isHuman) {
        if (isGameOver) return;
        JFXButton buttonUpdate = grid[row][col];

        if (buttonUpdate.getText().isEmpty()) {
            buttonUpdate.setText(currentPlayer);
            buttonUpdate.setStyle("-fx-font-size: 36px; -fx-text-fill: black;");
            lblStatus.setText("Current Player: " + (isHuman ? playerName : "AI"));
            currentPlayer = currentPlayer.equals("X") ? "O" : "X";
        }
    }

    @Override
    public void notifyWinner(Piece winningPiece) {
        isGameOver = true;
        lblStatus.getStyleClass().clear();

        if (winningPiece == Piece.X) {
            lblStatus.setText(playerName + ", you have won the game!");
            lblStatus.setStyle("-fx-text-fill: #4CAF50; -fx-font-size: 24px; -fx-font-weight: bold;");
        } else if (winningPiece == Piece.O) {
            lblStatus.setText("Game over, AI has won the game!");
            lblStatus.setStyle("-fx-text-fill: #F44336; -fx-font-size: 24px; -fx-font-weight: bold;");
        } else {
            lblStatus.setText("It's a tie!");
            lblStatus.setStyle("-fx-text-fill: #f21193; -fx-font-size: 24px; -fx-font-weight: bold;");
        }

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), lblStatus);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();

        pneOver.setVisible(true);
        pneOver.toFront();
        Platform.runLater(btnPlayAgain::requestFocus);
    }

    @FXML
    void btnPlayAgainOnAction(ActionEvent event) {
        resetBoard();
    }

    private void resetBoard() {
        lblStatus.setStyle("-fx-text-fill: #000000; -fx-font-size: 24px; -fx-font-weight: bold;");
        board.resetBoard();
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                grid[i][j].setText("");
                grid[i][j].setStyle("-fx-background-color: transparent; -fx-border-color: black; -fx-border-width: 2;");
            }
        }

        currentPlayer = "X";
        isGameOver = false;
        lblStatus.setText(playerName + ", it's your turn");
        pneOver.setVisible(false);
    }
}

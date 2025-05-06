package com.fx.tictactoejavafx;

import java.util.Arrays;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    // All GUI variables
    private final GridPane gridPane = new GridPane();
    private final BorderPane borderPane = new BorderPane();
    private final Label title = new Label("Tic Tac Toe");
    private final Button restartButton = new Button("Restart Now");
    Font font = Font.font("Roboto", FontWeight.BOLD, 30);
    private final Button[] btns = new Button[9];
    
    // Style constants to ensure buttons stay bright
    private static final double ACTIVE_OPACITY = 1.0;

    // All Logic Variables
    private boolean gameOver = false;
    private int activePlayer = 0;
    private final String[] players = {"O", "X"};
    private final int[] gameStates = {-1, -1, -1, -1, -1, -1, -1, -1, -1};
    private final int[][] winningPositions = {
            {0, 1, 2},
            {3, 4, 5},
            {6, 7, 8},
            {0, 3, 6},
            {1, 4, 7},
            {2, 5, 8},
            {0, 4, 8},
            {2, 4, 6}
    };

    // Constants for player representation in the game state array
    private static final int EMPTY = -1;
    private static final int PLAYER_O = 0; // Human Player
    private static final int PLAYER_X = 1; // AI Player

    // Constructor for HelloApplication
    public HelloApplication() {
        // Initialize the game state array in the constructor.
        Arrays.fill(gameStates, EMPTY);
    }

    @Override
    public void start(Stage stage) {
        this.createGUI();
        this.handleEvent();
        Scene scene = new Scene(borderPane, 550, 650);
        stage.setTitle("Tic Tac Toe");
        stage.setScene(scene);
        stage.show();
    }

    // This function is for creating GUI
    private void createGUI() {
        // Creating title
        title.setFont(font);
        // Creating restart button
        restartButton.setFont(font);
        restartButton.setDisable(!gameOver);

        // Setting title and restart button to border pane
        borderPane.setTop(title);
        borderPane.setBottom(restartButton);
        // Setting border pane components to center
        BorderPane.setAlignment(title, Pos.CENTER);
        BorderPane.setAlignment(restartButton, Pos.CENTER);
        // Adding padding to border pane
        borderPane.setPadding(new Insets(20, 20, 20, 20));

        // Working on 9 game buttons
        int label = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button button = new Button();
                button.setId(String.valueOf(label));
                button.setFont(font);
                button.setPrefHeight(150);
                button.setPrefWidth(150);
                gridPane.add(button, j, i); // Component, column, row
                gridPane.setAlignment(Pos.CENTER);
                btns[label++] = button;
            }
        }
        borderPane.setCenter(gridPane);
    }

    // Method for handling events
    private void handleEvent() {
        restartButton.setOnAction(actionEvent -> {
            gameOver = false;
            activePlayer = 0;
            for (Button button : btns) {
                button.setGraphic(null);
                // Make sure buttons are enabled and not dimmed
                button.setDisable(false);
                button.setOpacity(1.0);
            }
            Arrays.fill(gameStates, EMPTY);
            restartButton.setDisable(true);
            System.out.println("Restart button clicked");
        });
        for (Button button : btns) {
            button.setOnAction(actionEvent -> {
                Button btn = (Button) actionEvent.getSource();
                int idS = Integer.parseInt(btn.getId());
                if (!gameOver) { // Only allow moves if the game is not over
                    if (gameStates[idS] == EMPTY) {
                        // Proceed
                        btn.setGraphic(new ImageView(
                                new Image("file:src/main/resources/assets/circle.png", 100, 100, false, false)
                        ));
                        gameStates[idS] = PLAYER_O; // Human player makes a move
                        // Instead of disabling button, just keep track of it in gameStates
                        checkForWinner();
                        if (!gameOver) {
                            makeAIMove(); // AI makes a move
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Notification");
                        alert.setContentText("Player " + players[activePlayer] + " has already marked this place");
                        alert.show();
                    }
                }
            });
        }
    }

    // This method checks for winners
    private void checkForWinner() {
        if (gameOver)
            return;
        for (int i = 0; i < 8; i++) {
            if (isWinner(winningPositions[i])) {
                // Determine the winner based on the gameStates
                int winner = gameStates[winningPositions[i][0]];
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Notification");
                alert.setContentText("Player " + (winner == PLAYER_O ? "O" : "X") + " won");
                alert.show();
                gameOver = true;
                restartButton.setDisable(false);
                // Don't disable the buttons, so they don't appear dimmed
                break;
            }
        }
        if (!gameOver && isBoardFull()) {
            // It's a draw
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notification");
            alert.setContentText("It's a draw!");
            alert.show();
            gameOver = true;
            restartButton.setDisable(false);
            // Don't disable the buttons, so they don't appear dimmed
        }
    }

    private boolean isWinner(int[] pos) {
        if (gameStates[pos[0]] == EMPTY) {
            return false;
        }
        return (gameStates[pos[0]] == gameStates[pos[1]])
                && (gameStates[pos[0]] == gameStates[pos[2]]);
    }

    private boolean isBoardFull() {
        for (int state : gameStates) {
            if (state == EMPTY) {
                return false;
            }
        }
        return true;
    }

    private void makeAIMove() {
        int bestMove = findBestMove(gameStates);

        // Make the best move found by the minimax algorithm
        if (bestMove != -1) {
            btns[bestMove].setGraphic(new ImageView(
                    new Image("file:src/main/resources/assets/cross.png", 100, 100, false, false)
            ));
            gameStates[bestMove] = PLAYER_X;
            // Make sure the button isn't dimmed
            btns[bestMove].setOpacity(1.0);
            checkForWinner();
        }
    }

    // Minimax algorithm with alpha-beta pruning
    private int minimaxWithAlphaBeta(int[] board, int depth, int player, int alpha, int beta) {
        // Base cases: check for win or draw
        if (isBoardFull(board)) {
            return 0;
        }
        if (isWinner(board, PLAYER_O)) {
            return -1; // Human wins
        }
        if (isWinner(board, PLAYER_X)) {
            return 1; // AI wins
        }

        // Recursive calls for Minimax
        if (player == PLAYER_X) { // Maximizing player (AI)
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 9; i++) {
                if (board[i] == EMPTY) {
                    int[] newBoard = board.clone();
                    newBoard[i] = PLAYER_X;
                    int score = minimaxWithAlphaBeta(newBoard, depth + 1, PLAYER_O, alpha, beta);
                    bestScore = Math.max(score, bestScore);
                    alpha = Math.max(alpha, bestScore);
                    if (alpha >= beta) {
                        break; // Prune
                    }
                }
            }
            return bestScore;
        } else { // Minimizing player (Human)
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 9; i++) {
                if (board[i] == EMPTY) {
                    int[] newBoard = board.clone();
                    newBoard[i] = PLAYER_O;
                    int score = minimaxWithAlphaBeta(newBoard, depth + 1, PLAYER_X, alpha, beta);
                    bestScore = Math.min(score, bestScore);
                    beta = Math.min(beta, bestScore);
                    if (alpha >= beta) {
                        break; // Prune
                    }
                }
            }
            return bestScore;
        }
    }

    // Helper functions for minimax
    private boolean isBoardFull(int[] board) {
        for (int state : board) {
            if (state == EMPTY) {
                return false;
            }
        }
        return true;
    }

    private boolean isWinner(int[] board, int player) {
        for (int[] pos : winningPositions) {
            if (board[pos[0]] == player && board[pos[1]] == player && board[pos[2]] == player) {
                return true;
            }
        }
        return false;
    }

    private int findBestMove(int[] board) {
        int bestMove = -1;
        int bestScore = Integer.MIN_VALUE;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        for (int i = 0; i < 9; i++) {
            if (board[i] == EMPTY) {
                int[] newBoard = board.clone();
                newBoard[i] = PLAYER_X;
                int score = minimaxWithAlphaBeta(newBoard, 0, PLAYER_O, alpha, beta);
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = i;
                }
            }
        }
        return bestMove;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
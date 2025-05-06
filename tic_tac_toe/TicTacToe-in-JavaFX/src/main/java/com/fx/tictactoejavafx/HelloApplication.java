package com.fx.tictactoejavafx;

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

import java.util.Arrays;

public class HelloApplication extends Application {
    // All GUI variables
    private final GridPane gridPane = new GridPane();
    private final BorderPane borderPane = new BorderPane();
    private final Label title = new Label("Tic Tac Toe");
    private final Button restartButton = new Button("Restart Now");
    Font font = Font.font("Roboto", FontWeight.BOLD, 30);
    private final Button[] btns = new Button[9];

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
            }
            Arrays.fill(gameStates, -1);
            restartButton.setDisable(true);
            System.out.println("Restart button clicked");
        });
        for (Button button : btns) {
            button.setOnAction(actionEvent -> {
                Button btn = (Button) actionEvent.getSource();
                int idS = Integer.parseInt(btn.getId());
                if (gameOver) {
                    // Game Over and print message
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Notification");
                    alert.setContentText("Game Over!! Try to restart the game");
                    alert.show();
                } else {
                    // Game is not over, make move
                    if (gameStates[idS] == -1) {
                        // Proceed
                        btn.setGraphic(new ImageView(
                                new Image("file:src/main/resources/assets/circle.png", 100, 100, false, false)
                        ));
                        gameStates[idS] = activePlayer;
                        checkForWinner();
                        activePlayer = 1;
                        if (!gameOver) {
                            makeAIMove();
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
                // Active player is the winner
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Notification");
                alert.setContentText("Player " + players[activePlayer] + " won");
                alert.show();
                gameOver = true;
                restartButton.setDisable(false);
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
        }
    }

    private boolean isWinner(int[] pos) {
        if (gameStates[pos[0]] == -1) {
            return false;
        }
        return (gameStates[pos[0]] == gameStates[pos[1]])
                && (gameStates[pos[0]] == gameStates[pos[2]]);
    }

    private boolean isBoardFull() {
        for (int state : gameStates) {
            if (state == -1) {
                return false;
            }
        }
        return true;
    }

    // Minimax Algorithm
    private void makeAIMove() {
        int bestMove = -1;
        int bestScore = Integer.MIN_VALUE;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        for (int i = 0; i < 9; i++) {
            if (gameStates[i] == -1) {
                gameStates[i] = activePlayer;
                int score = minimaxWithAlphaBeta(0, 1 - activePlayer, alpha, beta);
                gameStates[i] = -1;

                if (score > bestScore) {
                    bestScore = score;
                    bestMove = i;
                }
            }
        }

        if (bestMove != -1) {
            btns[bestMove].setGraphic(new ImageView(
                    new Image("file:src/main/resources/assets/cross.png", 100, 100, false, false)
            ));
            gameStates[bestMove] = activePlayer;
            checkForWinner();
            activePlayer = 0;
        }
    }

    private int minimaxWithAlphaBeta(int depth, int player, int alpha, int beta) {
        if (isWinner(winningPositions[0])) return -1;
        if (isWinner(winningPositions[1])) return 1;
        if (isWinner(winningPositions[2])) return -1;
        if (isWinner(winningPositions[3])) return 1;
        if (isWinner(winningPositions[4])) return -1;
        if (isWinner(winningPositions[5])) return 1;
        if (isWinner(winningPositions[6])) return -1;
        if (isWinner(winningPositions[7])) return 1;
        if (isBoardFull()) return 0;

        if (player == 0) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 9; i++) {
                if (gameStates[i] == -1) {
                    gameStates[i] = player;
                    int score = minimaxWithAlphaBeta(depth + 1, 1 - player, alpha, beta);
                    gameStates[i] = -1;
                    bestScore = Math.max(score, bestScore);
                    alpha = Math.max(alpha, bestScore);
                    if (alpha >= beta)
                        break;
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 9; i++) {
                if (gameStates[i] == -1) {
                    gameStates[i] = player;
                    int score = minimaxWithAlphaBeta(depth + 1, 1 - player, alpha, beta);
                    gameStates[i] = -1;
                    bestScore = Math.min(score, bestScore);
                    beta = Math.min(beta, bestScore);
                    if (beta <= alpha)
                        break;
                }
            }
            return bestScore;
        }
    }

    private int evaluateBoard() {
        int score = 0;
        for (int[] positions : winningPositions) {
            int countPlayer = 0;
            int countOpponent = 0;
            for (int pos : positions) {
                if (gameStates[pos] == activePlayer)
                    countPlayer++;
                else if (gameStates[pos] != -1)
                    countOpponent++;
            }
            if (countPlayer == 3)
                score += 100; // AI has three in a row
            else if (countPlayer == 2 && countOpponent == 0)
                score += 10; // AI has two in a row
            else if (countPlayer == 1 && countOpponent == 0)
                score += 1; // AI has one in a row
            else if (countOpponent == 3)
                score -= 100; // Opponent has three in a row
            else if (countOpponent == 2 && countPlayer == 0)
                score -= 10; // Opponent has two in a row
            else if (countOpponent == 1 && countPlayer == 0)
                score -= 1; // Opponent has one in a row
        }
        return score;
    }

    private int minimaxWithEvaluation(int depth, int player) {
        if (isWinner(winningPositions[0])) return -1;
        if (isWinner(winningPositions[1])) return 1;
        if (isWinner(winningPositions[2])) return -1;
        if (isWinner(winningPositions[3])) return 1;
        if (isWinner(winningPositions[4])) return -1;
        if (isWinner(winningPositions[5])) return 1;
        if (isWinner(winningPositions[6])) return -1;
        if (isWinner(winningPositions[7])) return 1;
        if (isBoardFull()) return 0;

        if (player == 0) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 9; i++) {
                if (gameStates[i] == -1) {
                    gameStates[i] = player;
                    int score = minimaxWithEvaluation(depth + 1, 1 - player);
                    gameStates[i] = -1;
                    bestScore = Math.max(score, bestScore);
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 9; i++) {
                if (gameStates[i] == -1) {
                    gameStates[i] = player;
                    int score = minimaxWithEvaluation(depth + 1, 1 - player);
                    gameStates[i] = -1;
                    bestScore = Math.min(score, bestScore);
                }
            }
            return bestScore;
        }
    }



    public static void main(String[] args) {
        launch(args);
    }
}

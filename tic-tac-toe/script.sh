#!/bin/bash


export DISPLAY=:1.0
(pkill java || true)
cd TicTacToe-in-JavaFX
echo "Compiling..."
mvn clean install
echo "Executing Program..."
mvn clean javafx:run -Djavafx.verbose=true

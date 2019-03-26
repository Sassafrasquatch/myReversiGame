Reversi

Sometimes known under the brand-name Othello, Reversi is a relatively simple game
White and Black players take turns placing a piece on the board in such a way that
their pieces sandwich a number of the other player's pieces in a single line,
horizontally, vertically, or diagonally. When this move is made, all of the pieces
sandwiched in this manner are flipped to the color of the player that made the move.
When neither player has any legal moves remaining, the game ends and the player with
more pieces flipped to their color wins. 

This project is a simple GUI implemenation of a Reversi game. New Game creates a new game,
the player goes first and clicks the tile where they want to play a piece to make their move.
The player then clicks anywhere else on the board to tell the computer player to take their turn.
New game will clear the board and start over with the initial 4 tiles in place. Closing the window
before a game is over will create a file called save_game.dat that stores the board state, which
is loaded up the next time the game is started. This file is deleted when the game is over.
The computer player does a simple enumeration over all board positions to see which move will
give it the most points in that turn, then takes that greedy move.
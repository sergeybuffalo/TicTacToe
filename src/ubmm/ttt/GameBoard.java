/**
 * Author: Panya Chanawangsa
 * Last modified: 11/12/2012
 * You are allowed to use my code however you like,
 * as long as you leave this notice as is in your project.
 */
package ubmm.ttt;

import ubmm.ttt.TicTacToeCell.States;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class GameBoard extends Activity implements OnClickListener {	
	// For debugging purposes.
	protected static final String TAG = "ttt";
	// Player's turn.
		private static enum Turn { P1, P2};
	// Dimensions of the board.
	int dim;

	// Contains the names of both players.
	String[] playerNames;
	// Current turn.
	Turn t;
	// TextView for displaying the current player's name.
	TextView turnDisplayTv;
	// The game board is essentially a tale.
	TableLayout gameBoard;
	// Restart and quit buttons
	Button restartBtn, quitBtn;	
	// Contains the actual game logic.
	Turn[][] logicBoard;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_board);

		turnDisplayTv = (TextView) this.findViewById(R.id.turn_display);
		restartBtn = (Button) this.findViewById(R.id.restart_btn);
		quitBtn = (Button) this.findViewById(R.id.quit_btn);
		gameBoard = (TableLayout) this.findViewById(R.id.game_board_tb);

		restartBtn.setOnClickListener(this);
		quitBtn.setOnClickListener(this);
		
		Intent i = this.getIntent();
		playerNames = i.getExtras().getStringArray("player_names");
		dim = i.getExtras().getInt("dimensions");
		
		Log.d(TAG, "Dimensions of the board = " + dim);
		
		initBoard();
	}

	/**
	 * Initialize the game board. Add cells to the table.
	 */
	private void initBoard() {
		t = Turn.P2;
		changeTurn();
		logicBoard = new Turn[dim][dim];
		// Initialize the logic board.
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				logicBoard[i][j] = null;
			}
		}

		// Dynamically add cells (which are essentially buttons) to the game board (TableLayout).
		// First, determine the proper size of each button.
		int buttonSize = getProperButtonSize();
		
		for (int i = 0 ; i < dim; i++) {
			TableRow tr = new TableRow(this);
			for (int j = 0; j < dim; j++) {
				final TicTacToeCell newCell = new TicTacToeCell(this);	
				newCell.setPosition(i, j);
				newCell.setSize(buttonSize);

				newCell.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (t == Turn.P1) { 
							newCell.setBackground(States.O);
						} else {
							newCell.setBackground(States.X);
						}
						
						// Both players should not be able to pick the same spot again.
						newCell.setEnabled(false);
						
						// Update the logic board.
						logicBoard[newCell.getRow()][newCell.getCol()] = t;
						
						// Determine if this is the end of the game.
						if (isOver(newCell.getRow(), newCell.getCol())) {
							displayVictoryMessage();
						}
					}					
				});
				tr.addView(newCell);
			}
			gameBoard.addView(tr);
		}
	}

	private int getProperButtonSize() {
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);		
		int screenWidth = size.x;
		return (int)(screenWidth * 0.8 / dim);
	}

	/**
	 * Determines if there is a winner. Only row r and column c need to be checked,
	 * unless r and c are on the diagonals.
	 * @param r - row of the last move
	 * @param c - column of the last move
	 */
	private boolean isOver(int r, int c) {
		if (checkRow(r) || checkCol(c)) {
			Log.d(TAG, t + " won!");
			return true;
		}

		// If (r,c) is a diagonal entry, we need to check the diagonals.
		if (r == c) {
			if (checkPrimaryDiagonal()) {
				Log.d(TAG, t + " won!");
				return true;
			}
		}

		if (r == dim - c - 1) {
			if (checkSecondaryDiagonal()) {
				Log.d(TAG, t + " won!");
				return true;
			}
		}
		
		// Change turn.					
		changeTurn();
		return false;
	}

	private boolean checkRow(int r) {
		for (int j = 0; j < dim; j++) 
			if (logicBoard[r][j] != t)
				return false;
		return true;
	}

	private boolean checkCol(int c) {
		for (int i = 0; i < dim; i++) 
			if (logicBoard[i][c] != t)
				return false;
		return true;
	}

	private boolean checkPrimaryDiagonal() {
		int i = 0, j = 0;
		while (i < dim && j < dim) {
			if (logicBoard[i][j] != t) 
				return false;
			i++; j++;
		}
		return true;
	}

	private boolean checkSecondaryDiagonal() {
		int i = 0, j = dim - 1;
		while (i < dim && j >= 0) {
			if (logicBoard[i][j] != t) {
				return false;
			}
			i++; j--;
		}
		return true;
	}

	private void changeTurn() {
		t = (t == Turn.P1) ? Turn.P2 : Turn.P1; 
		turnDisplayTv.setText(playerNames[t.ordinal()] + "'s turn.");
	}
	
	/** Restart the entire game. */
	private void restart() {
		gameBoard.removeAllViews();
		initBoard();
	}
	
	/** Display the victory message and ask if the user wants a rematch. */
	private void displayVictoryMessage() {
		turnDisplayTv.setText(playerNames[t.ordinal()] + " won!");
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GameBoard.this);
		alertDialogBuilder.setTitle("UB Tic-Tac-Toe")
		.setMessage(playerNames[t.ordinal()] + " won the game!\n" + "Rematch?")
		.setCancelable(false)
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				restart();
			}
		})
		.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// Go back to the parent activity.
				GameBoard.this.finish();
			}
		}).create().show();
	}

	/** Handle button click events. */
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.restart_btn:
				restart();
				break;
			case R.id.quit_btn:
				this.finish();
				break;
		}
	}
}

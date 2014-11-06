package ubmm.ttt;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class TicTacToeActivity extends Activity implements OnClickListener {
	private static int MIN_DIM = 3;
	private static int MAX_DIM = 7;
	EditText player1Et, player2Et, dimensionsEt;
	Button submitBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		player1Et = (EditText) this.findViewById(R.id.plyer_1_name_et);
		player2Et = (EditText) this.findViewById(R.id.plyer_2_name_et);
		dimensionsEt = (EditText) this.findViewById(R.id.dimension_et);
		submitBtn = (Button) this.findViewById(R.id.submit_btn);

		// Attach a listener to this button. 
		// Note that this activity needs to implement the OnClickListener interface.
		submitBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.submit_btn:
			String p1Name = player1Et.getText().toString();
			String p2Name = player2Et.getText().toString();
			String dimensions = dimensionsEt.getText().toString();

			if (p1Name.isEmpty() || p2Name.isEmpty()) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TicTacToeActivity.this);
				alertDialogBuilder.setTitle("UB Tic-Tac-Toe")
				.setMessage("Both players need to have a name!")
				.setCancelable(false)
				.setPositiveButton("OK", null).create().show();
			} else if (dimensions.isEmpty()) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TicTacToeActivity.this);
				alertDialogBuilder.setTitle("UB Tic-Tac-Toe")
				.setMessage("Specify the dimensions of the board!")
				.setCancelable(false)
				.setPositiveButton("OK", null).create().show();
			} else if (Integer.parseInt(dimensions) < MIN_DIM || Integer.parseInt(dimensions) > MAX_DIM) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TicTacToeActivity.this);
				alertDialogBuilder.setTitle("UB Tic-Tac-Toe")
				.setMessage("Allowed range is from " + MIN_DIM + " to " + MAX_DIM + ".")
				.setCancelable(false)
				.setPositiveButton("OK", null).create().show();
			} else {
				// Start a new activity and pass a string array 
				// containing usernames to that activity.
				Intent i = new Intent(this, GameBoard.class);
				i.putExtra("player_names", new String[] {p1Name, p2Name});
				i.putExtra("dimensions", Integer.parseInt(dimensions));
				this.startActivity(i);
			}
			break;
		}
	}
}
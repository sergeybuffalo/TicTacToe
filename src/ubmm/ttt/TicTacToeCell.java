package ubmm.ttt;

import android.content.Context;
import android.widget.Button;
import android.widget.TableRow.LayoutParams;

public class TicTacToeCell extends Button {
	public static enum States {BLANK, X, O};
	private States _s;
	private int _row;
	private int _col;
	
	public TicTacToeCell(Context context) {
		super(context);
		// Initially the background should be blank.
		_s = States.BLANK;
		
		setBackground(_s);		
	}
	
	public void setState(States s) {
		_s = s;
		this.setBackground(_s);
	}
	
	public void setSize(int size) {
		LayoutParams buttonLayoutParams = new LayoutParams(size, size);			
		buttonLayoutParams.setMargins(4, 4, 4, 4);
		this.setLayoutParams(buttonLayoutParams);
	}
	
	public void setBackground(States s) {
		switch (s) {
		case BLANK:
			this.setBackgroundResource(R.drawable.blank);
			break;
		case X:
			this.setBackgroundResource(R.drawable.x);
			break;
		case O:
			this.setBackgroundResource(R.drawable.o);
			break;
		}
	}
	
	/** Accessor methods. */
	public void setPosition(int row, int col) {
		_row = row;
		_col = col;
	}	

	public int getRow() {
		return _row;
	}
	
	public int getCol() {
		return _col;
	}
}
